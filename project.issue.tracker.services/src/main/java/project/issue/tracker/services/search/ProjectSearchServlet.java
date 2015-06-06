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
import project.issue.tracker.database.models.DBProject;
import project.issue.tracker.utils.FORM_PARAMS;
import project.issue.tracker.utils.Utils;

@WebServlet(urlPatterns = {"/searchProject.do"}, name = "ProjectSearch")
public class ProjectSearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String projectName = req.getParameter(FORM_PARAMS.LIST_PROJECT.PROJECT_NAME);

        PrintWriter out = resp.getWriter();

        JSONObject responseJSON = new JSONObject();
        JSONArray projectsArray = new JSONArray();

        for (DBProject project : DBProject.getAllProjects()) {

            if (!Utils.isNull(projectName) && !project.getName().toLowerCase().contains(projectName.toLowerCase())) {
                continue;
            }
                JSONObject dummy = new JSONObject();

                dummy.put("pID", project.getId());
                dummy.put("pName", project.getName());
                dummy.put("pDesc", project.getDescription());
                dummy.put("pUsers", "");
                dummy.put("pTasks", "");

                projectsArray.add(dummy);
        }

        responseJSON.put("projects", projectsArray);

        out.print(responseJSON.toString());
        resp.getWriter().flush();
    }
}
