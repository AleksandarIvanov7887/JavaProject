package project.issue.tracker.services.view;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import project.issue.tracker.database.models.DBProject;
import project.issue.tracker.database.models.DBTask;
import project.issue.tracker.database.models.DBUser;

@WebServlet(urlPatterns = {"/updateAutocomplete.do"}, name = "Autocomplete Updater")
public class BackendInformaiton extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        JSONObject responseJSON = new JSONObject();
        JSONObject projects = new JSONObject();
        JSONObject tasks = new JSONObject();
        JSONObject users = new JSONObject();

        List<DBProject> projectList = DBProject.getAllProjects();
        for (int i = 0; i < projectList.size(); i++) {
            projects.put("" + i, projectList.get(i).getName());
        }
        List<DBTask> taskList = DBTask.getAllTasks();
        for (int i = 0; i < taskList.size(); i++) {
            tasks.put("" + i, taskList.get(i).getTitle());
        }
        List<DBUser> userList = DBUser.getAllUsers();
        for (int i = 0; i < userList.size(); i++) {
            users.put("" + i, userList.get(i).getName());
        }

        responseJSON.put("projects", projects);
        responseJSON.put("projectsLength", projectList.size());
        responseJSON.put("tasks", tasks);
        responseJSON.put("tasksLength", taskList.size());
        responseJSON.put("users", users);
        responseJSON.put("usersLength", userList.size());

        resp.getWriter().print(responseJSON.toString());
        resp.getWriter().flush();
    }
}
