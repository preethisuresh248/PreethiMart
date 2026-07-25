<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="${product.name} — PreethiMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="margin-top: 24px; margin-bottom: 50px;">
    <!-- Breadcrumb -->
    <div style="font-size: 13px; color: var(--text-muted); margin-bottom: 20px;">
        <a href="${pageContext.request.contextPath}/products">Shop</a> &gt;
        <a href="${pageContext.request.contextPath}/products?category=<c:out value='${product.category}'/>"><c:out value="${product.category}"/></a> &gt;
        <span><c:out value="${product.name}"/></span>
    </div>

    <!-- Main Product Layout -->
    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 40px; margin-bottom: 40px;">
        <!-- Left: Image -->
        <div class="card" style="padding: 10px; display: flex; align-items: center; justify-content: center; min-height: 380px;">
            <img src="<c:out value='${product.imageUrl}'/>" alt="<c:out value='${product.name}'/>"
                 style="width: 100%; max-height: 420px; object-fit: contain; border-radius: 8px;"
                 onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500';">
        </div>

        <!-- Right: Details -->
        <div class="card" style="display: flex; flex-direction: column; justify-content: space-between;">
            <div>
                <span class="product-category"><c:out value="${product.category}"/></span>
                <h1 style="font-size: 26px; font-weight: 800; margin: 8px 0 12px;"><c:out value="${product.name}"/></h1>

                <!-- Rating summary -->
                <div class="product-rating" style="margin-bottom: 16px;">
                    <c:forEach begin="1" end="5" var="star">
                        <i class="fa-solid fa-star${product.averageRating >= star ? '' : (product.averageRating >= star - 0.5 ? '-half-stroke' : ' text-muted')}"></i>
                    </c:forEach>
                    <span style="color: var(--text-muted); font-size: 13px; margin-left: 6px;">
                        <fmt:formatNumber value="${product.averageRating}" maxFractionDigits="1"/> / 5.0 (<c:out value="${product.reviewCount}"/> ratings)
                    </span>
                </div>

                <div style="font-size: 32px; font-weight: 800; color: var(--primary); margin-bottom: 16px;">
                    ₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>
                    <span style="font-size: 13px; font-weight: normal; color: var(--text-muted);">(incl. all taxes)</span>
                </div>

                <div style="margin-bottom: 20px; font-size: 14px; line-height: 1.6; color: var(--text-main);">
                    <c:out value="${product.description}"/>
                </div>

                <div style="background: #f8fafc; padding: 12px 16px; border-radius: 8px; margin-bottom: 24px; font-size: 13px;">
                    <div><i class="fa-solid fa-store" style="color: var(--primary); margin-right: 6px;"></i> Sold by: <strong><c:out value="${product.sellerName}"/></strong></div>
                    <div style="margin-top: 6px;">
                        <i class="fa-solid fa-boxes-stacked" style="color: var(--primary); margin-right: 6px;"></i>
                        Availability:
                        <c:choose>
                            <c:when test="${product.stockQuantity > 0}">
                                <strong style="color: var(--success);"><c:out value="${product.stockQuantity}"/> units in stock</strong>
                            </c:when>
                            <c:otherwise>
                                <strong style="color: var(--danger);">Currently Out of Stock</strong>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- Action buttons -->
            <div style="display: flex; gap: 12px; flex-wrap: wrap;">
                <c:choose>
                    <c:when test="${product.stockQuantity > 0}">
                        <form action="${pageContext.request.contextPath}/cart/add" method="POST" style="display: flex; gap: 10px; flex: 1;">
                            <input type="hidden" name="productId" value="${product.id}">
                            <input type="number" name="quantity" value="1" min="1" max="${product.stockQuantity}"
                                   class="form-control" style="width: 80px; text-align: center;">
                            <button type="submit" class="btn btn-primary" style="flex: 1;">
                                <i class="fa-solid fa-cart-plus"></i> Add to Cart
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-secondary" disabled style="flex: 1;">Out of Stock</button>
                    </c:otherwise>
                </c:choose>

                <!-- Wishlist button -->
                <form action="${pageContext.request.contextPath}/wishlist/toggle" method="POST" style="margin: 0;">
                    <input type="hidden" name="productId" value="${product.id}">
                    <input type="hidden" name="redirect" value="${pageContext.request.requestURI}?id=${product.id}">
                    <button type="submit" class="btn ${inWishlist ? 'btn-danger' : 'btn-secondary'}" title="Save to wishlist">
                        <i class="fa-${inWishlist ? 'solid' : 'regular'} fa-heart"></i> ${inWishlist ? 'Saved' : 'Wishlist'}
                    </button>
                </form>
            </div>
        </div>
    </div>

    <!-- Customer Reviews Section (F8) -->
    <div class="card">
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border); padding-bottom: 16px; margin-bottom: 20px;">
            <h3 style="font-size: 20px; font-weight: 700;">
                <i class="fa-solid fa-star" style="color: var(--warning);"></i> Customer Reviews (<c:out value="${product.reviewCount}"/>)
            </h3>
        </div>

        <c:choose>
            <c:when test="${empty reviews}">
                <p style="color: var(--text-muted); font-size: 14px; margin-bottom: 24px;">No reviews yet for this product. Be the first verified buyer to share your feedback!</p>
            </c:when>
            <c:otherwise>
                <div style="display: flex; flex-direction: column; gap: 16px; margin-bottom: 30px;">
                    <c:forEach var="r" items="${reviews}">
                        <div style="padding: 14px; background: #f8fafc; border-radius: 8px; border: 1px solid var(--border);">
                            <div style="display: flex; justify-content: space-between; margin-bottom: 6px;">
                                <strong><c:out value="${r.buyerName}"/></strong>
                                <span style="font-size: 12px; color: var(--text-muted);">
                                    <fmt:formatDate value="${r.createdAt}" pattern="dd MMM yyyy"/>
                                </span>
                            </div>
                            <div class="product-rating" style="margin-bottom: 6px;">
                                <c:forEach begin="1" end="5" var="star">
                                    <i class="fa-solid fa-star${r.rating >= star ? '' : ' text-muted'}"></i>
                                </c:forEach>
                                <span class="badge" style="background: #10b981; font-size: 10px; margin-left: 6px;"><i class="fa-solid fa-check"></i> Verified Purchase</span>
                            </div>
                            <p style="font-size: 14px; color: var(--text-main);"><c:out value="${r.comment}"/></p>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Write a Review Form -->
        <c:if test="${not empty sessionScope.currentUser and sessionScope.currentUser.role == 'BUYER'}">
            <div style="border-top: 1px solid var(--border); padding-top: 20px;">
                <h4 style="font-size: 16px; font-weight: 700; margin-bottom: 12px;">Leave a Verified Review</h4>
                <form action="${pageContext.request.contextPath}/reviews/add" method="POST">
                    <input type="hidden" name="productId" value="${product.id}">
                    <div style="display: flex; gap: 16px; margin-bottom: 12px; align-items: center;">
                        <label style="font-size: 13px; font-weight: 600;">Rating:</label>
                        <select name="rating" class="form-control" style="width: 140px;">
                            <option value="5">⭐⭐⭐⭐⭐ (5/5)</option>
                            <option value="4">⭐⭐⭐⭐ (4/5)</option>
                            <option value="3">⭐⭐⭐ (3/5)</option>
                            <option value="2">⭐⭐ (2/5)</option>
                            <option value="1">⭐ (1/5)</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <textarea name="comment" rows="3" class="form-control" placeholder="Share your experience with this item..." required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary btn-sm">Submit Review</button>
                </form>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
