$(document).ready(function() {
    window.session = {
        'user_bean': {},
        'applicationName': Cookies.get('ctxt')
    };

    try {
        var u_bean_arr = Cookies.get('uid').split(',');
        window.session.user_bean.username = u_bean_arr[0];
        window.session.user_bean.full_name = u_bean_arr[1];
        window.session.user_bean.mail = u_bean_arr[2];
        window.session.user_bean.utype = u_bean_arr[3];
        window.session.user_bean.items_per_page = parseInt(u_bean_arr[4]);
        $('#user_bar_mail').text(session.user_bean.mail);
        var arr = session.user_bean.full_name.split(' ');
        $('#Account').text(arr[0]).append('<b class="caret"></b>');
    } catch (error) {
        session.user_bean.items_per_page = 15;
    }

    if (isNaN(session.user_bean.items_per_page))
        session.user_bean.items_per_page = 30;

    window.last_active_element = undefined;
    $('#list_projects_li_a').on('click', function(e) {
        $('#list_projects_li').addClass('active');
        $('#list_projects').show();

        $('#list_tasks').hide();
        $('#list_tasks_li').removeClass('active');
        $('#list_users').hide();
        $('#list_users_li').removeClass('active');

        $('#list_projects_single').hide();
        $('#list_tasks_single').hide();
        $('#list_users_single').hide();
    });

    $('#list_tasks_li_a').on('click', function(e) {
        $('#list_tasks_li').addClass('active');
        $('#list_tasks').show();

        $('#list_projects').hide();
        $('#list_projects_li').removeClass('active');
        $('#list_users').hide();
        $('#list_users_li').removeClass('active');

        $('#list_projects_single').hide();
        $('#list_tasks_single').hide();
        $('#list_users_single').hide();
    });

    $('#list_users_li_a').on('click', function(e) {
        $('#list_users_li').addClass('active');
        $('#list_users').show();

        $('#list_tasks').hide();
        $('#list_tasks_li').removeClass('active');
        $('#list_projects').hide();
        $('#list_projects_li').removeClass('active');

        $('#list_projects_single').hide();
        $('#list_tasks_single').hide();
        $('#list_users_single').hide();
    });

    $('#create_project_li_a').on('click', function() {
        $('#create_task_li').removeClass('active');
        $('#create_user_li').removeClass('active');
        $('#create_project_li').addClass('active');

        $('#create_user').hide();
        $('#create_task').hide();
        $("#create_project").show();
    });

    $("#create_task_li_a").on('click', function() {
        $('#create_project_li').removeClass('active');
        $('#create_user_li').removeClass('active');
        $('#create_task_li').addClass('active');

        $('#create_user').hide();
        $('#create_project').hide();
        $("#create_task").show();
    });

    $("#create_user_li_a").on('click', function() {
        $('#create_task_li').removeClass('active');
        $('#create_project_li').removeClass('active');
        $('#create_user_li').addClass('active');

        $('#create_project').hide();
        $('#create_task').hide();
        $("#create_user").show();
    });

    var el_List = $('#nav_list');
    el_List.on('mouseover', function() {
        el_List.addClass('active');
    });
    el_List.on('mouseout', function() {
        el_List.removeClass('active');
    });

    var el_Create = $('#nav_create');
    el_Create.on('mouseover', function() {
        el_Create.addClass('active');
    });
    el_Create.on('mouseout', function() {
        el_Create.removeClass('active');
    });

    var el_User = $('#nav_user');
    el_User.on('mouseover', function() {
        el_User.addClass('active');
    });
    el_User.on('mouseout', function() {
        el_User.removeClass('active');
    });

    $('#List').on('click', function() {
        $('#Create_div').hide();
        viewProfileHide();
        $('#List_div').show();
    });

    $('#Create').on('click', function() {
        $('#List_div').hide();
        viewProfileHide();
        $('#Create_div').show();
    });

    $('#view_profile_a').on('click', function(e) {
        var create_div = $('#Create_div');
        var list_div = $('#List_div');


        if (!list_div.is(':hidden')) {
            window.last_active_element = list_div;
        } else {
            window.last_active_element = create_div;
        }

        renderProfile({
            'create_div': create_div,
            'list_div': list_div,
            'el': $('#ViewProfile_div')
        });
    });

    function renderProfile(params) {
        $.ajax({
            url: window.session.applicationName + "/viewProfile.do",
            context: document.body,
            dataType: "json",
            type: "GET",
            timeout: 10000,
            success: function(data) {
                params.el.find('#user_name').val(data.username);
                params.el.find('#user_name_full').val(data.full_name);
                params.el.find('#user_email').val(data.mail);
                params.el.find('#user_type').val(data.uType);
                params.el.find('#items_per_page').val(data.items_per_page);

                session.user_bean.username = data.username;
                session.user_bean.user_name_first = data.fName;
                session.user_bean.user_name_middle = data.mName;
                session.user_bean.user_name_last = data.lName;
                session.user_bean.mail = data.mail;
                session.user_bean.utype = data.uType;
                session.user_bean.items_per_page = data.items_per_page;

                params.create_div.hide();
                params.list_div.hide();
                params.el.show();
            },
            error: function(dummy, message) {
            }
        });

    }

    $('#profile_back_button').on('click', function(e) {
        viewProfileHide();
        if (window.last_active_element) {
            last_active_element.show();
        } else {
            $('#Create_div').show();
        }
    });

    $('#VP_edit_btn').on('click', function(e) {
        var el = $(e.target).parent().parent();
        if ($(e.target).val() == 'Edit') {
            el.find('#user_password_1').removeAttr('disabled');
            el.find('#user_password_2').removeAttr('disabled');
            el.find('#user_password_3').removeAttr('disabled');
            el.find('#user_name_full').removeAttr('disabled');
            el.find('#items_per_page').removeAttr('disabled');
            el.find('#VP_save_btn').removeAttr('disabled');

            $('#VP_edit_btn').val('Cancel');

        } else if ($(e.target).val() == 'Cancel') {
            var vp_el = $('#VP_edit_btn').parent().parent();
            vp_el.find('#user_password_1').val('').attr('disabled', 'disabled');
            vp_el.find('#user_password_2').val('').attr('disabled', 'disabled');
            vp_el.find('#user_password_3').val('').attr('disabled', 'disabled');
            vp_el.find('#user_name_full').val(session.user_bean.full_name).attr('disabled', 'disabled');
            vp_el.find('#items_per_page').val(session.user_bean.items_per_page).attr('disabled', 'disabled');
            vp_el.find('#VP_save_btn').attr('disabled', 'disabled');

            $('#VP_edit_btn').val('Edit');
        }
    });

    function viewProfileHide() {
        var vp_el = $('#VP_edit_btn').parent().parent();

        vp_el.find('#user_password_1').attr('disabled', 'disabled');
        vp_el.find('#user_password_2').attr('disabled', 'disabled');
        vp_el.find('#user_password_3').attr('disabled', 'disabled');
        vp_el.find('#user_name_first').attr('disabled', 'disabled');
        vp_el.find('#user_name_middle').attr('disabled', 'disabled');
        vp_el.find('#user_name_last').attr('disabled', 'disabled');
        vp_el.find('#user_email').attr('disabled', 'disabled');
        vp_el.find('#items_per_page').attr('disabled', 'disabled');
        vp_el.find('#VP_save_btn').attr('disabled', 'disabled');

        $('#VP_edit_btn').val('Edit');
        vp_el.parent().parent().parent().hide();
    }

    $('#VP_save_btn').on('click', function(e) {
        var el = $('#user_change_form');
        var req_body = {
            'old_password': el.find('#user_password_1').val(),
            'password_new': el.find('#user_password_2').val(),
            'password_new_rep': el.find('#user_password_3').val(),
            'user_name_full': el.find('#user_name_full').val(),
            'user_email': el.find('#user_email').val(),
            'user_items_per_page': el.find('#items_per_page option:selected').text()
        };

        if (req_body.password_new !== req_body.password_new_rep) {
            alert('New passwords must match.');
            return;
        }

        var up = /^[a-zA-Z_0-9 \.\-,:]{1,20}$/;
        var person = /^[a-zA-Z \-]{1,50}$/;

        if (!new RegExp(up).test(req_body.old_password)) {
            alert('Bad old password.');
            return;
        } else if (!new RegExp(up).test(req_body.password_new)) {
            alert('Bad new password.');
            return;
        } else if (!new RegExp(person).test(req_body.user_name_full)) {
            alert('Bad full name.');
            return;
        } else if (!(10 <= req_body.user_items_per_page <= 60)) {
            alert('Bad input for items per page.');
            return;
        }

        $.ajax({
            url: window.session.applicationName + "/changeUser.do",
            context: document.body,
            dataType: "json",
            type: "POST",
            data: req_body,
            timeout: 10000,
            success: function(data) {
                if (data.error) {
                    alert(data.error);
                    return;
                }
                el.find('#VP_edit_btn').trigger('click');


                Cookies.expire('uid');
                Cookies.set('uid',
                        data.username
                        + ',' + data.full_name
                        + ',' + data.mail
                        + ',' + data.utype
                        + ',' + data.itemspp);

                var u_bean_arr = Cookies.get('uid').split(',');
                window.session.user_bean.username = u_bean_arr[0];
                window.session.user_bean.full_name = u_bean_arr[1];
                window.session.user_bean.mail = u_bean_arr[2];
                window.session.user_bean.utype = u_bean_arr[3];
                window.session.user_bean.items_per_page = parseInt(u_bean_arr[4]);

                var arr = session.user_bean.full_name.split(' ');
                $('#Account').text(arr[0]).append('<b class="caret"></b>');

                el.find('#user_name_full').val(session.user_bean.full_name);
                el.find('#items_per_page').val(session.user_bean.items_per_page);

                alert('Profile updated.');

                renderProfile({
                    'create_div': $('#Create_div'),
                    'list_div': $('#List_div'),
                    'el': $('#ViewProfile_div')
                });
            },
            error: function(dummy, message) {
            }
        });
    });

    $('#search_project_btn').on('click', function() {
        var el = $('#list_projects_form');
        var req_body = {
            'project_name': el.find('#project_name').val()
        };

        var p = /^[a-zA-Z_0-9 \.\-,:]{0,50}$/;

        if (!new RegExp(p).test(req_body.project_name)) {
            alert('Bad project name');
            return;
        }

        $.ajax({
            url: window.session.applicationName + "/searchProject.do",
            context: document.body,
            dataType: "json",
            type: "GET",
            data: req_body,
            timeout: 10000,
            success: function(data) {
                if (!data.projects.length) {
                    alert('No projects found');
                    return;
                }
                var body_el = $('#list_projects_table_body');
                body_el.empty();
                var pag_ul = $('#list_projects_pagination');
                pag_ul.empty();
                renderProjects(data, body_el, pag_ul);
            },
            error: function(dummy, message) {
            }
        });
    });

    function renderProjects(json, body_el, pag_ul) {
        var arr = json.projects;

        for (var i = 0; i < arr.length; i++) {
            body_el.append(
                    '<tr' + (i < parseInt(session.user_bean.items_per_page) ? "" : " hidden=\"hidden\"") + '><td>' + arr[i].pID +
                    '</td><td><a href="#" class="anchor-listings-project">' + arr[i].pName +
                    '</a></td><td>' + arr[i].pDesc + 
                    '</td><td>' + arr[i].pUsers +
                    '</td><td>' + arr[i].pTasks +
                    '</td></tr>');
        }

        $('.anchor-listings-project').on('click', function(e) {
            listSingleProject({
                'el': $('#list_projects_single'),
                'hide_div': $('#list_projects'),
                'id': $(e.target).parent().prev().text()
            });
            var kotva = e.target;
            var id = $(kotva).parent().prev().text();
            var name = $(kotva).text();
            var desc = $(kotva).parent().next().text();

            var el = $('#list_projects_single');
            el.find('#project_id').val(id);
            el.find('#project_name').val(name);
            el.find('#project_desc').val(desc);

            $('#list_projects').hide();
            el.show();
        });

        var pag_ul_el_ctr = Math.ceil(arr.length / parseInt(session.user_bean.items_per_page));
        pag_ul.append('<li class="disabled"><a href="#list_projects_table_body" id="list_proj_pag_back">&laquo;</a></li>');
        pag_ul.append('<li class="active list_projects_pag"><a href="#list_projects_table_body">1</a></li> ');
        for (var i = 1; i < pag_ul_el_ctr; i++) {
            pag_ul.append('<li class="list_projects_pag"><a href="#list_projects_table_body">' + (i + 1) + '</a></li>');
        }
        pag_ul.append('<li class=' + (pag_ul_el_ctr < 2 ? "disabled" : "") + '><a href="#list_projects_table_body" id="list_proj_pag_forw">&raquo;</a></li>');

        pag_ul.find('.list_projects_pag').on('click', function(e) {
            var src = $(e.target).parent();
            var new_num = $(src.children()[0]).text();
            var old_num = pag_ul.find('.active').children().text();

            pag_ul.find('.active').removeClass('active');
            src.addClass('active');

            for (var i = (old_num - 1) * parseInt(session.user_bean.items_per_page); i < (old_num) * parseInt(session.user_bean.items_per_page) && i < body_el.children().length; i++) {
                $(body_el.children()[i]).hide();
            }

            for (var i = (new_num - 1) * parseInt(session.user_bean.items_per_page); i < (new_num) * parseInt(session.user_bean.items_per_page) && i < body_el.children().length; i++) {
                $(body_el.children()[i]).show();
            }

            new_num == 1 ? $(pag_ul.children().first().addClass('disabled')) : $(pag_ul.children().first().removeClass('disabled'));
            new_num == pag_ul_el_ctr ? $(pag_ul.children().last().addClass('disabled')) : $(pag_ul.children().last().removeClass('disabled'));
        });

        pag_ul.find('#list_proj_pag_back').on('click', function(e) {
            var curr_page = parseInt(pag_ul.find('.active').first().text());
            var body_els = body_el.children();

            if (curr_page == "1") {
                e.preventDefault();
                return;
            }

            for (var i = curr_page * parseInt(session.user_bean.items_per_page) - 1; i >= (curr_page - 1) * parseInt(session.user_bean.items_per_page); i--) {
                $(body_els[i]).hide();
            }

            for (var i = (curr_page - 2) * parseInt(session.user_bean.items_per_page); i < (curr_page - 1) * parseInt(session.user_bean.items_per_page); i++) {
                $(body_els[i]).show();
            }

            pag_ul.find('.active').prev().addClass('active').next().removeClass('active');

            if (curr_page == "2") {
                pag_ul.children().first().addClass('disabled');
            }
            if (curr_page == pag_ul_el_ctr) {
                $(pag_ul.children().last().removeClass('disabled'));
            }
        });

        pag_ul.find('#list_proj_pag_forw').on('click', function(e) {
            var curr_page = parseInt(pag_ul.find('.active').first().text());
            var body_els = body_el.children();

            if (curr_page == pag_ul_el_ctr) {
                e.preventDefault();
                return;
            }

            for (var i = curr_page * parseInt(session.user_bean.items_per_page) - 1; i >= (curr_page - 1) * parseInt(session.user_bean.items_per_page); i--) {
                $(body_els[i]).hide();
            }

            for (var i = curr_page * parseInt(session.user_bean.items_per_page); i < (curr_page + 1) * parseInt(session.user_bean.items_per_page); i++) {
                $(body_els[i]).show();
            }

            if (curr_page == "1") {
                pag_ul.children().first().removeClass('disabled');
            }
            if (curr_page == pag_ul_el_ctr - 1) {
                pag_ul.children().last().addClass('disabled');
            }

            pag_ul.find('.active').next().addClass('active').prev().removeClass('active');
        });

    }

    $('#list_project_back_button').on('click', function() {
        hideSingleProject();
        $('#list_projects').show();
    });

    function hideSingleProject() {
        var el = $('#list_projects_single');
        el.find('#project_name').attr('disabled', 'disabled');
        el.find('#project_desc').attr('disabled', 'disabled');
        el.find('#list_projects_single_save').attr('disabled', 'disabled');
        el.find('#list_projects_single_edit').val('Edit');

        el.hide();
    }

    $('#list_projects_single_form').find('#list_projects_single_edit').on('click', function(e) {
        var el = $('#list_projects_single_form');
        if ($(e.target).val() == 'Edit') {
            $(e.target).val('Cancel');

            el.find('#list_projects_single_save').removeAttr('disabled');
            el.find('#project_desc').removeAttr('disabled');
            el.find('#project_name').removeAttr('disabled');

        } else if ($(e.target).val() == 'Cancel') {
            $(e.target).val('Edit');

            el.find('#list_projects_single_save').attr('disabled', 'disabled');
            el.find('#project_desc').attr('disabled', 'disabled');
            el.find('#project_name').attr('disabled', 'disabled');
        }

    });

    $('#list_projects_single_form').find('#list_projects_single_save').on('click', function() {
        var form = $('#list_projects_single_form');

        var req_data = {
            'id': form.find('#project_id').val(),
            'name': form.find('#project_name').val(),
            'desc': form.find('#project_desc').val()
        };

        $.ajax({
            url: window.session.applicationName + "/changeProject.do",
            context: document.body,
            dataType: "json",
            type: "POST",
            data: req_data,
            timeout: 10000,
            success: function(data) {
                $('#list_projects_single_form').find('#list_projects_single_edit').trigger('click');
            },
            error: function(dummy, message) {

            }
        });

    });

    $('#search_task_btn').on('click', function() {
        var el = $('#list_tasks_form');
        var req_body = {
            'project': el.find('#task_parent').val(),
            'task_name': el.find('#task_name').val(),
            'assignee': el.find('#task_assignee').val(),
            'status': el.find('#task_status').val()
        };

        var p = /^[a-zA-Z_0-9 \.\-,:]{0,20}$/;
        var person = /^[a-zA-Z \-]{0,50}$/;

        if (!new RegExp(p).test(req_body.project)) {
            alert("Bad parent project name.");
            return;
        } else if (!new RegExp(p).test(req_body.task_name)) {
            alert('Bad task name.');
            return;
        } else if (!new RegExp(person).test(req_body.assignee)) {
            alert('Bad assignee name.');
            return;
        }


        $.ajax({
            url: window.session.applicationName + "/searchTask.do",
            context: document.body,
            dataType: "json",
            type: "GET",
            data: req_body,
            timeout: 10000,
            success: function(data) {
                if (!data.tasks.length) {
                    alert('No tasks found');
                    return;
                }
                var body_el = $('#list_tasks_table_body');
                body_el.empty();
                var pag_ul = $('#list_tasks_pagination');
                pag_ul.empty();
                renderTasks(data, body_el, pag_ul);
            },
            error: function(dummy, message) {
            }
        });
    });

    $('#list_task_back_button').on('click', function() {
        hideSingleTask();
        $('#list_tasks').show();
    });

    function hideSingleTask() {
        var el = $('#list_tasks_single');
        el.find("#task_assignee").attr('disabled', 'disabled');
        el.find('#task_status').attr('disabled', 'disabled');
        el.find('#list_tasks_single_save').attr('disabled', 'disabled');
        el.find('#list_tasks_single_edit').val('Edit');

        el.hide();
    }

    $('#list_tasks_single_form').find('#list_tasks_single_edit').on('click', function(e) {
        var el = $('#list_tasks_single_form');
        if ($(e.target).val() == 'Edit') {
            $(e.target).val('Cancel');

            el.find('#task_assignee').removeAttr('disabled');
            if (el.find('#task_assignee').val() === session.user_bean.full_name) {
                el.find('#task_status').removeAttr('disabled');
            }
            el.find('#task_final_date').removeAttr('disabled');
            el.find('#list_tasks_single_save').removeAttr('disabled');

        } else if ($(e.target).val() == 'Cancel') {
            listSingleTask({
                'id': el.find('#task_id').val(),
                'div_el': $('#list_tasks_single'),
                'task_list_div': $('#list_tasks'),
                'project_name': el.find('#task_parent').val()
            });

            $(e.target).val('Edit');

            el.find('#task_assignee').attr('disabled', 'disabled');
            el.find('#task_status').attr('disabled', 'disabled');
            el.find('#task_final_date').attr('disabled', 'disabled');
            el.find('#list_tasks_single_save').attr('disabled', 'disabled');
        }
    });

    function renderTasks(json, body_el, pag_ul) {
        var arr = json.tasks;
        if (!arr.length) {
            return;
        }
        for (var i = 0; i < arr.length; i++) {
            body_el.append('<tr' + (i < parseInt(session.user_bean.items_per_page) ? "" : " hidden=\"hidden\"") +
                    (json.important ? " class=\"tr_for_important_tasks\"" : "") +
                    '>' + (json.important ? '' : '') +
                    '<td>' + arr[i].tID +
                    '</td><td><a href="#" class="anchor-listings-task">' + arr[i].tName +
                    '</a></td><td>' + arr[i].tProject +
                    '</td><td>' + arr[i].tAssignee +
                    '</td><td>' + arr[i].tStatus +
                    '</td><td>' + arr[i].tDDate +
                    '</td></tr>');
        }

        $('.anchor-listings-task').on('click', function(e) {
            e.preventDefault();
            listSingleTask({
                'id': $(e.target).parent().prev().text(),
                'project_name': $(e.target).parent().next().text(),
                'div_el': $('#list_tasks_single'),
                'task_list_div': $('#list_tasks')
            });

        });
        if (json.important) {
            $('.tr_for_important_tasks').on('click', function(e) {
                if ($(e.target).prop('tagName') === 'A') {
                    e.preventDefault();
                    return;
                }
                var url = $(e.target).parent().find('#shit').attr('href');
                window.open(url);
            });
        }

        var pag_ul_el_ctr = Math.ceil(arr.length / parseInt(session.user_bean.items_per_page));
        pag_ul.append('<li class="disabled"><a href="#list_tasks_table_body" id="list_task_pag_back">&laquo;</a></li>');
        pag_ul.append('<li class="active list_tasks_pag"><a href="#list_tasks_table_body">1</a></li>');
        for (var i = 1; i < pag_ul_el_ctr; i++) {
            pag_ul.append('<li class="list_tasks_pag"><a href="#list_tasks_table_body">' + (i + 1) + '</a></li>');
        }
        pag_ul.append('<li class=' + (pag_ul_el_ctr < 2 ? "disabled" : "") + '><a href="#list_tasks_table_body" id="list_task_pag_forw">&raquo;</a>')

        pag_ul.find('.list_tasks_pag').on('click', function(e) {
            var src = $(e.target).parent();
            var new_num = $(src.children()[0]).text();
            var old_num = pag_ul.find('.active').children().text();

            pag_ul.find('.active').removeClass('active');
            src.addClass('active');

            for (var i = (old_num - 1) * parseInt(session.user_bean.items_per_page); i < (old_num) * parseInt(session.user_bean.items_per_page) && i < body_el.children().length; i++) {
                $(body_el.children()[i]).hide();
            }

            for (var i = (new_num - 1) * parseInt(session.user_bean.items_per_page); i < (new_num) * parseInt(session.user_bean.items_per_page) && i < body_el.children().length; i++) {
                $(body_el.children()[i]).show();
            }

            new_num == 1 ? $(pag_ul.children().first().addClass('disabled')) : $(pag_ul.children().first().removeClass('disabled'));
            new_num == pag_ul_el_ctr ? $(pag_ul.children().last().addClass('disabled')) : $(pag_ul.children().last().removeClass('disabled'));
        });

        pag_ul.find('#list_task_pag_back').on('click', function(e) {
            var curr_page = parseInt(pag_ul.find('.active').first().text());
            var body_els = body_el.children();

            if (curr_page == "1") {
                e.preventDefault();
                return;
            }

            for (var i = curr_page * parseInt(session.user_bean.items_per_page) - 1; i >= (curr_page - 1) * parseInt(session.user_bean.items_per_page); i--) {
                $(body_els[i]).hide();
            }

            for (var i = (curr_page - 2) * parseInt(session.user_bean.items_per_page); i < (curr_page - 1) * parseInt(session.user_bean.items_per_page); i++) {
                $(body_els[i]).show();
            }

            pag_ul.find('.active').prev().addClass('active').next().removeClass('active');

            if (curr_page == "2") {
                pag_ul.children().first().addClass('disabled');
            }
            if (curr_page == pag_ul_el_ctr) {
                $(pag_ul.children().last().removeClass('disabled'));
            }
        });

        pag_ul.find('#list_task_pag_forw').on('click', function(e) {
            var curr_page = parseInt(pag_ul.find('.active').first().text());
            var body_els = body_el.children();

            if (curr_page == pag_ul_el_ctr) {
                e.preventDefault();
                return;
            }

            for (var i = curr_page * parseInt(session.user_bean.items_per_page) - 1; i >= (curr_page - 1) * parseInt(session.user_bean.items_per_page); i--) {
                $(body_els[i]).hide();
            }

            for (var i = curr_page * parseInt(session.user_bean.items_per_page); i < (curr_page + 1) * parseInt(session.user_bean.items_per_page); i++) {
                $(body_els[i]).show();
            }

            if (curr_page == "1") {
                pag_ul.children().first().removeClass('disabled');
            }
            if (curr_page == pag_ul_el_ctr - 1) {
                pag_ul.children().last().addClass('disabled');
            }

            pag_ul.find('.active').next().addClass('active').prev().removeClass('active');
        });
    }

    $('#search_user_btn').on('click', function() {
        var el = $('#list_users_form');

        var req_body = {
            'username': el.find('#user_name').val(),
            'user_name_full': el.find('#user_name_full').val(),
            'user_email': el.find('#user_email').val(),
            'user_type': el.find('#user_type').val()
        };

        var p = /^[a-zA-Z_0-9 \.\-,:]{0,100}$/;
        var up = /^[a-zA-Z_0-9\.\-,:]{0,20}$/;
        var person = /^[a-zA-Z \-]{0,50}$/;

        if (!new RegExp(p).test(req_body.username)) {
            alert('Bad username');
            return;
        } else if (!new RegExp(person).test(req_body.user_name_full)) {
            alert('Bad full name.');
            return;
        } else if (!(req_body.user_type === 'Administrator' || req_body.user_type === 'User')) {
            alert('Bad input for user type');
            return;
        }

        $.ajax({
            url: window.session.applicationName + "/searchUser.do",
            context: document.body,
            dataType: "json",
            type: "GET",
            data: req_body,
            timeout: 10000,
            success: function(data) {
                if (!data.users.length) {
                    alert('No users found');
                    return;
                }
                var body_el = $('#list_users_table_body');
                body_el.empty();
                var pag_ul = $('#list_user_pagination');
                pag_ul.empty();
                renderUser(data, body_el, pag_ul);
            },
            error: function(dummy, message) {
            }
        });
    });

    function renderUser(json, body_el, pag_ul) {
        var arr = json.users;
        if (!arr.length) {
            return;
        }

        for (var i = 0; i < arr.length; i++) {
            body_el.append('<tr' + (i < parseInt(session.user_bean.items_per_page) ? "" : " hidden=\"hidden\"") + '><td>' + arr[i].uID +
                    '</td><td><a href="#" class="anchor-listings-user">' + arr[i].uFullName +
                    '</a></td><td>' + arr[i].uUsername +
                    '</td><td>' + arr[i].uRole +
                    '</td><td><a href="mailto:' + arr[i].uEmail +
                    '" class="anchor-listings-user-email">' + arr[i].uEmail +
                    '</a></td></tr>');
        }

        $('.anchor-listings-user').on('click', function(e) {
            var kotva = e.target;
            var id = $(kotva).parent().prev().text();

            $.ajax({
                url: window.session.applicationName + "/viewUser.do",
                context: document.body,
                dataType: "json",
                type: "GET",
                data: {'user_id': id},
                timeout: 10000,
                success: function(data) {
                    if (data.error) {
                        alert(data.error);
                        return;
                    }
                    var el = $('#list_users_single');

                    el.find('#user_id').val(data.id);
                    el.find('#user_name').val(data.username);
                    el.find('#user_name_full').val(data.full_name);
                    el.find('#user_email').val(data.mail);
                    el.find('#user_role').val(data.utype);

                    $('#list_users').hide();
                    el.show();

                },
                error: function(dummy, message) {
                }
            });

        });

        var pag_ul_el_ctr = Math.ceil(arr.length / parseInt(session.user_bean.items_per_page));
        pag_ul.append('<li class="disabled"><a href="#list_users_table_body" id="list_user_pag_back">&laquo;</a></li>');
        pag_ul.append('<li class="active list_users_pag"><a href="#list_users_table_body">1</a></li>');
        for (var i = 1; i < pag_ul_el_ctr; i++) {
            pag_ul.append('<li class="list_users_pag"><a href="#list_tasks_table_body">' + (i + 1) + '</a></li>');
        }
        pag_ul.append('<li class=' + (pag_ul_el_ctr < 2 ? "disabled" : "") + '><a href="#list_tasks_table_body" id="list_user_pag_forw">&raquo;</a>');

        pag_ul.find('.list_users_pag').on('click', function(e) {
            var src = $(e.target).parent();
            var new_num = $(src.children()[0]).text();
            var old_num = pag_ul.find('.active').children().text();

            pag_ul.find('.active').removeClass('active');
            src.addClass('active');

            for (var i = (old_num - 1) * parseInt(session.user_bean.items_per_page); i < (old_num) * parseInt(session.user_bean.items_per_page) && i < body_el.children().length; i++) {
                $(body_el.children()[i]).hide();
            }

            for (var i = (new_num - 1) * parseInt(session.user_bean.items_per_page); i < (new_num) * parseInt(session.user_bean.items_per_page) && i < body_el.children().length; i++) {
                $(body_el.children()[i]).show();
            }

            new_num == 1 ? $(pag_ul.children().first().addClass('disabled')) : $(pag_ul.children().first().removeClass('disabled'));
            new_num == pag_ul_el_ctr ? $(pag_ul.children().last().addClass('disabled')) : $(pag_ul.children().last().removeClass('disabled'));
        });

        pag_ul.find('#list_user_pag_back').on('click', function(e) {
            var curr_page = parseInt(pag_ul.find('.active').first().text());
            var body_els = body_el.children();

            if (curr_page == "1") {
                e.preventDefault();
                return;
            }
            
            for (var i = curr_page * parseInt(session.user_bean.items_per_page) - 1; i >= (curr_page - 1) * parseInt(session.user_bean.items_per_page); i--) {
                $(body_els[i]).hide();
            }
            
            for (var i = (curr_page - 2) * parseInt(session.user_bean.items_per_page); i < (curr_page - 1) * parseInt(session.user_bean.items_per_page); i++) {
                $(body_els[i]).show();
            }

            pag_ul.find('.active').prev().addClass('active').next().removeClass('active');

            if (curr_page == "2") {
                pag_ul.children().first().addClass('disabled');
            }
            if (curr_page == pag_ul_el_ctr) {
                $(pag_ul.children().last().removeClass('disabled'));
            }
        });

        pag_ul.find('#list_user_pag_forw').on('click', function(e) {
            var curr_page = parseInt(pag_ul.find('.active').first().text());
            var body_els = body_el.children();

            if (curr_page == pag_ul_el_ctr) {
                e.preventDefault();
                return;
            }
            for (var i = curr_page * parseInt(session.user_bean.items_per_page) - 1; i >= (curr_page - 1) * parseInt(session.user_bean.items_per_page); i--) {
                $(body_els[i]).hide();
            }
            for (var i = curr_page * parseInt(session.user_bean.items_per_page); i < (curr_page + 1) * parseInt(session.user_bean.items_per_page); i++) {
                $(body_els[i]).show();
            }
            if (curr_page == "1") {
                pag_ul.children().first().removeClass('disabled')
            }
            if (curr_page == pag_ul_el_ctr - 1) {
                pag_ul.children().last().addClass('disabled')
            }
            pag_ul.find('.active').next().addClass('active').prev().removeClass('active');
        });
    }

    $('#list_user_back_button').on('click', function() {
        $('#list_users_single').hide();
        $('#list_users').show();
    });
    
    function listSingleTask(params) {
        $.ajax({
            url: window.session.applicationName + "/viewTask.do",
            context: document.body,
            dataType: "json",
            type: "GET",
            data: {'task_id': params.id, 'project_name': params.project_name},
            timeout: 10000,
            success: function(data) {
                if (data.error) {
                    alert(data.error);
                    return;
                }
                fillTaskView(data, params);
                renderComments(data, params);
                params.task_list_div.hide();
                params.div_el.show();
            },
            error: function(dummy, message) {

            }
        });
    }

    function fillTaskView(data, elements) {
        elements.div_el.find('#task_id').val(data.task.id);
        elements.div_el.find('#task_name').val(data.task.title);
        elements.div_el.find('#task_parent').val(data.task.project);
        elements.div_el.find('#task_desc').val(data.task.desc);
        elements.div_el.find('#task_final_date').val(data.task.ddate);
        elements.div_el.find('#task_assignee').val(data.task.assignee);
        elements.div_el.find('#task_status').val(data.task.status);
    }

    function renderComments(data, elements) {
        var comm_div = elements.div_el.find('#task_comments');
        comm_div.empty();

        data.comments.forEach(function(c) {
            comm_div.append(renderSingleComment(c.from, c.date, c.text));
        });
    }

    function renderSingleComment(from, date, text) {
        return '<div class="my-jumbotron"><div class="text-left"><b>' + from +
                '</b>   <em>' + date +
                '</em></div><div class="text-left" style="margin-left:10px;">' + text +
                '</div></div>';
    }

    $('#add_comment_btn').on('click', function() {
        var req = {
            'task_id': $('#list_tasks_single_form').find('#task_id').val(),
            'comment': $('#add_comment_textarea').val(),
            'project_name': $('#list_tasks_single_form').find('#task_parent').val()
        };

        $.ajax({
            url: window.session.applicationName + "/addComment.do",
            context: document.body,
            dataType: "json",
            type: "POST",
            data: req,
            timeout: 10000,
            success: function(data) {
                if (data.success) {

                    $('#add_comment_textarea').val('');
                    listSingleTask({
                        'id': req.task_id,
                        'div_el': $('#list_tasks_single'),
                        'task_list_div': $('#list_tasks'),
                        'project_name': req.project_name
                    });
                    alert(data.success);
                } else if (data.error) {
                    alert(data.error);
                }
            },
            error: function(dummy, message) {
            }
        });

    });
    
    $('#list_tasks_single_save').on('click', function() {
        var form_el = $('#list_tasks_single_form');
        var req_data = {
            'task_id': form_el.find('#task_id').val(),
            'project_name': form_el.find('#task_parent').val(),
            'ddate': form_el.find('#task_final_date').val(),
            'assignee': form_el.find('#task_assignee').val(),
            'task_status': form_el.find('#task_status').val()
        };

        $.ajax({
            url: window.session.applicationName + "/changeTask.do",
            context: document.body,
            dataType: "json",
            type: "POST",
            data: req_data,
            timeout: 10000,
            success: function(data) {
                if (data.error) {
                    alert(data.error);
                } else {
                    alert(data.success);

                }
                $('#list_tasks_single_form').find('#list_tasks_single_edit').trigger('click');
            },
            error: function(dummy, message) {
                alert("Something went wrong try again.");
                $('#list_tasks_single_form').find('#list_tasks_single_edit').trigger('click');
            }
        });
    });
    
    function listSingleProject(params) {
        $.ajax({
            url: window.session.applicationName + "/viewProject.do",
            context: document.body,
            dataType: "json",
            type: "GET",
            data: {'id': params.id},
            timeout: 10000,
            success: function(data) {

                params.el.find('#project_id').val(data.id);
                params.el.find('#project_name').val(data.name);
                params.el.find('#project_desc').val(data.desc);

                params.hide_div.hide();
                params.el.show();
            },
            error: function(dummy, message) {
            }
        });
    }

    function renderSingleProject(data, params) {
        params.el.find('#project_id').val(data.id);
        params.el.find('#project_name').val(data.name);
        params.el.find('#project_desc').val(data.desc);
    }
    
    $('#notes_create_task').on('click', function(e) {
        $('#Create').click();
        $('#create_task_li_a').click();
    });
    
    $('#notes_important').on('click', function(e) {
        $('#List').click();
        $('#list_tasks_li_a').click();
        $('#list_tasks_table_body').empty();
        var el = $('#list_tasks_form');
        el.find('#task_status').val('');
        el.find('#task_assignee').val('');
        el.find('#task_parent').val('');
        el.find('#task_name').val('');

        el.find('#search_task_btn').trigger('click');

    });
    
    $('#notes_active_tasks').on('click', function(e) {
        $('#List').click();
        $('#list_tasks_li_a').click();
        $('#list_tasks_table_body').empty();
        var el = $('#list_tasks_form');
        el.find('#task_status').val('In Process');
        el.find('#task_assignee').val(window.session.user_bean.full_name);

        el.find('#task_parent').val('')
        el.find('#task_name').val('')

        el.find('#search_task_btn').trigger('click');
    });
    
    $('#notes_open_tasks').on('click', function(e) {
        $('#List').click();
        $('#list_tasks_li_a').click();
        var el = $('#list_tasks_form');
        el.find('#task_status').val('Open');
        el.find('#task_assignee').val(window.session.user_bean.full_name);
        el.find('#search_task_btn').trigger('click');
    });
    
    $('#create_project_form').find('#create_project').on('click', function() {
        var el = $('#create_project_form');
        var req_body = {
            'name': el.find('#project_name').val(),
            'desc': el.find('#project_desc').val()
        };

        var p = /^[a-zA-Z_0-9 \.\-,:]{1,50}$/;
        var desc = /^[a-zA-Z_0-9\.\-,:\s()]{1,2000}$/;

        if (!new RegExp(p).test(req_body.name)) {
            alert("Bad project name.");
            return;
        } else if (!new RegExp(desc).test(req_body.desc)) {
            alert("Bad project description.");
            return;
        }

        $.ajax({
            url: window.session.applicationName + "/createProject.do",
            context: document.body,
            dataType: "json",
            type: "POST",
            data: req_body,
            timeout: 10000,
            success: function(data) {
                if (data.success) {
                    alert(data.success);
                    el.find('#project_name').val('');
                    el.find('#project_desc').val('');
                } else if (data.error) {
                    alert(data.error);
                }
            },
            error: function(dummy, message) {
            }
        });
    });

    $('#create_task_form').find('#create_task').on('click', function() {
        var el = $('#create_task_form');
        var req_body = {
            'tParent': el.find('#task_parent').val(),
            'tName': el.find('#task_name').val(),
            'tDesc': el.find('#task_desc').val(),
            'tDDate': el.find('#task_final_date').val(),
            'tAssignee': el.find('#task_assignee').val(),
            'tStatus': el.find('#task_status').val(),
            'tComment': el.find('#task_comment').val(),
            'tImportant': el.find('#task_important').is(':checked') + ''
        };

        var p = /^[a-zA-Z_0-9 \.\-,:]{1,50}$/;
        var assignee = /^[a-zA-Z \-]{1,50}$/;
        var com_desc = /^[a-zA-Z_0-9\.\-,:\s()]{0,2000}$/;
        var status = /^Open|In Process|Completed&/;
        var date = /^(\d{4})-(\d{2})-(\d{2})&/;

        if (!new RegExp(p).test(req_body.tParent)) {
            alert("Bad parent project name.");
            return;
        } else if (!new RegExp(p).test(req_body.tName)) {
            alert("Bad task name.");
            return;
        } else if (!new RegExp(com_desc).test(req_body.tDesc)) {
            alert('Bad task description.');
            return;
        } else if (!new RegExp(assignee).test(req_body.tAssignee)) {
            alert('Bad assignee.');
            return;
        } else if (!new RegExp(status).test(req_body.tStatus)) {
            alert('Bad status for task.');
            return;
        } else if (!new RegExp(com_desc).test(req_body.tComment)) {
            alert('Bad comment for task.');
            return;
        } else if (!req_body.tDDate.length) {
            alert('Bad date.');
            return;
        }

        $.ajax({
            url: window.session.applicationName + "/createTask.do",
            context: document.body,
            dataType: "json",
            type: "POST",
            data: req_body,
            timeout: 10000,
            success: function(data) {
                if (data.success) {
                    el.find('#task_parent').val('');
                    el.find('#task_name').val('');
                    el.find('#task_desc').val('');
                    el.find('#task_final_date').val('');
                    el.find('#task_assignee').val('');
                    el.find('#task_status').val('');
                    el.find('#task_comment').val('');
                    alert(data.success);
                } else if (data.error) {
                    alert(data.error);
                }
            },
            error: function(dummy, message) {
            }
        });
    });

    $('#create_user_form').find('#create_user').on('click', function() {
        var el = $('#create_user_form');
        var req_body = {
            'username': el.find('#user_name').val(),
            'password': el.find('#user_password_1').val(),
            'password_copy': el.find('#user_password_2').val(),
            'user_name_full': el.find('#user_name_full').val(),
            'user_email': el.find('#user_email').val(),
            'user_type': el.find('#user_type').val(),
            'user_items_per_page': el.find('#items_per_page').val()
        };

        var up = /^[a-zA-Z_0-9\.\-,:]{1,20}$/;
        var person = /^[a-zA-Z \-]{1,50}$/;

        if (req_body.password !== req_body.password_copy) {
            alert('Password must match.');
            return;
        }

        if (!new RegExp(up).test(req_body.username)) {
            alert("Bad username.");
            return;
        } else if (!new RegExp(up).test(req_body.password)) {
            alert("Bad password");
            return;
        } else if (!new RegExp(person).test(req_body.user_name_full)) {
            alert('Cant name a person like that.');
            return;
        } else if (!(req_body.user_type === 'Administrator' || req_body.user_type === 'User')) {
            alert('Bad input for user.');
            return;
        } else if (!(10 <= req_body.user_items_per_page <= 60)) {
            alert('Bad input for items per page. Select from options given.');
            return;
        }

        $.ajax({
            url: window.session.applicationName + "/createUser.do",
            context: document.body,
            dataType: "json",
            type: "POST",
            data: req_body,
            timeout: 10000,
            success: function(data) {
                if (data.success) {
                    el.find('#user_name').val('');
                    el.find('#user_password_1').val('');
                    el.find('#user_name_full').val('');
                    el.find('#user_email').val('');
                    el.find('#user_type').val('');
                    el.find('#items_per_page').val('');
                    el.find('#user_password_2').val('');
                    alert(data.success);
                } else if (data.error) {
                    alert(data.error);
                }
            },
            error: function(dummy, message) {
            }
        });
    });
});
