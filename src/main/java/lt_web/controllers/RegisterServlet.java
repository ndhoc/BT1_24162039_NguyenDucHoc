package lt_web.controllers;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lt_web.service.UserService;
import lt_web.service.impl.UserServiceImpl;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService users = new UserServiceImpl();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            users.register(req.getParameter("username"), req.getParameter("password"), req.getParameter("confirm"),
                    req.getParameter("email"), req.getParameter("fullname"), req.getParameter("phone"));
            resp.sendRedirect(req.getContextPath() + "/login?registered=1");
        } catch (IllegalArgumentException e) {
            req.setAttribute("alert", e.getMessage());
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
        }
    }
}
