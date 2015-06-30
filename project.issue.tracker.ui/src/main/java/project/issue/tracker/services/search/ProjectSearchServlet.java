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
import project.issue.tracker.database.QuerySelector;
import project.issue.tracker.database.models.Project;
import project.issue.tracker.utils.FORM_PARAMS;
import project.issue.tracker.utils.Utils;

@WebServlet(urlPatterns = {"/searchProject.do"}, name = "ProjectSearch")
public class ProjectSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String projectName = req.getParameter(FORM_PARAMS.LIST_PROJECT.PROJECT_NAME);

        PrintWriter out = resp.getWriter();

        JSONObject responseJSON = new JSONObject();
        JSONArray projectsArray = new JSONArray();

        QuerySelector selector = QuerySelector.getInstance();
        
        for (Project project : selector.getAllProjects()) {

            if (!Utils.isNull(projectName) && !project.getProjectName().toLowerCase().contains(projectName.toLowerCase())) {
                continue;
            }
                JSONObject projects = new JSONObject();

                projects.put("pID", project.getId());
                projects.put("pName", project.getProjectName());
                projects.put("pDesc", project.getDescription());
                projects.put("pUsers", "");
                projects.put("pTasks", "");

                projectsArray.add(projects);
        }

        responseJSON.put("projects", projectsArray);

        out.print(responseJSON.toString());
        resp.getWriter().flush();
    }
}
