package project.issue.tracker.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.issue.tracker.database.models.DBUser;
import project.issue.tracker.utils.ATTRIBUTES;
import project.issue.tracker.utils.FORM_PARAMS;

@WebFilter(description = "Login filter for session creation", servletNames = {"Loginer"}, filterName = "login")
public class LoginFilter implements Filter {
    private String au, ap;

    public void init(FilterConfig filterConfig) throws ServletException {
        au = filterConfig.getServletContext().getInitParameter("administrator.username");
        ap = filterConfig.getServletContext().getInitParameter("administrator.password");
    }

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

    public void destroy() {
    }
}
