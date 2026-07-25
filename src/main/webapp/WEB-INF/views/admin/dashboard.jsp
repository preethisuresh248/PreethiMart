<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Admin Portal — PreethiMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="margin-top: 30px; margin-bottom: 60px;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
        <div>
            <h1 style="font-size: 26px; font-weight: 800;"><i class="fa-solid fa-shield-halved"></i> Platform Administration</h1>
            <p style="color: var(--text-muted); font-size: 14px;">Monitor platform health, audit accounts, and enforce listing policies.</p>
        </div>
        <div style="display: flex; gap: 10px;">
            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">
                <i class="fa-solid fa-users"></i> Users
            </a>
            <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-secondary">
                <i class="fa-solid fa-boxes-packing"></i> Orders
            </a>
            <a href="${pageContext.request.contextPath}/admin/listings" class="btn btn-secondary">
                <i class="fa-solid fa-list-check"></i> Listings Moderation
            </a>
        </div>
    </div>

    <!-- KPI Metric Cards -->
    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 20px; margin-bottom: 30px;">
        <div class="card" style="margin-bottom: 0;">
            <div style="color: var(--text-muted); font-size: 13px; font-weight: 600; text-transform: uppercase;">Total Users</div>
            <div style="font-size: 28px; font-weight: 800; color: var(--primary); margin: 6px 0;"><c:out value="${totalUsers}"/></div>
            <div style="font-size: 12px; color: var(--success);"><i class="fa-solid fa-user-check"></i> Buyers & Sellers Active</div>
        </div>

        <div class="card" style="margin-bottom: 0;">
            <div style="color: var(--text-muted); font-size: 13px; font-weight: 600; text-transform: uppercase;">Total Orders</div>
            <div style="font-size: 28px; font-weight: 800; color: var(--secondary); margin: 6px 0;"><c:out value="${totalOrders}"/></div>
            <div style="font-size: 12px; color: var(--text-muted);"><i class="fa-solid fa-cart-shopping"></i> Lifetime Completed</div>
        </div>

        <div class="card" style="margin-bottom: 0;">
            <div style="color: var(--text-muted); font-size: 13px; font-weight: 600; text-transform: uppercase;">Platform GMV</div>
            <div style="font-size: 28px; font-weight: 800; color: #10b981; margin: 6px 0;">₹<fmt:formatNumber value="${gmv}" pattern="#,##0.00"/></div>
            <div style="font-size: 12px; color: var(--success);"><i class="fa-solid fa-arrow-trend-up"></i> Gross Merchandise Value</div>
        </div>

        <div class="card" style="margin-bottom: 0;">
            <div style="color: var(--text-muted); font-size: 13px; font-weight: 600; text-transform: uppercase;">Catalog Listings</div>
            <div style="font-size: 28px; font-weight: 800; color: var(--warning); margin: 6px 0;"><c:out value="${totalProducts}"/></div>
            <div style="font-size: 12px; color: var(--text-muted);"><i class="fa-solid fa-tags"></i> Active Products</div>
        </div>
    </div>

    <!-- Recent Platform Orders -->
    <div class="card">
        <h3 style="font-size: 18px; font-weight: 700; margin-bottom: 16px; border-bottom: 1px solid var(--border); padding-bottom: 10px;">
            Recent Platform Orders
        </h3>

        <div class="table-container" style="box-shadow: none; margin-bottom: 0;">
            <table>
                <thead>
                    <tr>
                        <th>Order #</th>
                        <th>Buyer</th>
                        <th>Amount</th>
                        <th>Status</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="o" items="${recentOrders}">
                        <tr>
                            <td><strong>#PM-<c:out value="${o.id}"/></strong></td>
                            <td><c:out value="${o.buyerName}"/> (<c:out value="${o.buyerEmail}"/>)</td>
                            <td style="font-weight: 700;">₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
                            <td><span class="status-pill status-${o.status.toLowerCase()}"><c:out value="${o.status}"/></span></td>
                            <td style="font-size: 12px; color: var(--text-muted);"><fmt:formatDate value="${o.createdAt}" pattern="dd MMM yyyy, hh:mm a"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
