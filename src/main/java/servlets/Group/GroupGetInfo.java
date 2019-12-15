package servlets.Group;

import dbmodules.dao.GroupDAO;
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

import static dbmodules.types.Criteria.ALL;
import static dbmodules.types.Criteria.ID;
import static webdebugger.WebInputDebugger.parseCriteria;
import static webdebugger.WebInputDebugger.printMessage;

public class GroupGetInfo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        GroupDAO groupDAO = new GroupDAO();

        String idString = request.getParameter("id");
        String message = "Проверяю переданные параметры... <br>Критерий: ";

        if(Objects.isNull(idString)) {
            message += printMessage(2,"Переданы пустые параметры.");
            response.getWriter().write(message);
        } else {
            idString = parseCriteria(ID, idString);
            if(!Objects.isNull(idString)) {
                int id = Integer.parseInt(idString);
                Group group = groupDAO.selectById(id);

                response.getWriter().write("Преподаватели:");
                if(!group.getTeachers().isEmpty()) {
                    response.getWriter().write("\t<table><tr>\n" +
                            "\t\t<td style=\"display: none;\">ID группы</td>\n" +
                            "\t\t<td>ФИО</td>\n" +
                            "\t\t<td>Операции</td>\n" +
                            "\t</tr>");
                    for (Teacher teacher : group.getTeachers()) {
                        response.getWriter().write("\t<tr id=\"groupTeacher" + teacher.getId() + "\">\n" +
                                "\t\t<td class=\"groupId\" style=\"display: none;\">" + group.getId() + "</td>\n" +
                                "\t\t<td>" + teacher.getName() + "</td>\n" +
                                "\t\t<td><a class=\"removeTeacherFromGroup\" href=\"#removeTeacher" + teacher.getId() + "FromGroup\">Убрать из группы</a></td>\n" +
                                "\t</tr>");
                    }
                } else {
                    response.getWriter().write(" нет.<br>");
                }
                response.getWriter().write("</table>Студенты: ");
                if(!group.getStudents().isEmpty()) {
                    for(Iterator<Student> iterator = group.getStudents().iterator();
                        iterator.hasNext();) {
                        Student student = iterator.next();
                        if(iterator.hasNext()) {
                            response.getWriter().write(student.getName() + ", ");
                        } else {
                            response.getWriter().write(student.getName() + ". ");
                        }
                    }
                } else {
                    response.getWriter().write(" нет.");
                }
            }
        }


    }
}
