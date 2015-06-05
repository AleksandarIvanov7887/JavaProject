package com.track.be.db.event;

public class EventSystem {

    public static Thread event_thread;

    private int hour;

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public EventSystem(int hour, int minute) {

        this.hour = hour;
        this.minute = minute;
    }

    private int minute;

    private static EventSystem instance;

    public static final void init(String h, String m) {
        if (instance != null) {
            return;
        }

        int hour = 22;
        int minute = 15;
        try {
            hour = Integer.parseInt(h);
            minute = Integer.parseInt(m);
        } catch (NumberFormatException e) {
            throw new RuntimeException("In web.xml some update.hour or .minute MUST be valid HH:MM EET format." +
                    "Currently they are 22:15 every evening");
        } finally {
            instance = new EventSystem(hour, minute);
        }
    }

    public static EventSystem getInstance() {
        return instance;
    }
}
