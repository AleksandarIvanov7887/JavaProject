package project.issue.tracker.services.change.project;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.issue.tracker.database.models.DBProject;
import project.issue.tracker.utils.FORM_PARAMS;

@WebServlet(urlPatterns = {"/changeProject.do"}, name = "ProjectChanger")
public class ChangeProjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter(FORM_PARAMS.CHANGE_PROJECT.ID);
        String name = req.getParameter(FORM_PARAMS.CHANGE_PROJECT.NAME);
        String desc = req.getParameter(FORM_PARAMS.CHANGE_PROJECT.DESCRIPTION);

        DBProject project = new DBProject(id);
        project.updateName(name);
        project.updateDescription(desc);

        resp.getWriter().print("{\"success\":\" OK\"}");
        resp.getWriter().flush();
    }
}
