<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    if (session.getAttribute("adminLoggedIn") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>SIS - Admin Panel</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: Arial, sans-serif; background: #f0f2f5; }
        header { background: #37474f; color: white; padding: 1rem 2rem;
                 display: flex; justify-content: space-between; align-items: center; }
        header h1 { font-size: 1.2rem; }
        header a { color: #cfd8dc; text-decoration: none; font-size: .9rem; }
        .container { max-width: 1000px; margin: 2rem auto; padding: 0 1rem; }

        .card { background: white; padding: 1.5rem; border-radius: 8px;
                box-shadow: 0 1px 4px rgba(0,0,0,.1); margin-bottom: 1.5rem; }
        .card h3 { color: #37474f; margin-bottom: 1rem; }
        .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: .8rem; }
        label { display: block; font-size: .8rem; color: #666; margin-bottom: .2rem; }
        input { width: 100%; padding: .5rem .7rem; border: 1px solid #ccc;
                border-radius: 4px; font-size: .9rem; margin-bottom: .8rem; }
        .btn { padding: .55rem 1.2rem; border: none; border-radius: 4px;
               cursor: pointer; font-size: .9rem; }
        .btn-add    { background: #43a047; color: white; }
        .btn-edit   { background: #1976d2; color: white; }
        .btn-delete { background: #e53935; color: white; }
        .btn:hover  { opacity: .85; }

        table { width: 100%; border-collapse: collapse; font-size: .9rem; }
        th { background: #37474f; color: white; padding: .6rem 1rem; text-align: left; }
        td { padding: .6rem 1rem; border-bottom: 1px solid #eee; }
        tr:hover td { background: #f9f9f9; }
        .error { color: red; font-size: .85rem; margin-bottom: 1rem; }
    </style>
</head>
<body>
<header>
    <h1>Admin Panel – Student Information System</h1>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
</header>

<div class="container">

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <!-- Add / Edit Form -->
    <div class="card">
        <c:choose>
            <c:when test="${not empty editStudent}">
                <h3>Edit Student</h3>
                <form method="post" action="${pageContext.request.contextPath}/admin">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id"     value="${editStudent.id}">
                    <div class="form-row">
                        <div>
                            <label>Roll No</label>
                            <input name="rollNo"   value="${editStudent.rollNo}"   required>
                        </div>
                        <div>
                            <label>Name</label>
                            <input name="name"     value="${editStudent.name}"     required>
                        </div>
                        <div>
                            <label>Email</label>
                            <input name="email"    value="${editStudent.email}"    required type="email">
                        </div>
                        <div>
                            <label>Branch</label>
                            <input name="branch"   value="${editStudent.branch}"   required>
                        </div>
                        <div>
                            <label>Semester</label>
                            <input name="semester" value="${editStudent.semester}" required type="number" min="1" max="8">
                        </div>
                    </div>
                    <button class="btn btn-add" type="submit">Update Student</button>
                    &nbsp;
                    <a href="${pageContext.request.contextPath}/admin">
                        <button class="btn" type="button" style="background:#757575;color:white">Cancel</button>
                    </a>
                </form>
            </c:when>
            <c:otherwise>
                <h3>Add Student</h3>
                <form method="post" action="${pageContext.request.contextPath}/admin">
                    <input type="hidden" name="action" value="add">
                    <div class="form-row">
                        <div>
                            <label>Roll No</label>
                            <input name="rollNo"   placeholder="CS001"    required>
                        </div>
                        <div>
                            <label>Name</label>
                            <input name="name"     placeholder="Full Name" required>
                        </div>
                        <div>
                            <label>Email</label>
                            <input name="email"    placeholder="email@college.com" required type="email">
                        </div>
                        <div>
                            <label>Password</label>
                            <input name="password" placeholder="Initial password" required type="password">
                        </div>
                        <div>
                            <label>Branch</label>
                            <input name="branch"   placeholder="CSE"      required>
                        </div>
                        <div>
                            <label>Semester</label>
                            <input name="semester" placeholder="1-8"      required type="number" min="1" max="8">
                        </div>
                    </div>
                    <button class="btn btn-add" type="submit">Add Student</button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Student List -->
    <div class="card">
        <h3>All Students</h3>
        <table>
            <tr>
                <th>Roll No</th><th>Name</th><th>Email</th>
                <th>Branch</th><th>Semester</th><th>Actions</th>
            </tr>
            <c:forEach var="s" items="${students}">
                <tr>
                    <td>${s.rollNo}</td>
                    <td>${s.name}</td>
                    <td>${s.email}</td>
                    <td>${s.branch}</td>
                    <td>${s.semester}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin?action=edit&id=${s.id}">
                            <button class="btn btn-edit">Edit</button>
                        </a>
                        &nbsp;
                        <a href="${pageContext.request.contextPath}/admin?action=delete&id=${s.id}"
                           onclick="return confirm('Delete this student?')">
                            <button class="btn btn-delete">Delete</button>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty students}">
                <tr><td colspan="6" style="text-align:center">No students found.</td></tr>
            </c:if>
        </table>
    </div>

</div>
</body>
</html>
