<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="All Orders — PreethiMart Admin" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="margin-top: 30px; margin-bottom: 60px;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
        <div>
            <h1 style="font-size: 26px; font-weight: 800;"><i class="fa-solid fa-boxes-packing"></i> All Platform Orders</h1>
            <p style="color: var(--text-muted); font-size: 14px;">Complete transaction audit ledger for PreethiMart.</p>
        </div>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">
            <i class="fa-solid fa-arrow-left"></i> Back to Console
        </a>
    </div>

    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th>Order #</th>
                    <th>Buyer</th>
                    <th>Items</th>
                    <th>Total Paid</th>
                    <th>Method</th>
                    <th>Status</th>
                    <th>Date</th>
                    <th>Status Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="o" items="${orders}">
                    <tr>
                        <td><strong>#PM-<c:out value="${o.id}"/></strong></td>
                        <td>
                            <div><strong><c:out value="${o.buyerName}"/></strong></div>
                            <div style="font-size: 11px; color: var(--text-muted);"><c:out value="${o.buyerEmail}"/></div>
                        </td>
                        <td><c:out value="${o.items.size()}"/> items</td>
                        <td style="font-weight: 700;">₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
                        <td><c:out value="${o.paymentMethod}"/></td>
                        <td><span class="status-pill status-${o.status.toLowerCase()}"><c:out value="${o.status}"/></span></td>
                        <td style="font-size: 12px; color: var(--text-muted);"><fmt:formatDate value="${o.createdAt}" pattern="dd MMM yyyy"/></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/orders/status" method="POST" style="margin:0;">
                                <input type="hidden" name="orderId" value="${o.id}">
                                <select name="status" class="form-control" style="padding: 2px 6px; font-size: 12px;" onchange="this.form.submit()">
                                    <option value="PENDING" ${o.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                                    <option value="CONFIRMED" ${o.status == 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                                    <option value="SHIPPED" ${o.status == 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                                    <option value="DELIVERED" ${o.status == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                    <option value="CANCELLED" ${o.status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                                </select>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
