<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Đăng nhập | 24162039</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20261110-2"></head><body>
<main class="card auth"><p class="eyebrow">24162039 · Nguyễn Đức Học</p><h1>Đăng nhập</h1>
<p>Thông tin đăng nhập được lưu trong Session. Chọn “Ghi nhớ đăng nhập” để Cookie tự khôi phục phiên trong vòng 30 phút.</p>
<c:if test="${not empty alert}"><p class="alert" role="alert"><c:out value="${alert}"/></p></c:if>
<c:if test="${param.registered == '1'}"><p class="success">Đăng ký thành công. Bạn có thể đăng nhập.</p></c:if>
<c:if test="${param.loggedout == '1'}"><p class="success">Đã đăng xuất, hủy Session và xóa các Cookie đăng nhập.</p></c:if>
<form action="${pageContext.request.contextPath}/login" method="post">
<input type="hidden" name="csrf" value="${sessionScope.csrf}">
<label for="username">Tên đăng nhập</label><input id="username" name="username" value="<c:out value='${param.username}'/>" autocomplete="username" maxlength="50" required>
<label for="password">Mật khẩu</label><input id="password" type="password" name="password" autocomplete="current-password" required>
<label class="check"><input type="checkbox" name="remember"> Ghi nhớ đăng nhập bằng Cookie (30 phút)</label>
<button type="submit">Đăng nhập</button></form>
<p class="auth-footer"><span>Chưa có tài khoản?</span> <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
<p class="auth-footer"><a href="${pageContext.request.contextPath}/">Về trang chủ</a></p>
<p class="muted">Tài khoản thực hành: admin / 123456 · manager / 123456 · user / 123456</p>
</main></body></html>
