<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Incoming Orders — PreethiMart Seller" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="margin-top: 30px; margin-bottom: 60px;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
        <div>
            <h1 style="font-size: 26px; font-weight: 800;"><i class="fa-solid fa-box-open"></i> Incoming Customer Orders</h1>
            <p style="color: var(--text-muted); font-size: 14px;">Manage fulfillment and advance order status workflow (Pending &rarr; Confirmed &rarr; Shipped &rarr; Delivered).</p>
        </div>
        <a href="${pageContext.request.contextPath}/seller/dashboard" class="btn btn-secondary">
            <i class="fa-solid fa-arrow-left"></i> Back to Dashboard
        </a>
    </div>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="card" style="text-align: center; padding: 60px 20px;">
                <i class="fa-solid fa-inbox" style="font-size: 48px; color: var(--text-muted); margin-bottom: 16px;"></i>
                <h3 style="font-size: 18px; margin-bottom: 8px;">No incoming orders</h3>
                <p style="color: var(--text-muted);">When buyers order your products, they will appear here for fulfillment.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div style="display: flex; flex-direction: column; gap: 20px;">
                <c:forEach var="o" items="${orders}">
                    <div class="card" style="margin-bottom: 0;">
                        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border); padding-bottom: 12px; margin-bottom: 14px; flex-wrap: wrap; gap: 10px;">
                            <div>
                                <span style="font-weight: 700; font-size: 16px;">Order #PM-<c:out value="${o.id}"/></span>
                                <span style="font-size: 12px; color: var(--text-muted); margin-left: 8px;">
                                    Buyer: <strong><c:out value="${o.buyerName}"/></strong> (<c:out value="${o.buyerEmail}"/>)
                                </span>
                            </div>

                            <!-- O2 Workflow Status Updater -->
                            <form action="${pageContext.request.contextPath}/seller/orders/status" method="POST"
                                  style="display: flex; align-items: center; gap: 8px; margin: 0;">
                                <input type="hidden" name="orderId" value="${o.id}">
                                <label style="font-size: 12px; font-weight: 600; color: var(--text-muted);">Status:</label>
                                <select name="status" class="form-control" style="padding: 4px 8px; font-size: 12px; width: 140px;" onchange="this.form.submit()">
                                    <option value="PENDING" ${o.status == 'PENDING' ? 'selected' : ''}>⏳ Pending</option>
                                    <option value="CONFIRMED" ${o.status == 'CONFIRMED' ? 'selected' : ''}>✅ Confirmed</option>
                                    <option value="SHIPPED" ${o.status == 'SHIPPED' ? 'selected' : ''}>🚚 Shipped</option>
                                    <option value="DELIVERED" ${o.status == 'DELIVERED' ? 'selected' : ''}>🎉 Delivered</option>
                                    <option value="CANCELLED" ${o.status == 'CANCELLED' ? 'selected' : ''}>❌ Cancelled</option>
                                </select>
                            </form>
                        </div>

                        <!-- Seller Ordered Items -->
                        <div style="display: flex; flex-direction: column; gap: 10px; margin-bottom: 14px;">
                            <c:forEach var="item" items="${o.items}">
                                <div style="display: flex; justify-content: space-between; align-items: center; font-size: 13px;">
                                    <div style="display: flex; align-items: center; gap: 10px;">
                                        <img src="<c:out value='${item.productImageUrl}'/>" alt="<c:out value='${item.productName}'/>"
                                             style="width: 40px; height: 40px; object-fit: cover; border-radius: 4px;"
                                             onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500';">
                                        <div>
                                            <strong><c:out value="${item.productName}"/></strong>
                                            <div style="color: var(--text-muted);">Qty: <c:out value="${item.quantity}"/> &times; ₹<fmt:formatNumber value="${item.priceAtPurchase}" pattern="#,##0.00"/></div>
                                        </div>
                                    </div>
                                    <div style="font-weight: 700;">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></div>
                                </div>
                            </c:forEach>
                        </div>

                        <div style="background: #f8fafc; padding: 10px 14px; border-radius: 6px; font-size: 12px; color: var(--text-muted);">
                            <i class="fa-solid fa-location-dot"></i> Ship to: <strong><c:out value="${o.shippingAddress}"/></strong>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
