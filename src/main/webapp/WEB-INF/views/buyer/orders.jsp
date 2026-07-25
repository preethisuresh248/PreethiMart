<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="My Orders — PreethiMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="margin-top: 30px; margin-bottom: 60px;">
    <h1 style="font-size: 26px; font-weight: 800; margin-bottom: 24px;">
        <i class="fa-solid fa-receipt"></i> Order History
    </h1>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="card" style="text-align: center; padding: 60px 20px;">
                <i class="fa-solid fa-bag-shopping" style="font-size: 50px; color: var(--text-muted); margin-bottom: 16px;"></i>
                <h3 style="font-size: 18px; margin-bottom: 8px;">No orders found</h3>
                <p style="color: var(--text-muted); margin-bottom: 20px;">You haven't placed any orders yet.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Start Shopping</a>
            </div>
        </c:when>
        <c:otherwise>
            <div style="display: flex; flex-direction: column; gap: 20px;">
                <c:forEach var="o" items="${orders}">
                    <div class="card" style="margin-bottom: 0;">
                        <!-- Header -->
                        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border); padding-bottom: 12px; margin-bottom: 14px; flex-wrap: wrap; gap: 10px;">
                            <div>
                                <span style="font-weight: 700; font-size: 16px;">Order #PM-<c:out value="${o.id}"/></span>
                                <span style="font-size: 12px; color: var(--text-muted); margin-left: 8px;">
                                    Placed on <fmt:formatDate value="${o.createdAt}" pattern="dd MMM yyyy, hh:mm a"/>
                                </span>
                            </div>

                            <div style="display: flex; align-items: center; gap: 12px;">
                                <span class="status-pill status-${o.status.toLowerCase()}">
                                    <c:out value="${o.status}"/>
                                </span>
                                <span style="font-size: 16px; font-weight: 800; color: var(--primary);">
                                    ₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/>
                                </span>
                            </div>
                        </div>

                        <!-- Items List -->
                        <div style="display: flex; flex-direction: column; gap: 12px; margin-bottom: 14px;">
                            <c:forEach var="item" items="${o.items}">
                                <div style="display: flex; justify-content: space-between; align-items: center; font-size: 14px;">
                                    <div style="display: flex; align-items: center; gap: 12px;">
                                        <img src="<c:out value='${item.productImageUrl}'/>" alt="<c:out value='${item.productName}'/>"
                                             style="width: 45px; height: 45px; object-fit: cover; border-radius: 6px;"
                                             onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500';">
                                        <div>
                                            <a href="${pageContext.request.contextPath}/products/detail?id=${item.productId}" style="font-weight: 600;">
                                                <c:out value="${item.productName}"/>
                                            </a>
                                            <div style="font-size: 12px; color: var(--text-muted);">
                                                Qty: <c:out value="${item.quantity}"/> &times; ₹<fmt:formatNumber value="${item.priceAtPurchase}" pattern="#,##0.00"/>
                                                &bull; Sold by: <c:out value="${item.sellerName}"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div style="display: flex; align-items: center; gap: 16px;">
                                        <span style="font-weight: 700;">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></span>
                                        <a href="${pageContext.request.contextPath}/products/detail?id=${item.productId}" class="btn btn-secondary btn-sm">
                                            <i class="fa-solid fa-star"></i> Review
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Delivery Address & Actions -->
                        <div style="background: #f8fafc; padding: 10px 14px; border-radius: 6px; font-size: 12px; color: var(--text-muted); display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 8px;">
                            <div>
                                <i class="fa-solid fa-location-dot"></i> Delivery to: <c:out value="${o.shippingAddress}"/>
                            </div>
                            <div>
                                Payment: <strong><c:out value="${o.paymentMethod}"/></strong>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
