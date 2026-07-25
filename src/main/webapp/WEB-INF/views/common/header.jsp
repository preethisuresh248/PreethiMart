<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'PreethiMart — Enterprise E-Commerce Platform'}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<nav class="navbar">
    <div class="container navbar-inner">
        <a href="${pageContext.request.contextPath}/products" class="brand">
            <i class="fa-solid fa-bag-shopping"></i> Preethi<span>Mart</span>
        </a>

        <form action="${pageContext.request.contextPath}/products" method="GET" class="nav-search">
            <input type="text" name="q" placeholder="Search products, brands, essentials..." value="<c:out value='${searchQuery}'/>">
            <c:if test="${not empty selectedCategory and selectedCategory != 'ALL'}">
                <input type="hidden" name="category" value="<c:out value='${selectedCategory}'/>">
            </c:if>
            <button type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
        </form>

        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/products"><i class="fa-solid fa-store"></i> Shop</a></li>

            <c:choose>
                <c:when test="${not empty sessionScope.currentUser}">
                    <c:if test="${sessionScope.currentUser.role == 'BUYER'}">
                        <li>
                            <a href="${pageContext.request.contextPath}/wishlist">
                                <i class="fa-regular fa-heart"></i> Wishlist
                            </a>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/cart">
                                <i class="fa-solid fa-cart-shopping"></i> Cart
                                <c:if test="${cartItemCount > 0}">
                                    <span class="badge"><c:out value="${cartItemCount}"/></span>
                                </c:if>
                            </a>
                        </li>
                        <li><a href="${pageContext.request.contextPath}/orders"><i class="fa-solid fa-receipt"></i> Orders</a></li>
                    </c:if>

                    <c:if test="${sessionScope.currentUser.role == 'SELLER'}">
                        <li><a href="${pageContext.request.contextPath}/seller/dashboard"><i class="fa-solid fa-chart-line"></i> Dashboard</a></li>
                        <li><a href="${pageContext.request.contextPath}/seller/orders"><i class="fa-solid fa-box-open"></i> Incoming Orders</a></li>
                    </c:if>

                    <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                        <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa-solid fa-shield-halved"></i> Admin Console</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/users"><i class="fa-solid fa-users"></i> Users</a></li>
                    </c:if>

                    <li>
                        <span class="role-badge role-${fn:toLowerCase(sessionScope.currentUser.role)}">
                            <c:out value="${sessionScope.currentUser.role}"/>
                        </span>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/auth/logout" style="color: var(--danger);">
                            <i class="fa-solid fa-arrow-right-from-bracket"></i> Logout
                        </a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li><a href="${pageContext.request.contextPath}/auth/login"><i class="fa-solid fa-arrow-right-to-bracket"></i> Sign In</a></li>
                    <li><a href="${pageContext.request.contextPath}/auth/register" class="btn btn-primary btn-sm" style="color:white;"><i class="fa-solid fa-user-plus"></i> Join Free</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>

<div class="container" style="margin-top: 20px;">
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error">
            <i class="fa-solid fa-circle-exclamation"></i> <c:out value="${errorMessage}"/>
        </div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">
            <i class="fa-solid fa-circle-check"></i> <c:out value="${successMessage}"/>
        </div>
    </c:if>
    <c:if test="${not empty sessionScope.sellerSuccess}">
        <div class="alert alert-success">
            <i class="fa-solid fa-circle-check"></i> <c:out value="${sessionScope.sellerSuccess}"/>
        </div>
        <c:remove var="sellerSuccess" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.sellerError}">
        <div class="alert alert-error">
            <i class="fa-solid fa-circle-exclamation"></i> <c:out value="${sessionScope.sellerError}"/>
        </div>
        <c:remove var="sellerError" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.adminSuccess}">
        <div class="alert alert-success">
            <i class="fa-solid fa-circle-check"></i> <c:out value="${sessionScope.adminSuccess}"/>
        </div>
        <c:remove var="adminSuccess" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.cartError}">
        <div class="alert alert-error">
            <i class="fa-solid fa-circle-exclamation"></i> <c:out value="${sessionScope.cartError}"/>
        </div>
        <c:remove var="cartError" scope="session"/>
    </c:if>
</div>
