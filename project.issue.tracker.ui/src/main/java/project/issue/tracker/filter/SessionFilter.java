package project.issue.tracker.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.issue.tracker.utils.Utils;

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
