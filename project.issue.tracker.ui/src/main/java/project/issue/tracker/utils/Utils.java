package project.issue.tracker.utils;

import project.issue.tracker.database.models.User;

import org.apache.commons.validator.GenericValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Utils {

    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(ATTRIBUTES.USER_BEAN) != null;
    }

    public static boolean isAdmin(HttpServletRequest request) {
        return isLoggedIn(request) && 
        		(User.TYPE_ADMIN).equals(((User) request.getSession(false).getAttribute(ATTRIBUTES.USER_BEAN)).getRole());
    }

    public static boolean isNull(String s) {
        return GenericValidator.isBlankOrNull(s);
    }

}
