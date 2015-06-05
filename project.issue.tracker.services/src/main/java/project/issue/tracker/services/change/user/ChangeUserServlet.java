package project.issue.tracker.services.change.user;

import com.track.be.models.DBUser;
import com.track.utils.ATTRIBUTES;
import com.track.utils.FORM_PARAMS;
import net.sf.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet( urlPatterns = {"/changeUser.do"}, name = "UserChanger")
public class ChangeUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        String oldPasword = req.getParameter(FORM_PARAMS.CHANGE_USER.OLD_PASSWORD);
        String newPassword = req.getParameter(FORM_PARAMS.CHANGE_USER.NEW_PASSWORD);
        String fullName = req.getParameter(FORM_PARAMS.CHANGE_USER.FULL_NAME);
        String itemsPerPage = req.getParameter(FORM_PARAMS.CHANGE_USER.ITEMS_PER_PAGE);

        PrintWriter out = resp.getWriter();

        if (!oldPasword.matches("^[a-zA-Z_0-9\\.\\-,:]{1,20}$") ||
                !newPassword.matches("^[a-zA-Z_0-9\\.\\-,:]{1,20}$") ||
                !fullName.matches("^[a-zA-Z \\-]{1,50}$") ||
                !(itemsPerPage.charAt(0) >= '1' && itemsPerPage.charAt(0) <= '6' && itemsPerPage.charAt(1) == '0' && itemsPerPage.length() == 2)) {

            out.print("{\"error\":\"Bad input.\"}");
            out.flush();
            return;
        }


        DBUser userBean = (DBUser) req.getSession().getAttribute(ATTRIBUTES.USER_BEAN);

        DBUser currUser = new DBUser(userBean.getId());

        if (!currUser.isCorrectPassword(oldPasword)) {
            out.print("{\"error\":\"Bad password. Old password was not correct.\"}");
            out.flush();
            return;
        }

        if(!(currUser.updateItemsPerPage(itemsPerPage) && currUser.updateName(fullName) && currUser.updatePassword(newPassword))) {
            out.print("{\"error\":\"User could not be fully updated.\"}");
            out.flush();
            return;
        }

        userBean = new DBUser(userBean.getId());

        req.getSession().removeAttribute(ATTRIBUTES.USER_BEAN);
        req.getSession().setAttribute(ATTRIBUTES.USER_BEAN, userBean);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("username", currUser.getUserName());
        jsonResponse.put("full_name", currUser.getName());
        jsonResponse.put("mail", currUser.getEmail());
        jsonResponse.put("utype", currUser.isAdmin() ? "Administrator" : "User");
        jsonResponse.put("itemspp", currUser.getItemsPerPage());

        resp.getWriter().print(jsonResponse.toString());
        resp.getWriter().flush();
    }
}
