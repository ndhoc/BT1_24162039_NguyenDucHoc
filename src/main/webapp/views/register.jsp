<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Đăng ký | 24162039</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20261110-2"></head><body><main class="card auth">
<p class="eyebrow">24162039 · Nguyễn Đức Học</p><h1>Tạo tài khoản</h1><p>Nhập thông tin để đăng ký tài khoản thành viên.</p>
<c:if test="${not empty alert}"><p class="alert" role="alert"><c:out value="${alert}"/></p></c:if>
<form action="${pageContext.request.contextPath}/register" method="post"><input type="hidden" name="csrf" value="${sessionScope.csrf}">
<label for="fullname">Họ và tên</label><input id="fullname" name="fullname" maxlength="100" value="<c:out value='${param.fullname}'/>" autocomplete="name" required>
<label for="username">Tên đăng nhập</label><input id="username" name="username" pattern="[A-Za-z0-9_]{3,50}" title="3–50 chữ cái, chữ số hoặc dấu gạch dưới" value="<c:out value='${param.username}'/>" autocomplete="username" required>
<label for="email">Email</label><input id="email" type="email" name="email" maxlength="255" value="<c:out value='${param.email}'/>" autocomplete="email" required>
<label for="phone">Số điện thoại</label><input id="phone" name="phone" type="tel" maxlength="15" value="<c:out value='${param.phone}'/>" autocomplete="tel" required>
<label for="password">Mật khẩu</label><input id="password" type="password" name="password" minlength="6" maxlength="128" autocomplete="new-password" required>
<label for="confirm">Xác nhận mật khẩu</label><input id="confirm" type="password" name="confirm" minlength="6" maxlength="128" autocomplete="new-password" required>
<button type="submit">Đăng ký</button></form><p><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
</main></body></html>
