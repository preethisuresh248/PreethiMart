<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="User Management — PreethiMart Admin" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container" style="margin-top: 30px; margin-bottom: 60px;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
        <div>
            <h1 style="font-size: 26px; font-weight: 800;"><i class="fa-solid fa-users"></i> Platform User Directory</h1>
            <p style="color: var(--text-muted); font-size: 14px;">Audit buyer and merchant accounts across PreethiMart.</p>
        </div>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">
            <i class="fa-solid fa-arrow-left"></i> Back to Console
        </a>
    </div>

    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th>User ID</th>
                    <th>Name</th>
                    <th>Email Address</th>
                    <th>Role</th>
                    <th>Joined On</th>
                    <th style="text-align: right;">Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td>#<c:out value="${u.id}"/></td>
                        <td><strong><c:out value="${u.name}"/></strong></td>
                        <td><c:out value="${u.email}"/></td>
                        <td>
                            <span class="role-badge role-${u.role.toLowerCase()}">
                                <c:out value="${u.role}"/>
                            </span>
                        </td>
                        <td style="font-size: 12px; color: var(--text-muted);">
                            <fmt:formatDate value="${u.createdAt}" pattern="dd MMM yyyy, hh:mm a"/>
                        </td>
                        <td style="text-align: right;">
                            <c:if test="${u.role != 'ADMIN'}">
                                <form action="${pageContext.request.contextPath}/admin/users/delete" method="POST"
                                      onsubmit="return confirm('Confirm deletion of user #${u.id}?');" style="margin: 0; display: inline;">
                                    <input type="hidden" name="id" value="${u.id}">
                                    <button type="submit" class="btn btn-secondary btn-sm" style="color: var(--danger);">
                                        <i class="fa-solid fa-user-xmark"></i> Delete
                                    </button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
