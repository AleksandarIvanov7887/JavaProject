package project.issue.tracker.services.change.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import net.sf.json.JSONObject;
import project.issue.tracker.database.db.QuerySelector;
import project.issue.tracker.database.models.User;
import project.issue.tracker.utils.ATTRIBUTES;
import project.issue.tracker.utils.FORM_PARAMS;

@WebServlet( urlPatterns = {"/changeUser.do"}, name = "UserChanger")
public class ChangeUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String REG_EX_USERNAME_PASS = "^[a-zA-Z_0-9\\.\\-,:]{1,20}$";
	private static final String REG_EX_NAME = "^[a-zA-Z \\-]{1,50}$";

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        String oldPasword = req.getParameter(FORM_PARAMS.CHANGE_USER.OLD_PASSWORD);
        String newPassword = req.getParameter(FORM_PARAMS.CHANGE_USER.NEW_PASSWORD);
        String fullName = req.getParameter(FORM_PARAMS.CHANGE_USER.FULL_NAME);
        String itemsPerPage = req.getParameter(FORM_PARAMS.CHANGE_USER.ITEMS_PER_PAGE);

        PrintWriter out = resp.getWriter();

        if (!oldPasword.matches(REG_EX_USERNAME_PASS) ||
                !newPassword.matches(REG_EX_USERNAME_PASS) ||
                !fullName.matches(REG_EX_NAME) ||
                !(itemsPerPage.charAt(0) >= '1' && itemsPerPage.charAt(0) <= '6' && itemsPerPage.charAt(1) == '0' && itemsPerPage.length() == 2)) {

            out.print("{\"error\":\"Bad input.\"}");
            out.flush();
            return;
        }

        
        QuerySelector selector = QuerySelector.getInstance();
        User currentUser = (User) req.getSession().getAttribute(ATTRIBUTES.USER_BEAN);

        if (BCrypt.checkpw(oldPasword, currentUser.getPassword())) {
            out.print("{\"error\":\"Bad password. Old password was not correct.\"}");
            out.flush();
            return;
        }
        try {
	        currentUser.setFullName(fullName);
	        currentUser.setPassword(newPassword);
	        currentUser.setItemsPerPage(Integer.valueOf(itemsPerPage));
	        
	        selector.persistObject(currentUser);
        } catch (Exception exc) {
        	exc.printStackTrace();
        	out.print("{\"error\":\"User could not be fully updated.\"}");
            out.flush();
            return;
        }

        req.getSession().removeAttribute(ATTRIBUTES.USER_BEAN);
        req.getSession().setAttribute(ATTRIBUTES.USER_BEAN, currentUser);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("username", currentUser.getUserName());
        jsonResponse.put("full_name", currentUser.getFullName());
        jsonResponse.put("mail", currentUser.getEmail());
        jsonResponse.put("utype", currentUser.getRole());
        jsonResponse.put("itemspp", currentUser.getItemsPerPage());

        resp.getWriter().print(jsonResponse.toString());
        resp.getWriter().flush();
    }
}
