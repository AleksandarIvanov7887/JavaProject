package project.issue.tracker.filter;

import project.issue.tracker.database.utils.*;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"}, dispatcherTypes = {DispatcherType.REQUEST}, filterName = "session")
public class SessionFilter implements Filter {
    
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String path = servletRequest.getRequestURL().toString();
        String context = servletRequest.getServletContext().getContextPath();

        if (path.endsWith(context)  ||  path.substring(0, path.length() - 1).endsWith(context)) {
            servletResponse.sendRedirect(context + "/login.jsp");
        } else if (path.contains("/login.jsp") || path.contains("/login.do")) {
            if (Utils.isLoggedIn(servletRequest)) {
                servletResponse.sendRedirect(context + "/index.jsp");
            } else {
                chain.doFilter(request, response);
            }
        } else if ((path.contains(".jsp") || path.contains(".do"))) {
            if (!Utils.isLoggedIn(servletRequest)) {
                servletResponse.sendRedirect(context + "/login.jsp");
            } else {
                chain.doFilter(request, response);
            }
        } else { // resources
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }
}
