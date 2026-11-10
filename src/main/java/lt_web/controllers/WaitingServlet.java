package lt_web.controllers;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lt_web.model.User;

@WebServlet("/waiting")
public class WaitingServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("account");
        String target = user == null ? "/login" : user.getRoleid() == 1 ? "/admin/home" : user.getRoleid() == 2 ? "/manager/home" : "/home";
        resp.sendRedirect(req.getContextPath() + target);
    }
}
