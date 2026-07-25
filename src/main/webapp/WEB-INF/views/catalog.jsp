<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="PreethiMart — Discover Authentic Products" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container">
    <!-- Hero Banner -->
    <div class="hero">
        <h1>Welcome to PreethiMart</h1>
        <p>Your one-stop destination for genuine Electronics, Fashion, Farm-Fresh Groceries, and Home essentials.</p>
    </div>

    <!-- Category Filter Chips -->
    <div class="category-bar">
        <a href="${pageContext.request.contextPath}/products"
           class="category-chip ${empty selectedCategory or selectedCategory == 'ALL' ? 'active' : ''}">
            <i class="fa-solid fa-border-all"></i> All Categories
        </a>
        <c:forEach var="cat" items="${categories}">
            <a href="${pageContext.request.contextPath}/products?category=<c:out value='${cat}'/>"
               class="category-chip ${selectedCategory == cat ? 'active' : ''}">
                <c:out value="${cat}"/>
            </a>
        </c:forEach>
    </div>

    <!-- Results Header -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <h2 style="font-size: 20px; font-weight: 700;">
            <c:choose>
                <c:when test="${not empty searchQuery}">
                    Search results for "<c:out value="${searchQuery}"/>"
                </c:when>
                <c:when test="${not empty selectedCategory and selectedCategory != 'ALL'}">
                    <c:out value="${selectedCategory}"/> Catalog
                </c:when>
                <c:otherwise>
                    Featured Products
                </c:otherwise>
            </c:choose>
            <span style="font-size: 14px; font-weight: normal; color: var(--text-muted);">
                (<c:out value="${totalProducts}"/> items found)
            </span>
        </h2>
    </div>

    <!-- Products Grid -->
    <c:choose>
        <c:when test="${empty products}">
            <div class="card" style="text-align: center; padding: 60px 20px;">
                <i class="fa-solid fa-box-open" style="font-size: 48px; color: var(--text-muted); margin-bottom: 16px;"></i>
                <h3 style="font-size: 18px; margin-bottom: 8px;">No products found</h3>
                <p style="color: var(--text-muted); margin-bottom: 20px;">We couldn't find any products matching your criteria.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Clear Filters</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="product-grid">
                <c:forEach var="p" items="${products}">
                    <div class="product-card">
                        <a href="${pageContext.request.contextPath}/products/detail?id=${p.id}">
                            <img src="<c:out value='${p.imageUrl}'/>" alt="<c:out value='${p.name}'/>" class="product-image"
                                 onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500';">
                        </a>
                        <div class="product-body">
                            <span class="product-category"><c:out value="${p.category}"/></span>
                            <a href="${pageContext.request.contextPath}/products/detail?id=${p.id}" class="product-title">
                                <c:out value="${p.name}"/>
                            </a>

                            <div class="product-rating">
                                <c:forEach begin="1" end="5" var="star">
                                    <i class="fa-solid fa-star${p.averageRating >= star ? '' : (p.averageRating >= star - 0.5 ? '-half-stroke' : ' text-muted')}"></i>
                                </c:forEach>
                                <span style="color: var(--text-muted); font-size: 12px; margin-left: 4px;">
                                    (<fmt:formatNumber value="${p.averageRating}" maxFractionDigits="1"/> / <c:out value="${p.reviewCount}"/>)
                                </span>
                            </div>

                            <div style="font-size: 12px; color: var(--text-muted); margin-bottom: 8px;">
                                Sold by: <strong><c:out value="${p.sellerName != null ? p.sellerName : 'Preethi Verified'}"/></strong>
                            </div>

                            <div class="product-price-row">
                                <div>
                                    <span class="product-price">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></span>
                                </div>

                                <c:choose>
                                    <c:when test="${p.stockQuantity > 0}">
                                        <form action="${pageContext.request.contextPath}/cart/add" method="POST" style="margin: 0;">
                                            <input type="hidden" name="productId" value="${p.id}">
                                            <input type="hidden" name="quantity" value="1">
                                            <input type="hidden" name="redirect" value="${pageContext.request.requestURI}?${pageContext.request.queryString}">
                                            <button type="submit" class="btn btn-primary btn-sm">
                                                <i class="fa-solid fa-cart-plus"></i> Add
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-pill status-cancelled">Out of Stock</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <div style="display: flex; justify-content: center; gap: 8px; margin-bottom: 40px;">
                    <c:forEach begin="1" end="${totalPages}" var="pg">
                        <a href="${pageContext.request.contextPath}/products?page=${pg}&category=<c:out value='${selectedCategory}'/>&q=<c:out value='${searchQuery}'/>"
                           class="btn ${pg == currentPage ? 'btn-primary' : 'btn-secondary'} btn-sm">
                            <c:out value="${pg}"/>
                        </a>
                    </c:forEach>
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
