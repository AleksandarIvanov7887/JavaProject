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
import project.issue.tracker.database.db.QuerySelector;
import project.issue.tracker.database.models.Comment;
import project.issue.tracker.database.models.Task;
import project.issue.tracker.utils.FORM_PARAMS;

@WebServlet(urlPatterns = {"/viewTask.do"}, name = "TaskViewer")
public class TaskViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        PrintWriter out = resp.getWriter();

        String taskID = req.getParameter(FORM_PARAMS.VIEW_TASK.ID);
        QuerySelector selector = QuerySelector.getInstance();
        Task task = selector.getTaskById(taskID);
        
        if (task == null || task.getProject() == null) {
            out.print("\"error\":\"no such project or task\"");
            out.flush();
            return;
        }

        JSONObject jsonResponse = new JSONObject();
        JSONObject taskObject = new JSONObject();

        taskObject.put("id", task.getId());
        taskObject.put("project", task.getProject().getProjectName());
        taskObject.put("title", task.getTitle());
        taskObject.put("desc", task.getDescription());
        taskObject.put("ddate", task.getDueDate());
        taskObject.put("assignee", task.getAssignee().getFullName());
        taskObject.put("status", task.getStatus());

        jsonResponse.put("task", taskObject);

        JSONArray comments = new JSONArray();
        for (Comment c : task.getComments()) {
            JSONObject comment = new JSONObject();

            comment.put("from", c.getAuthor().getFullName());
            comment.put("date", c.getDate());
            comment.put("text", c.getContent());

            comments.add(comment);
        }

        jsonResponse.put("comments", comments);

        resp.getWriter().print(jsonResponse.toString());
        resp.getWriter().flush();
    }
}
