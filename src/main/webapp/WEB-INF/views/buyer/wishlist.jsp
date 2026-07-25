<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="My Wishlist — PreethiMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="margin-top: 30px; margin-bottom: 60px;">
    <h1 style="font-size: 26px; font-weight: 800; margin-bottom: 24px;">
        <i class="fa-solid fa-heart" style="color: var(--danger);"></i> Saved to Wishlist
    </h1>

    <c:choose>
        <c:when test="${empty wishlist}">
            <div class="card" style="text-align: center; padding: 60px 20px;">
                <i class="fa-regular fa-heart" style="font-size: 48px; color: var(--text-muted); margin-bottom: 16px;"></i>
                <h3 style="font-size: 18px; margin-bottom: 8px;">Your Wishlist is Empty</h3>
                <p style="color: var(--text-muted); margin-bottom: 20px;">Save products you love and review them later!</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Browse Products</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="product-grid">
                <c:forEach var="item" items="${wishlist}">
                    <div class="product-card">
                        <a href="${pageContext.request.contextPath}/products/detail?id=${item.productId}">
                            <img src="<c:out value='${item.productImageUrl}'/>" alt="<c:out value='${item.productName}'/>"
                                 class="product-image" onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500';">
                        </a>
                        <div class="product-body">
                            <span class="product-category"><c:out value="${item.category}"/></span>
                            <a href="${pageContext.request.contextPath}/products/detail?id=${item.productId}" class="product-title">
                                <c:out value="${item.productName}"/>
                            </a>

                            <div class="product-price-row">
                                <span class="product-price">₹<fmt:formatNumber value="${item.productPrice}" pattern="#,##0.00"/></span>

                                <div style="display: flex; gap: 6px;">
                                    <form action="${pageContext.request.contextPath}/cart/add" method="POST" style="margin:0;">
                                        <input type="hidden" name="productId" value="${item.productId}">
                                        <button type="submit" class="btn btn-primary btn-sm"><i class="fa-solid fa-cart-plus"></i> Move to Cart</button>
                                    </form>

                                    <form action="${pageContext.request.contextPath}/wishlist/toggle" method="POST" style="margin:0;">
                                        <input type="hidden" name="productId" value="${item.productId}">
                                        <button type="submit" class="btn btn-secondary btn-sm" style="color: var(--danger);"><i class="fa-solid fa-trash"></i></button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
