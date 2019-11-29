package servlets.Teacher;

import dbmodules.dao.StudentDAO;
import dbmodules.dao.TeacherDAO;
import dbmodules.tables.Student;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static dbmodules.types.Criteria.ALL;
import static dbmodules.types.Criteria.ID;
import static webdebugger.WebInputDebugger.*;
import static webdebugger.WebInputDebugger.printMessage;

public class TeacherDeleter extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        TeacherDAO teacherDAO = new TeacherDAO();

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
                List<Teacher> list = new ArrayList<>();
                if(criteria.equals(ID)) {
                    Teacher teacher = teacherDAO.selectById(Integer.parseInt(criteriaValueParsed));
                    if(!Objects.isNull(teacher)) {
                        list.add(teacher);
                    }
                } else {
                    if(!criteriaValueParsed.isEmpty()) {
                        list = teacherDAO.select(criteria, criteriaValueParsed);
                    } else {
                        message += "Статус" + printMessage(2,"Ошибка. Пустое значение критерия.");
                    }
                }
                for (Iterator<Teacher> iterator = list.iterator();
                     iterator.hasNext();) {
                    Teacher teacher = iterator.next();
                    if(teacher.getGroups().isEmpty()) {
                        teacherDAO.delete(teacher);
                    } else {
                        iterator.remove();
                    }
                }
                message += "<br>Удалено " + list.size() + " записей";
            }
        }

        response.getWriter().write(message);
    }
}
