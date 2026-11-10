# Bài tập 01 – Servlet, JSP/JSTL, JDBC, MVC ba tầng

**Sinh viên:** Nguyễn Đức Học – **MSSV:** 24162039.

## Chức năng

- Login với Session: `/login/session`; phiên hết hạn sau 30 phút không hoạt động.
- Login với Cookie: `/login/cookie`, hoặc chọn ghi nhớ trên form Login. Cookie sống 30 phút, tự phục hồi Session khi quay lại. Cookie chứa token ngẫu nhiên, HttpOnly, SameSite=Lax; SQL chỉ lưu SHA-256 của token. Không lưu mật khẩu/tên người dùng trong Cookie.
- Register: `/register`; kiểm tra dữ liệu, xác nhận mật khẩu và trùng username/email/phone; thành viên mới luôn có roleid=5. Mật khẩu lưu bằng PBKDF2-HMAC-SHA256 có salt.
- Điều hướng `/waiting`: roleid 1 → `/admin/home`, 2 → `/manager/home`, 5 → `/home`.
- Logout: hủy Session, thu hồi token và xóa Cookie.
- Category: danh sách, tìm kiếm, thêm/sửa/xóa, tải và hiển thị ảnh PNG/JPEG; sửa không chọn ảnh sẽ giữ ảnh cũ; thay ảnh/xóa danh mục sẽ dọn ảnh cũ. Xóa có trang xác nhận và POST.
- Filter xác thực/phân quyền, CSRF cho POST, UTF-8, JSP xuất dữ liệu bằng `c:out`, JDBC dùng PreparedStatement và try-with-resources.

## Môi trường

JDK **21**, Maven **3.9+**, Tomcat **11**, SQL Server **2019+** bật TCP/IP cổng 1433. Project dùng Jakarta Servlet 6.1, JSP 4.0, JSTL 3.0; không dùng Tomcat 9 (`javax.servlet`).

## Tạo cơ sở dữ liệu

Chạy `database/schema.sql`, sau đó `database/seed.sql` trong SSMS, DBeaver hoặc sqlcmd. Script tạo database riêng `MVC_24162039`, không sửa database `Online` của project cũ. Có thể chạy lại script mà không xóa dữ liệu.

```powershell
sqlcmd -S tcp:localhost,1433 -U sa -C -b -f 65001 -i database/schema.sql
sqlcmd -S tcp:localhost,1433 -U sa -C -b -f 65001 -i database/seed.sql
```

`sqlcmd` sẽ yêu cầu nhập mật khẩu SQL Server. Trước khi build, sao chép `src/main/resources/database.properties.example` thành `src/main/resources/database.properties`, sau đó điền tài khoản và mật khẩu SQL Server của máy bạn. File chứa thông tin local được Git bỏ qua. Có thể dùng biến môi trường **DB_URL**, **DB_USER**, **DB_PASSWORD** trước khi khởi động Tomcat để ghi đè cấu hình. `trustServerCertificate=true` dùng cho SQL Server local với chứng chỉ tự ký; khi triển khai thực tế cần chứng chỉ được tin cậy.

## Build và chạy

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21.0.12.1'
mvn clean package
```

Copy `target/24162039_NguyenDucHoc.war` vào `TOMCAT_HOME/webapps`, rồi khởi động Tomcat. Với cổng mặc định 8080, mở **http://localhost:8080/24162039_NguyenDucHoc/**.

Ảnh mặc định lưu tại `CATALINA_BASE/uploads/category`, ngoài WAR; có thể đổi bằng **UPLOAD_DIR** hoặc JVM property **app.upload.dir**.

### Import vào Spring Tools/Eclipse

1. File → Import → Maven → Existing Maven Projects → chọn thư mục chứa `pom.xml`.
2. Chọn JDK 21, thêm Apache Tomcat 11 trong Servers.
3. Maven → Update Project; Run As → Run on Server.
4. Mở `/24162039_NguyenDucHoc/`; cấu hình SQL Server như trên.

## Tài khoản mẫu

| Tài khoản | Mật khẩu | Vai trò |
| --- | --- | --- |
| admin | 123456 | Quản trị viên, được CRUD Category |
| manager | 123456 | Quản lý |
| user | 123456 | Thành viên |

## Cấu trúc MVC ba tầng

```text
src/main/java/lt_web/
  controllers/       Servlet nhận request và chọn JSP
  filters/           UTF-8, xác thực, phân quyền và CSRF
  model/             User, Category
  service/           Interface nghiệp vụ
  service/impl/      Validation và xử lý nghiệp vụ
  dao/               Interface truy cập dữ liệu
  dao/impl/          Câu lệnh JDBC SQL Server
  utils/             Kết nối DB, Cookie, hash mật khẩu, lưu ảnh
src/main/resources/database.properties.example
src/main/webapp/views/     JSP/JSTL
database/                  Schema và dữ liệu mẫu
```

## Kiểm tra thủ công

1. Dùng `admin / 123456` để đăng nhập bằng Session và Cookie.
2. Đăng ký tài khoản mới, kiểm tra báo lỗi trùng thông tin và mật khẩu xác nhận.
3. Đăng nhập bằng tài khoản mới; thành viên không có quyền truy cập trang quản trị.
4. Dùng admin để thêm, tìm kiếm, sửa và xóa danh mục, thử tải/thay ảnh.
5. Đăng xuất để kết thúc Session và xóa Cookie ghi nhớ.
