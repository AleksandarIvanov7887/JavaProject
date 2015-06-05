package project.issue.tracker.services.creation;

import com.track.be.models.DBUser;
import com.track.utils.FORM_PARAMS;
import net.sf.json.JSONObject;
import org.apache.commons.validator.GenericValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CreateUser", urlPatterns = {"/createUser.do"})
public class CreateUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");

        String username = req.getParameter(FORM_PARAMS.CREATE_USER.USERNAME);
        String password = req.getParameter(FORM_PARAMS.CREATE_USER.PASSOWRD);
        String fullName = req.getParameter(FORM_PARAMS.CREATE_USER.FULL_NAME);
        String email = req.getParameter(FORM_PARAMS.CREATE_USER.EMAIL);
        String itemsPerPage = req.getParameter(FORM_PARAMS.CHANGE_USER.ITEMS_PER_PAGE);
        String userType = req.getParameter(FORM_PARAMS.CREATE_USER.USER_TYPE).equals("User") ? "0" : "1";

        PrintWriter out = resp.getWriter();

        if(!username.matches("^[a-zA-Z_0-9\\.\\-,:]{1,20}$") ||
                !password.matches("^[a-zA-Z_0-9\\.\\-,:]{1,20}$") ||
                !fullName.matches("^[a-zA-Z \\-]{1,50}$") ||
                !GenericValidator.isEmail(email) ||
                !(itemsPerPage.charAt(0) >= '1' && itemsPerPage.charAt(0) <= '6' && itemsPerPage.charAt(1) == '0' && itemsPerPage.length() == 2)
                ) {
            out.print("{\"error\":\"Bad Input\"}");
            out.flush();
            return;
        }

        JSONObject responseJSON = new JSONObject();
        for (DBUser user : DBUser.getAllUsers()) {
            if (user.getName().equalsIgnoreCase(fullName)) {
                responseJSON.put("error", "User Full name already exists");
                out.print(responseJSON.toString());
                out.flush();
                return;

            }
        }
        if (new DBUser(username, password).isExistInDB()) {
            responseJSON.put("error", "User/pass already exists");
            out.print(responseJSON.toString());
            out.flush();
            return;
        }
        if (new DBUser(username, password, fullName, userType, email, itemsPerPage, false).save()) {
            responseJSON.put("success", "User Created");
        } else {
            responseJSON.put("error", "User not Created");
        }

        out.print(responseJSON.toString());
        out.flush();
    }
}
