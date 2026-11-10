<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Trang chủ | 24162039</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20261110-2"></head><body><main class="card">
<p class="eyebrow">Lập trình Web · Bài tập 01</p><h1>Servlet · JDBC · MVC</h1><p>Nguyễn Đức Học — MSSV: 24162039</p>
<c:choose><c:when test="${not empty sessionScope.account}">
<h2>Xin chào, <c:out value="${sessionScope.account.fullName}"/>!</h2><p>Bạn đã đăng nhập thành công bằng <strong><c:out value="${sessionScope.authMode}"/></strong>.</p>
<dl><dt>Tài khoản</dt><dd><c:out value="${sessionScope.account.username}"/></dd><dt>Email</dt><dd><c:out value="${sessionScope.account.email}"/></dd><dt>Vai trò</dt><dd><c:choose><c:when test="${sessionScope.account.roleid == 1}">Quản trị viên</c:when><c:when test="${sessionScope.account.roleid == 2}">Quản lý</c:when><c:otherwise>Thành viên</c:otherwise></c:choose></dd></dl>
<c:if test="${sessionScope.account.roleid == 1}"><p><a class="button" href="${pageContext.request.contextPath}/admin/category/list">Quản lý danh mục</a></p></c:if>
<form action="${pageContext.request.contextPath}/logout" method="post" class="inline"><input type="hidden" name="csrf" value="${sessionScope.csrf}"><button class="secondary">Đăng xuất</button></form>
</c:when><c:otherwise><p>Ứng dụng đăng nhập bằng Cookie, Session, đăng ký tài khoản và quản lý danh mục theo kiến trúc ba tầng.</p><nav><a class="button" href="${pageContext.request.contextPath}/login/session">Login với Session</a><a class="button secondary" href="${pageContext.request.contextPath}/login/cookie">Login với Cookie</a><a href="${pageContext.request.contextPath}/register">Đăng ký</a></nav></c:otherwise></c:choose>
</main></body></html>
