package project.issue.tracker.utils;

public class FORM_PARAMS {
    private FORM_PARAMS() {   	
    }
    
    public static class CREATE_TASK {
        private CREATE_TASK() {
        }

        public static final String PROJECT = "tParent";
        public static final String TASK_NAME = "tName";
        public static final String DESCRIPTION = "tDesc";
        public static final String DUE_DATE = "tDDate";
        public static final String ASSIGNEE = "tAssignee";
        public static final String STATUS = "tStatus";
        public static final String IMPORTANT = "tImportant";
        public static final String COMMENT = "tComment";
    }

    public static class LOGIN {
        private LOGIN() {
        }

        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }

    public static class CREATE_USER {
        private CREATE_USER() {
        }

        public static final String USERNAME = "username";
        public static final String PASSOWRD = "password";
        public static final String FULL_NAME = "user_name_full";
        public static final String EMAIL = "user_email";
        public static final String USER_TYPE = "user_type";
    }

    public static class CREATE_PROJECT {
        private CREATE_PROJECT() {
        }

        public static final String PROJECT_NAME = "name";
        public static final String PROJECT_DESCRIPTION = "desc";
    }

    public static class LIST_PROJECT {
        private LIST_PROJECT() {
        }

        public static final String PROJECT_NAME = "project_name";
    }

    public static class LIST_TASK {
        private LIST_TASK() {}
        public static final String PROJECT_NAME = "project";
        public static final String TASK_NAME = "task_name";
        public static final String DUE_DATE = "due_date";
        public static final String ASSIGNEE = "assignee";
        public static final String STATUS = "status";
    }

    public static class LIST_USER {
        private LIST_USER(){}
        public static final String USERNAME = "username";
        public static final String FULL_NAME = "user_name_full";
        public static final String EMAIL = "user_email";
        public static final String USER_TYPE = "user_type";
    }

    public static class CHANGE_USER {
        private CHANGE_USER() {}
        public static final String OLD_PASSWORD = "old_password";
        public static final String NEW_PASSWORD = "password_new";
        public static final String EMAIL = "user_email";
        public static final String ITEMS_PER_PAGE = "user_items_per_page";
        public static final String FULL_NAME = "user_name_full";
    }

    public static class CHANGE_PROJECT {
        private CHANGE_PROJECT() {}
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "desc";
    }

    public static class CHANGE_TASK {
        private CHANGE_TASK() {}
        public static final String ID = "task_id";
        public static final String PROJECT_NAME = "project_name";
        public static final String DUE_DATE = "ddate";
        public static final String ASSIGNEE = "assignee";
        public static final String TASK_STATUS = "task_status";
    }

    public static class DELETE_USER {
        private DELETE_USER() {}
        public static final String ID = "user_id";
    }

    public static class VIEW_PROJECT {
        private VIEW_PROJECT() {
        }

        public static final String ID = "id";
    }

    public static class VIEW_TASK {
        private VIEW_TASK() {
        }

        public static final String ID = "task_id";
        public static final String PROJECT_NAME = "project_name";
    }

    public static class VIEW_USER {
        private VIEW_USER() {
        }

        public static final String ID = "user_id";
    }

    public static class ADD_COMMENT {
        private ADD_COMMENT() {
        }

        public static final String TASK_ID = "task_id";
        public static final String PROJECT_NAME = "project_name";
        public static final String COMMENT = "comment";
    }
}
