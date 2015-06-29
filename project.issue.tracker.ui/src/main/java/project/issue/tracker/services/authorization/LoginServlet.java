package project.issue.tracker.services.authorization;

import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.issue.tracker.database.models.User;
import project.issue.tracker.utils.ATTRIBUTES;

import java.io.IOException;

@WebServlet( urlPatterns = {"/login.do"}, name = "Loginer")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        User currentUser = (User) req.getSession(false).getAttribute(ATTRIBUTES.USER_BEAN);
        JSONObject responseJSON = new JSONObject();
        
        responseJSON.put("username", currentUser.getUserName());
        responseJSON.put("full_name", currentUser.getFullName());
        responseJSON.put("mail", currentUser.getEmail());
        responseJSON.put("utype", currentUser.getRole());
        responseJSON.put("itemspp", currentUser.getItemsPerPage());
        responseJSON.put("application_context", req.getServletContext().getContextPath());

        resp.getWriter().print(responseJSON.toString());
        resp.getWriter().flush();
    }
}
