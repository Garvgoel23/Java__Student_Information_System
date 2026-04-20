package com.sis.servlet;

import com.sis.dao.StudentDAO;
import com.sis.model.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final StudentDAO dao = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("student") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Student student = (Student) session.getAttribute("student");

        try {
            List<String[]> marks      = dao.getMarks(student.getId());
            List<String[]> attendance = dao.getAttendance(student.getId());
            req.setAttribute("marks", marks);
            req.setAttribute("attendance", attendance);
        } catch (Exception e) {
            req.setAttribute("dbError", e.getMessage());
        }

        req.getRequestDispatcher("/dashboard.jsp").forward(req, res);
    }
}
