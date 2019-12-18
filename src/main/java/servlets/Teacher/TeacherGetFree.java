package servlets.Teacher;

import dbmodules.dao.TeacherDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;

import static webdebugger.WebInputDebugger.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeacherGetFree extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        setQueryParametres(request,response);

        String groupIdString = request.getParameter("id");

        groupIdString = parseCriteria(Criteria.ID, groupIdString);

        if(!Objects.isNull(groupIdString)) {
            int groupId = Integer.parseInt(groupIdString);
            TeacherDAO teacherDAO = new TeacherDAO();
            List<Teacher> teachers = teacherDAO.select(Criteria.ALL,"");
            List<Teacher> freeTeachers = new ArrayList<>();
            boolean check = true;
            for(Teacher teacher : teachers) {
                System.out.println(teacher);
                if(!teacher.getGroups().isEmpty()) {
                    for (Group group : teacher.getGroups()) {
                        if (group.getId() == groupId) {
                            check = false;
                            System.out.println(group);
                            break;
                        }
                    }
                }
                if(check) {
                    System.out.println("Added " + teacher.getId());
                    freeTeachers.add(teacher);
                }
                check = true;
            }
            if(freeTeachers.isEmpty()) {
                System.out.println(3);
                response.getWriter().write("Некого назначить.");
            } else {
                response.getWriter().write("\t<table><tr>\n" +
                        "\t\t<td style=\"display: none;\">ID группы</td>\n" +
                        "\t\t<td>ФИО</td>\n" +
                        "\t\t<td>Операции</td>\n" +
                        "\t</tr>");
                for (Teacher teacher : freeTeachers) {
                    response.getWriter().write("\t<tr id=\"freeTeacher"
                            + teacher.getId() + "\">\n" +
                            "\t\t<td class=\"groupId\" style=\"display: none;\">"
                            + groupId + "</td>\n" +
                            "\t\t<td>" + teacher.getName() + "</td>\n" +
                            "\t\t<td><a class=\"putInGroup\" " +
                            "href=\"#putTeacher"
                            + teacher.getId() + "\">Назначить</a></td>\n" +
                            "\t</tr>");
                }
            }
            teacherDAO.closeEntityManager();
        }
    }
}
