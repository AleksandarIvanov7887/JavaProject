package project.issue.tracker.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import project.issue.tracker.database.db.event.EventRunnable;
import project.issue.tracker.database.db.event.EventSystem;

@WebListener(value = "App initilization event listener")
public class DummyContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        String hour = sce.getServletContext().getInitParameter("update.hour");
        String minute = sce.getServletContext().getInitParameter("update.minute");

        EventSystem.init(hour, minute);

        EventSystem.event_thread = new Thread(new EventRunnable());
        EventSystem.event_thread.start();

    }

    public void contextDestroyed(ServletContextEvent sce) {
        if (EventSystem.event_thread != null) {
            EventSystem.event_thread.interrupt();
        }
    }
}
