package lt_web.service;

import lt_web.model.User;

public interface UserService {
    User login(String username, String password);
    User get(String username);
    void register(String username, String password, String confirm, String email, String fullname, String phone);
    boolean checkExistUsername(String username);
    boolean checkExistEmail(String email);
    boolean checkExistPhone(String phone);
}
