package project.issue.tracker.services.creation;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.issue.tracker.database.db.QuerySelector;
import project.issue.tracker.database.models.Comment;
import project.issue.tracker.database.models.Project;
import project.issue.tracker.database.models.Task;
import project.issue.tracker.database.models.User;
import project.issue.tracker.utils.ATTRIBUTES;
import project.issue.tracker.utils.FORM_PARAMS;
import project.issue.tracker.utils.Utils;

@WebServlet(urlPatterns = { "/createTask.do" }, name = "TaskCreator")
public class CreateTaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String REG_EX_TASK_NAME = "^[a-zA-Z_0-9 \\.\\-,:]{1,50}$";
	private static final String REG_EX_ASSIGNE = "^[a-zA-Z \\-]{1,50}$";
	private static final String REG_EX_DESC = "^^[a-zA-Z_0-9\\s\\.\\-,:()]{1,2000}$$";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/json");

		String taskName = req.getParameter(FORM_PARAMS.CREATE_TASK.TASK_NAME);
		String projectName = req.getParameter(FORM_PARAMS.CREATE_TASK.PROJECT);
		String description = req.getParameter(FORM_PARAMS.CREATE_TASK.DESCRIPTION);
		String dueDate = req.getParameter(FORM_PARAMS.CREATE_TASK.DUE_DATE);
		String assignee = req.getParameter(FORM_PARAMS.CREATE_TASK.ASSIGNEE);
		String status = req.getParameter(FORM_PARAMS.CREATE_TASK.STATUS);
		String important = req.getParameter(FORM_PARAMS.CREATE_TASK.IMPORTANT);
		String comment = req.getParameter(FORM_PARAMS.CREATE_TASK.COMMENT);

		PrintWriter out = resp.getWriter();

		User userBean = (User) req.getSession().getAttribute(
				ATTRIBUTES.USER_BEAN);

		if (!taskName.matches(REG_EX_TASK_NAME)
				|| !description
						.matches(REG_EX_DESC)
				|| !assignee.matches(REG_EX_ASSIGNE)
				|| !status.matches("^Open|In Process|Completed&")) {

			out.print("{\"error\":\"Not a valid data entered.\"}");
			out.flush();
			return;
		}

		QuerySelector selector = QuerySelector.getInstance();
		List<Project> projects = selector.getProjectByName(projectName);

		Project taskParent = null;
		if (!projects.isEmpty()) {
			taskParent = projects.get(0);
		}
		if (taskParent == null) {
			out.print("{\"error\":\"no such project\"}");
			out.flush();
		}

		List<User> users = selector.getUsersByName(assignee);
		User taskUser = null;
		if (!users.isEmpty()) {
			taskUser = users.get(0);
		}
		if (taskUser == null) {
			out.print("{\"error\":\"no such assignee\"}");
			out.flush();
			return;
		}

		int taskCount = 0;
		for (Task t : selector.getAllTasks()) {
			if (t.getAssignee().equals(taskUser)
					&& (t.getStatus().equals("In Process") || t.getStatus()
							.equals("Open"))) {
				taskCount++;
			}
			if (t.getTitle().equals(taskName)) {
				out.print("{\"error\":\"Task Title already exists.\"}");
				out.flush();
				return;
			}
		}

		Task newTask = new Task();
		newTask.setStatus(status);
		newTask.setProject(taskParent);
		newTask.setTitle(taskName);
		newTask.setDescription(description);
		newTask.setAuthor(userBean);
		newTask.setAssignee(taskUser);
		if (Boolean.valueOf(important)) {
			newTask.setImportant(true);
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		Date parsedDate;
		try {
			parsedDate = formatter.parse(dueDate);
			newTask.setDueDate(parsedDate);
			selector.persistObject(newTask);
		} catch (Exception exc) {
			out.print("{\"error\":\"task not created\"}");
			out.flush();
			
			exc.printStackTrace();
			return;
		}


		if (!Utils.isNull(comment)) {
			Comment newComment = new Comment();
			newComment.setAuthor(userBean);
			newComment.setContent(comment);
			newComment.setTask(newTask);
			newComment.setDate(new Date());
			
			try {
				selector.persistObject(newComment);
			} catch (Exception exc) {
				out.print("{\"error\":\"task created but comment not added\"}");
				out.flush();
				return;
			}
		}

		out.print("{\"success\":\"Task created"
				+ (Utils.isNull(comment) ? "." : " and comment added.")
				+ (taskCount >= 2 ? "Note that this user has more then 2 tasks assigned at the moment."
						: "") + "\"}");
		out.flush();
	}
}
