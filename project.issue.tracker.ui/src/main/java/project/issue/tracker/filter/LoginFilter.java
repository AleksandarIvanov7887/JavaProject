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

import org.mindrot.jbcrypt.BCrypt;

import project.issue.tracker.database.QuerySelector;
import project.issue.tracker.database.models.User;
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
        	User userAdmin = new User();
        	userAdmin.setUserName(au);
        	userAdmin.setPassword(ap);
        	userAdmin.setRole(User.TYPE_ADMIN);
        	userAdmin.setItemsPerPage(10);
            servletRequest.getSession(true).setAttribute(ATTRIBUTES.USER_BEAN, userAdmin);
            chain.doFilter(request, response);
        } else {
        	User foundUser = isLoginSuccessful(user, pass);
            if (foundUser != null) {
                servletRequest.getSession(true).setAttribute(ATTRIBUTES.USER_BEAN, foundUser);
                chain.doFilter(request, response);
            } else {
                ((HttpServletResponse) response).sendRedirect(ctxt + "/login.jsp");
            }
        }
    }

    public void destroy() {
    }
    
    private User isLoginSuccessful(String username, String password) {
    	QuerySelector selector = QuerySelector.getInstance();
    	User foundUser = selector.getUsersByUName(username).get(0);
    	String pass = foundUser.getPassword();
    	return BCrypt.checkpw(password, pass) ? foundUser : null;
    }
}
