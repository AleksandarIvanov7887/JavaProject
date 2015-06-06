<div class="col-sm-7 col-sm-offset-0">
    <input type="button" value="Delete User" class="btn btn-danger form-control" id="delete_user_button"/>
</div>
<script  type="text/javascript">
    $('#delete_user_button').on('click', function() {
        var el = $('#show_user_form');
        var req_body = {
            'user_id': el.find('#user_id').val()
        };

        console.log('rabotq')
        $.ajax({
            url: "/deleteUser.do",
            context: document.body,
            dataType: "json",
            type: "POST",
            data: req_body,
            timeout: 10000,
            success: function (data) {
                // TODO hide list_users_single  div :) and show other shit which are cleared of old user
            },
            error: function (dummy, message) {
                console.log('request failed : ' + message)
                // TODO
            }
        })
    })
</script>
