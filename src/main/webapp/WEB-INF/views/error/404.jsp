<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Page Not Found — PreethiMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="max-width: 580px; margin-top: 60px; margin-bottom: 80px; text-align: center;">
    <div class="card" style="padding: 50px 30px;">
        <div style="font-size: 72px; font-weight: 900; color: var(--primary); line-height: 1; margin-bottom: 12px;">404</div>
        <h2 style="font-size: 22px; font-weight: 700; margin-bottom: 8px;">Page Not Found</h2>
        <p style="color: var(--text-muted); font-size: 14px; margin-bottom: 24px;">
            The product or page you are looking for has been moved, renamed, or does not exist.
        </p>
        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
            <i class="fa-solid fa-store"></i> Return to Catalog
        </a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
