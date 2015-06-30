package project.issue.tracker.services.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import project.issue.tracker.database.QuerySelector;
import project.issue.tracker.database.models.Project;
import project.issue.tracker.utils.FORM_PARAMS;

@WebServlet(urlPatterns = {"/viewProject.do"}, name = "ProjectViewer")
public class ProjectViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        String projectId = req.getParameter(FORM_PARAMS.VIEW_PROJECT.ID);
        QuerySelector selector = QuerySelector.getInstance();
        Project project = selector.getProjectById(projectId);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("id", project.getId());
        jsonResponse.put("name", project.getProjectName());
        jsonResponse.put("desc", project.getDescription());

        resp.getWriter().print(jsonResponse.toString());
        resp.getWriter().flush();
    }
}
