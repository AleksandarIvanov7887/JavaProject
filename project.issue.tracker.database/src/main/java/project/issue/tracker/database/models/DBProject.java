package project.issue.tracker.database.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import project.issue.tracker.database.db.Database;
import project.issue.tracker.database.db.Fields;
import project.issue.tracker.database.db.Pair;

public class DBProject {

    private String id;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    private String name;
    private String description;

    public DBProject(String id) {
        this.id = id;
        init();
    }

    public DBProject(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public DBProject(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public boolean save() {
        final Database db = new Database();

        try {
            this.id = db.insertWithReturnKey(Fields.TABLE_ALL_PROJECTS, this.name, "");
            db.delete(Fields.TABLE_ALL_PROJECTS, false, this.id);

            List<Pair> dataList = new ArrayList<Pair>();
            dataList.add(new Pair(Fields.NAME, this.name));
            dataList.add(new Pair(Fields.DESCRIPTION, this.description));
            db.insertWithKey(Fields.TABLE_PROJECT_DATA, dataList, this.id, "");

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
            TreeMap<String, Object> projectMap = db.select(Fields.TABLE_PROJECT_DATA, this.id, "");

            this.name = projectMap.get(Fields.NAME).toString();
            this.description = projectMap.get(Fields.DESCRIPTION).toString();
        } finally {
            db.close();
        }
    }

    public static List<DBProject> getAllProjects() {
        List<DBProject> list = new ArrayList<DBProject>();
        final Database db = new Database();
        try {
            TreeMap<String, Object> projectsMap = db.select(Fields.TABLE_PROJECT_DATA, "", "");

            if (projectsMap != null) {
                for (Map.Entry<String, Object> project : projectsMap.entrySet()) {
                    try {
                        String tempId = project.getKey();

                        TreeMap<String, Object> eventDataMap = (TreeMap<String, Object>) project.getValue();
                        String tempName = eventDataMap.get(Fields.NAME).toString();
                        String tempDesctiption = eventDataMap.get(Fields.DESCRIPTION).toString();

                        DBProject dbProject = new DBProject(tempId, tempName, tempDesctiption);
                        list.add(dbProject);

                    } catch (NullPointerException e) {
                    }
                }
            }
        } finally {
            db.close();
        }
        Collections.reverse(list); // NOTE this is reversed so that new will show on top
        return list;
    }

    public boolean updateName(String value) {
        final Database db = new Database();
        try {
            db.insertWithKey(Fields.TABLE_PROJECT_DATA, value, this.id, Fields.NAME);
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
            db.insertWithKey(Fields.TABLE_PROJECT_DATA, value, this.id, Fields.DESCRIPTION);
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
            db.delete(Fields.TABLE_PROJECT_DATA, true, this.id, "");
            db.delete(Fields.TABLE_PROJECTS_TASKS, true, this.id, "", "", "", "");

        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }
}
