package project.issue.tracker.services.creation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.validator.GenericValidator;

import project.issue.tracker.database.db.QuerySelector;
import project.issue.tracker.database.models.User;
import project.issue.tracker.utils.FORM_PARAMS;

@WebServlet(name = "CreateUser", urlPatterns = { "/createUser.do" })
public class CreateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String REG_EX_USERNAME_PASS = "^[a-zA-Z_0-9\\.\\-,:]{1,20}$";
	private static final String REG_EX_NAME = "^[a-zA-Z \\-]{1,50}$";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/json");

		String username = req.getParameter(FORM_PARAMS.CREATE_USER.USERNAME);
		String password = req.getParameter(FORM_PARAMS.CREATE_USER.PASSOWRD);
		String fullName = req.getParameter(FORM_PARAMS.CREATE_USER.FULL_NAME);
		String email = req.getParameter(FORM_PARAMS.CREATE_USER.EMAIL);
		String itemsPerPage = req
				.getParameter(FORM_PARAMS.CHANGE_USER.ITEMS_PER_PAGE);
		String userType = req.getParameter(FORM_PARAMS.CREATE_USER.USER_TYPE)
				.equals("User") ? "0" : "1";

		PrintWriter out = resp.getWriter();

		if (!username.matches(REG_EX_USERNAME_PASS)
				|| !password.matches(REG_EX_USERNAME_PASS)
				|| !fullName.matches(REG_EX_NAME)
				|| !GenericValidator.isEmail(email)
				|| !(itemsPerPage.charAt(0) >= '1'
						&& itemsPerPage.charAt(0) <= '6'
						&& itemsPerPage.charAt(1) == '0' && itemsPerPage
						.length() == 2)) {
			out.print("{\"error\":\"Bad Input\"}");
			out.flush();
			return;
		}

		QuerySelector selector = QuerySelector.getInstance();

		JSONObject responseJSON = new JSONObject();
		List<User> users = selector.getUsersByName(fullName);
		if (!users.isEmpty()) {
			responseJSON.put("error", "User Full name already exists");
			out.print(responseJSON.toString());
			out.flush();
			return;
		}
		users = selector.getUsersByUName(username);
		if (!users.isEmpty()) {
			responseJSON.put("error", "User already exists");
			out.print(responseJSON.toString());
			out.flush();
			return;
		}
		try {
			User newUser = new User();
			newUser.setEmail(email);
			newUser.setUserName(username);
			newUser.setPassword(password);
			newUser.setItemsPerPage(Integer.valueOf(itemsPerPage));
			newUser.setRole(userType);
			newUser.setFullName(fullName);
			
			selector.persistObject(newUser);
			
			responseJSON.put("success", "User Created");
			
		} catch (Exception ex) {
			responseJSON.put("error", "User not created");
		} finally {
			out.print(responseJSON.toString());
			out.flush();
		}
	}
}
