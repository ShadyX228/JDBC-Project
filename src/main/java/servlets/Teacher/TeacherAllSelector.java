package servlets.Teacher;

import dbmodules.dao.TeacherDAO;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static webdebugger.WebInputDebugger.generateTeacherTable;
import static webdebugger.WebInputDebugger.setQueryParametres;

public class TeacherAllSelector extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setQueryParametres(request,response);
        TeacherDAO teacherDAO = new TeacherDAO();
        List<Teacher> list = teacherDAO.select(Criteria.ALL,"");
        response.getWriter().write(generateTeacherTable(list));
        teacherDAO.closeEntityManager();
    }
}
