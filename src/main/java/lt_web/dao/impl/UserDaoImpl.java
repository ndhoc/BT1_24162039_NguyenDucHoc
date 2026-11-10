package lt_web.dao.impl;

import java.sql.*;
import lt_web.dao.*;
import lt_web.model.User;
import lt_web.utils.DBConnection;

public class UserDaoImpl implements UserDao {
    private final DBConnection database = new DBConnection();

    private User map(ResultSet rs) throws SQLException {
        User user = new User(rs.getString("username"), rs.getString("fullname"));
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setAvatar(rs.getString("avatar"));
        user.setRoleid(rs.getInt("roleid"));
        user.setPhone(rs.getString("phone"));
        user.setCreatedDate(rs.getDate("createddate"));
        return user;
    }

    private User find(String sql, String value) {
        try (Connection conn = database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    @Override public User get(String username) {
        return find("SELECT * FROM [User] WHERE username = ?", username);
    }

    @Override public void insert(User user) {
        String sql = "INSERT INTO [User](email,username,fullname,password,avatar,roleid,phone,createddate) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail()); ps.setString(2, user.getUsername());
            ps.setString(3, user.getFullName()); ps.setString(4, user.getPassword());
            ps.setString(5, user.getAvatar()); ps.setInt(6, user.getRoleid());
            ps.setString(7, user.getPhone()); ps.setDate(8, user.getCreatedDate());
            ps.executeUpdate();
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    private boolean exists(String sql, String value) {
        try (Connection conn = database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
    @Override public boolean checkExistUsername(String value) { return exists("SELECT 1 FROM [User] WHERE username=?", value); }
    @Override public boolean checkExistEmail(String value) { return exists("SELECT 1 FROM [User] WHERE email=?", value); }
    @Override public boolean checkExistPhone(String value) { return exists("SELECT 1 FROM [User] WHERE phone=?", value); }

    @Override public void saveRememberToken(int id, String hash, Timestamp expires) {
        try (Connection conn = database.getConnection(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO RememberToken(token_hash,user_id,expires_at) VALUES(?,?,?)")) {
            ps.setString(1, hash); ps.setInt(2, id); ps.setTimestamp(3, expires); ps.executeUpdate();
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
    @Override public User findByRememberToken(String hash) {
        return find("SELECT u.* FROM [User] u JOIN RememberToken t ON t.user_id=u.id WHERE t.token_hash=? AND t.expires_at>SYSUTCDATETIME()", hash);
    }
    @Override public void deleteRememberToken(String hash) {
        try (Connection conn = database.getConnection(); PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM RememberToken WHERE token_hash=?")) {
            ps.setString(1, hash); ps.executeUpdate();
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
}
