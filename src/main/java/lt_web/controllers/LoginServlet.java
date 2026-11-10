package lt_web.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lt_web.model.User;
import lt_web.service.AuthService;
import lt_web.utils.AuthCookies;

@WebServlet({"/login", "/login/session", "/login/cookie"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("account") != null) {
            response.sendRedirect(request.getContextPath() + "/waiting"); return;
        }
        request.setAttribute("cookieMode", request.getServletPath().equals("/login/cookie"));
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = authService.authenticate(username, password);
            if (user == null) throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không đúng.");
            boolean remember = request.getServletPath().equals("/login/cookie") || "on".equals(request.getParameter("remember"));
            if (remember) AuthCookies.remember(request, response, user);
            else AuthCookies.clear(request, response);
            AuthCookies.signIn(request, user, remember ? "Cookie" : "Session");
            response.sendRedirect(request.getContextPath() + "/waiting");
        } catch (IllegalArgumentException e) {
            request.setAttribute("alert", e.getMessage());
            request.setAttribute("cookieMode", request.getServletPath().equals("/login/cookie"));
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}
