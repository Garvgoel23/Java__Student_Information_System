package com.sis.servlet;

import com.sis.dao.StudentDAO;
import com.sis.model.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final StudentDAO dao = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String role     = req.getParameter("role");     // "student" or "admin"
        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();

        try {
            if ("admin".equals(role)) {
                if (dao.loginAdmin(username, password)) {
                    HttpSession session = req.getSession();
                    session.setAttribute("adminLoggedIn", true);
                    res.sendRedirect(req.getContextPath() + "/admin");
                } else {
                    req.setAttribute("error", "Invalid admin credentials.");
                    req.getRequestDispatcher("/login.jsp").forward(req, res);
                }
            } else {
                Student student = dao.loginStudent(username, password);
                if (student != null) {
                    HttpSession session = req.getSession();
                    session.setAttribute("student", student);
                    res.sendRedirect(req.getContextPath() + "/dashboard");
                } else {
                    req.setAttribute("error", "Invalid Roll No or Password.");
                    req.getRequestDispatcher("/login.jsp").forward(req, res);
                }
            }
        } catch (Exception e) {
            req.setAttribute("error", "Database error: " + e.getMessage());
            req.getRequestDispatcher("/login.jsp").forward(req, res);
        }
    }
}
