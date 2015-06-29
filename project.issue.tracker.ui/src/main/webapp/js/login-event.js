Cookies.expire('uid');
        $(document).ready(function () {

            $('#login_btn').on('click', function () {
                var req_body = {
                    'username': $('#user').val(),
                    'password': $('#pass').val()
                };

                var up = /^[a-zA-Z_0-9\.\-,:]{1,20}$/;

                $.ajax({
                    url: "<%=context%>/login.do",
                    context: document.body,
                    dataType: "json",
                    type: "POST",
                    data: req_body,
                    timeout: 10000,
                    beforeSend: function (xhr) {
                        if (!new RegExp(up).test(req_body.username) || !new RegExp(up).test(req_body.password)) {
                            alert('Incorrect fields for username/password.');
                            xhr.abort();
                        }
                    },
                    success: function (data) {
                        Cookies.set('uid',
                                data.username
                                +',' +data.full_name
                                +',' +data.mail
                                +',' +data.utype
                                +',' +data.itemspp);
                        Cookies.set('ctxt', data.application_context);
                        window.location.pathname = "<%= context %>/index.jsp";
                    },
                    error: function (dummy, message) {
                        alert('Incorrect');
                    }
                });


            });
        });