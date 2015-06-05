<form action="#" class="form-horizontal" id="show_user_form">
    <div class="form-group">
        <label for="user_id" class="control-label col-sm-2">ID#</label>

        <div class="col-sm-5">
            <input type="text" id="user_id" class="form-control" disabled="disabled"/>
        </div>
    </div>

    <div class="form-group">
        <label for="user_name" class="control-label col-sm-2">Username</label>

        <div class="col-sm-5">
            <input type="text" id="user_name" class="form-control" disabled="disabled"/>
        </div>
    </div>

    <div class="form-group">
        <label for="user_name_full" class="control-label col-sm-2">Full name</label>

        <div class="col-sm-5">
            <input type="text" id="user_name_full" class="form-control" disabled="disabled">
        </div>
        <%--<div class="col-sm-1">
            <input placeholder="Middle name" type="text" name="user_name_middle" class="form-control" disabled="disabled">
        </div>
        <div class="col-sm-2">
            <input placeholder="Last name" type="text" name="user_name_last" class="form-control" disabled="disabled">
        </div>--%>
    </div>

    <div class="form-group">
        <label for="user_email" class="control-label col-sm-2">e-mail</label>

        <div class="col-sm-5">
            <input class="form-control" id="user_email" disabled="disabled"/>
        </div>
    </div>

    <div class="form-group">
        <label for="user_role" class="col-sm-2 control-label">User type</label>

        <div class="col-sm-5">
            <input type="text" disabled="disabled" id="user_role" class="form-control"/>
<%--            <select id="task_status" class="form-control" name="user_type" disabled="disabled">
                <option >Administrator</option>
                <option selected="selected">User</option>
            </select>--%>
        </div>
    </div>
<%--
    <div class="form-group">
        <div class="col-sm-2 col-sm-offset-5">
            <input type="button" value="Search" class="btn btn-primary form-control" id="search_user_btn"/>
        </div>
    </div>--%>
    <%--// TODO if--%>
    <%--<% if(Utils.isAdmin(request)) { %>--%>
        <%--<%@include file="delete_user_inc.jsp"%>--%>
    <%--<% } %>--%>
</form>
