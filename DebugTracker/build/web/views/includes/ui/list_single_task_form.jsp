<form action="#" class="form-horizontal" method="POST" role="form" id="list_tasks_single_form">
    <div class="form-group">
        <label for="task_id" class="col-sm-2 control-label">ID#</label>

        <div class="col-sm-5">
            <input type="text" id="task_id" class="form-control" disabled="disabled"/>
        </div>
    </div>
    <div class="form-group">
        <label for="task_parent" class="control-label col-sm-2">Project</label>

        <div class="col-sm-5">
            <input type="text" id="task_parent" class="form-control" disabled="disabled"/>
        </div>
    </div>
    <div class="form-group">
        <label for="task_name" class="col-sm-2 control-label">Title</label>

        <div class="col-sm-5">
            <input type="text" id="task_name" class="form-control"  disabled="disabled"/>

        </div>
    </div>
    <br>

    <div class="form-group">
        <label for="task_desc" class="col-sm-2 control-label">Description</label>

        <div class="col-sm-5">
            <textarea class="form-control" rows="5" id="task_desc"  disabled="disabled"></textarea>
        </div>

    </div>
    <br>

    <%--<div class="form-group">
        <label for="task_final_date" class="control-label col-sm-2">Due Date</label>

        <div class="col-sm-5">

            <input type="date" id="task_final_date" class="form-control"  disabled="disabled"/>
        </div>
    </div>--%>
    <div class="form-group">
        <label for="datetimepicker2" class="control-label col-sm-2">Due Date</label>

        <div class="col-sm-5">
            <div class='input-group date nov_date_picker' id="datetimepicker2">
                <input type='text' class="form-control" id="task_final_date" disabled="disabled" placeholder="Due Date"/>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                    </span>
            </div>
        </div>
    </div>
    <br>

    <div class="form-group">
        <label for="task_assignee" class="control-label col-sm-2">Assignee</label>

        <div class="col-sm-5">
            <input type="text" class="form-control" id="task_assignee"  disabled="disabled"/>
        </div>
    </div>
    <br>

    <div class="form-group">
        <label for="task_status" class="control-label col-sm-2">Status</label>

        <div class="col-sm-5">

            <select id="task_status" class="form-control"  disabled="disabled">
                <option selected="selected">Open</option>
                <option>In Process</option>
                <option>Completed</option>
            </select>
        </div>
    </div>

    <%--<div class="form-group">--%>
    <div class="col-sm-offset-2 col-sm-2">
        <input type="button" class="btn btn-primary form-control" value="Edit" id="list_tasks_single_edit">
    </div>
    <div class="col-sm-offset-1 col-sm-2">
        <input type="button" class="btn btn-primary form-control" value="Save" id="list_tasks_single_save" disabled="disabled">
    </div>
    <%--</div>--%>
</form>
