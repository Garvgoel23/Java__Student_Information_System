<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    if (session.getAttribute("student") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>SIS - Dashboard</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: Arial, sans-serif; background: #f0f2f5; }
        header { background: #1976d2; color: white; padding: 1rem 2rem;
                 display: flex; justify-content: space-between; align-items: center; }
        header h1 { font-size: 1.2rem; }
        header a { color: white; text-decoration: none; font-size: .9rem; }
        .container { max-width: 900px; margin: 2rem auto; padding: 0 1rem; }

        .profile { background: white; padding: 1.5rem; border-radius: 8px;
                   box-shadow: 0 1px 4px rgba(0,0,0,.1); margin-bottom: 1.5rem; }
        .profile h2 { color: #1976d2; margin-bottom: 1rem; }
        .profile-grid { display: grid; grid-template-columns: 1fr 1fr; gap: .5rem; }
        .profile-grid span { font-size: .9rem; color: #555; }
        .profile-grid strong { color: #222; }

        .section { background: white; padding: 1.5rem; border-radius: 8px;
                   box-shadow: 0 1px 4px rgba(0,0,0,.1); margin-bottom: 1.5rem; }
        .section h3 { color: #1976d2; margin-bottom: 1rem; }
        table { width: 100%; border-collapse: collapse; font-size: .9rem; }
        th { background: #1976d2; color: white; padding: .6rem 1rem; text-align: left; }
        td { padding: .6rem 1rem; border-bottom: 1px solid #eee; }
        tr:hover td { background: #f5f5f5; }

        .low { color: red; font-weight: bold; }
    </style>
</head>
<body>
<header>
    <h1>Student Information System</h1>
    <span>Welcome, ${sessionScope.student.name} &nbsp;|&nbsp;
        <a href="${pageContext.request.contextPath}/logout">Logout</a></span>
</header>

<div class="container">

    <!-- Profile -->
    <div class="profile">
        <h2>My Profile</h2>
        <div class="profile-grid">
            <span>Roll No</span>     <strong>${sessionScope.student.rollNo}</strong>
            <span>Name</span>        <strong>${sessionScope.student.name}</strong>
            <span>Email</span>       <strong>${sessionScope.student.email}</strong>
            <span>Branch</span>      <strong>${sessionScope.student.branch}</strong>
            <span>Semester</span>    <strong>${sessionScope.student.semester}</strong>
        </div>
    </div>

    <!-- Marks -->
    <div class="section">
        <h3>Marks</h3>
        <table>
            <tr><th>Subject</th><th>Marks Obtained</th><th>Max Marks</th><th>Percentage</th></tr>
            <c:forEach var="row" items="${marks}">
                <tr>
                    <td>${row[0]}</td>
                    <td>${row[1]}</td>
                    <td>${row[2]}</td>
                    <td>
                        <c:set var="pct" value="${(row[1] / row[2]) * 100}" />
                        <c:choose>
                            <c:when test="${pct < 40}">
                                <span class="low"><fmt:formatNumber value="${pct}" maxFractionDigits="1"/>%</span>
                            </c:when>
                            <c:otherwise><fmt:formatNumber value="${pct}" maxFractionDigits="1"/>%</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty marks}">
                <tr><td colspan="4" style="text-align:center">No marks data found.</td></tr>
            </c:if>
        </table>
    </div>

    <!-- Attendance -->
    <div class="section">
        <h3>Attendance</h3>
        <table>
            <tr><th>Subject</th><th>Attended / Total</th><th>Percentage</th></tr>
            <c:forEach var="row" items="${attendance}">
                <tr>
                    <td>${row[0]}</td>
                    <td>${row[1]}</td>
                    <td>
                        <%-- row[2] already has % formatted in DAO --%>
                        <c:choose>
                            <c:when test="${row[2].startsWith('7') or row[2].startsWith('8') or row[2].startsWith('9') or row[2].startsWith('1')}">
                                ${row[2]}
                            </c:when>
                            <c:otherwise><span class="low">${row[2]}</span></c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty attendance}">
                <tr><td colspan="3" style="text-align:center">No attendance data found.</td></tr>
            </c:if>
        </table>
    </div>

</div>
</body>
</html>
