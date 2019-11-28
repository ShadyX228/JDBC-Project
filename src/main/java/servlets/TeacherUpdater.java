package servlets;

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
import java.util.Objects;

import static dbmodules.types.Criteria.ALL;
import static dbmodules.types.Criteria.ID;
import static webdebugger.WebInputDebugger.*;
import static webdebugger.WebInputDebugger.printMessage;

public class TeacherUpdater extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String idString = request.getParameter("id");
        String criteriaString = request.getParameter("criteria");
        String criteriaValue = request.getParameter("criteriaValue");
        String message = "Проверяю переданные параметры... <br>ID: " + idString;

        TeacherDAO teacherDAO = new TeacherDAO();

        String idParsed = parseCriteria(ID, idString);
        if(!Objects.isNull(idParsed)) {
            Teacher teacher = teacherDAO.selectById(Integer.parseInt(idParsed));
            if (Objects.isNull(teacher)) {
                message += printMessage(2, "Ошибка. Нет студента с таким ID или ID введен не верно.");
                ;
            } else {
                message += printMessage(1, "OK.");
                message += "Поле, которое надо обновить: " + criteriaString;
                Criteria criteria = checkCriteria(criteriaString);
                if (Objects.isNull(criteria)
                        || criteria.equals(ALL)
                        || criteria.equals(ID)) {
                    message += printMessage(2, "Ошибка. Нет такого поля.");
                } else {
                    message += printMessage(1, "OK.");
                    message += "Новое значение: " + criteriaValue;
                    String criteriaValueParsed = parseCriteria(criteria, criteriaValue);

                    if (Objects.isNull(criteriaValueParsed)) {
                        message += printMessage(2, "Ошибка. Неверное значение для введенного поля.");
                    } else {
                        message += printMessage(1, "OK.");
                        teacherDAO.update(teacher, criteria, criteriaValueParsed);
                        message += "Запись обновлена.";
                    }
                }
            }
        } else {
            message += printMessage(2,"Некорректно введенный id.");
        }
        response.getWriter().write(message);
    }
}
