package servlets.Teacher;

import dbmodules.dao.StudentDAO;
import dbmodules.dao.TeacherDAO;
import dbmodules.tables.Student;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static webdebugger.WebInputDebugger.generateTeacherTable;

public class TeacherAllSelector extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        TeacherDAO teacherDAO = new TeacherDAO();
        List<Teacher> list = teacherDAO.select(Criteria.ALL,"");
        response.getWriter().write(generateTeacherTable(list));
        teacherDAO.closeEM();
    }
}
