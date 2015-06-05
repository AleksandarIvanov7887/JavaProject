package com.track.be.db;

public class InitDB {

    public static void start() {
        Database database = new Database();

        try {
            database.dropTable(Fields.TABLE_EVENTS);
        } catch (NullPointerException e) {
        }
        database.createTable(Fields.TABLE_EVENTS, 4);

        try {
            database.dropTable(Fields.TABLE_PROJECTS_TASKS);
        } catch (NullPointerException e) {
        }
        database.createTable(Fields.TABLE_PROJECTS_TASKS, 5);

        try {
            database.dropTable(Fields.TABLE_PROJECT_DATA);
        } catch (NullPointerException e) {
        }
        database.createTable(Fields.TABLE_PROJECT_DATA, 2);

        try {
            database.dropTable(Fields.TABLE_USER_DATA);
        } catch (NullPointerException e) {
        }
        database.createTable(Fields.TABLE_USER_DATA, 3);

        try {
            database.dropTable(Fields.TABLE_USER_LOGIN_DATA);
        } catch (NullPointerException e) {
        }
        database.createTable(Fields.TABLE_USER_LOGIN_DATA, 2);

        try {
            database.dropTable(Fields.TABLE_ALL_USERS);
        } catch (NullPointerException e) {
        }
        database.createTable(Fields.TABLE_ALL_USERS, 1);

        try {
            database.dropTable(Fields.TABLE_ALL_PROJECTS);
        } catch (NullPointerException e) {
        }
        database.createTable(Fields.TABLE_ALL_PROJECTS, 1);

        database.close();
    }
}
