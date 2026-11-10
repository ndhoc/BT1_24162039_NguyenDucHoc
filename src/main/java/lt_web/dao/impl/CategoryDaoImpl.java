package lt_web.dao.impl;

import java.sql.*;
import java.util.*;
import lt_web.dao.*;
import lt_web.model.Category;
import lt_web.utils.DBConnection;

public class CategoryDaoImpl implements CategoryDao {
    private final DBConnection database = new DBConnection();
    private Category map(ResultSet rs) throws SQLException {
        return new Category(rs.getInt("cate_id"), rs.getString("cate_name"), rs.getString("icons"));
    }
    private List<Category> query(String sql, Object value) {
        try (Connection conn = database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (value != null) ps.setObject(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                List<Category> result = new ArrayList<>();
                while (rs.next()) result.add(map(rs));
                return result;
            }
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
    @Override public List<Category> getAll() { return query("SELECT * FROM Category ORDER BY cate_id", null); }
    @Override public Category get(int id) {
        List<Category> values = query("SELECT * FROM Category WHERE cate_id=?", id);
        return values.isEmpty() ? null : values.get(0);
    }
    @Override public Category get(String name) {
        List<Category> values = query("SELECT * FROM Category WHERE cate_name=?", name);
        return values.isEmpty() ? null : values.get(0);
    }
    @Override public List<Category> search(String keyword) {
        // Escape SQL LIKE wildcards so the search term is treated as literal text.
        String escaped = keyword.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![");
        return query("SELECT * FROM Category WHERE cate_name LIKE ? ESCAPE '!' ORDER BY cate_id", "%" + escaped + "%");
    }
    @Override public void insert(Category category) {
        try (Connection conn = database.getConnection(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Category(cate_name,icons) VALUES(?,?)")) {
            ps.setString(1, category.getName()); ps.setString(2, category.getIcon()); ps.executeUpdate();
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
    @Override public void edit(Category category) {
        try (Connection conn = database.getConnection(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE Category SET cate_name=?,icons=? WHERE cate_id=?")) {
            ps.setString(1, category.getName()); ps.setString(2, category.getIcon()); ps.setInt(3, category.getId());
            if (ps.executeUpdate() == 0) throw new IllegalArgumentException("Danh mục không còn tồn tại.");
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
    @Override public void delete(int id) {
        try (Connection conn = database.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM Category WHERE cate_id=?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) throw new IllegalArgumentException("Danh mục không còn tồn tại.");
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
}
