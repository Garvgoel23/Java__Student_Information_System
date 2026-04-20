<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>SIS - Login</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: Arial, sans-serif; background: #f0f2f5;
               display: flex; justify-content: center; align-items: center; height: 100vh; }
        .card { background: white; padding: 2rem; border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0,0,0,.15); width: 340px; }
        h2  { text-align: center; margin-bottom: 1.5rem; color: #333; }
        .tabs { display: flex; margin-bottom: 1.2rem; }
        .tab { flex: 1; padding: .6rem; text-align: center; cursor: pointer;
               border: 1px solid #ccc; background: #eee; font-size: .9rem; }
        .tab.active { background: #1976d2; color: white; border-color: #1976d2; }
        label { display: block; font-size: .85rem; color: #555; margin-bottom: .3rem; }
        input, select { width: 100%; padding: .55rem .7rem; margin-bottom: 1rem;
                        border: 1px solid #ccc; border-radius: 4px; font-size: .95rem; }
        button { width: 100%; padding: .7rem; background: #1976d2; color: white;
                 border: none; border-radius: 4px; font-size: 1rem; cursor: pointer; }
        button:hover { background: #1565c0; }
        .error { color: red; font-size: .85rem; margin-bottom: 1rem; text-align: center; }
        #roleField { display: none; }
    </style>
    <script>
        function setRole(role) {
            document.getElementById('role').value = role;
            document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
            document.querySelector('.tab.' + role).classList.add('active');
            document.getElementById('usernameLabel').textContent =
                role === 'admin' ? 'Username' : 'Roll Number';
        }
    </script>
</head>
<body>
<div class="card">
    <h2>Student Info System</h2>

    <div class="tabs">
        <div class="tab student active" onclick="setRole('student')">Student</div>
        <div class="tab admin"          onclick="setRole('admin')">Admin</div>
    </div>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error">${error}</div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <input type="hidden" id="role" name="role" value="student">

        <label id="usernameLabel">Roll Number</label>
        <input type="text" name="username" placeholder="Enter Roll No / Username" required>

        <label>Password</label>
        <input type="password" name="password" placeholder="Password" required>

        <button type="submit">Login</button>
    </form>
</div>
</body>
</html>
