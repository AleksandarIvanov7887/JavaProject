package project.issue.tracker.services.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import project.issue.tracker.database.models.User;
import project.issue.tracker.utils.ATTRIBUTES;

@WebServlet(urlPatterns = {"/viewProfile.do"}, name = "ProfileViewer")
public class ViewProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        JSONObject jsonResponse = new JSONObject();
        User currentUser = (User) req.getSession().getAttribute(ATTRIBUTES.USER_BEAN);

        jsonResponse.put("username", currentUser.getUserName());
        jsonResponse.put("full_name", currentUser.getFullName());
        jsonResponse.put("mail", currentUser.getEmail());
        jsonResponse.put("uType", currentUser.getRole());
        jsonResponse.put("items_per_page", currentUser.getItemsPerPage());

        resp.getWriter().print(jsonResponse.toString());
        resp.getWriter().flush();
    }
}
