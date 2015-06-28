package project.issue.tracker.services.view;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import project.issue.tracker.database.models.DBComment;
import project.issue.tracker.database.models.DBProject;
import project.issue.tracker.database.models.DBTask;
import project.issue.tracker.database.models.DBUser;
import project.issue.tracker.utils.FORM_PARAMS;

@WebServlet(urlPatterns = {"/viewTask.do"}, name = "TaskViewer")
public class TaskViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        PrintWriter out = resp.getWriter();

        String taskID = req.getParameter(FORM_PARAMS.VIEW_TASK.ID);
        String projectName = req.getParameter(FORM_PARAMS.VIEW_TASK.PROJECT_NAME).toLowerCase();

        DBProject project = null;
        for (DBProject p : DBProject.getAllProjects()) {
            if (p.getName().toLowerCase().equals(projectName)) {
                project = p;
            }
        }
        if (null == project) {
            out.print("\"error\":\"no such project\"");
            out.flush();
            return;
        }
        DBTask task = new DBTask(taskID, project.getId());
        if (null == task.getTitle()) {
            out.print("{\"error\":\"no such task refresh list plase\"}");
        }

        JSONObject jsonResponse = new JSONObject();
        JSONObject taskObject = new JSONObject();

        taskObject.put("id", task.getId());
        taskObject.put("project", project.getName());
        taskObject.put("title", task.getTitle());
        taskObject.put("desc", task.getDescription());
        taskObject.put("ddate", task.getDueDate());
        taskObject.put("assignee", new DBUser(task.getAssigneeId()).getName());
        taskObject.put("status", task.getStatus());

        jsonResponse.put("task", taskObject);

        JSONArray comments = new JSONArray();
        for (DBComment c : new DBComment(project.getId(), task.getId()).getAllComments()) {
            JSONObject comment = new JSONObject();

            comment.put("from", new DBUser(c.getAuthorId()).getName());
            comment.put("date", c.getDate());
            comment.put("text", c.getContent());

            comments.add(comment);
        }

        jsonResponse.put("comments", comments);

        resp.getWriter().print(jsonResponse.toString());
        resp.getWriter().flush();
    }
}
