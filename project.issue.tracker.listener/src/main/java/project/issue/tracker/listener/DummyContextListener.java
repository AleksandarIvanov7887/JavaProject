package project.issue.tracker.listener;

import com.track.be.db.InitDB;
import com.track.be.db.event.EventRunnable;
import com.track.be.db.event.EventSystem;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener(value = "App initilization event listener")
public class DummyContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (Boolean.parseBoolean(sce.getServletContext().getInitParameter("initializeDB"))) {
            InitDB.start();
        }

        String hour = sce.getServletContext().getInitParameter("update.hour");
        String minute = sce.getServletContext().getInitParameter("update.minute");

        EventSystem.init(hour, minute);

        EventSystem.event_thread = new Thread(new EventRunnable());
        EventSystem.event_thread.start();

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (EventSystem.event_thread != null) {
            EventSystem.event_thread.interrupt();
        }
    }
}
