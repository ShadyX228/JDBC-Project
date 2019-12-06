package servlets.Student;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;
import servlets.Main.MainServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dbmodules.types.Criteria.*;
import static webdebugger.WebInputDebugger.*;


public class StudentDeleter extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String criteriaString = request.getParameter("criteria");
        String criteriaValue = request.getParameter("criteriaValue");
        String message = "Проверяю переданные параметры... <br>Критерий: ";

        Criteria criteria = checkCriteria(criteriaString);
        if(Objects.isNull(criteria) || criteria.equals(ALL)) {
            message += criteriaString;
            if(!Objects.isNull(criteria) && criteria.equals(ALL)) {
                message += printMessage(2,"Ошибка. Нельзя удалить всех сразу.");;
            } else {
                message += printMessage(2,"Ошибка. Нет такого критерия.");
            }
        } else {
            message += criteria + printMessage(1,"OK.");
            String criteriaValueParsed;
            if(!criteria.equals(ALL)) {
                message += "Значение критерия: " + criteriaValue;
                criteriaValueParsed = parseCriteria(criteria, criteriaValue);
            } else {
                criteriaValueParsed = "";
            }
            if(Objects.isNull(criteriaValueParsed)) {
                message += printMessage(2,"Ошибка. Неверное значение для введенного критерия.");;
            } else {
                message += printMessage(1,"OK.");
                List<Student> list = new ArrayList<>();
                StudentDAO studentDAO = new StudentDAO();
                if(criteria.equals(ID)) {
                    Student student = studentDAO.selectById(Integer.parseInt(criteriaValueParsed));
                    if(!Objects.isNull(student)) {
                        list.add(student);
                    }
                } else {
                    if(!criteriaValueParsed.isEmpty()) {
                        list = studentDAO.select(criteria, criteriaValueParsed);
                    } else {
                        message += "Статус" + printMessage(2,"Ошибка. Пустое значение критерия.");
                        response.getWriter().write(message);
                    }
                }
                for (Student student : list) {
                    studentDAO.delete(student);
                }
                response.getWriter().write("<br>Удалено " + list.size() + " записей");
                studentDAO.closeEM();
            }
        }
    }
}
