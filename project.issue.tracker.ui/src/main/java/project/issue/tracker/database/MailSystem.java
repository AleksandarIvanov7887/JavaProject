package project.issue.tracker.database;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import project.issue.tracker.database.models.Task;
import project.issue.tracker.database.models.User;

public class MailSystem {

    public static final String MAIL_FROM = "system@debugtracker.com";
    public static final String MAIL_SUBJECT = "DebugTracket Event Log";

    private static void sendMail(String toMail, String msg) {
        String host = "localhost";
        Properties properties = System.getProperties();
        properties.setProperty("mail.debugtracker.com", host);
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setHeader("Content-Type", "text/html; charset=utf-8");
            message.setFrom(new InternetAddress(MAIL_FROM));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
            message.setSubject(MAIL_SUBJECT, "UTF-8");

            message.setContent(msg, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException mex) {
        	mex.printStackTrace();
        }
    }

	public static void sendMailAboutComment(Task task, User userBean, String comment) {
		String userEmail = userBean.getEmail();
		String userName = userBean.getFullName();
		String taskTitle = task.getTitle();
		sendMail(userEmail, userName + " made a comment about the task - " + taskTitle + " : " + comment);
	}

	public static void sendMailAboutChangedStatus(Task currTask) {
		String userEmail = currTask.getAuthor().getEmail();
		String userName = currTask.getAssignee().getFullName();
		String taskTitle = currTask.getTitle();
		String status = currTask.getStatus();
		sendMail(userEmail, userName + " changed the status of the task - " + taskTitle + " to " + status);
	}

	public static void sendMailAboutAssigneChange(Task currTask) {
		String userEmail = currTask.getAuthor().getEmail();
		String userName = currTask.getAssignee().getFullName();
		String currTaskTitle = currTask.getTitle();
		sendMail(userEmail, "The new assigne for task - " + currTaskTitle + " is " + userName);
	}

	public static void sendMailAboutNewDueDate(Task currTask) {
		String userEmail = currTask.getAuthor().getEmail();
		String dueDate = currTask.getDueDate().toString();
		String currTaskTitle = currTask.getTitle();
		sendMail(userEmail, "The new due date for task - " + currTaskTitle + " is " + dueDate);
		
	}
}
