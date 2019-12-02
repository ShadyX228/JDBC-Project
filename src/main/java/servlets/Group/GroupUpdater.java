package servlets.Group;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.dao.TeacherDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static webdebugger.WebInputDebugger.*;
import static webdebugger.WebInputDebugger.printMessage;

public class GroupUpdater extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String numberString = request.getParameter("number");
        String newNumberString = request.getParameter("newNumber");

        String message = "Проверяю переданные параметры...<br>";
        if(numberString.isEmpty() || newNumberString.isEmpty()) {
            message += "Статус" + printMessage(2,"Переданы пустое значение.");
        } else {
            message += "Значения не пусты " + printMessage(1, "OK.");
            message += "Номер исходной группы: " + numberString + "<br>";
            message += "Новый номер: " + numberString + "<br>";

            numberString = parseCriteria(Criteria.ID, numberString);
            newNumberString = parseCriteria(Criteria.ID, newNumberString);
            if(!Objects.isNull(numberString) && !Objects.isNull(newNumberString)) {
                message += "Статус" + printMessage(1, "OK.");
                int number = Integer.parseInt(numberString);
                int newNumber = Integer.parseInt(newNumberString);
                GroupDAO groupDAO = new GroupDAO();

                Group group = checkGroup(number,groupDAO);
                if(!Objects.isNull(group)) {
                    Group checkGroup = checkGroup(newNumber, groupDAO);
                    if(!Objects.isNull(checkGroup)) {
                        message += "Статус" + printMessage(2,"Группа с новым номером уже существует.");
                    } else {
                        message += printMessage(1,"OK.");
                        groupDAO.update(group,newNumber);
                        message += "Номер группы обновлен";
                    }
                } else {
                    message += "Статус" + printMessage(2,"Нет исходной группы с таким номером.");
                }

            } else {
                message += printMessage(2,"Ошибка. Некорректные параметры.");
            }
        }
        response.getWriter().write(message);
    }
}
