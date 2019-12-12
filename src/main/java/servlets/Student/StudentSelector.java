package servlets.Student;

import dbmodules.dao.StudentDAO;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static dbmodules.types.Criteria.*;
import static webdebugger.WebInputDebugger.*;


public class StudentSelector extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String birthString = request.getParameter("birth");
        String genderString = request.getParameter("gender");
        String groupString = request.getParameter("group");

        idString = parseCriteria(ID, idString);
        birthString = parseCriteria(BIRTH, birthString);
        genderString = parseCriteria(GENDER, genderString);
        groupString = parseCriteria(ID, groupString);

        HashMap<Criteria, String> map = new HashMap<>();

        List<Student> students;
        if (!Objects.isNull(idString)) {
            map.put(ID, idString);
        } else {
            if (!name.isEmpty()) {
                map.put(NAME, name);
            }

            if (!Objects.isNull(birthString)) {
                if(!birthString.isEmpty()) {
                    map.put(BIRTH, birthString);
                }
            }

            if (!Objects.isNull(genderString)) {
                if(!genderString.isEmpty()) {
                    map.put(GENDER, genderString);
                }
            }

            if (!Objects.isNull(groupString)) {
                map.put(GROUP, groupString);
            }
        }

        StudentDAO studentDAO = new StudentDAO();
        if(map.isEmpty()) {
            map.put(ALL, " ");
        }
        //response.getWriter().write(map + "<br>");
        students = studentDAO.select(map);

        response.getWriter().write("\t<tr>\n" +
                "\t\t<td>ID</td>\n" +
                "\t\t<td>ФИО</td>\n" +
                "\t\t<td>День рождения</td>\n" +
                "\t\t<td>Пол</td>\n" +
                "\t\t<td>Группа</td>\n" +
                "\t\t<td>Операции</td>\n" +
                "\t</tr>");
        if(!students.isEmpty()) {
            for (Student student : students) {
                if(!Objects.isNull(student)) {
                    response.getWriter().write("<tr id=\"student" + student.getId() + "\">\n");
                    response.getWriter().write("<td class=\"id\">" + student.getId() + "</td>");
                    response.getWriter().write("<td class=\"name\">" + student.getName() + "</td>");
                    response.getWriter().write("<td class=\"birth\">" + student.getBirth() + "</td>");
                    response.getWriter().write("<td class=\"gender\">" + student.getGender() + "</td>");
                    response.getWriter().write("<td class=\"group\">" + student.getGroup().getNumber() + "</td>");
                    response.getWriter().write("<td class=\"operations\">" +
                            "<a class=\"delete\" href=\"#deleteStudent" + student.getId() + "\">Удалить</a><br>" +
                            "<a class=\"update\" href=\"#updateStudent" + student.getId() + "\">Изменить</a>" +
                            "</td>");
                    response.getWriter().write("</tr>");
                }
            }
        } else {
            response.getWriter().write("<tr><td colspan=\"6\">Нет записей.</tr></td>");
        }
        studentDAO.closeEM();
    }
}
