<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Danh mục | 24162039</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20261110-2"></head><body><main class="card auth">
<p class="eyebrow">Quản lý danh mục</p><h1>${editing ? 'Sửa danh mục' : 'Thêm danh mục'}</h1>
<c:if test="${not empty alert}"><p class="alert" role="alert"><c:out value="${alert}"/></p></c:if>
<form action="${pageContext.request.contextPath}/admin/category/${editing ? 'edit' : 'add'}" method="post" enctype="multipart/form-data"><input type="hidden" name="csrf" value="${sessionScope.csrf}"><input type="hidden" name="id" value="${category.id}">
<label for="catename">Tên danh mục</label><input id="catename" name="catename" maxlength="255" value="<c:out value='${category.name}'/>" required>
<c:if test="${not empty category.icon}"><c:url value="/image" var="imageUrl"><c:param name="fname" value="${category.icon}"/></c:url><img class="preview" src="${imageUrl}" alt="Ảnh hiện tại"></c:if>
<label for="icon">Ảnh đại diện</label><input id="icon" type="file" name="icon" accept="image/png,image/jpeg"><p class="muted">PNG/JPEG, tối đa 5 MB và 4096 × 4096 pixel. Để trống để giữ ảnh hiện tại.</p>
<button type="submit">${editing ? 'Lưu thay đổi' : 'Thêm danh mục'}</button><a href="${pageContext.request.contextPath}/admin/category/list">Hủy</a>
</form></main></body></html>
