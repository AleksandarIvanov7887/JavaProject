<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    
<head>
    <%@ include file="includes/bootstrap_header.jsp" %>
    <title>Issue Tracker</title>
    <% String context = request.getServletContext().getContextPath(); %>
    <script src="${pageContext.request.contextPath}/includes/javascript/validation.js" type="text/javascript"></script>
    <script type="text/javascript">
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
    </script>
</head>

<body>
    <div class="container my-jumbotron login-bg" style="width:500px; margin-top:150px;">
        <form action="#" class="form-horizontal Clean" style="width:470px; text-align:center;">
            <img style="margin:15px 0px; width: 90%; height: 90%;" src="${pageContext.request.contextPath}/img/logo.jpg">
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1 offset-top">
                    <input id="user" class="form-control text-center"  type="text" placeholder="Username"/>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1">
                    <input id="pass" class="form-control text-center"  style="width:100%;" type="password" placeholder="Password"/>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1">
                    <input class="btn btn-danger col-lg-10 help-block" style="width:100%; background:#df691a;" type="button" id="login_btn" value="Login"/>
                </div>
            </div>
        </form>
    </div>
</body>

</html>
