<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Create Account — PreethiMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="max-width: 500px; margin-top: 40px; margin-bottom: 60px;">
    <div class="card">
        <h2 style="font-size: 24px; font-weight: 800; text-align: center; margin-bottom: 6px;">Create Your Account</h2>
        <p style="text-align: center; color: var(--text-muted); font-size: 14px; margin-bottom: 24px;">Join PreethiMart as a shopper or verified seller</p>

        <form action="${pageContext.request.contextPath}/auth/register" method="POST">
            <div class="form-group">
                <label for="name">Full Name</label>
                <input type="text" id="name" name="name" class="form-control" placeholder="e.g. Preethi Sundaram"
                       value="<c:out value='${name}'/>" required autofocus>
            </div>

            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" class="form-control" placeholder="e.g. preethi@example.com"
                       value="<c:out value='${email}'/>" required>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Minimum 6 characters" required minlength="6">
            </div>

            <div class="form-group">
                <label for="role">Account Type / Role</label>
                <select id="role" name="role" class="form-control" required>
                    <option value="BUYER" ${role == 'BUYER' or empty role ? 'selected' : ''}>🛍️ Shopper / Buyer (Browse & Order)</option>
                    <option value="SELLER" ${role == 'SELLER' ? 'selected' : ''}>💼 Merchant / Seller (List Products & Fulfill Orders)</option>
                </select>
                <small style="color: var(--text-muted); font-size: 11px; display: block; margin-top: 4px;">
                    Note: System Administrators are provisioned via database seed accounts.
                </small>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 10px;">
                <i class="fa-solid fa-user-plus"></i> Create Account
            </button>
        </form>

        <div style="margin-top: 24px; padding-top: 18px; border-top: 1px solid var(--border); font-size: 13px; text-align: center;">
            Already have an account? <a href="${pageContext.request.contextPath}/auth/login">Sign in here</a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
