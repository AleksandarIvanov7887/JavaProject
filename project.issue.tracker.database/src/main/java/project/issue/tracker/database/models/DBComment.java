package project.issue.tracker.database.models;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import project.issue.tracker.database.db.Database;
import project.issue.tracker.database.db.Fields;

public class DBComment {

    private String id;
    private String authorId;
    private String date;
    private String content;
    private String projectId;
    private String taskId;
    private String authorName;

    public DBComment(String projectId, String taskId) {
        this.projectId = projectId;
        this.taskId = taskId;
    }

    public DBComment(String id, String projectId, String taskId) {
        this.id = id;
        this.projectId = projectId;
        this.taskId = taskId;
    }

    public DBComment(String authorId, String date, String content, String projectId, String taskId) {
        this.authorId = authorId;
        this.date = date;
        this.content = content;
        this.projectId = projectId;
        this.taskId = taskId;
    }

    public DBComment(String id, String authorId, String date, String content, String authorName,boolean init) {
        this.id = id;
        this.authorId = authorId;
        this.date = date;
        this.content = content;
        this.authorName = authorName;
    }

    public boolean save() {
        final Database db = new Database();

        try {
            this.id = db.insertWithReturnKey(Fields.TABLE_PROJECTS_TASKS, this.authorId, this.projectId, this.taskId, Fields.C_ALL_COMMENTS, "", "");
            db.delete(Fields.TABLE_PROJECTS_TASKS, false, this.projectId, this.taskId, Fields.C_ALL_COMMENTS, this.id, "");

            List<Database.Pair> dataList = new ArrayList<Database.Pair>();
            dataList.add(new Database.Pair(Fields.AUTHOR_ID, this.authorId));
            dataList.add(new Database.Pair(Fields.DATE, this.date));
            dataList.add(new Database.Pair(Fields.CONTENT_OLD, this.content));
            db.insertWithKey(Fields.TABLE_PROJECTS_TASKS, dataList, this.projectId, this.taskId, Fields.C_COMMENTS, this.id, "");

        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public List<DBComment> getAllComments() {
        List<DBComment> list = new ArrayList<DBComment>();
        final Database db = new Database();
        try {
            TreeMap<String, Object> commentsMap = db.select(Fields.TABLE_PROJECTS_TASKS, this.projectId, this.taskId, Fields.C_COMMENTS, "", "");

            if (commentsMap != null) {
                for (Map.Entry<String, Object> comment : commentsMap.entrySet()) {
                    try {
                        String tempId = comment.getKey();

                        if (tempId.equals(Fields.C_ALL_COMMENTS)) {
                            continue;
                        }
                        TreeMap<String, Object> eventDataMap = (TreeMap<String, Object>) comment.getValue();
                        String tempAuthorId = eventDataMap.get(Fields.AUTHOR_ID).toString();
                        String tempDate = eventDataMap.get(Fields.DATE).toString();
                        String tempContent = eventDataMap.get(Fields.CONTENT_OLD).toString();

                        DBUser dbUser = new DBUser(tempAuthorId);
                        DBComment dbcomment = new DBComment(tempId, tempAuthorId, tempDate, tempContent,dbUser.getName(),true);
                        list.add(dbcomment);

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
            db.delete(Fields.TABLE_PROJECTS_TASKS, true, this.projectId, this.taskId, Fields.C_COMMENTS, this.id, "");

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

    public String getAuthorId() {
        return authorId;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getAuthorName() {
        return authorName;
    }
    

}
