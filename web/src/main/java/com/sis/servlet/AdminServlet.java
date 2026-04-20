package com.sis.servlet;

import com.sis.dao.StudentDAO;
import com.sis.model.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private final StudentDAO dao = new StudentDAO();

    // Guard: redirect if not logged in as admin
    private boolean isAdmin(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("adminLoggedIn") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        if (!isAdmin(req, res)) return;

        String action = req.getParameter("action");

        try {
            if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("editStudent", dao.getStudentById(id));
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                dao.deleteStudent(id);
                res.sendRedirect(req.getContextPath() + "/admin");
                return;
            }
            req.setAttribute("students", dao.getAllStudents());
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        }

        req.getRequestDispatcher("/admin.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        if (!isAdmin(req, res)) return;

        String action = req.getParameter("action");

        try {
            Student s = new Student();
            s.setRollNo(req.getParameter("rollNo").trim());
            s.setName(req.getParameter("name").trim());
            s.setEmail(req.getParameter("email").trim());
            s.setBranch(req.getParameter("branch").trim());
            s.setSemester(Integer.parseInt(req.getParameter("semester").trim()));

            if ("add".equals(action)) {
                s.setPassword(req.getParameter("password").trim());
                dao.addStudent(s);
            } else if ("update".equals(action)) {
                s.setId(Integer.parseInt(req.getParameter("id")));
                dao.updateStudent(s);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Error: " + e.getMessage());
        }

        res.sendRedirect(req.getContextPath() + "/admin");
    }
}
