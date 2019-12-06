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
        String message = "Проверяю переданные параметры...<br> ";

        idString = parseCriteria(ID, idString);
        birthString = parseCriteria(BIRTH, birthString);
        genderString = parseCriteria(GENDER, genderString);
        groupString = parseCriteria(GROUP, groupString);

        List<Object> checkList = new ArrayList<>();
        Set<Student> students = new HashSet<>();
        if(!Objects.isNull(idString)) {
            checkList.add(idString); // 0 - id
            checkList.add(null); // 1 - name
            checkList.add(null); // 2 - birth
            checkList.add(null); // 3 - gender
            checkList.add(null); // 4 - group
        } else {
            checkList.add(null); // 0 - id
            if (!name.isEmpty()) {
                checkList.add(name); // 1 - name
            } else {
                checkList.add(null);
            }
            checkList.add(birthString); // 2 - birth
            checkList.add(genderString); // 3 - gender
            checkList.add(groupString); // 4 - group
        }

        int index = 0;
        StudentDAO studentDAO = new StudentDAO();
        for(Iterator<Object> iterator = checkList.iterator();iterator.hasNext();) {
            Object object = iterator.next();
            //response.getWriter().write(object + " ");
            if(!Objects.isNull(object)) {
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
        if(!students.isEmpty()) {
            response.getWriter().write("\t<tr>\n" +
                    "\t\t<td>ID</td>\n" +
                    "\t\t<td>ФИО</td>\n" +
                    "\t\t<td>День рождения</td>\n" +
                    "\t\t<td>Пол</td>\n" +
                    "\t\t<td>Группа</td>\n" +
                    "\t\t<td>Операции</td>\n" +
                    "\t</tr>");
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
            response.getWriter().write("Нет записей.");
        }
       /* if((!Objects.isNull(idString)
                || !Objects.isNull(genderString)
                || !Objects.isNull(birthString)
                || !Objects.isNull(groupString))
                && (!idString.isEmpty()
                || !genderString.isEmpty()
                || !birthString.isEmpty()
                || !groupString.isEmpty())) {

            //StudentDAO studentDAO = new StudentDAO();
            //Student student = studentDAO.selectById(id);
            //studentDAO.update(student, name, birth, gender, group);
            //studentDAO.closeEM();
            //groupDAO.closeEM();
            //response.getWriter().write("Запись <span class=\"lastId\">" + student.getId()  + "</span> обновлена.");
        }  else {
            message += "Переданы пустые параметры либо они некорректны: <span class=\"error\">-1</span>";
            response.getWriter().write(message);
        }
        response.getWriter().write(message);
        if(!list.isEmpty()) {
            response.getWriter().write("<table border=1>\n" +
                    "\t<tr>\n" +
                    "\t\t<td>ID</td>\n" +
                    "\t\t<td>ФИО</td>\n" +
                    "\t\t<td>День рождения</td>\n" +
                    "\t\t<td>Пол</td>\n" +
                    "\t\t<td>Группа</td>\n" +
                    "\t</tr>");
            for (Student student : list) {
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + student.getId() + "</td>");
                response.getWriter().write("<td>" + student.getName() + "</td>");
                response.getWriter().write("<td>" + student.getBirth() + "</td>");
                response.getWriter().write("<td>" + student.getGender() + "</td>");
                response.getWriter().write("<td>" + student.getGroup().getNumber() + "</td>");
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</table>");
        } else {
            response.getWriter().write("Нет записей.");
        }*/
    }
}
