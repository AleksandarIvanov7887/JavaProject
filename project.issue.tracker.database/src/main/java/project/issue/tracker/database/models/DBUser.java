package project.issue.tracker.database.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import project.issue.tracker.database.db.Database;
import project.issue.tracker.database.db.Fields;
import project.issue.tracker.database.utils.RZSecurity;

public class DBUser {

    private static final String TYPE_NORMAL = "0";
    private static final String TYPE_ADMIN = "1";
    private static final String DEFAULT_ITEM_PER_PAGE = "25";

    private String id;
    private String userName;
    private String email;
    private String password;
    private String passwordSalt;
    private String name;
    private String type;
    private String itemsPerPage;

    public DBUser(String id) {
        this.id = id;
        init();
    }

    public DBUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public DBUser(String userName, String password, String name, String type) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.type = type;
    }
    public DBUser(String id, String userName, String email, String name, String type, String itemsPerPage) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.name = name;
        this.type = type;
        this.itemsPerPage = itemsPerPage;
    }
    public DBUser( String userName, String password, String name, String type,String email, String itemsPerPage,boolean dummy) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.name = name;
        this.type = type;
        this.itemsPerPage = itemsPerPage;
    }

    public void init() {
        final Database db = new Database();
        try {
            TreeMap<String, Object> profileDataMap = db.select(Fields.TABLE_USER_DATA, this.id, "", "");

            this.userName = profileDataMap.get(Fields.USERNAME).toString();
            this.name = profileDataMap.get(Fields.NAME).toString();
            this.email = profileDataMap.get(Fields.EMAIL).toString();
            this.type = profileDataMap.get(Fields.TYPE).toString();
            this.itemsPerPage = profileDataMap.get(Fields.ITEMS_PER_PAGE).toString();

            TreeMap<String, Object> loginDataMap = db.select(Fields.TABLE_USER_LOGIN_DATA, this.userName, "");

            this.password = loginDataMap.get(Fields.PASSWORD).toString();
            this.passwordSalt = loginDataMap.get(Fields.PASSWORD_SALT).toString();
        } finally {
            db.close();
        }
    }

    public boolean save() {
        this.hashPassword();
        final Database db = new Database();

        try {
            this.id = db.insertWithReturnKey(Fields.TABLE_ALL_USERS, this.userName, "");
            db.delete(Fields.TABLE_ALL_USERS, false, this.id);

            List<Database.Pair> authenticationDataList = new ArrayList<Database.Pair>();
            authenticationDataList.add(new Database.Pair(Fields.PASSWORD, this.password));
            authenticationDataList.add(new Database.Pair(Fields.PASSWORD_SALT, this.passwordSalt));
            authenticationDataList.add(new Database.Pair(Fields.USER_ID, this.id));
            db.insertWithKey(Fields.TABLE_USER_LOGIN_DATA, authenticationDataList, this.userName, "");

            List<Database.Pair> dataList = new ArrayList<Database.Pair>();
            dataList.add(new Database.Pair(Fields.USERNAME, this.userName));
            dataList.add(new Database.Pair(Fields.NAME, this.name));
            dataList.add(new Database.Pair(Fields.EMAIL, this.email));
            dataList.add(new Database.Pair(Fields.TYPE, this.type));
            dataList.add(new Database.Pair(Fields.ITEMS_PER_PAGE, this.itemsPerPage));
            db.insertWithKey(Fields.TABLE_USER_DATA, dataList, this.id, "", "");

        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public static List<Database.Pair> getAllUsersByNames() {
        List<Database.Pair> list = new ArrayList<Database.Pair>();
        final Database db = new Database();
        try {
            TreeMap<String, Object> userMap = db.select(Fields.TABLE_USER_DATA, "", "", "");

            if (userMap != null) {
                for (Map.Entry<String, Object> user : userMap.entrySet()) {
                    try {
                        String tempId = user.getKey();
                        TreeMap<String, Object> eventDataMap = (TreeMap<String, Object>) user.getValue();
                        String tempName = eventDataMap.get(Fields.NAME).toString();

                        Database.Pair pair = new Database.Pair(tempId, tempName);
                        list.add(pair);

                    } catch (NullPointerException e) {
                    }
                }
            }
        } finally {
            db.close();
        }

        return list;
    }

    public static List<DBUser> getAllUsers() {
        List<DBUser> list = new ArrayList<DBUser>();
        final Database db = new Database();
        try {
            TreeMap<String, Object> userMap = db.select(Fields.TABLE_USER_DATA, "", "", "");

            if (userMap != null) {
                for (Map.Entry<String, Object> user : userMap.entrySet()) {
                    try {
                        String tempId = user.getKey();
                        TreeMap<String, Object> eventDataMap = (TreeMap<String, Object>) user.getValue();
                        String tempName = eventDataMap.get(Fields.NAME).toString();
                        String tempUsername = eventDataMap.get(Fields.USERNAME).toString();
                        String tempMail = eventDataMap.get(Fields.EMAIL).toString();
                        String tempType = eventDataMap.get(Fields.TYPE).toString();
                        String tempItems = eventDataMap.get(Fields.ITEMS_PER_PAGE).toString();

                        DBUser pair = new DBUser(tempId, tempUsername, tempMail, tempName, tempType, tempItems);
                        list.add(pair);

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

    public boolean isLoginSuccsessful() {
        final Database db = new Database();

        try {
            TreeMap<String, Object> loginData = db.select(Fields.TABLE_USER_LOGIN_DATA, this.userName, "");

            if (loginData.isEmpty()) {
                return false;
            }
            String dbSalt = loginData.get(Fields.PASSWORD_SALT).toString();
            String dbPassword = loginData.get(Fields.PASSWORD).toString();
            String dbId = loginData.get(Fields.USER_ID).toString();

            String encryptedPassword = RZSecurity.encryptData(this.password, dbSalt);

            if (dbPassword.equals(encryptedPassword)) {
                this.id = dbId;
                return true;
            }
        } finally {
            db.close();

        }
        return false;
    }

    public boolean isExistInDB() {
        if (userName == null) {
            return false;
        }
        final Database db = new Database();

        try {
            TreeMap<String, Object> usersMapList = db.select(Fields.TABLE_ALL_USERS, "", "");

            for (Map.Entry<String, Object> entry : usersMapList.entrySet()) {
                if (entry.getValue().equals(this.userName)) {
                    this.id = entry.getKey();
                    return true;
                }
            }

        } catch (NullPointerException e) {
        } finally {
            db.close();
        }

        return false;
    }

    public boolean addToImportantTasks(String projectId, String taskId) {
        final Database db = new Database();
        try {
            String complexId = projectId + "-" + taskId;

            db.insert(Fields.TABLE_USER_DATA, complexId, this.id, Fields.C_IMPORTANT_TASKS, "");
        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean removeFromImportantTasks(String taskId) {
        final Database db = new Database();
        try {
            TreeMap<String, Object> tasksMap = db.select(Fields.TABLE_USER_DATA, this.id, Fields.C_IMPORTANT_TASKS, "");

            if (tasksMap != null) {
                for (Map.Entry<String, Object> task : tasksMap.entrySet()) {
                    try {
                        String key = task.getKey();
                        String task_id = task.getValue().toString();
                        if (taskId.equals(task_id)) {
                            db.delete(Fields.TABLE_USER_DATA, false, this.id, Fields.C_IMPORTANT_TASKS, key);
                        }
                    } catch (NullPointerException e) {
                    }
                }
            }
        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public List<DBTask> getAllImportantTasks() {
        final Database db = new Database();
        List<DBTask> list = new ArrayList<DBTask>();
        try {
            TreeMap<String, Object> tasksMap = db.select(Fields.TABLE_USER_DATA, this.id, Fields.C_IMPORTANT_TASKS, "");

            if (tasksMap != null) {
                for (Map.Entry<String, Object> task : tasksMap.entrySet()) {
                    try {
                        String complexId = (String) task.getValue();
                        String[] complexIdArray = complexId.split("-");
                        String projectId = complexIdArray[0];
                        String taskId = complexIdArray[1];

                        list.add(new DBTask(taskId, projectId));

                    } catch (NullPointerException e) {
                    }
                }
            }
        } catch (NullPointerException e) {
        } finally {
            db.close();
        }

        return list;
    }
    public boolean updateName(String value) {
        final Database db = new Database();
        try {
            db.insertWithKey(Fields.TABLE_USER_DATA, value, this.id, Fields.NAME, "");
        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    private boolean updateType(String value) {
        final Database db = new Database();
        try {
            db.insertWithKey(Fields.TABLE_USER_DATA, value, this.id, Fields.TYPE, "");
        } catch (NullPointerException e) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean updateTypeToAdmin() {
        return this.updateType(DBUser.TYPE_ADMIN);
    }

    public boolean updateTypeToNormal() {
        return this.updateType(DBUser.TYPE_NORMAL);
    }

    public boolean updateItemsPerPage(String value) {
        final Database db = new Database();
        try {
            db.insertWithKey(Fields.TABLE_USER_DATA, value, this.id, Fields.ITEMS_PER_PAGE, "");
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
            db.delete(Fields.TABLE_USER_DATA, true, this.id, "", "");
            db.delete(Fields.TABLE_USER_LOGIN_DATA, true, this.email, "", "");

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

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getItemsPerPage() {
        return itemsPerPage;
    }

    public boolean isAdmin() {
        return this.type.equals(DBUser.TYPE_ADMIN);
    }

    private void hashPassword() {
        this.passwordSalt = RZSecurity.getSALT2();
        this.password = RZSecurity.encryptData(password, this.passwordSalt);
    }

    @Override
    public String toString() {
        return "DBUser{" + "id=" + id + ", userName=" + userName + ", email=" + email + ", password=" + password + ", passwordSalt=" + passwordSalt + ", name=" + name + ", type=" + type + ", itemsPerPage=" + itemsPerPage + '}';
    }

    public String getUserName() {
        return userName;
    }

    public boolean isCorrectPassword(String currentPassword) {
        final Database db = new Database();
        try {
            String userSalt = db.selectSingle(Fields.TABLE_USER_LOGIN_DATA, this.userName, Fields.PASSWORD_SALT);
            String userPassword = db.selectSingle(Fields.TABLE_USER_LOGIN_DATA, this.userName, Fields.PASSWORD);
            String encryptedPassword = RZSecurity.encryptData(currentPassword, userSalt);
            if (encryptedPassword.equals(userPassword)) {
                return true;
            }
        } finally {
            db.close();
        }
        return false;
    }

    public boolean updatePassword(String newPassword) {

        this.password = newPassword;
        this.hashPassword();

        final Database db = new Database();
        try {
            List<Database.Pair> authenticationDataList = new ArrayList<Database.Pair>();
            authenticationDataList.add(new Database.Pair(Fields.PASSWORD, this.password));
            authenticationDataList.add(new Database.Pair(Fields.PASSWORD_SALT, this.passwordSalt));
            db.insertWithKey(Fields.TABLE_USER_LOGIN_DATA, authenticationDataList, userName, "");
        } finally {
            db.close();
        }

        return true;
    }
}

