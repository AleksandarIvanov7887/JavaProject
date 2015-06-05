<%@ page import="com.track.utils.Utils" %>
<%@ page import="com.track.utils.ATTRIBUTES" %>
<%@ page import="com.track.be.models.DBProject" %>
<%@ page import="com.track.be.models.DBTask" %>
<%@ page import="com.track.be.models.DBUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <%@include file="includes/bootstrap_header.jsp" %>
        <script src="includes/javascript/hide_show_tables.js" type="text/javascript"></script>
        <script src="includes/javascript/validation.js" type="text/javascript"></script>
        <title><%="DebugTracker"%></title>
        <script type="text/javascript">
            var project_names = [];
            var task_names = [];
            var assignee_names = [];

        </script>
    </head>
    <body>
        <div id="wrap">
            <%@include file="includes/ui/user_header.jsp" %>

            <div class="container">

                <div class="row">

                    <div class="col-lg-10">

                        <div id="List_div"<%=Utils.isAdmin(request) ? " hidden=\"hidden\"" : ""%>>
                            <div class="row">
                                <div class="col-lg-12">
                                    <ul class="nav nav-tabs">
                                        <li id="list_projects_li" class="active"><a id="list_projects_li_a" href="#">List Projects</a></li>
                                        <li id="list_tasks_li"><a id="list_tasks_li_a" href="#">List Tasks</a></li>
                                        <li id="list_users_li"><a id="list_users_li_a" href="#">List Users</a></li>
                                    </ul>
                                </div>
                            </div>

                            <div id="list_projects" class="row">
                                <div class="col-lg-12 offset-top">
                                    <form class="form-horizontal" action="#" id="list_projects_form">

                                        <div class="form-group">
                                            <label for="project_name" class="control-label col-sm-2">Project name</label>

                                            <div class="col-sm-10">
                                                <input type="text" id="project_name" placeholder="Name" class="form-control"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-2 col-sm-offset-10">
                                                <input type="button" value="Search" class="btn btn-primary form-control" id="search_project_btn"/>
                                            </div>

                                        </div>
                                    </form>
                                </div>
                                <div class="col-lg-12 offset-top">
                                    <ul class="nav nav-tabs col-lg-12">
                                    </ul>
                                </div>
                                <div class="col-lg-12 offset-top" id="list_projects_content">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Project Name</th>
                                                <th>Description</th>
                                                <th></th>
                                                <th></th>
                                            </tr>
                                        </thead>
                                        <tbody id="list_projects_table_body">

                                        </tbody>
                                    </table>
                                </div>
                                <div class="col-lg-12 text-center offset-top">
                                    <ul class="pagination" id="list_projects_pagination">

                                    </ul>
                                </div>
                            </div>

                            <div id="list_projects_single" class="row col-lg-12" hidden="hidden">
                                <div class="row">
                                    <div class="col-lg-2 offset-top">
                                        <button class="btn btn-default btn-block" id="list_project_back_button"><span
                                                class="glyphicon glyphicon-arrow-left"></span></button>
                                    </div>
                                </div>
                                <ul class="nav nav-tabs col-lg-12 offset-top"><!-- add margin top :)-->
                                </ul>
                                <div class="row offset-top">
                                    <div class="col-lg-12">
                                        <form class="form-horizontal" action="#" method="POST" id="list_projects_single_form">
                                            <div class="form-group">
                                                <label for="project_id" class="control-label col-sm-2">Project ID</label>

                                                <div class="col-sm-10">
                                                    <input type="text" id="project_id" class="form-control" disabled="disabled"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="project_name" class="control-label col-sm-2">Project name</label>

                                                <div class="col-sm-10">
                                                    <input type="text" id="project_name" class="form-control" disabled="disabled"/>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label for="project_desc" class="control-label col-sm-2">Description</label>

                                                <div class="col-sm-10">
                                                    <textarea class="form-control" rows="5" id="project_desc"
                                                              disabled="disabled"></textarea>
                                                </div>
                                            </div>

                                            <%
                                                if (Utils.isAdmin(request)) {
                                            %>
                                            <div class="col-sm-2 col-sm-offset-2">
                                                <input type="button" value="Edit" class="btn btn-primary form-control"
                                                       id="list_projects_single_edit"/>
                                            </div>

                                            <div class="col-sm-2 col-sm-offset-1">
                                                <input type="button" value="Save" class="btn btn-primary form-control"
                                                       id="list_projects_single_save" disabled="disabled"/>
                                            </div>
                                            <%
                                                }
                                            %>

                                        </form>
                                    </div>
                                </div>

                            </div>

                            <div id="list_tasks" hidden="hidden" class="row">
                                <div class="col-lg-12 offset-top">
                                    <form action="#" class="form-horizontal" id="list_tasks_form" role="form">
                                        <div class="form-group">
                                            <label for="task_parent" class="control-label col-sm-2">Project</label>

                                            <div class="col-sm-10">

                                                <input type="text" placeholder="Project Name" id="task_parent" class="form-control"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="task_name" class="col-sm-2 control-label">Title</label>

                                            <div class="col-sm-10">
                                                <input type="text" id="task_name" class="form-control" placeholder="Title"/>

                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="task_assignee" class="control-label col-sm-2">Assignee</label>

                                            <div class="col-sm-10">
                                                <input type="text" placeholder="Assignee" id="task_assignee" class="form-control"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="task_status" class="control-label col-sm-2">Status</label>

                                            <div class="col-sm-10">

                                                <select id="task_status" class="form-control">
                                                    <option selected="selected">Open</option>
                                                    <option>In Process</option>
                                                    <option>Completed</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-offset-10 col-sm-2">
                                                <input type="button" class="btn btn-primary form-control" value="Search" id="search_task_btn">
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-lg-12 offset-top">
                                    <ul class="nav nav-tabs col-lg-12"><!-- add margin top :)-->
                                    </ul>
                                </div>
                                <div class="col-lg-12 offset-top">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Title</th>
                                                <th>Project</th>
                                                <th>Assignee</th>
                                                <th>Status</th>
                                                <th>Due Date</th>
                                            </tr>
                                        </thead>
                                        <tbody id="list_tasks_table_body">

                                        </tbody>
                                    </table>
                                </div>
                                <div class="col-lg-12 text-center offset-top">
                                    <ul class="pagination" id="list_tasks_pagination">

                                    </ul>
                                </div>
                            </div>

                            <div id="list_tasks_single" class="row col-lg-12" hidden="hidden">
                                <div class="row">
                                    <div class="col-lg-2 offset-top">
                                        <button class="btn btn-default btn-block" id="list_task_back_button"><span
                                                class="glyphicon glyphicon-arrow-left"></span></button>
                                    </div>
                                </div>
                                <ul class="nav nav-tabs col-lg-12 offset-top"><!-- add margin top :)-->
                                </ul>
                                <div class="row offset-top">
                                    <div class="col-lg-12">
                                        <%@include file="includes/ui/list_single_task_form.jsp" %>
                                    </div>
                                </div>
                                <ul class="nav nav-tabs col-lg-12 offset-top"><!-- add margin top :)-->
                                </ul>
                                <div class="row col-lg-12 offset-top">

                                    <div class="col-lg-2">
                                        <p class="text-right"><b>Leave Comment</b></p>
                                    </div>
                                    <div class="col-lg-8">
                                        <textarea rows="5" class="form-control" id="add_comment_textarea" placeholder="Add Comment"></textarea>
                                    </div>
                                    <div class="col-lg-2">
                                        <button class="btn btn-primary form-control" id="add_comment_btn">Add Comment</button>
                                    </div>
                                </div>
                                <ul class="nav nav-tabs col-lg-12 offset-top"><!-- add margin top :)-->
                                </ul>
                                <div class="row offset-top">
                                    <label for="task_comments" class="control-label col-sm-2">Comments</label>
                                    <hr>
                                    <div class="col-lg-12" id="task_comments">
                                    </div>
                                </div>
                                <ul class="nav nav-tabs col-lg-12 offset-top"><!-- add margin top :)-->
                                </ul>
                            </div>

                            <div id="list_users" hidden="hidden" class="row">
                                <div class="col-lg-12 offset-top">
                                    <form action="#" class="form-horizontal" id="list_users_form">
                                        <div class="form-group">
                                            <label for="user_name" class="control-label col-sm-2">Username</label>

                                            <div class="col-sm-10">
                                                <input type="text" id="user_name" class="form-control" placeholder="Username"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="user_names" class="control-label col-sm-2">Full name</label>

                                            <div id="user_names" class="col-sm-10">
                                                <input placeholder="Full Name" type="text" id="user_name_full" class="form-control">
                                            </div>

                                        </div>

                                        <div class="form-group">
                                            <label for="user_email" class="control-label col-sm-2">e-mail</label>

                                            <div class="col-sm-10">
                                                <input type="email" class="form-control" id="user_email" placeholder="dummy@dummy.com"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="user_type" class="col-sm-2 control-label">User type</label>

                                            <div class="col-sm-10">

                                                <select id="user_type" class="form-control">
                                                    <option>Administrator</option>
                                                    <option selected="selected">User</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-2 col-sm-offset-10">
                                                <input type="button" value="Search" class="btn btn-primary form-control" id="search_user_btn"/>
                                            </div>
                                        </div>

                                    </form>
                                </div>
                                <div class="col-lg-12 offset-top">
                                    <ul class="nav nav-tabs col-lg-12">
                                    </ul>
                                </div>
                                <div class="col-lg-12 offset-top">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Full Name</th>
                                                <th>Username</th>
                                                <th>Role</th>
                                                <th>E-mail</th>
                                            </tr>
                                        </thead>
                                        <tbody id="list_users_table_body">

                                        </tbody>
                                    </table>

                                </div>
                                <div class="col-lg-12 text-center offset-top">
                                    <ul class="pagination" id="list_user_pagination">

                                    </ul>
                                </div>
                            </div>

                            <div id="list_users_single" class="row col-lg-12" hidden="hidden">
                                <div class="row">
                                    <div class="col-lg-2 offset-top">
                                        <button class="btn btn-default btn-block" id="list_user_back_button"><span
                                                class="glyphicon glyphicon-arrow-left"></span></button>
                                    </div>
                                </div>
                                <ul class="nav nav-tabs col-lg-12 offset-top"><!-- add margin top :)-->
                                </ul>
                                <div class="row offset-top">
                                    <div class="col-lg-12">
                                        <%@include file="includes/ui/list_single_user_form.jsp" %>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <% if (Utils.isAdmin(request)) { %>
                        <div id="Create_div">
                            <div class="row">
                                <div class="col-lg-12">
                                    <ul class="nav nav-tabs">
                                        <li id="create_project_li" class="active"><a id="create_project_li_a" href="#">Create Project</a></li>
                                        <li id="create_task_li"><a id="create_task_li_a" href="#">Create Task</a></li>
                                        <li id="create_user_li"><a id="create_user_li_a" href="#">Create User</a></li>
                                    </ul>
                                </div>
                            </div>

                            <div id="create_project" class="row">
                                <div class="col-lg-12"><br><br>

                                    <form class="form-horizontal" action="#" id="create_project_form">
                                        <div class="form-group">
                                            <label for="project_name" class="control-label col-sm-2">Project name</label>

                                            <div class="col-sm-10">
                                                <input type="text" id="project_name" placeholder="Name" name="project_name"
                                                       class="form-control"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="project_desc" class="control-label col-sm-2">Description</label>

                                            <div class="col-sm-10">
                                                <textarea class="form-control" name="project_description" rows="5" id="project_desc"
                                                          placeholder="Some information about project"></textarea>
                                            </div>
                                        </div>

                                        <div class="col-sm-2 col-sm-offset-10" style="padding:0px">
                                            <input type="button" value="Create Project" class="btn btn-primary form-control" id="create_project"/>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <div id="create_task" class="row" hidden="hidden">
                                <div class="col-lg-12"><br><br>

                                    <form action="#" class="form-horizontal" role="form" id="create_task_form">
                                        <div class="form-group">
                                            <label for="task_parent" class="control-label col-sm-2">Project</label>

                                            <div class="col-sm-10">
                                                <input type="text" placeholder="Project name" id="task_parent" class="form-control"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="task_name" class="col-sm-2 control-label">Title</label>

                                            <div class="col-sm-10">
                                                <input type="text" id="task_name" class="form-control" placeholder="Task Name"/>

                                            </div>
                                        </div>
                                        <br>

                                        <div class="form-group">
                                            <label for="task_desc" class="col-sm-2 control-label">Description</label>

                                            <div class="col-sm-10">
                                                <textarea class="form-control" rows="5" id="task_desc" placeholder="Description"></textarea>
                                            </div>

                                        </div>
                                        <br>

                                        <div class="form-group">
                                            <label for="datetimepicker1" class="control-label col-sm-2">Due Date</label>

                                            <div class="col-sm-10">
                                                <div class='input-group date nov_date_picker' id="datetimepicker1">
                                                    <input type='text' class="form-control" id="task_final_date" placeholder="Due Date"/>
                                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                        <br>

                                        <div class="form-group">
                                            <label for="task_assignee" class="control-label col-sm-2">Assignee</label>

                                            <div class="col-sm-10">

                                                <input type="text" placeholder="Assignee" id="task_assignee" class="form-control"/>

                                            </div>
                                        </div>
                                        <br>

                                        <div class="form-group">
                                            <label for="task_status" class="control-label col-sm-2">Status</label>

                                            <div class="col-sm-10">

                                                <select id="task_status" class="form-control">
                                                    <option selected="selected">Open</option>
                                                    <option disabled="disabled">In Process</option>
                                                    <option disabled="disabled">Completed</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-sm-2" for="task_comment">Comment</label>

                                            <div class="col-sm-10">
                                                <textarea class="form-control" rows="5" id="task_comment" placeholder="Add Comment"></textarea>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-offset-4 col-sm-10">
                                                <div class="checkbox" style="float:right; margin-right:160px;">
                                                    <label>
                                                        <input type="checkbox" id="task_important">Important
                                                    </label>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-sm-offset-9 col-sm-3" style="padding:0px;">
                                            <input type="button" class="btn btn-primary form-control" value="Create Task" id="create_task">
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <div id="create_user" class="row" hidden="hidden">
                                <div class="col-lg-12"><br><br>

                                    <form action="#" class="form-horizontal" id="create_user_form">
                                        <div class="form-group">
                                            <label for="user_name" class="control-label col-sm-2">Username</label>

                                            <div class="col-sm-10">
                                                <input type="text" id="user_name" class="form-control" placeholder="Username"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="user_password_1" class="col-sm-2 control-label">Password</label>

                                            <div class="col-sm-10">
                                                <input type="password" id="user_password_1" class="form-control" placeholder="Password"/>
                                                <span class="help-block">Type same password in next field</span>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="user_password_2" class="col-sm-2 control-label">Repeat Password</label>

                                            <div class="col-sm-10">
                                                <input type="password" id="user_password_2" class="form-control" placeholder="Repeat Password"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="user_names" class="control-label col-sm-2">Full name</label>

                                            <div id="user_names" class="col-sm-10">
                                                <input placeholder="Full Name" id="user_name_full" type="text" class="form-control">
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="user_email" class="control-label col-sm-2">e-mail</label>

                                            <div class="col-sm-10">
                                                <input type="email" class="form-control" id="user_email"
                                                       placeholder="dummy@dummy.com"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="user_type" class="col-sm-2 control-label">User type</label>

                                            <div class="col-sm-10">

                                                <select class="form-control" id="user_type">
                                                    <option>Administrator</option>
                                                    <option selected="selected">User</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="items_per_page" class="col-sm-2 control-label">Items per page</label>

                                            <div class="col-sm-10">

                                                <select id="items_per_page" class="form-control" id="user_items_per_page">
                                                    <option>10</option>
                                                    <option>20</option>
                                                    <option selected="selected">30</option>
                                                    <option>40</option>
                                                    <option>50</option>
                                                    <option>60</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="col-sm-2 col-sm-offset-10" style="padding:0px">
                                            <input type="button" value="Create User" id="create_user" class="btn btn-primary form-control"/>
                                        </div>

                                    </form>
                                </div>
                            </div>
                        </div>
                        <% } %>

                        <div id="ViewProfile_div" hidden="hidden">
                            <div class="row">
                                <div class="col-lg-2">
                                    <button class="btn btn-default btn-block" id="profile_back_button"><span
                                            class="glyphicon glyphicon-arrow-left"></span></button>
                                </div>
                            </div>
                            <ul class="nav nav-tabs col-lg-12 offset-top"><!-- add margin top :)-->
                            </ul>
                            <div class="row offset-top">
                                <div class="col-lg-12">
                                    <form action="#" method="POST" class="form-horizontal" id="user_change_form">
                                        <div class="form-group">
                                            <label for="user_name" class="control-label col-sm-2">Username</label>

                                            <div class="col-sm-10">
                                                <input type="text" id="user_name" name="username" class="form-control" disabled="disabled"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="user_password_1" class="col-sm-2 control-label">Old Password</label>

                                            <div class="col-sm-10">
                                                <input type="password" id="user_password_1" name="old_password" class="form-control"
                                                       disabled="disabled"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="user_password_2" class="col-sm-2 control-label">New Password</label>

                                            <div class="col-sm-10">
                                                <input type="password" id="user_password_2" name="password_new" class="form-control"
                                                       disabled="disabled"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="user_password_3" class="col-sm-2 control-label">Repeat Password</label>

                                            <div class="col-sm-10">
                                                <input type="password" id="user_password_3" name="password_new_rep" class="form-control"
                                                       disabled="disabled"/>
                                                <span class="help-block">Repeat password</span>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="user_names" class="control-label col-sm-2">Full name</label>

                                            <div id="user_names" class="col-sm-10">
                                                <input placeholder="Full Name" type="text" class="form-control" disabled="disabled"
                                                       id="user_name_full">
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="user_email" class="control-label col-sm-2">e-mail</label>

                                            <div class="col-sm-10">
                                                <input type="email" class="form-control" name="user_email" id="user_email"
                                                       placeholder="dummy@dummy.com" disabled="disabled"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="user_type" class="col-sm-2 control-label">User type</label>

                                            <div class="col-sm-10">

                                                <input type="text" disabled="disabled" id="user_type" class="form-control" value="User"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="items_per_page" class="col-sm-2 control-label">Items per page</label>

                                            <div class="col-sm-10">

                                                <select id="items_per_page" class="form-control" name="user_items_per_page" disabled="disabled">
                                                    <option>10</option>
                                                    <option>20</option>
                                                    <option selected="selected">30</option>
                                                    <option>40</option>
                                                    <option>50</option>
                                                    <option>60</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-sm-2 col-sm-offset-2">
                                            <input type="Button" class="btn btn-primary form-control" id="VP_edit_btn" value="Edit"/>
                                        </div>
                                        <div class="col-sm-2 col-sm-offset-1">
                                            <input type="button" value="Save" class="btn btn-primary form-control" disabled="disabled"
                                                   id="VP_save_btn"/>
                                        </div>

                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-2">

                        <div id="update_progress" style="height: 3px; background-color: #3276b1"></div>

                        <div class="list-group" id="bar_fixed_width">
                            <a href="#" class="list-group-item notes-important text-center">
                                <span>Tasks</span>
                            </a>
                            <% if (Utils.isAdmin(request)) { %>
                            <a href="#create_task_li_a" class="list-group-item text-center" id="notes_create_task">Create Task</a>
                            <% } %>
                            <a href="#list_tasks_li_a" class="list-group-item text-center" id="notes_active_tasks">My Tasks in progress</a>
                            <a href="#list_tasks_li_a" class="list-group-item text-center" id="notes_open_tasks">My Tasks</a>
                            <% if (Utils.isAdmin(request)) { %>
                            <a href="#list_tasks_li_a" class="list-group-item text-center" id="notes_important">Important</a>
                            <% } %>
                        </div>
                    </div>

                </div>

                <ul class="nav nav-tabs col-lg-10 offset-top offset-bottom">
                </ul>
            </div>
        </div>



        <div id="footer">
            <div class="container text-center">
                <span class="text-primary"><script>document.write(new Date().getFullYear());</script> &copy DebugTracker.</span>
                <select id="project_names_collection" hidden="hidden">
                    <%
                        for (DBProject p : DBProject.getAllProjects()) {
                    %><option><%=p.getName()%></option><%
                                }
                    %>
                </select>
                <select id="task_names_collection" hidden="hidden">
                    <%
                        for (DBTask t : DBTask.getAllTasks()) {
                    %><option><%=t.getTitle()%></option><%
                                }
                    %>
                </select>
                <select id="user_names_collection" hidden="hidden">
                    <%
                        for (DBUser u : DBUser.getAllUsers()) {
                    %><option><%=u.getName()%></option><%
                                }
                    %>
                </select>
                <script type="text/javascript">
                    $(document).ready(function() {
                        var pnc = $('#project_names_collection').children();
                        var tnc = $('#task_names_collection').children();
                        var unc = $('#user_names_collection').children();
                        for (var i = 0; i < pnc.length; i++) {
                            project_names[i] = pnc[i].innerHTML;
                        }
                        for (var i = 0; i < tnc.length; i++) {
                            task_names[i] = tnc[i].innerHTML;
                        }
                        for (var i = 0; i < unc.length; i++) {
                            assignee_names[i] = unc[i].innerHTML;
                        }

                        $('#create_project_form').find('#project_name').autocomplete({source: project_names});
                        var ctf = $('#create_task_form');
                        ctf.find('#task_parent').autocomplete({source: project_names});
                        ctf.find('#task_name').autocomplete({source: task_names});
                        ctf.find('#task_assignee').autocomplete({source: assignee_names});
                        $('#create_user_form').find('#user_name_full').autocomplete({source: assignee_names});
                        $('#list_projects_form').find('#project_name').autocomplete({source: project_names});
                        var ltf = $('#list_tasks_form');
                        ltf.find('#task_parent').autocomplete({source: project_names});
                        ltf.find('#task_name').autocomplete({source: task_names});
                        ltf.find('#task_assignee').autocomplete({source: assignee_names});
                        $('#list_users_form').find('#user_name_full').autocomplete({source: assignee_names});
                        $('#user_change_form').find('#user_name_full').autocomplete({source: assignee_names});
                        $('#list_tasks_single_form').find('#task_assignee').autocomplete({source: assignee_names});

                        var bar = $('#update_progress');
                        bar.width('0');
                        var bar_helper = $('#bar_fixed_width');
                        var currentTime = 1;

                        function update() {
                            currentTime++;
                            bar.css('width', Math.floor(parseFloat(currentTime / 240) * parseInt(bar_helper.width())) + 'px')
                            if (currentTime > 240) {
                                clearInterval(bar_updater_interval)
                            }
                        }

                        function restart() {
                            clearInterval(bar_updater_interval);
                            currentTime = 1;
                            bar.width('0');
                            bar_updater_interval = setInterval(update, 250);
                        }

                        var bar_updater_interval = setInterval(update, 250);

                        setInterval(function() {
                            $.ajax({
                                url: window.session.applicationName + "/updateAutocomplete.do",
                                context: document.body,
                                dataType: "json",
                                type: "GET",
                                timeout: 30000,
                                success: function(data) {
                                    console.log('update autocomplete');

                                    //                            project_names = [];
                                    for (var i = 0; i < parseInt(data.projectsLength); i++) {
                                        project_names[i] = data.projects[i];
                                    }
                                    //                            task_names = [];
                                    for (var i = 0; i < parseInt(data.tasksLength); i++) {
                                        task_names[i] = data.tasks[i];
                                    }
                                    //                            assignee_names = [];
                                    for (var i = 0; i < parseInt(data.usersLength); i++) {
                                        assignee_names[i] = data.users[i];
                                    }

                                    restart();
                                },
                                error: function(dummy, message) {
                                    console.log('request failed : ' + message + ' emi')
                                }
                            })

                        }, 60000); // every 1 minute


                        $('.nov_date_picker').datetimepicker({
                            format: 'DD-MM-YYYY hh:mm:ss'
                        })
                    })
                </script>
            </div>
        </div>
    </body>
</html>
