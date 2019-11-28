package servlets;

import dbmodules.dao.StudentDAO;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dbmodules.types.Criteria.ALL;
import static dbmodules.types.Criteria.ID;
import static webdebugger.WebInputDebugger.*;


public class StudentSelector extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        List<Student> list = new ArrayList<>();

        StudentDAO studentDAO = new StudentDAO();

        String criteriaString = request.getParameter("criteria");
        String criteriaValue = request.getParameter("criteriaValue");
        String message = "Проверяю переданные параметры... <br>Критерий: ";

        Criteria criteria = checkCriteria(criteriaString);
        if(Objects.isNull(criteria)) {
            message += criteriaString
                    + printMessage(2, "Ошибка. Нет такого критерия.");
        } else {
            message += criteria + printMessage(1,"OK.");
            String criteriaValueParsed;
            if(!criteria.equals(ALL)) {
                criteriaValueParsed = parseCriteria(criteria, criteriaValue);
            } else {
                criteriaValueParsed = "";
            }
            message += "Статус ";
            if(Objects.isNull(criteriaValueParsed) ) {
                message += printMessage(2,
                        "Ошибка. Неверное значение для введенного критерия.");

            } else {
                if(!criteria.equals(ALL)) {
                    message += printMessage(1, "OK.");
                }
                if(criteria.equals(ID)) {
                    Student student = studentDAO
                            .selectById(Integer
                                    .parseInt(criteriaValueParsed));
                    if(!Objects.isNull(student)) {
                        list.add(student);
                    }
                } else {
                    list = studentDAO.select(criteria, criteriaValueParsed);
                }
            }
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
        }
    }
}
