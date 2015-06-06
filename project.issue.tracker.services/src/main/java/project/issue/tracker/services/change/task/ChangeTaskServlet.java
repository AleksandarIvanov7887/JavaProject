package project.issue.tracker.services.change.task;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.issue.tracker.database.models.DBEvent;
import project.issue.tracker.database.models.DBProject;
import project.issue.tracker.database.models.DBTask;
import project.issue.tracker.database.models.DBUser;
import project.issue.tracker.utils.ATTRIBUTES;
import project.issue.tracker.utils.FORM_PARAMS;
import project.issue.tracker.utils.Utils;

@WebServlet(urlPatterns = {"/changeTask.do"}, name = "TaskChanger")
public class ChangeTaskServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        String taskID = req.getParameter(FORM_PARAMS.CHANGE_TASK.ID);
        String projectName = req.getParameter(FORM_PARAMS.CHANGE_TASK.PROJECT_NAME);
        String taskDDate = req.getParameter(FORM_PARAMS.CHANGE_TASK.DUE_DATE);
        String taskAssignee = req.getParameter(FORM_PARAMS.CHANGE_TASK.ASSIGNEE);
        String taskStatus = req.getParameter(FORM_PARAMS.CHANGE_TASK.TASK_STATUS);

        DBUser userBean = (DBUser) req.getSession().getAttribute(ATTRIBUTES.USER_BEAN);

        PrintWriter out = resp.getWriter();

        DBProject project = null;
        for (DBProject p : DBProject.getAllProjects()) {
            if (p.getName().equals(projectName)) {
                project = p;
                break;
            }
        }

        if (project == null) {
            out.print("{\"error\":\"No project found for task.\"}");
            out.flush();
            return;
        }

        DBTask currTask = new DBTask(taskID, project.getId());

        if (Utils.isNull(currTask.getTitle())) {
            out.print("{\"error\":\"Task not found\"}");
            out.flush();
            return;
        }

        DBUser currTaskAssignee = new DBUser(currTask.getAssigneeId());
        DBUser targetedAssignee = null;
        for (DBUser u : DBUser.getAllUsers()) {
            if (u.getName().equals(taskAssignee)) {
                targetedAssignee = u;
                break;
            }
        }

        if (targetedAssignee == null) {
            out.print("{\"error\":\"No assignee found with such name.\"}");
            out.flush();
            return;
        }

        int taskCount = 0;
        for (DBTask t : DBTask.getAllTasks()) {
            if (t.getAssigneeId().equals(targetedAssignee.getId()) && (t.getStatus().equals("In Process") || t.getStatus().equals("Open"))) {
                taskCount++;
            }
        }

        if (!currTask.getStatus().equals(taskStatus) && userBean.getName().equals(currTaskAssignee.getName())) {
            DBEvent event = new DBEvent(DBEvent.TYPE_CHANGE_STATUS, taskID, project.getId(), currTask.getStatus(), userBean.getId(), taskStatus);
            if (taskStatus.equals("In Process")) {
                currTask.updateStatusToInProgress();
            } else if (taskStatus.equals("Completed")) {
                currTask.updateStatusToCompleted();
            } else if (taskStatus.equals("Open")) {
                currTask.updateStatusToOpen();
            }
            event.save();
        }
        if (!currTask.getAssigneeId().equals(targetedAssignee.getId())) {
            DBEvent event = new DBEvent(DBEvent.TYPE_CHANGE_ASSIGNEE, taskID, project.getId(), currTask.getAssigneeId(), userBean.getId(), targetedAssignee.getId());
            event.save();
            currTask.updateAssignie(targetedAssignee.getId());
        }
        if (!currTask.getDueDate().equals(taskDDate)) {
            DBEvent event = new DBEvent(DBEvent.TYPE_CHANGE_DATE, taskID, project.getId(), currTask.getDueDate(), userBean.getId(), taskDDate);
            event.save();
            currTask.updateDueDate(taskDDate);
        }


        out.print("{\"success\":\"Change accepted." + (taskCount >= 2 ? "Note that this user has 2 or more " +
                "tasks assigned, that he is currently working on." : "") + "\"}");
        out.flush();
    }
}
