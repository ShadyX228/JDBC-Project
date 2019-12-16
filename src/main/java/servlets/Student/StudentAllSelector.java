package servlets.Student;

import dbmodules.dao.StudentDAO;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;
import servlets.Main.MainServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static webdebugger.WebInputDebugger.generateStudentsTable;

public class StudentAllSelector extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        StudentDAO studentDAO = new StudentDAO();
        List<Student> list = studentDAO.select(Criteria.ALL,"");
        response.getWriter().write(generateStudentsTable(list));
        studentDAO.closeEM();
    }
}
