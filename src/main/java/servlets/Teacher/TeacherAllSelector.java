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

public class TeacherAllSelector extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        TeacherDAO teacherDAO = new TeacherDAO();
        List<Teacher> list = teacherDAO.select(Criteria.ALL,"");
        response.getWriter().write("\t<tr>\n" +
                "\t\t<td>ID</td>\n" +
                "\t\t<td>ФИО</td>\n" +
                "\t\t<td>День рождения</td>\n" +
                "\t\t<td>Пол</td>\n" +
                "\t\t<td>Операции</td>\n" +
                "\t</tr>");
        if(!list.isEmpty()) {
            for (Teacher teacher : list) {
                response.getWriter().write("<tr id=\"student" + teacher.getId() + "\">\n");
                response.getWriter().write("<td class=\"id\">" + teacher.getId() + "</td>");
                response.getWriter().write("<td class=\"name\">" + teacher.getName() + "</td>");
                response.getWriter().write("<td class=\"birth\">" + teacher.getBirth() + "</td>");
                response.getWriter().write("<td class=\"gender\">" + teacher.getGender() + "</td>");
                response.getWriter().write("<td class=\"operations\">" +
                        "<a class=\"delete\" href=\"#deleteTeacher" + teacher.getId() + "\">Удалить</a><br>" +
                        "<a class=\"update\" href=\"#updateTeacher" + teacher.getId() + "\">Изменить</a>" +
                        "<a class=\"getInfo\" href=\"#getInfoTeacher" + teacher.getId() + "\">Информация</a>" +
                        "</td>");
                response.getWriter().write("</tr>");
            }
        } else {
            response.getWriter().write("<tr><td colspan=\"6\">Нет записей.</tr></td>");
        }
        teacherDAO.closeEM();
    }
}
