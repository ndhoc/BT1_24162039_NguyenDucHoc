package lt_web.model;

public class User implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String email;
    private String password;
    private String avatar;
    private int roleid;
    private String phone;
    private java.sql.Date createdDate;

    private String username;
    private String fullName;

    public User() {
    }

    public User(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public int getRoleid() { return roleid; }
    public void setRoleid(int roleid) { this.roleid = roleid; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public java.sql.Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(java.sql.Date createdDate) { this.createdDate = createdDate; }
}
