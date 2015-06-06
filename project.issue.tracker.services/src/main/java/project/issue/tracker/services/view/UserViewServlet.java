package project.issue.tracker.services.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import project.issue.tracker.database.models.DBUser;
import project.issue.tracker.utils.FORM_PARAMS;
import project.issue.tracker.utils.Utils;

@WebServlet(urlPatterns = {"/viewUser.do"}, name = "UserViewer")
public class UserViewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        String userID = req.getParameter(FORM_PARAMS.VIEW_USER.ID);
        DBUser wantedUser = new DBUser(userID);

        JSONObject jsonResponse = new JSONObject();
        if (Utils.isNull(wantedUser.getUserName())) {
            resp.getWriter().print("{\"error\":\"No such user\"}");
        }

        jsonResponse.put("id", wantedUser.getId());
        jsonResponse.put("username", wantedUser.getUserName());
        jsonResponse.put("full_name", wantedUser.getName());
        jsonResponse.put("mail", wantedUser.getEmail());
        jsonResponse.put("utype", wantedUser.isAdmin() ? "Administrator" : "User");

        resp.getWriter().print(jsonResponse.toString());
        resp.getWriter().flush();
    }
}
