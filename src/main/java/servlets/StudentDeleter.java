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


public class StudentDeleter extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        StudentDAO studentDAO = new StudentDAO();

        String criteriaString = request.getParameter("criteria");
        String criteriaValue = request.getParameter("criteriaValue");
        String message = "Проверяю переданные параметры... <br>Критерий: ";

        Criteria criteria = checkCriteria(criteriaString);
        if(Objects.isNull(criteria) || criteria.equals(ALL)) {
            message += criteriaString;
            if(criteria.equals(ALL)) {
                message += " - нельзя удалить всех сразу.";
            } else {
                message += " - Ошибка. " +
                        "Нет такого критерия.<br>";
            }
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
                List<Student> list = studentDAO.select(criteria, criteriaValueParsed);
                for(Student student : list) {
                    studentDAO.delete(student);
                }
                message += "<br>Удалено " + list.size() + " записей";
            }
        }

        response.getWriter().write(message);
    }
}
