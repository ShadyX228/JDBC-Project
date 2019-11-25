package servlets;

import dbmodules.dao.GroupDAO;
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
            message += criteriaString + " - Ошибка. " +
                    "Нет такого критерия.<br>";
        } else {
            message += criteria + " - OK.<br>";
            String criteriaValueParsed;
            if(!criteria.equals(ALL)) {
                message += "Значение критерия: " + criteriaValue + " - ";
                criteriaValueParsed = parseCriteria(criteria, criteriaValue, new GroupDAO());
            } else {
                criteriaValueParsed = "";
            }
            if(Objects.isNull(criteriaValueParsed) ) {
                message += "Ошибка. " +
                        "Неверное значение для введенного критерия.";

            } else {
                message += "OK.";
                list = studentDAO.select(criteria, criteriaValueParsed);
            }
        }

        response.getWriter().write(message);
        response.getWriter().write("<table border=1>\n" +
                "\t<tr>\n" +
                "\t\t<td>ID</td>\n" +
                "\t\t<td>ФИО</td>\n" +
                "\t\t<td>День рождения</td>\n" +
                "\t\t<td>Пол</td>\n" +
                "\t\t<td>Группа</td>\n" +
                "\t</tr>");
        for(Student student : list) {
            response.getWriter().write("<tr>");
            response.getWriter().write("<td>" + student.getId() + "</td>");
            response.getWriter().write("<td>" + student.getName() + "</td>");
            response.getWriter().write("<td>" + student.getBirth() + "</td>");
            response.getWriter().write("<td>" + student.getGender() + "</td>");
            response.getWriter().write("<td>" + student.getGroup().getNumber() + "</td>");
            response.getWriter().write("</tr>");
        }
        response.getWriter().write("</table>");
    }
}
