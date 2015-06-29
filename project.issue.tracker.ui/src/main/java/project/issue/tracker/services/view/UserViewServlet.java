package project.issue.tracker.services.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import project.issue.tracker.database.db.QuerySelector;
import project.issue.tracker.database.models.User;
import project.issue.tracker.utils.FORM_PARAMS;
import project.issue.tracker.utils.Utils;

@WebServlet(urlPatterns = {"/viewUser.do"}, name = "UserViewer")
public class UserViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        String userID = req.getParameter(FORM_PARAMS.VIEW_USER.ID);
        QuerySelector selector = QuerySelector.getInstance();
        User wantedUser = selector.getUserById(userID);

        JSONObject jsonResponse = new JSONObject();
        if (Utils.isNull(wantedUser.getUserName())) {
            resp.getWriter().print("{\"error\":\"No such user\"}");
        }

        jsonResponse.put("id", wantedUser.getId());
        jsonResponse.put("username", wantedUser.getUserName());
        jsonResponse.put("full_name", wantedUser.getFullName());
        jsonResponse.put("mail", wantedUser.getEmail());
        jsonResponse.put("utype", wantedUser.getRole());

        resp.getWriter().print(jsonResponse.toString());
        resp.getWriter().flush();
    }
}
