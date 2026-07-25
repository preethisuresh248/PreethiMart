<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Sign In — PreethiMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="max-width: 480px; margin-top: 40px; margin-bottom: 60px;">
    <div class="card">
        <h2 style="font-size: 24px; font-weight: 800; text-align: center; margin-bottom: 6px;">Welcome Back</h2>
        <p style="text-align: center; color: var(--text-muted); font-size: 14px; margin-bottom: 24px;">Sign in to your PreethiMart account</p>

        <form action="${pageContext.request.contextPath}/auth/login" method="POST">
            <c:if test="${not empty param.redirect}">
                <input type="hidden" name="redirect" value="<c:out value='${param.redirect}'/>">
            </c:if>

            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" class="form-control" placeholder="you@domain.com"
                       value="<c:out value='${email}'/>" required autofocus>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 10px;">
                <i class="fa-solid fa-arrow-right-to-bracket"></i> Sign In
            </button>
        </form>

        <div style="margin-top: 24px; padding-top: 18px; border-top: 1px solid var(--border); font-size: 13px; text-align: center;">
            Don't have an account? <a href="${pageContext.request.contextPath}/auth/register">Create one here</a>
        </div>

        <!-- Academic Demo Credentials Quick Filler -->
        <div style="margin-top: 24px; background: #f8fafc; padding: 14px; border-radius: 8px; border: 1px dashed var(--border);">
            <div style="font-size: 12px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; margin-bottom: 8px;">
                ⚡ Demo Accounts Quick-Fill:
            </div>
            <div style="display: flex; gap: 6px; flex-wrap: wrap;">
                <button type="button" class="btn btn-secondary btn-sm" onclick="fillCreds('buyer1@preethimart.com', 'Buyer@123')">
                    Buyer
                </button>
                <button type="button" class="btn btn-secondary btn-sm" onclick="fillCreds('seller1@preethimart.com', 'Seller@123')">
                    Seller
                </button>
                <button type="button" class="btn btn-secondary btn-sm" onclick="fillCreds('admin@preethimart.com', 'Admin@123')">
                    Admin
                </button>
            </div>
        </div>
    </div>
</div>

<script>
function fillCreds(email, pwd) {
    document.getElementById('email').value = email;
    document.getElementById('password').value = pwd;
}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
