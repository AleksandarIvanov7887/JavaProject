package com.track.filter;

import com.track.be.models.DBUser;
import com.track.utils.ATTRIBUTES;
import com.track.utils.FORM_PARAMS;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(description = "Login filter for session creation", servletNames = {"Loginer"}, filterName = "login")
public class LoginFilter implements Filter {
    private String au, ap;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        au = filterConfig.getServletContext().getInitParameter("administrator.username");
        ap = filterConfig.getServletContext().getInitParameter("administrator.password");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String ctxt = request.getServletContext().getContextPath();

        String user = request.getParameter(FORM_PARAMS.LOGIN.USERNAME);
        String pass = request.getParameter(FORM_PARAMS.LOGIN.PASSWORD);

        if (au.equals(user) && ap.equals(pass)) {

            servletRequest.getSession(true).setAttribute(ATTRIBUTES.USER_BEAN, new DBUser(null, ap, au, "1"));
            chain.doFilter(request, response);
        } else {
            DBUser userBean = new DBUser(user, pass);
            if (userBean.isLoginSuccsessful()) {
                userBean.init();
                servletRequest.getSession(true).setAttribute(ATTRIBUTES.USER_BEAN, userBean);

                chain.doFilter(request, response);
            } else {
                ((HttpServletResponse) response).sendRedirect(ctxt + "login.jsp");
            }
        }
    }

    @Override
    public void destroy() {
    }
}
