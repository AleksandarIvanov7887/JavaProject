package project.issue.tracker.services.change.task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.issue.tracker.database.db.QuerySelector;
import project.issue.tracker.database.models.Comment;
//import project.issue.tracker.database.models.DBEvent;
import project.issue.tracker.database.models.Task;
import project.issue.tracker.database.models.User;
import project.issue.tracker.database.utils.MailSystem;
import project.issue.tracker.utils.ATTRIBUTES;
import project.issue.tracker.utils.FORM_PARAMS;

import java.io.IOException;
import java.util.Date;

@WebServlet(urlPatterns = {"/addComment.do"}, name = "TaskCommenter")
public class AddCommentToTaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        String taskID = req.getParameter(FORM_PARAMS.ADD_COMMENT.TASK_ID);
        String comment = req.getParameter(FORM_PARAMS.ADD_COMMENT.COMMENT);

        User currentUser = (User) req.getSession().getAttribute(ATTRIBUTES.USER_BEAN);

        QuerySelector selector = QuerySelector.getInstance();
        
        Task task = selector.getTaskById(taskID);
        if (task == null ) {
            resp.getWriter().print("{\"error\":\"Error: comment not added.\nProject does not exist.\"}");
            resp.getWriter().flush();
            return;
        }
        try {
        	Comment newComment = new Comment();
        	newComment.setAuthor(currentUser);
        	newComment.setContent(comment);
        	newComment.setDate(new Date());
        	newComment.setTask(task);
        	
        	selector.persistObject(newComment);
            resp.getWriter().print("{\"success\":\"comment added\"}");
            
            MailSystem.sendMailAboutComment(task, currentUser, comment);
            
        } catch (Exception e) {
        	e.printStackTrace();
            resp.getWriter().print("{\"error\":\"Error: comment not added\"}");
        }

        resp.getWriter().flush();
    }
}
