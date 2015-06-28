package project.issue.tracker.services.search;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import project.issue.tracker.database.models.DBUser;
import project.issue.tracker.utils.FORM_PARAMS;
import project.issue.tracker.utils.Utils;

@WebServlet(urlPatterns = {"/searchUser.do"}, name = "userSearch")
public class UserSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");
        String username = req.getParameter(FORM_PARAMS.LIST_USER.USERNAME);
        String fullName = req.getParameter(FORM_PARAMS.LIST_USER.FULL_NAME);
        String email = req.getParameter(FORM_PARAMS.LIST_USER.EMAIL);
        String userType = req.getParameter(FORM_PARAMS.LIST_USER.USER_TYPE);

        PrintWriter out = resp.getWriter();
        JSONObject responseJSON = new JSONObject();
        JSONArray usersArray = new JSONArray();

        for (DBUser user : DBUser.getAllUsers()) {
            if (!Utils.isNull(username) && !user.getName().toLowerCase().contains(username)) {
                continue;
            }

            if (!Utils.isNull(email) && !user.getEmail().contains(email)) {
                continue;
            }

            if (!Utils.isNull(fullName) && !user.getName().contains(fullName)) {
                continue;
            }
            String type = user.isAdmin() ? "Administrator" : "User";
            if (!Utils.isNull(userType) && !type.equals(userType)) {
                continue;
            }

            JSONObject dummy = new JSONObject();

            dummy.put("uID", user.getId());
            dummy.put("uFullName", user.getName());
            dummy.put("uUsername", user.getUserName());
            dummy.put("uRole", user.isAdmin() ? "Administrator" : "User");
            dummy.put("uEmail", user.getEmail());

            usersArray.add(dummy);

        }


        responseJSON.put("users", usersArray);
        out.print(responseJSON.toString());
        out.flush();

    }
}
