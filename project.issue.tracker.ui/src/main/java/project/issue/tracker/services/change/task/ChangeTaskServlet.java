package project.issue.tracker.services.change.task;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





import project.issue.tracker.database.db.QuerySelector;
import project.issue.tracker.database.models.Task;
import project.issue.tracker.database.models.User;
//import project.issue.tracker.database.models.DBEvent;
import project.issue.tracker.utils.ATTRIBUTES;
import project.issue.tracker.utils.FORM_PARAMS;
import project.issue.tracker.utils.Utils;

@WebServlet(urlPatterns = {"/changeTask.do"}, name = "TaskChanger")
public class ChangeTaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        String taskID = req.getParameter(FORM_PARAMS.CHANGE_TASK.ID);
        String projectName = req.getParameter(FORM_PARAMS.CHANGE_TASK.PROJECT_NAME);
        String taskDDate = req.getParameter(FORM_PARAMS.CHANGE_TASK.DUE_DATE);
        String taskAssignee = req.getParameter(FORM_PARAMS.CHANGE_TASK.ASSIGNEE);
        String taskStatus = req.getParameter(FORM_PARAMS.CHANGE_TASK.TASK_STATUS);

        User userBean = (User) req.getSession().getAttribute(ATTRIBUTES.USER_BEAN);

        PrintWriter out = resp.getWriter();
        
        QuerySelector selector = QuerySelector.getInstance();
        Task currTask = selector.getTaskById(taskID);

        if (Utils.isNull(currTask.getTitle())) {
            out.print("{\"error\":\"Task not found\"}");
            out.flush();
            return;
        }

        User currTaskAssignee = currTask.getAssignee();
        User targetedAssignee = selector.getUsersByName(taskAssignee).get(0);
        if (targetedAssignee == null) {
            out.print("{\"error\":\"No assignee found with such name.\"}");
            out.flush();
            return;
        }

        int taskCount = 0;
        for (Task t : selector.getAllTasks()) {
            if (t.getAssignee().getFullName().equals(targetedAssignee.getFullName()) && (t.getStatus().equals("In Process") || t.getStatus().equals("Open"))) {
                taskCount++;
            }
        }

//        if (!currTask.getStatus().equals(taskStatus) && userBean.getFullName().equals(currTaskAssignee.getFullName())) {
//            DBEvent event = new DBEvent(DBEvent.TYPE_CHANGE_STATUS, taskID, project.getId(), currTask.getStatus(), userBean.getId(), taskStatus);
//            if (taskStatus.equals("In Process")) {
//                currTask.setStatus(Task.STATUS_IN_PROGESS);
//            } else if (taskStatus.equals("Completed")) {
//                currTask.setStatus(Task.STATUS_COMPLETED);;
//            } else if (taskStatus.equals("Open")) {
//                currTask.setStatus(Task.STATUS_OPEN);
//            }
//            event.save();
//        }
//        if (!currTask.getAssignee().getFullName().equals(targetedAssignee.getFullName())) {
//            DBEvent event = new DBEvent(DBEvent.TYPE_CHANGE_ASSIGNEE, taskID, project.getId(), currTask.getAssigneeId(), userBean.getId(), targetedAssignee.getId());
//            event.save();
//            currTask.updateAssignie(targetedAssignee.getId());
//        }
//        
//        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy/hh:mm:ss");
//		Date parsedDate = formatter.parse(taskDDate);
//        if (!currTask.getDueDate().equals(parsedDate)) {
//            DBEvent event = new DBEvent(DBEvent.TYPE_CHANGE_DATE, taskID, project.getId(), currTask.getDueDate(), userBean.getId(), taskDDate);
//            event.save();
//            currTask.updateDueDate(taskDDate);
//        }


        out.print("{\"success\":\"Change accepted." + (taskCount >= 2 ? "Note that this user has 2 or more " +
                "tasks assigned, that he is currently working on." : "") + "\"}");
        out.flush();
    }
}
