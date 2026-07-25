<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Internal Error — PreethiMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="max-width: 580px; margin-top: 60px; margin-bottom: 80px; text-align: center;">
    <div class="card" style="padding: 50px 30px;">
        <div style="font-size: 72px; font-weight: 900; color: var(--danger); line-height: 1; margin-bottom: 12px;">500</div>
        <h2 style="font-size: 22px; font-weight: 700; margin-bottom: 8px;">Unexpected Error</h2>
        <p style="color: var(--text-muted); font-size: 14px; margin-bottom: 24px;">
            An unexpected server issue occurred while processing your request. For your security, raw stack traces are suppressed. Our team has been notified.
        </p>
        <div style="display: flex; gap: 12px; justify-content: center;">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                <i class="fa-solid fa-store"></i> Return Home
            </a>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-secondary">
                Report Issue
            </a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
