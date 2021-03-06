package com.track.be.db.event;

import com.track.be.models.DBEvent;
import com.track.be.models.DBTask;
import com.track.be.models.DBUser;
import com.track.be.utilities.MailSystem;

import java.util.Calendar;
import java.util.List;

public class EventRunnable implements Runnable {

    @Override
    public void run() {
        for (; ; ) {
            try {
                long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();

                Calendar eventCalendar = Calendar.getInstance();
                eventCalendar.setTimeInMillis(currentTimeInMillis);
                eventCalendar.add(Calendar.DAY_OF_YEAR, 1);
                eventCalendar.set(Calendar.HOUR, EventSystem.getInstance().getHour());
                eventCalendar.set(Calendar.MINUTE, EventSystem.getInstance().getMinute());
                eventCalendar.set(Calendar.AM_PM, Calendar.AM);

                long eventTime = eventCalendar.getTimeInMillis();

                long sleepTime = eventTime - currentTimeInMillis;

                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                EventSystem.event_thread = null;
                break;
            }

            //send emails to users
            List<DBEvent> eventList = DBEvent.getAllWaitingEvents();
            for (DBEvent event : eventList) {
                DBTask task = new DBTask(event.getTaskId(), event.getProjectId());
                DBUser user = new DBUser(task.getAssigneeId());
                String email = user.getEmail();

                MailSystem.sendMail(email, event.toString());

                event.deleteFromWaiting();
            }

        }
    }
}
