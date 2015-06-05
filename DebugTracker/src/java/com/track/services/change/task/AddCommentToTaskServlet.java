package com.track.services.change.task;

import com.track.be.models.DBComment;
import com.track.be.models.DBEvent;
import com.track.be.models.DBProject;
import com.track.be.models.DBUser;
import com.track.utils.ATTRIBUTES;
import com.track.utils.FORM_PARAMS;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(urlPatterns = {"/addComment.do"}, name = "TaskCommenter")
public class AddCommentToTaskServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        String taskID = req.getParameter(FORM_PARAMS.ADD_COMMENT.TASK_ID);
        String comment = req.getParameter(FORM_PARAMS.ADD_COMMENT.COMMENT);
        String project_name = req.getParameter(FORM_PARAMS.ADD_COMMENT.PROJECT_NAME);

        DBUser userBean = (DBUser) req.getSession().getAttribute(ATTRIBUTES.USER_BEAN);

        DBProject project = null;
        for (DBProject p : DBProject.getAllProjects()) {
            if (p.getName().equals(project_name)) {
                project = p;
            }
        }
        if (project == null) {
            resp.getWriter().print("{\"error\":\"Error: comment not added.\nProject does not exist.\"}");
            resp.getWriter().flush();
            return;
        }

        DBComment commentDB = new DBComment(userBean.getId(), new SimpleDateFormat("dd-MM-yyyy").format(new Date()), comment, project.getId(), taskID);
        if (commentDB.save()) {
            resp.getWriter().print("{\"success\":\"comment added\"}");
            DBEvent event = new DBEvent(DBEvent.TYPE_ADD_COMMENT, taskID, project.getId(), "", userBean.getId(), comment);
            event.save();
        } else {
            resp.getWriter().print("{\"error\":\"Error: comment not added\"}");
        }

        resp.getWriter().flush();
    }
}
