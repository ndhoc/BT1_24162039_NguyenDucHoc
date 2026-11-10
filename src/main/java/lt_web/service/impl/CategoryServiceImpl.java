package lt_web.service.impl;

import java.util.List;
import lt_web.dao.CategoryDao;
import lt_web.dao.impl.CategoryDaoImpl;
import lt_web.model.Category;
import lt_web.service.CategoryService;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao dao = new CategoryDaoImpl();
    private void validate(Category category) {
        String name = category.getName() == null ? "" : category.getName().trim();
        if (name.isEmpty() || name.length() > 255) throw new IllegalArgumentException("Tên danh mục phải có 1–255 ký tự.");
        category.setName(name);
    }
    @Override public void insert(Category category) { validate(category); dao.insert(category); }
    @Override public void edit(Category category) { validate(category); dao.edit(category); }
    @Override public void delete(int id) { dao.delete(id); }
    @Override public Category get(int id) { return dao.get(id); }
    @Override public Category get(String name) { return dao.get(name); }
    @Override public List<Category> getAll() { return dao.getAll(); }
    @Override public List<Category> search(String keyword) { return keyword == null || keyword.isBlank() ? getAll() : dao.search(keyword.trim()); }
}
