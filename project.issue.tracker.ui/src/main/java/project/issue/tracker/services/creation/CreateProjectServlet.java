package project.issue.tracker.services.creation;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import project.issue.tracker.database.db.QuerySelector;
import project.issue.tracker.database.models.Project;
import project.issue.tracker.utils.FORM_PARAMS;

@WebServlet(urlPatterns = { "/createProject.do" }, name = "projectCreator")
public class CreateProjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String REG_EX_PROJNAME = "^[a-zA-Z_0-9 \\.\\-,:]{1,50}$";
	private static final String REG_EX_PROJDESC = "^^[a-zA-Z_0-9\\s\\.\\-,:()]{1,2000}$$";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/json");

		String projectName = req
				.getParameter(FORM_PARAMS.CREATE_PROJECT.PROJECT_NAME);
		String projectDesc = req
				.getParameter(FORM_PARAMS.CREATE_PROJECT.PROJECT_DESCRIPTION);
		JSONObject responseJSON = new JSONObject();

		if (!projectName.matches(REG_EX_PROJNAME)
				|| !projectDesc.matches(REG_EX_PROJDESC)) {
			responseJSON.put("error", "Bad input.\nPlease enter valid data.");
			resp.getWriter().print(responseJSON.toString());
			resp.getWriter().flush();
			return;
		}

		QuerySelector selector = QuerySelector.getInstance();
		List<Project> projects = selector.getProjectByName(projectName);

		if (!projects.isEmpty()) {
			responseJSON.put("error", "Project name already EXISTS");
			resp.getWriter().print(responseJSON.toString());
			resp.getWriter().flush();
			return;
		}

		Project newProject = new Project();
		newProject.setDescription(projectDesc);
		newProject.setProjectName(projectName);
		try {
			selector.persistObject(newProject);
			
			responseJSON.put("success", "Project Created");
		} catch (Exception e) {
			responseJSON.put("error", "Project NOT Created");
		} finally {
			resp.getWriter().print(responseJSON.toString());
			resp.getWriter().flush();
		}
	}
}
