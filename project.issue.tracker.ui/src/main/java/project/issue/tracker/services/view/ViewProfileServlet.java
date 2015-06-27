package project.issue.tracker.services.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import project.issue.tracker.database.models.DBUser;
import project.issue.tracker.utils.ATTRIBUTES;

@WebServlet(urlPatterns = {"/viewProfile.do"}, name = "ProfileViewer")
public class ViewProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        JSONObject jsonResponse = new JSONObject();
        DBUser userBean = (DBUser) req.getSession().getAttribute(ATTRIBUTES.USER_BEAN);

        jsonResponse.put("username", userBean.getUserName());
        jsonResponse.put("full_name", userBean.getName());
        jsonResponse.put("mail", userBean.getEmail());
        jsonResponse.put("uType", userBean.isAdmin() ? "Administrator" : "User");
        jsonResponse.put("items_per_page", userBean.getItemsPerPage());

        resp.getWriter().print(jsonResponse.toString());
        resp.getWriter().flush();
    }
}
