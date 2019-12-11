package servlets.Student;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import servlets.Main.MainServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
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
        groupString = parseCriteria(GROUP, groupString);

        List<Object> valuesList = new ArrayList<>();
        List<Criteria> criteriaList = new ArrayList<>();

        Set<Student> students = new HashSet<>();
        if(!Objects.isNull(idString)) {
            valuesList.add(idString); // 0 - id
            valuesList.add(null); // 1 - name
            valuesList.add(null); // 2 - birth
            valuesList.add(null); // 3 - gender
            valuesList.add(null); // 4 - group
        } else {
            valuesList.add(null); // 0 - id
            if (!name.isEmpty()) {
                valuesList.add(name); // 1 - name
            } else {
                valuesList.add(null);
            }
            valuesList.add(birthString); // 2 - birth
            if(!Objects.isNull(genderString)) {
                valuesList.add(genderString); // 3 - gender
            } else {
                valuesList.add(null);
            }
            valuesList.add(groupString); // 4 - group
        }

        int index = 0;
        StudentDAO studentDAO = new StudentDAO();
        for(Iterator<Object> iterator = valuesList.iterator();iterator.hasNext();) {
            Object criteria = iterator.next();
            if(!Objects.isNull(criteria)) {
                switch (index) {
                    case 0: { // - id
                        students.add(studentDAO.selectById(Integer.parseInt(idString)));
                        break;
                    }
                    case 1: { // name
                        students.addAll(studentDAO.select(NAME, name));
                        break;
                    }
                    case 2: { // birth
                        students.addAll(studentDAO.select(BIRTH, birthString));
                        break;
                    }
                    case 3: { // gender
                        students.addAll(studentDAO.select(GENDER, genderString));
                        break;
                    }
                    case 4: { // group
                        students.addAll(studentDAO.select(GROUP, groupString));
                        break;
                    }
                }
            }
            index++;
        }
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
        } else {
            response.getWriter().write("<tr><td colspan=\"6\">Нет записей.</tr></td>");
        }
    }
}
