package lt_web.controllers;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import lt_web.model.Category;
import lt_web.service.CategoryService;
import lt_web.service.impl.CategoryServiceImpl;
import lt_web.utils.ImageStorage;

@WebServlet({"/admin/category/list", "/admin/category/add", "/admin/category/edit", "/admin/category/delete"})
@MultipartConfig(fileSizeThreshold = 1048576, maxFileSize = 5242880, maxRequestSize = 6291456)
public class CategoryServlet extends HttpServlet {
    private final CategoryService categories = new CategoryServiceImpl();
    private String action(HttpServletRequest req) { return req.getServletPath().substring(req.getServletPath().lastIndexOf('/') + 1); }
    private int id(HttpServletRequest req) {
        try { int id = Integer.parseInt(req.getParameter("id")); if (id > 0) return id; }
        catch (NumberFormatException ignored) { }
        throw new IllegalArgumentException("Mã danh mục không hợp lệ.");
    }
    private Category existing(HttpServletRequest req) {
        Category category = categories.get(id(req));
        if (category == null) throw new IllegalArgumentException("Danh mục không tồn tại.");
        return category;
    }
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = action(req);
            if (action.equals("list")) {
                req.setAttribute("cateList", categories.search(req.getParameter("q")));
                req.getRequestDispatcher("/views/admin/list-category.jsp").forward(req, resp);
            } else {
                if (!action.equals("add")) req.setAttribute("category", existing(req));
                req.setAttribute("editing", action.equals("edit"));
                req.getRequestDispatcher(action.equals("delete") ? "/views/admin/delete-category.jsp" : "/views/admin/category-form.jsp").forward(req, resp);
            }
        } catch (IllegalArgumentException e) { resp.sendError(404, e.getMessage()); }
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = action(req);
        if (action.equals("list")) { resp.sendError(405); return; }
        Category category = new Category();
        String newIcon = null;
        try {
            if (action.equals("delete")) {
                category = existing(req);
                categories.delete(category.getId());
                cleanup(category.getIcon());
            } else {
                String oldIcon = null;
                if (action.equals("edit")) { category = existing(req); oldIcon = category.getIcon(); }
                category.setName(req.getParameter("catename"));
                if (category.getName() == null || category.getName().isBlank() || category.getName().trim().length() > 255)
                    throw new IllegalArgumentException("Tên danh mục phải có 1–255 ký tự.");
                newIcon = ImageStorage.save(req.getPart("icon"));
                if (newIcon != null) category.setIcon(newIcon);
                if (action.equals("edit")) categories.edit(category); else categories.insert(category);
                if (newIcon != null) cleanup(oldIcon);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/category/list?saved=" + action);
        } catch (IllegalArgumentException e) {
            cleanup(newIcon);
            req.setAttribute("alert", e.getMessage()); req.setAttribute("category", category);
            req.setAttribute("editing", action.equals("edit"));
            req.getRequestDispatcher(action.equals("delete") ? "/views/admin/delete-category.jsp" : "/views/admin/category-form.jsp").forward(req, resp);
        } catch (RuntimeException | IOException | ServletException e) { cleanup(newIcon); throw e; }
    }
    private void cleanup(String icon) {
        try { ImageStorage.delete(icon); }
        catch (IOException e) { getServletContext().log("Could not remove old category image", e); }
    }
}
