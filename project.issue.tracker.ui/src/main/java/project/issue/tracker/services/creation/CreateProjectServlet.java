package project.issue.tracker.services.creation;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import project.issue.tracker.database.models.DBProject;
import project.issue.tracker.utils.FORM_PARAMS;

@WebServlet( urlPatterns = {"/createProject.do"}, name = "projectCreator")
public class CreateProjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        String projectName = req.getParameter(FORM_PARAMS.CREATE_PROJECT.PROJECT_NAME);
        String projectDesc = req.getParameter(FORM_PARAMS.CREATE_PROJECT.PROJECT_DESCRIPTION);
        JSONObject responseJSON = new JSONObject();

        if(!projectName.matches("^[a-zA-Z_0-9 \\.\\-,:]{1,50}$") || !projectDesc.matches("^[a-zA-Z_0-9\\s\\.\\-,:()]{1,2000}$")) {
            responseJSON.put("error", "Bad input.\nPlease enter valid data.");
            resp.getWriter().print(responseJSON.toString());
            resp.getWriter().flush();
            return;
        }


        for (DBProject P : DBProject.getAllProjects()) {
            if (projectName.equalsIgnoreCase(P.getName())) {
                responseJSON.put("error", "Project name already EXISTS");
                resp.getWriter().print(responseJSON.toString());
                resp.getWriter().flush();
                return;
            }
        }

        // Do something
        if (new DBProject(projectName, projectDesc).save()) {
            responseJSON.put("success", "Project Created");
        } else {
            responseJSON.put("error", "Project NOT Created");
        }

        resp.getWriter().print(responseJSON.toString());
        resp.getWriter().flush();
    }
}
