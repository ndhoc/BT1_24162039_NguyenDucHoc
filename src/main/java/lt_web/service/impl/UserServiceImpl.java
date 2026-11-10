package lt_web.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import lt_web.dao.*;
import lt_web.dao.impl.UserDaoImpl;
import lt_web.model.User;
import lt_web.service.UserService;
import lt_web.utils.PasswordUtils;

public class UserServiceImpl implements UserService {
    private final UserDao dao = new UserDaoImpl();
    @Override public User get(String username) { return dao.get(username); }
    @Override public User login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isEmpty())
            throw new IllegalArgumentException("Tài khoản và mật khẩu không được để trống.");
        User user = dao.get(username.trim());
        return user != null && PasswordUtils.verify(password, user.getPassword()) ? user : null;
    }
    @Override public boolean checkExistUsername(String v) { return dao.checkExistUsername(v); }
    @Override public boolean checkExistEmail(String v) { return dao.checkExistEmail(v); }
    @Override public boolean checkExistPhone(String v) { return dao.checkExistPhone(v); }

    @Override public void register(String username, String password, String confirm, String email, String fullname, String phone) {
        username = username == null ? "" : username.trim();
        email = email == null ? "" : email.trim();
        fullname = fullname == null ? "" : fullname.trim();
        phone = phone == null ? "" : phone.trim();
        if (!username.matches("[A-Za-z0-9_]{3,50}"))
            throw new IllegalArgumentException("Tài khoản gồm 3–50 chữ cái, chữ số hoặc dấu gạch dưới.");
        if (password == null || password.length() < 6 || password.length() > 128)
            throw new IllegalArgumentException("Mật khẩu phải có 6–128 ký tự.");
        if (!password.equals(confirm)) throw new IllegalArgumentException("Mật khẩu xác nhận không khớp.");
        if (email.length() > 255 || !email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))
            throw new IllegalArgumentException("Email không hợp lệ.");
        if (fullname.isEmpty() || fullname.length() > 100) throw new IllegalArgumentException("Họ tên phải có 1–100 ký tự.");
        if (phone.length() > 15 || !phone.matches("\\+?[0-9]{9,15}")) throw new IllegalArgumentException("Số điện thoại phải có 9–15 chữ số (có thể bắt đầu bằng +).");
        if (checkExistUsername(username)) throw new IllegalArgumentException("Tài khoản đã tồn tại.");
        if (checkExistEmail(email)) throw new IllegalArgumentException("Email đã tồn tại.");
        if (checkExistPhone(phone)) throw new IllegalArgumentException("Số điện thoại đã tồn tại.");
        User user = new User(username, fullname);
        user.setEmail(email); user.setPhone(phone); user.setPassword(PasswordUtils.hash(password));
        user.setRoleid(5); user.setCreatedDate(Date.valueOf(LocalDate.now()));
        try { dao.insert(user); }
        catch (DataAccessException e) {
            if (e.getCause() instanceof java.sql.SQLException sql && (sql.getErrorCode() == 2601 || sql.getErrorCode() == 2627))
                throw new IllegalArgumentException("Tài khoản, email hoặc số điện thoại đã tồn tại.");
            throw e;
        }
    }
}
