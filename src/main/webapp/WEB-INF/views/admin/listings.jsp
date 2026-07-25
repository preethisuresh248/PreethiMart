<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Listing Moderation — PreethiMart Admin" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="margin-top: 30px; margin-bottom: 60px;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
        <div>
            <h1 style="font-size: 26px; font-weight: 800;"><i class="fa-solid fa-list-check"></i> Catalog Moderation</h1>
            <p style="color: var(--text-muted); font-size: 14px;">Review product listings and enforce marketplace catalog compliance.</p>
        </div>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">
            <i class="fa-solid fa-arrow-left"></i> Back to Console
        </a>
    </div>

    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Seller</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Status</th>
                    <th style="text-align: right;">Moderation Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td style="display: flex; align-items: center; gap: 12px;">
                            <img src="<c:out value='${p.imageUrl}'/>" alt="<c:out value='${p.name}'/>"
                                 style="width: 40px; height: 40px; object-fit: cover; border-radius: 4px;"
                                 onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500';">
                            <div>
                                <strong><c:out value="${p.name}"/></strong>
                                <div style="font-size: 11px; color: var(--text-muted);">ID: #<c:out value="${p.id}"/></div>
                            </div>
                        </td>
                        <td><c:out value="${p.sellerName}"/></td>
                        <td><span class="product-category"><c:out value="${p.category}"/></span></td>
                        <td>₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                        <td>
                            <span class="status-pill status-${p.status == 'ACTIVE' ? 'delivered' : 'cancelled'}">
                                <c:out value="${p.status}"/>
                            </span>
                        </td>
                        <td style="text-align: right;">
                            <form action="${pageContext.request.contextPath}/admin/listings/moderate" method="POST" style="margin: 0; display: inline;">
                                <input type="hidden" name="id" value="${p.id}">
                                <c:choose>
                                    <c:when test="${p.status == 'ACTIVE'}">
                                        <input type="hidden" name="status" value="MODERATED">
                                        <button type="submit" class="btn btn-secondary btn-sm" style="color: var(--danger);">
                                            <i class="fa-solid fa-ban"></i> Suspend / Hide
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" name="status" value="ACTIVE">
                                        <button type="submit" class="btn btn-secondary btn-sm" style="color: var(--success);">
                                            <i class="fa-solid fa-check"></i> Restore Listing
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
