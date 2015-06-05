package project.issue.tracker.database.utils;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSystem {

    public static final String MAIL_FROM = "system@debugtracker.com";
    public static final String MAIL_SUBJECT = "DebugTracket Event Log";

    public static void sendMail(String toMail, String msg) {
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
        }
    }
}
