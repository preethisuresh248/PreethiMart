<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Your Shopping Cart — PreethiMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="margin-top: 30px; margin-bottom: 60px;">
    <h1 style="font-size: 26px; font-weight: 800; margin-bottom: 24px;">
        <i class="fa-solid fa-cart-shopping"></i> Shopping Cart
        <span style="font-size: 16px; font-weight: normal; color: var(--text-muted);">(<c:out value="${cart.totalItems}"/> items)</span>
    </h1>

    <c:choose>
        <c:when test="${empty cart.items}">
            <div class="card" style="text-align: center; padding: 60px 20px;">
                <i class="fa-solid fa-cart-arrow-down" style="font-size: 54px; color: var(--text-muted); margin-bottom: 16px;"></i>
                <h3 style="font-size: 20px; margin-bottom: 8px;">Your Cart is Empty</h3>
                <p style="color: var(--text-muted); margin-bottom: 24px;">Explore our catalog to find top products and essentials.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                    <i class="fa-solid fa-store"></i> Start Shopping
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 30px; align-items: start;">
                <!-- Cart Items Table -->
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Item</th>
                                <th>Unit Price</th>
                                <th style="text-align: center;">Quantity</th>
                                <th>Subtotal</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cart.items}">
                                <tr>
                                    <td style="display: flex; align-items: center; gap: 14px;">
                                        <img src="<c:out value='${item.productImageUrl}'/>" alt="<c:out value='${item.productName}'/>"
                                             style="width: 50px; height: 50px; object-fit: cover; border-radius: 6px;"
                                             onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500';">
                                        <div>
                                            <a href="${pageContext.request.contextPath}/products/detail?id=${item.productId}" style="font-weight: 600;">
                                                <c:out value="${item.productName}"/>
                                            </a>
                                            <div style="font-size: 11px; color: var(--text-muted);">
                                                Stock available: <c:out value="${item.availableStock}"/>
                                            </div>
                                        </div>
                                    </td>
                                    <td>₹<fmt:formatNumber value="${item.productPrice}" pattern="#,##0.00"/></td>
                                    <td style="text-align: center;">
                                        <form action="${pageContext.request.contextPath}/cart/update" method="POST" style="display: inline-flex; align-items: center; gap: 6px;">
                                            <input type="hidden" name="productId" value="${item.productId}">
                                            <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.availableStock}"
                                                   class="form-control" style="width: 65px; text-align: center; padding: 4px;"
                                                   onchange="this.form.submit()">
                                        </form>
                                    </td>
                                    <td style="font-weight: 700;">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart/remove" method="POST" style="margin: 0;">
                                            <input type="hidden" name="productId" value="${item.productId}">
                                            <button type="submit" class="btn btn-secondary btn-sm" style="color: var(--danger);" title="Remove item">
                                                <i class="fa-solid fa-trash-can"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Order Calculation Summary Card -->
                <div class="card">
                    <h3 style="font-size: 18px; font-weight: 700; margin-bottom: 16px; border-bottom: 1px solid var(--border); padding-bottom: 10px;">
                        Order Summary
                    </h3>

                    <div style="display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 14px;">
                        <span style="color: var(--text-muted);">Bag Subtotal:</span>
                        <span>₹<fmt:formatNumber value="${cart.subtotal}" pattern="#,##0.00"/></span>
                    </div>

                    <div style="display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 14px;">
                        <span style="color: var(--text-muted);">Estimated Tax (GST 5%):</span>
                        <span>₹<fmt:formatNumber value="${cart.estimatedTax}" pattern="#,##0.00"/></span>
                    </div>

                    <div style="display: flex; justify-content: space-between; margin-bottom: 16px; font-size: 14px;">
                        <span style="color: var(--text-muted);">Shipping Delivery:</span>
                        <span>
                            <c:choose>
                                <c:when test="${cart.shippingFee == 0}">
                                    <strong style="color: var(--success);">FREE</strong>
                                </c:when>
                                <c:otherwise>
                                    ₹<fmt:formatNumber value="${cart.shippingFee}" pattern="#,##0.00"/>
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>

                    <div style="display: flex; justify-content: space-between; margin-top: 16px; padding-top: 14px; border-top: 2px dashed var(--border); font-size: 20px; font-weight: 800; color: var(--primary);">
                        <span>Total Due:</span>
                        <span>₹<fmt:formatNumber value="${cart.grandTotal}" pattern="#,##0.00"/></span>
                    </div>

                    <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary" style="width: 100%; margin-top: 24px; padding: 12px;">
                        <i class="fa-solid fa-lock"></i> Proceed to Checkout
                    </a>

                    <div style="text-align: center; margin-top: 14px;">
                        <a href="${pageContext.request.contextPath}/products" style="font-size: 13px; color: var(--text-muted);">
                            <i class="fa-solid fa-arrow-left"></i> Continue Shopping
                        </a>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
