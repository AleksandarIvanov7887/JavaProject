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
import project.issue.tracker.database.models.DBTask;
import project.issue.tracker.database.models.DBUser;
import project.issue.tracker.utils.ATTRIBUTES;
import project.issue.tracker.utils.FORM_PARAMS;
import project.issue.tracker.utils.Utils;

@WebServlet(urlPatterns = {"/searchTask.do"}, name = "taskSearch")
public class TaskSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        String projectName = req.getParameter(FORM_PARAMS.LIST_TASK.PROJECT_NAME);
        String taskName = req.getParameter(FORM_PARAMS.LIST_TASK.TASK_NAME);
        String assignee = req.getParameter(FORM_PARAMS.LIST_TASK.ASSIGNEE);
        String status = req.getParameter(FORM_PARAMS.LIST_TASK.STATUS);

        DBUser userBean = (DBUser) req.getSession().getAttribute(ATTRIBUTES.USER_BEAN);

        PrintWriter out = resp.getWriter();
        JSONObject responseJSON = new JSONObject();
        JSONArray tasksArray = new JSONArray();

        if (Utils.isNull(projectName) && Utils.isNull(taskName) && Utils.isNull(assignee) && Utils.isNull(status)) {
            if (!Utils.isAdmin(req)) {
                return;
            }
            responseJSON.put("important", "true");

            for (DBTask task : userBean.getAllImportantTasks()) {
                DBProject project = new DBProject(task.getProjectId());
                DBUser user = new DBUser(task.getAssigneeId());
                JSONObject dummy = new JSONObject();

                dummy.put("tID", task.getId());
                dummy.put("tName", task.getTitle());
                dummy.put("tProject", project.getName());
                dummy.put("tAssignee", user.getName());
                dummy.put("tStatus", task.getStatus());
                dummy.put("tDDate", task.getDueDate());

                tasksArray.add(dummy);
            }
        } else {

            for (DBTask task : DBTask.getAllTasks()) {
                DBProject project = new DBProject(task.getProjectId());
                DBUser user = new DBUser(task.getAssigneeId());
                if (!Utils.isNull(projectName) && !project.getName().contains(projectName)) {
                    continue;
                }
                if (!Utils.isNull(taskName) && !task.getTitle().contains(taskName)) {
                    continue;
                }
                if (!Utils.isNull(assignee) && !user.getName().contains(assignee)) {
                    continue;
                }
                if (!Utils.isNull(status) && !task.getStatus().contains(status)) {
                    continue;
                }

                JSONObject dummy = new JSONObject();

                dummy.put("tID", task.getId());
                dummy.put("tName", task.getTitle());
                dummy.put("tProject", project.getName());
                dummy.put("tAssignee", user.getName());
                dummy.put("tStatus", task.getStatus());
                dummy.put("tDDate", task.getDueDate());

                tasksArray.add(dummy);
            }
        }

        responseJSON.put("tasks", tasksArray);
        out.print(responseJSON.toString());
        out.flush();
    }
}
