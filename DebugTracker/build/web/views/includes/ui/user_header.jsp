<div class="container">
    <nav class="navbar navbar-inverse " role="navigation">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="#" class="navbar-brand logo-qmatic"><img style="height:20px;" src="includes/images/logo.png"></a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <% if (Utils.isAdmin(request)) { %>

                <li id="nav_create" class="text-center"><a href="#create" class="navbar-brand" style="padding: 30px"
                                                           id="Create">Create</a></li>
                <% } %>
                <li id="nav_list" class="text-center"><a href="#list" class="navbar-brand" style="padding: 30px"
                                                         id="List">List</a></li>
            </ul>

            <ul class="nav navbar-nav navbar-right">

                <li class="dropdown" id="nav_user">
                    <a href="#" class="dropdown-toggle navbar-brand" style="padding: 30px" data-toggle="dropdown"
                       id="Account"></a>
                    <ul class="dropdown-menu text-right">
                        <li class="disabled"><a href="#" id="user_bar_mail"></a></li>
                        <li><a href="#" id="view_profile_a">View Profile</a></li>
                        <li class="divider"></li>
                        <li><a href="${pageContext.request.contextPath}/logout.do">Logout</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </nav>
</div>
