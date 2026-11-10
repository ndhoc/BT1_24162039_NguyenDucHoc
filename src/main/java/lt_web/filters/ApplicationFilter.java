package lt_web.filters;

import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import lt_web.dao.DataAccessException;
import lt_web.model.User;
import lt_web.utils.AuthCookies;

@WebFilter("/*")
public class ApplicationFilter implements Filter {
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8"); resp.setCharacterEncoding("UTF-8");
        resp.setHeader("X-Content-Type-Options", "nosniff");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.startsWith("/css/")) { chain.doFilter(req, resp); return; }
        resp.setHeader("Cache-Control", "no-store");
        try {
            HttpSession session = req.getSession();
            User account = (User) session.getAttribute("account");
            if (account == null && !path.equals("/logout")) {
                account = AuthCookies.restore(req, resp);
                if (account != null) AuthCookies.signIn(req, account, "Cookie");
            }
            session = req.getSession();
            if (session.getAttribute("csrf") == null) session.setAttribute("csrf", UUID.randomUUID().toString());
            if (path.startsWith("/views/")) { resp.sendError(404); return; }
            if (path.startsWith("/admin/") && account == null) {
                resp.sendRedirect(req.getContextPath() + "/login"); return;
            }
            if (path.startsWith("/admin/") && account.getRoleid() != 1) { resp.sendError(403); return; }
            if (path.startsWith("/manager/") && (account == null || account.getRoleid() != 2)) {
                resp.sendError(403); return;
            }
            if ("POST".equals(req.getMethod()) && !session.getAttribute("csrf").equals(req.getParameter("csrf"))) {
                resp.sendError(403, "Phiên làm việc không hợp lệ. Vui lòng tải lại biểu mẫu."); return;
            }
            chain.doFilter(req, resp);
        } catch (DataAccessException e) {
            req.getServletContext().log("JDBC operation failed", e);
            resp.setStatus(503);
            req.setAttribute("errorMessage", "Không thể kết nối cơ sở dữ liệu. Vui lòng kiểm tra SQL Server và cấu hình database.properties.");
            req.getRequestDispatcher("/views/error.jsp").forward(req, resp);
        }
    }
}
