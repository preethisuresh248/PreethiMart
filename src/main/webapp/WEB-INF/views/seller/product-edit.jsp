<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Edit Product — PreethiMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="max-width: 650px; margin-top: 30px; margin-bottom: 60px;">
    <div class="card">
        <h2 style="font-size: 22px; font-weight: 800; margin-bottom: 18px; border-bottom: 1px solid var(--border); padding-bottom: 12px;">
            <i class="fa-solid fa-pen-to-square"></i> Edit Product Listing
        </h2>

        <form action="${pageContext.request.contextPath}/seller/product/update" method="POST">
            <input type="hidden" name="id" value="${product.id}">

            <div class="form-group">
                <label>Product Name</label>
                <input type="text" name="name" class="form-control" value="<c:out value='${product.name}'/>" required>
            </div>

            <div class="form-group">
                <label>Category</label>
                <select name="category" class="form-control" required>
                    <c:forEach var="c" items="${categories}">
                        <option value="${c}" ${c == product.category ? 'selected' : ''}><c:out value="${c}"/></option>
                    </c:forEach>
                </select>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                <div class="form-group">
                    <label>Price (₹ INR)</label>
                    <input type="number" step="0.01" name="price" class="form-control" value="${product.price}" required min="1">
                </div>
                <div class="form-group">
                    <label>Available Stock</label>
                    <input type="number" name="stockQuantity" class="form-control" value="${product.stockQuantity}" required min="0">
                </div>
            </div>

            <div class="form-group">
                <label>Image URL</label>
                <input type="url" name="imageUrl" class="form-control" value="<c:out value='${product.imageUrl}'/>">
            </div>

            <div class="form-group">
                <label>Status</label>
                <select name="status" class="form-control">
                    <option value="ACTIVE" ${product.status == 'ACTIVE' ? 'selected' : ''}>ACTIVE (Visible in Catalog)</option>
                    <option value="INACTIVE" ${product.status == 'INACTIVE' ? 'selected' : ''}>INACTIVE (Hidden)</option>
                </select>
            </div>

            <div class="form-group">
                <label>Description</label>
                <textarea name="description" rows="4" class="form-control" required><c:out value="${product.description}"/></textarea>
            </div>

            <div style="display: flex; justify-content: flex-end; gap: 12px; margin-top: 24px;">
                <a href="${pageContext.request.contextPath}/seller/dashboard" class="btn btn-secondary">Cancel</a>
                <button type="submit" class="btn btn-primary">Save Changes</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
