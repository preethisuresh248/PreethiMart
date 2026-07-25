<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Secure Checkout — PreethiMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="margin-top: 30px; margin-bottom: 60px;">
    <h1 style="font-size: 26px; font-weight: 800; margin-bottom: 24px;">
        <i class="fa-solid fa-shield-heart"></i> Secure Checkout
    </h1>

    <div style="display: grid; grid-template-columns: 3fr 2fr; gap: 30px; align-items: start;">
        <!-- Left: Shipping Address & Mock Payment Step -->
        <div>
            <form action="${pageContext.request.contextPath}/checkout" method="POST">
                <div class="card">
                    <h3 style="font-size: 18px; font-weight: 700; margin-bottom: 16px; border-bottom: 1px solid var(--border); padding-bottom: 10px;">
                        1. Shipping Address
                    </h3>
                    <div class="form-group">
                        <label for="shippingAddress">Full Delivery Address (Street, City, State, PIN)</label>
                        <textarea id="shippingAddress" name="shippingAddress" class="form-control" rows="3"
                                  placeholder="e.g. 402 Green Glen Layout, Bellandur, Bangalore 560103" required><c:out value="${shippingAddress != null ? shippingAddress : 'Flat 402, Green Glen Layout, Bellandur, Bangalore, Karnataka - 560103'}"/></textarea>
                    </div>
                </div>

                <div class="card">
                    <h3 style="font-size: 18px; font-weight: 700; margin-bottom: 16px; border-bottom: 1px solid var(--border); padding-bottom: 10px;">
                        2. Mock Payment Confirmation (F5 Demo Step)
                    </h3>
                    <p style="font-size: 13px; color: var(--text-muted); margin-bottom: 16px;">
                        Select your preferred test payment instrument. No real credit card charge occurs.
                    </p>

                    <div style="display: flex; flex-direction: column; gap: 12px; margin-bottom: 20px;">
                        <label style="display: flex; align-items: center; gap: 12px; padding: 12px; border: 1px solid var(--border); border-radius: 8px; cursor: pointer;">
                            <input type="radio" name="paymentMethod" value="MOCK_CARD" checked>
                            <div>
                                <strong>💳 Mock Credit / Debit Card</strong>
                                <div style="font-size: 12px; color: var(--text-muted);">Simulated Visa / MasterCard / RuPay Instant Authorization</div>
                            </div>
                        </label>

                        <label style="display: flex; align-items: center; gap: 12px; padding: 12px; border: 1px solid var(--border); border-radius: 8px; cursor: pointer;">
                            <input type="radio" name="paymentMethod" value="MOCK_UPI">
                            <div>
                                <strong>📱 Mock UPI Payment</strong>
                                <div style="font-size: 12px; color: var(--text-muted);">Simulated GooglePay / PhonePe / Paytm Virtual Payment</div>
                            </div>
                        </label>

                        <label style="display: flex; align-items: center; gap: 12px; padding: 12px; border: 1px solid var(--border); border-radius: 8px; cursor: pointer;">
                            <input type="radio" name="paymentMethod" value="MOCK_NETBANKING">
                            <div>
                                <strong>🏦 Mock Internet Banking</strong>
                                <div style="font-size: 12px; color: var(--text-muted);">Simulated HDFC / SBI / ICICI Direct Gateway</div>
                            </div>
                        </label>
                    </div>

                    <button type="submit" class="btn btn-primary" style="width: 100%; padding: 14px; font-size: 16px;">
                        <i class="fa-solid fa-lock"></i> Authorize & Place Order (₹<fmt:formatNumber value="${cart.grandTotal}" pattern="#,##0.00"/>)
                    </button>
                </div>
            </form>
        </div>

        <!-- Right: Order Items Review -->
        <div class="card">
            <h3 style="font-size: 18px; font-weight: 700; margin-bottom: 16px; border-bottom: 1px solid var(--border); padding-bottom: 10px;">
                Review Items (<c:out value="${cart.totalItems}"/>)
            </h3>

            <div style="display: flex; flex-direction: column; gap: 12px; margin-bottom: 20px; max-height: 280px; overflow-y: auto;">
                <c:forEach var="item" items="${cart.items}">
                    <div style="display: flex; justify-content: space-between; align-items: center; font-size: 13px;">
                        <div style="display: flex; align-items: center; gap: 10px;">
                            <img src="<c:out value='${item.productImageUrl}'/>" alt="<c:out value='${item.productName}'/>"
                                 style="width: 40px; height: 40px; object-fit: cover; border-radius: 4px;"
                                 onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500';">
                            <div>
                                <div style="font-weight: 600;"><c:out value="${item.productName}"/></div>
                                <div style="color: var(--text-muted);">Qty: <c:out value="${item.quantity}"/> &times; ₹<fmt:formatNumber value="${item.productPrice}" pattern="#,##0.00"/></div>
                            </div>
                        </div>
                        <div style="font-weight: 700;">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></div>
                    </div>
                </c:forEach>
            </div>

            <div style="border-top: 1px solid var(--border); padding-top: 14px; display: flex; flex-direction: column; gap: 8px; font-size: 14px;">
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-muted);">Subtotal:</span>
                    <span>₹<fmt:formatNumber value="${cart.subtotal}" pattern="#,##0.00"/></span>
                </div>
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-muted);">Taxes (5% GST):</span>
                    <span>₹<fmt:formatNumber value="${cart.estimatedTax}" pattern="#,##0.00"/></span>
                </div>
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-muted);">Shipping:</span>
                    <span><c:out value="${cart.shippingFee == 0 ? 'FREE' : '₹49.00'}"/></span>
                </div>
                <div style="display: flex; justify-content: space-between; margin-top: 10px; padding-top: 10px; border-top: 2px dashed var(--border); font-size: 18px; font-weight: 800; color: var(--primary);">
                    <span>Total:</span>
                    <span>₹<fmt:formatNumber value="${cart.grandTotal}" pattern="#,##0.00"/></span>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
