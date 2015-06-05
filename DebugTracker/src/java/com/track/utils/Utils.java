package com.track.utils;

import com.track.be.models.DBUser;
import org.apache.commons.validator.GenericValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Utils {

    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(ATTRIBUTES.USER_BEAN) != null;
    }


    public static boolean isAdmin(HttpServletRequest request) {
        return isLoggedIn(request) && ((DBUser) request.getSession(false).getAttribute(ATTRIBUTES.USER_BEAN)).isAdmin();
    }

    public static boolean isNull(String s) {
        return GenericValidator.isBlankOrNull(s);
    }

}
