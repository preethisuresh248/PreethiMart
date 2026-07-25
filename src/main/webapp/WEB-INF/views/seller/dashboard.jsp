<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Seller Dashboard — PreethiMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="margin-top: 30px; margin-bottom: 60px;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; flex-wrap: wrap; gap: 12px;">
        <div>
            <h1 style="font-size: 26px; font-weight: 800;"><i class="fa-solid fa-store"></i> Merchant Dashboard</h1>
            <p style="color: var(--text-muted); font-size: 14px;">Manage catalog listings, track inventory levels, and process orders.</p>
        </div>
        <div style="display: flex; gap: 10px;">
            <a href="${pageContext.request.contextPath}/seller/orders" class="btn btn-secondary">
                <i class="fa-solid fa-box-open"></i> View Incoming Orders
            </a>
            <button type="button" class="btn btn-primary" onclick="document.getElementById('newProductModal').style.display='block'">
                <i class="fa-solid fa-plus"></i> Add New Product
            </button>
        </div>
    </div>

    <!-- Listings Table (F2 & Week 3) -->
    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock Level</th>
                    <th>Status</th>
                    <th style="text-align: right;">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty products}">
                        <tr>
                            <td colspan="6" style="text-align: center; padding: 40px; color: var(--text-muted);">
                                You haven't added any product listings yet. Click <strong>Add New Product</strong> to begin.
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="p" items="${products}">
                            <tr>
                                <td style="display: flex; align-items: center; gap: 12px;">
                                    <img src="<c:out value='${p.imageUrl}'/>" alt="<c:out value='${p.name}'/>"
                                         style="width: 44px; height: 44px; object-fit: cover; border-radius: 6px;"
                                         onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500';">
                                    <div>
                                        <strong><c:out value="${p.name}"/></strong>
                                        <div style="font-size: 11px; color: var(--text-muted);">ID: #<c:out value="${p.id}"/></div>
                                    </div>
                                </td>
                                <td><span class="product-category"><c:out value="${p.category}"/></span></td>
                                <td style="font-weight: 700;">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.stockQuantity <= 5}">
                                            <span style="color: var(--danger); font-weight: 700;"><c:out value="${p.stockQuantity}"/> (Low Stock)</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: var(--success); font-weight: 600;"><c:out value="${p.stockQuantity}"/> in stock</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <span class="status-pill status-${p.status == 'ACTIVE' ? 'delivered' : 'cancelled'}">
                                        <c:out value="${p.status}"/>
                                    </span>
                                </td>
                                <td style="text-align: right;">
                                    <div style="display: inline-flex; gap: 8px;">
                                        <a href="${pageContext.request.contextPath}/seller/product/edit?id=${p.id}" class="btn btn-secondary btn-sm">
                                            <i class="fa-solid fa-pen-to-square"></i> Edit
                                        </a>
                                        <form action="${pageContext.request.contextPath}/seller/product/delete" method="POST"
                                              onsubmit="return confirm('Are you sure you want to delete this listing?');" style="margin: 0;">
                                            <input type="hidden" name="id" value="${p.id}">
                                            <button type="submit" class="btn btn-secondary btn-sm" style="color: var(--danger);">
                                                <i class="fa-solid fa-trash-can"></i>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <!-- Modal for Adding New Product -->
    <div id="newProductModal" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 999; overflow-y: auto;">
        <div style="max-width: 600px; margin: 60px auto; background: white; border-radius: 12px; padding: 24px;">
            <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border); padding-bottom: 12px; margin-bottom: 20px;">
                <h3 style="font-size: 18px; font-weight: 700;">Add New Product Listing</h3>
                <button type="button" onclick="document.getElementById('newProductModal').style.display='none'" style="background: none; border: none; font-size: 20px; cursor: pointer;">&times;</button>
            </div>

            <form action="${pageContext.request.contextPath}/seller/product/create" method="POST">
                <div class="form-group">
                    <label>Product Name</label>
                    <input type="text" name="name" class="form-control" placeholder="e.g. Wireless Ergonomic Mouse" required>
                </div>

                <div class="form-group">
                    <label>Category</label>
                    <select name="category" class="form-control" required>
                        <option value="Electronics">Electronics</option>
                        <option value="Fashion">Fashion</option>
                        <option value="Groceries">Groceries & Gourmet</option>
                        <option value="Home">Home & Living</option>
                    </select>
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                    <div class="form-group">
                        <label>Price (₹ INR)</label>
                        <input type="number" step="0.01" name="price" class="form-control" placeholder="999.00" required min="1">
                    </div>
                    <div class="form-group">
                        <label>Initial Stock Quantity</label>
                        <input type="number" name="stockQuantity" class="form-control" placeholder="50" required min="0">
                    </div>
                </div>

                <div class="form-group">
                    <label>Image URL</label>
                    <input type="url" name="imageUrl" class="form-control" placeholder="https://example.com/photo.jpg"
                           value="https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500">
                </div>

                <div class="form-group">
                    <label>Product Description</label>
                    <textarea name="description" rows="3" class="form-control" placeholder="Detailed product specifications..." required></textarea>
                </div>

                <div style="display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px;">
                    <button type="button" class="btn btn-secondary" onclick="document.getElementById('newProductModal').style.display='none'">Cancel</button>
                    <button type="submit" class="btn btn-primary">Publish Listing</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
