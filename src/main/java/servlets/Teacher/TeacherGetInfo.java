package servlets.Teacher;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.TeacherDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.tables.Teacher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

import static dbmodules.types.Criteria.ID;
import static webdebugger.WebInputDebugger.parseCriteria;
import static webdebugger.WebInputDebugger.printMessage;

public class TeacherGetInfo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        TeacherDAO teacherDAO = new TeacherDAO();

        String idString = request.getParameter("id");
        String message = "Проверяю переданные параметры... <br>Критерий: ";

        if (Objects.isNull(idString)) {
            message += printMessage(2, "Переданы пустые параметры.");
            response.getWriter().write(message);
        } else {
            idString = parseCriteria(ID, idString);
            if (!Objects.isNull(idString)) {
                int id = Integer.parseInt(idString);
                Teacher teacher = teacherDAO.selectById(id);

                response.getWriter().write("\t<table><tr>\n" +
                        "\t\t<td style=\"display: none;\">ID преподавателя</td>\n" +
                        "\t\t<td>Номер</td>\n" +
                        "\t\t<td>Операции</td>\n" +
                        "\t</tr>");
                if (!teacher.getGroups().isEmpty()) {
                    for (Group group : teacher.getGroups()) {
                        response.getWriter().write("\t<tr id=\"groupTeacher" + group.getId() + "\">\n" +
                                "\t\t<td class=\"teacherId\" style=\"display: none;\">" + teacher.getId() + "</td>\n" +
                                "\t\t<td class=\"number\">" + group.getNumber() + "</td>\n" +
                                "\t\t<td><a class=\"removeTeacherFromGroup\" href=\"#removeGroupFromTeacher" + group.getId() + "FromGroup\">Убрать группу</a></td>\n" +
                                "\t</tr>");
                    }
                } else {
                    response.getWriter().write("<span class=\"error\">-1</span>");
                }
                response.getWriter().write("</table>");
                teacherDAO.closeEM();
            }
        }
    }
}

