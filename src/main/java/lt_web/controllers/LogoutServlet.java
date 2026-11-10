package lt_web.controllers;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lt_web.utils.AuthCookies;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/logout.jsp").forward(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AuthCookies.clear(req, resp);
        if (req.getSession(false) != null) req.getSession(false).invalidate();
        resp.sendRedirect(req.getContextPath() + "/login?loggedout=1");
    }
}
