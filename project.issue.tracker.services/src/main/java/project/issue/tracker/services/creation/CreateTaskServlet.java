package project.issue.tracker.services.creation;

import com.track.be.models.DBComment;
import com.track.be.models.DBProject;
import com.track.be.models.DBTask;
import com.track.be.models.DBUser;
import com.track.utils.ATTRIBUTES;
import com.track.utils.FORM_PARAMS;
import com.track.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(urlPatterns = {"/createTask.do"}, name = "TaskCreator")
public class CreateTaskServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        String taskName = req.getParameter(FORM_PARAMS.CREATE_TASK.TASK_NAME); //^[a-zA-Z_0-9 \.\-,:]{1,20}$
        String projectName = req.getParameter(FORM_PARAMS.CREATE_TASK.PROJECT); //^[a-zA-Z_0-9 \.\-,:]{1,20}$
        String description = req.getParameter(FORM_PARAMS.CREATE_TASK.DESCRIPTION);//^[a-zA-Z_0-9 \.\-,:]{1,20}$
        String dueDate = req.getParameter(FORM_PARAMS.CREATE_TASK.DUE_DATE);//^\d{1,2}-\d{1,2}-\d{4}&
        String assignee = req.getParameter(FORM_PARAMS.CREATE_TASK.ASSIGNEE);//^[a-zA-Z_0-9 \.\-,:]{1,20}$
        String status = req.getParameter(FORM_PARAMS.CREATE_TASK.STATUS);//^Open|In Process|Completed&
        String important = req.getParameter(FORM_PARAMS.CREATE_TASK.IMPORTANT);
        String comment = req.getParameter(FORM_PARAMS.CREATE_TASK.COMMENT);//^[a-zA-Z_0-9 \.\-,:]{1,20}$

        PrintWriter out = resp.getWriter();

        DBUser userBean = (DBUser) req.getSession().getAttribute(ATTRIBUTES.USER_BEAN);

        if (!taskName.matches("^[a-zA-Z_0-9 \\.\\-,:]{1,50}$") ||
                !projectName.matches("^[a-zA-Z_0-9 \\.\\-,:]{1,50}$") ||
                !description.matches("^^[a-zA-Z_0-9\\s\\.\\-,:()]{1,2000}$$") ||
                !assignee.matches("^^[a-zA-Z \\-]{1,50}$$") ||
                !comment.matches("^[a-zA-Z_0-9\\s\\.\\-,:()]{0,2000}$") ||
                !status.matches("^Open|In Process|Completed&")) {

            out.print("{\"error\":\"Not a valid data entered.\"}");
            out.flush();
            return;
        }

        DBProject taskParent = null;
        for (DBProject project : DBProject.getAllProjects()) {
            if (project.getName().equals(projectName)) {
                taskParent = project;
                break;
            }
        }
        if (taskParent == null) {
            out.print("{\"error\":\"no such project\"}");
            out.flush();
            return;
        }

        DBUser taskUser = null;
        for (DBUser user : DBUser.getAllUsers()) {
            if (user.getName().equals(assignee)) {
                taskUser = user;
                break;
            }
        }
        if (taskUser == null) {
            out.print("{\"error\":\"no such assignee\"}");
            out.flush();
            return;
        }

        int taskCount = 0;
        for (DBTask t : DBTask.getAllTasks()) {
            if (t.getAssigneeId().equals(taskUser.getId()) && (t.getStatus().equals("In Process") || t.getStatus().equals("Open"))) {
                taskCount++;
            }
            if (t.getTitle().equals(taskName)) {
                out.print("{\"error\":\"Task Title already exists.\"}");
                out.flush();
                return;
            }
        }

        DBTask task = new DBTask(taskParent.getId(), taskName, description, userBean.getId(), dueDate, taskUser.getId(), status);
        if (!task.save()) {
            out.print("{\"error\":\"task not created\"}");
            out.flush();
            return;
        }

        if (Boolean.valueOf(important)) {
            new DBUser(userBean.getId()).addToImportantTasks(taskParent.getId(), task.getId());
        }

        if (!Utils.isNull(comment)) {
            DBComment commentDB = new DBComment(userBean.getId(), new SimpleDateFormat("dd-MM-yyyy").format(new Date()), comment, taskParent.getId(), task.getId());
            if (!commentDB.save()) {
                out.print("{\"error\":\"task created but comment not added\"}");
                out.flush();
                return;
            }
        }

        // do something
        out.print("{\"success\":\"Task created" + (Utils.isNull(comment) ? "." : " and comment added.") +
                (taskCount >= 2 ? "Note that this user has more then 2 tasks assigned at the moment." : "") + "\"}");
        out.flush();
    }
}
