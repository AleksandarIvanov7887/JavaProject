<%@ page import="com.track.be.models.DBEvent" %>
<%@ page import="com.track.be.models.DBProject" %>
<%@ page import="com.track.be.models.DBTask" %>
<%@ page import="com.track.be.models.DBUser" %>
<%@ page import="com.track.utils.ATTRIBUTES" %>
<%@ page import="com.track.utils.Utils" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="includes/bootstrap_header.jsp" %>
    <%
        String taskID = request.getParameter("taskID");
        String projectName = request.getParameter("projectName");

        if (Utils.isNull(taskID) || Utils.isNull(projectName)) {
            response.sendRedirect(request.getServletContext().getContextPath() + "/index.jsp");
        }

        DBProject project = null;
        for (DBProject p : DBProject.getAllProjects()) {
            if (p.getName().equals(projectName)) {
                project = p;
            }
        }
        if (null == project) {
            response.sendRedirect(request.getServletContext().getContextPath() + "/index.jsp");
            return;
        }

        DBTask task = new DBTask(taskID, project.getId());

        DBUser userBean = (DBUser) session.getAttribute(ATTRIBUTES.USER_BEAN);
        if (!task.getAuthorId().equals(userBean.getId())) {
            response.sendRedirect(request.getServletContext().getContextPath() + "/index.jsp");
        }

        List<DBEvent> list = new DBEvent(taskID, project.getId()).getAllEventsForTask();

    %>
    <title><%=taskID + " " + task.getTitle()%>
    </title>
</head>
<body>
<%--// todo fill shit here for task. Request parameter will be given from JS GET method--%>
<%--todo request for events that have this id. Display it reversed order--%>

<div class="container offset-top">
    <table class="table table-hover">
        <thead>
        <tr>
            <td>#</td>
            <td>User</td>
            <td>Event</td>
            <td>New Value</td>
            <td>Old Value</td>
        </tr>
        </thead>
        <tbody>
        <%
            for (DBEvent e : list) {
                out.print("<tr>" +
                        "<td>" + e.getId() + "</td>" +
                        "<td>" + new DBUser(e.getAuthorId()).getName() + "</td>" +
                        "<td>" + e.getType() + "</td>" +
                        "<td>" + e.getContentNew() + "</td>" +
                        "<td>" + (e.getContentOld() == null ? "" : e.getContentOld()) + "</td>" + // Comment does not have previous value
                        "</tr>");
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>
