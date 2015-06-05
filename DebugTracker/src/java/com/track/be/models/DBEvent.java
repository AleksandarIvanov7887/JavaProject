package com.track.be.models;

import com.track.be.db.Database;
import com.track.be.db.Fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DBEvent {
    public static final String TYPE_CHANGE_DATE = "0";
    public static final String TYPE_CHANGE_ASSIGNEE = "1";
    public static final String TYPE_CHANGE_STATUS = "2";
    public static final String TYPE_ADD_COMMENT = "3";

    private String id;
    private String type;
    private String taskId;
    private String projectId;
    private String contentNew;
    private String authorId;
    private String contentOld;

    public DBEvent(String id) {
        this.id = id;
    }

    public DBEvent(String authorId, boolean nothing) {
        this.authorId = authorId;
    }

    public DBEvent(String taskId, String projectId) {
        this.taskId = taskId;
        this.projectId = projectId;
    }

    public DBEvent(String type, String taskId, String projectId, String contentOld, String authorId, String contentNew) {
        this.type = type;
        this.taskId = taskId;
        this.projectId = projectId;
        this.contentNew = contentNew;
        this.authorId = authorId;
        this.contentOld = contentOld;
    }

    public DBEvent(String id, String type, String taskId, String projectId, String contentOld, String authorId, String contentNew) {
        this.id = id;
        this.type = type;
        this.taskId = taskId;
        this.projectId = projectId;
        this.contentNew = contentNew;
        this.authorId = authorId;
        this.contentOld = contentOld;
    }

    public boolean save() {
        final Database db = new Database();

        try {
            this.id = db.insertWithReturnKey(Fields.TABLE_EVENTS, this.taskId, this.projectId, this.taskId, "", "");
            db.delete(Fields.TABLE_EVENTS, false, this.projectId, this.taskId, this.id, "");

            List<Database.Pair> dataList = new ArrayList<Database.Pair>();
            dataList.add(new Database.Pair(Fields.TYPE, this.type));
            dataList.add(new Database.Pair(Fields.AUTHOR_ID, this.authorId));
            dataList.add(new Database.Pair(Fields.CONTENT_NEW, this.contentNew));
            dataList.add(new Database.Pair(Fields.CONTENT_OLD, this.contentOld));
            db.insertWithKey(Fields.TABLE_EVENTS, dataList, this.projectId, this.taskId, this.id, "");

            db.insert(Fields.TABLE_EVENTS, this.id, this.authorId, this.projectId, this.taskId, ""); // put in userEventQueue - may have to change author.id
            db.insert(Fields.TABLE_EVENTS, this.id, Fields.C_WAITING_QUEUE, this.projectId, this.taskId, "");

        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public static List<DBEvent> getAllWaitingEvents() {
        List<DBEvent> list = new ArrayList<DBEvent>();
        final Database db = new Database();
        try {
            TreeMap<String, Object> eventsMap = db.select(Fields.TABLE_EVENTS, Fields.C_WAITING_QUEUE, "", "", "");

            if (eventsMap != null) {
                for (Map.Entry<String, Object> event : eventsMap.entrySet()) {
                    try {
                        String projectId = event.getKey();
                        TreeMap<String, Object> projectMap = (TreeMap<String, Object>) event.getValue();

                        for (Map.Entry<String, Object> taskEntry : projectMap.entrySet()) {
                            String taskId = taskEntry.getKey();
                            TreeMap<String, Object> eventMap = (TreeMap<String, Object>) event.getValue();

                            for (Map.Entry<String, Object> eventData : eventMap.entrySet()) {

                                String tempId = eventData.getKey();
                                String tempEventId = (String) eventData.getValue();

                                TreeMap<String, Object> eventDataMap = db.select(Fields.TABLE_EVENTS, projectId, taskId, tempEventId, "");
                                String tempType = eventDataMap.get(Fields.TYPE).toString();
                                String tempAuthorId = eventDataMap.get(Fields.AUTHOR_ID).toString();
                                String contentNew = eventDataMap.get(Fields.CONTENT_NEW).toString();
                                String contentOld = eventDataMap.get(Fields.CONTENT_OLD).toString();

                                DBEvent dbEvent = new DBEvent(tempId, tempType, taskId, projectId, contentOld, tempAuthorId, contentNew);
                                list.add(dbEvent);
                            }
                        }

                    } catch (NullPointerException e) {
                    }
                }
            }
        } finally {
            db.close();
        }

        return list;
    }

    @Override
    public String toString() {
        return new DBUser(this.getAuthorId()).getName() + " " + this.getType() + " " + this.getContentNew() +
                " for " + new DBTask(this.getTaskId(), this.getProjectId()).getTitle();
    }

    public List<DBEvent> getAllEventsForTask() {
        List<DBEvent> list = new ArrayList<DBEvent>();
        final Database db = new Database();
        try {
            TreeMap<String, Object> eventsMap = db.select(Fields.TABLE_EVENTS, this.projectId, this.taskId, "", "");

            if (eventsMap != null) {
                for (Map.Entry<String, Object> event : eventsMap.entrySet()) {
                    try {
                        String tempEventId = event.getKey();
                        TreeMap<String, Object> eventDataMap = (TreeMap<String, Object>) event.getValue();

                        String tempType = eventDataMap.get(Fields.TYPE).toString();
                        String tempAuthorId = eventDataMap.get(Fields.AUTHOR_ID).toString();
                        String contentNew = eventDataMap.get(Fields.CONTENT_NEW).toString();
                        String contentOld = eventDataMap.get(Fields.CONTENT_OLD).toString();

                        if (tempType.equals(TYPE_CHANGE_ASSIGNEE)) {
                            contentOld = new DBUser(contentOld).getName();
                            contentNew = new DBUser(contentNew).getName();
                        }

                        DBEvent dbEvent = new DBEvent(tempEventId, tempType, this.taskId, this.projectId, contentOld, tempAuthorId, contentNew);
                        list.add(dbEvent);

                    } catch (NullPointerException e) {
                    }
                }
            }
        } finally {
            db.close();
        }

        return list;
    }

    public boolean delete() {
        final Database db = new Database();

        try {
            db.delete(Fields.TABLE_EVENTS, true, this.id, "");

        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean deleteFromWaiting() {
        final Database db = new Database();

        try {
            db.delete(Fields.TABLE_EVENTS, false, Fields.C_WAITING_QUEUE, this.projectId, this.taskId, this.id, "");
        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean deleteUserEvents() {
        final Database db = new Database();

        try {
            db.delete(Fields.TABLE_EVENTS, false, this.authorId, this.projectId, this.taskId, this.id, "");
        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {

        if (type.equals(TYPE_ADD_COMMENT)) {
            return "Added a comment: ";
        } else if (type.equals(TYPE_CHANGE_ASSIGNEE)) {
            return "Changed the assignee to: ";
        } else if (type.equals(TYPE_CHANGE_DATE)) {
            return "Changed the due date to:";
        } else if (type.equals(TYPE_CHANGE_STATUS)) {
            return "Changed status to: ";
        } else {
            return "Warning no EVENT possible";
        }
    }

    public String getTypeAsEvent() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getContentNew() {
        return contentNew;
    }

    public void setContentNew(String contentNew) {
        this.contentNew = contentNew;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getContentOld() {
        return contentOld;
    }

    public void setContentOld(String contentOld) {
        this.contentOld = contentOld;
    }


}
