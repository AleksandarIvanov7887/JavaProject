<%@ page import="com.track.utils.ATTRIBUTES" %><%
    if (request.getSession(false) == null ||
            (request.getSession(false) != null && request.getSession(false).getAttribute(ATTRIBUTES.USER_ROLE) == null)) {
        response.sendRedirect(request.getServletContext().getContextPath() + "/login.jsp");
    }
%>
