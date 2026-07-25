<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Order Confirmed! — PreethiMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="max-width: 720px; margin-top: 40px; margin-bottom: 60px;">
    <div class="card" style="text-align: center; padding: 40px 30px;">
        <div style="width: 70px; height: 70px; border-radius: 50%; background: #d1fae5; color: #059669; display: flex; align-items: center; justify-content: center; font-size: 36px; margin: 0 auto 20px;">
            <i class="fa-solid fa-check"></i>
        </div>

        <h1 style="font-size: 28px; font-weight: 800; margin-bottom: 8px;">Order Confirmed!</h1>
        <p style="color: var(--text-muted); font-size: 15px; margin-bottom: 24px;">
            Thank you for shopping at PreethiMart! Your order has been placed and payment confirmed.
        </p>

        <div style="background: #f8fafc; border-radius: 8px; border: 1px solid var(--border); padding: 18px; text-align: left; margin-bottom: 24px; font-size: 14px;">
            <div style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                <span style="color: var(--text-muted);">Order Number:</span>
                <strong>#PM-<c:out value="${order.id}"/></strong>
            </div>
            <div style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                <span style="color: var(--text-muted);">Status:</span>
                <span class="status-pill status-${order.status == 'CONFIRMED' ? 'confirmed' : 'pending'}"><c:out value="${order.status}"/></span>
            </div>
            <div style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                <span style="color: var(--text-muted);">Payment Method:</span>
                <strong><c:out value="${order.paymentMethod}"/></strong>
            </div>
            <div style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                <span style="color: var(--text-muted);">Total Paid:</span>
                <strong style="color: var(--primary);">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></strong>
            </div>
            <div style="margin-top: 8px; border-top: 1px solid var(--border); padding-top: 8px;">
                <span style="color: var(--text-muted); display: block; margin-bottom: 4px;">Delivery Address:</span>
                <span><c:out value="${order.shippingAddress}"/></span>
            </div>
        </div>

        <div style="display: flex; gap: 12px; justify-content: center;">
            <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary">
                <i class="fa-solid fa-receipt"></i> View in Orders
            </a>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-secondary">
                <i class="fa-solid fa-store"></i> Continue Shopping
            </a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
