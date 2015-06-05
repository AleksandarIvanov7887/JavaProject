package com.track.services.auth;

import com.track.be.models.DBUser;
import com.track.utils.ATTRIBUTES;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet( urlPatterns = {"/login.do"}, name = "Loginer")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        DBUser userBean = (DBUser) req.getSession(false).getAttribute(ATTRIBUTES.USER_BEAN);
        JSONObject responseJSON = new JSONObject();

        responseJSON.put("username", userBean.getUserName());
        responseJSON.put("full_name", userBean.getName());
        responseJSON.put("mail", userBean.getEmail());
        responseJSON.put("utype", userBean.isAdmin() ? "Administrator" : "User");
        responseJSON.put("itemspp", userBean.getItemsPerPage());
        responseJSON.put("application_context", req.getServletContext().getContextPath());

        resp.getWriter().print(responseJSON.toString());
        resp.getWriter().flush();
    }
}
