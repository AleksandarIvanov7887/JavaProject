package com.track.be.models;

import com.track.be.db.Database;
import com.track.be.db.Fields;

import java.util.*;

public class DBTask {

    private static final String STATUS_OPEN = "1";
    private static final String STATUS_IN_PROGESS = "2";

    public String getStatus() {
        if ("1".equals(status)) {
            return "Open";
        } else if ("2".equals(status)) {
            return "In Process";
        } else if("3".equals(status)) {
            return "Completed";
        }
        return status;
    }

    private static final String STATUS_COMPLETED = "3";
    private static final String IMPORTANT = "1";
    private static final String NOT_IMPORTANT = "0";

    private String id;
    private String projectId;
    private String title;
    private String description;
    private String authorId;
    private String dueDate;
    private String assigneeId;
    private String status;
    private List<DBComment> comments;

    public DBTask(String projectId) {
        this.projectId = projectId;
    }

    public DBTask(String id, String projectId) {
        this.id = id;
        this.projectId = projectId;
        init();
    }

    public DBTask(String projectId, String title, String description, String authorId, String dueDate, String assigneeId, String status) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.authorId = authorId;
        this.dueDate = dueDate;
        this.assigneeId = assigneeId;
        this.status = status;
    }

    public DBTask(String id, String projectId, String title, String description, String authorId, String dueDate, String assigneeId, String status) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.authorId = authorId;
        this.dueDate = dueDate;
        this.assigneeId = assigneeId;
        this.status = status;
    }

    public boolean save() {
        final Database db = new Database();

        try {
            this.id = db.insertWithReturnKey(Fields.TABLE_PROJECTS_TASKS, this.title , this.projectId, Fields.C_ALL_TASKS, "", "", "");
            db.delete(Fields.TABLE_PROJECTS_TASKS, false, this.projectId, Fields.C_ALL_TASKS, this.id, "", "");

            List<Database.Pair> dataList = new ArrayList<Database.Pair>();
            dataList.add(new Database.Pair(Fields.TITLE, this.title));
            dataList.add(new Database.Pair(Fields.DESCRIPTION, this.description));
            dataList.add(new Database.Pair(Fields.AUTHOR_ID, this.authorId));
            dataList.add(new Database.Pair(Fields.DUE_DATE, this.dueDate));
            dataList.add(new Database.Pair(Fields.ASSIGNEE, this.assigneeId));
            dataList.add(new Database.Pair(Fields.STATUS, DBTask.STATUS_OPEN));
            db.insertWithKey(Fields.TABLE_PROJECTS_TASKS, dataList, this.projectId, this.id, "", "", "");

        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    private void init() {
        final Database db = new Database();
        try {
            TreeMap<String, Object> profileDataMap = db.select(Fields.TABLE_PROJECTS_TASKS, this.projectId, this.id, "", "", "");

            this.title = profileDataMap.get(Fields.TITLE).toString();
            this.description = profileDataMap.get(Fields.DESCRIPTION).toString();
            this.authorId = profileDataMap.get(Fields.AUTHOR_ID).toString();
            this.dueDate = profileDataMap.get(Fields.DUE_DATE).toString();
            this.assigneeId = profileDataMap.get(Fields.ASSIGNEE).toString();
            this.status = profileDataMap.get(Fields.STATUS).toString();
            DBComment commentList = new DBComment(this.projectId, this.id);
            this.comments = commentList.getAllComments();

        } finally {
            db.close();
        }
    }

    public List<DBTask> getAllTasksByProject() {
        List<DBTask> list = new ArrayList<DBTask>();
        final Database db = new Database();
        try {
            TreeMap<String, Object> tasksMap = db.select(Fields.TABLE_PROJECTS_TASKS, this.projectId, "", "", "", "");

            if (tasksMap != null) {
                for (Map.Entry<String, Object> task : tasksMap.entrySet()) {
                    try {
                        String tempId = task.getKey();

                        if (tempId.equals(Fields.C_ALL_TASKS)) {
                            continue;
                        }
                        TreeMap<String, Object> taskDataMap = (TreeMap<String, Object>) task.getValue();
                        String tempTitle = taskDataMap.get(Fields.TITLE).toString();
                        String tempDescription = taskDataMap.get(Fields.DESCRIPTION).toString();
                        String tempAuthorId = taskDataMap.get(Fields.AUTHOR_ID).toString();
                        String tempDueDate = taskDataMap.get(Fields.DUE_DATE).toString();
                        String tempAssignee = taskDataMap.get(Fields.ASSIGNEE).toString();
                        String tempStatus = taskDataMap.get(Fields.STATUS).toString();

                        DBTask dbTask = new DBTask(tempId, projectId, tempTitle, tempDescription, tempAuthorId, tempDueDate, tempAssignee, tempStatus);
                        list.add(dbTask);

                    } catch (NullPointerException e) {
                    }
                }
            }
        } finally {
            db.close();
        }

        return list;
    }

    public static List<DBTask> getAllTasks() {
        List<DBTask> list = new ArrayList<DBTask>();
        final Database db = new Database();
        try {
            TreeMap<String, Object> tasksMap = db.select(Fields.TABLE_PROJECTS_TASKS, "", "", "", "", "");

            if (tasksMap != null) {
                for (Map.Entry<String, Object> project : tasksMap.entrySet()) {
                    try {
                        String tempProjectId = project.getKey();

                        TreeMap<String, Object> projectDataMap = (TreeMap<String, Object>) project.getValue();
                        for (Map.Entry<String, Object> task : projectDataMap.entrySet()) {
                            String tempId = task.getKey();
                            if (tempId.equals(Fields.C_ALL_TASKS)) {
                                continue;
                            }
                            TreeMap<String, Object> taskDataMap = (TreeMap<String, Object>) task.getValue();
                            String tempTitle = taskDataMap.get(Fields.TITLE).toString();
                            String tempDescription = taskDataMap.get(Fields.DESCRIPTION).toString();
                            String tempAuthorId = taskDataMap.get(Fields.AUTHOR_ID).toString();
                            String tempDueDate = taskDataMap.get(Fields.DUE_DATE).toString();
                            String tempAssignee = taskDataMap.get(Fields.ASSIGNEE).toString();
                            String tempStatus = taskDataMap.get(Fields.STATUS).toString();

                            DBTask dbTask = new DBTask(tempId, tempProjectId, tempTitle, tempDescription, tempAuthorId, tempDueDate, tempAssignee, tempStatus);
                            list.add(dbTask);
                        }

                    } catch (NullPointerException e) {
                    }
                }
            }
        } finally {
            db.close();
        }
        Collections.reverse(list);
        return list;
    }

    public boolean addToChanges(String type,String authorId,String assigneeId,String content) {
        final Database db = new Database();
        try {
            String key = db.insertWithReturnKey(Fields.TABLE_PROJECTS_TASKS, this.projectId, this.id, Fields.C_ALL_CHANGES, "", "");
            db.delete(Fields.TABLE_PROJECTS_TASKS,false, this.projectId, key, Fields.C_ALL_CHANGES, key, "");

            List<Database.Pair> dataList = new ArrayList<Database.Pair>();
            dataList.add(new Database.Pair(Fields.TYPE, type));
            dataList.add(new Database.Pair(Fields.AUTHOR_ID, authorId));
            dataList.add(new Database.Pair(Fields.AUTHOR_ID, authorId));
            dataList.add(new Database.Pair(Fields.TASK_ASSIGNEE_ID, assigneeId));
            dataList.add(new Database.Pair(Fields.CONTENT_OLD, content));
            db.insertWithKey(Fields.TABLE_USER_DATA,dataList, this.projectId, this.id, Fields.C_CHANGES, key, "");
        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }
    
        public  List<DBEvent> getAllEvents() {
        List<DBEvent> list = new ArrayList<DBEvent>();
        final Database db = new Database();
        try {
            TreeMap<String, Object> changesMap = db.select(Fields.TABLE_USER_DATA, this.projectId, this.id, Fields.C_CHANGES, "", "");

            if (changesMap != null) {
                for (Map.Entry<String, Object> event : changesMap.entrySet()) {
                    try {
                        String tempId = event.getKey();
                        TreeMap<String, Object> eventDataMap = (TreeMap<String, Object>) event.getValue();
                        String tempType = eventDataMap.get(Fields.TYPE).toString();
                        String tempTaskId = eventDataMap.get(Fields.TASK_ID).toString();
                        String tempAuthorId = eventDataMap.get(Fields.AUTHOR_ID).toString();
                        String tempAssignee = eventDataMap.get(Fields.TASK_ASSIGNEE_ID).toString();
                        String tempContent = eventDataMap.get(Fields.CONTENT_OLD).toString();

                        DBEvent dbEvent = new DBEvent(tempId, tempType, tempTaskId, tempAuthorId, tempAssignee, tempContent);
                        list.add(dbEvent);

                    } catch (NullPointerException e) {
                    }
                }
            }
        } finally {
            db.close();
        }

        Collections.reverse(list);
        return list;
    }

    public boolean updateTitle(String value) {
        final Database db = new Database();
        try {
            db.insertWithKey(Fields.TABLE_PROJECTS_TASKS, value, this.projectId, this.id, Fields.TITLE, "", "");
        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean updateDescription(String value) {
        final Database db = new Database();
        try {
            db.insertWithKey(Fields.TABLE_PROJECTS_TASKS, value, this.projectId, this.id, Fields.DESCRIPTION, "", "");
        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean updateDueDate(String value) {
        final Database db = new Database();
        try {
            db.insertWithKey(Fields.TABLE_PROJECTS_TASKS, value, this.projectId, this.id, Fields.DUE_DATE, "", "");
        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean updateAssignie(String value) {
        final Database db = new Database();
        try {
            db.insertWithKey(Fields.TABLE_PROJECTS_TASKS, value, this.projectId, this.id, Fields.ASSIGNEE, "", "");
        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean updateStatusToOpen() {
        return this.updateStatus(DBTask.STATUS_OPEN);
    }

    public boolean updateStatusToInProgress() {
        return this.updateStatus(DBTask.STATUS_IN_PROGESS);
    }

    public boolean updateStatusToCompleted() {
        return this.updateStatus(DBTask.STATUS_COMPLETED);
    }

    private boolean updateStatus(String value) {
        final Database db = new Database();
        try {
            db.insertWithKey(Fields.TABLE_PROJECTS_TASKS, value, this.projectId, this.id, Fields.STATUS, "", "");
        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean delete() {
        final Database db = new Database();

        try {
            db.delete(Fields.TABLE_PROJECTS_TASKS, true, this.projectId, this.id, "", "", "");

        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean isStatusOpen() {
        return status.equals(DBTask.STATUS_OPEN);
    }

    public boolean isStatusInProgress() {
        return status.equals(DBTask.STATUS_IN_PROGESS);
    }

    public boolean isStatusCompleted() {
        return status.equals(DBTask.STATUS_COMPLETED);
    }

    public String getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public List<DBComment> getComments() {
        return comments;
    }

}
