package servlets.Group;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Objects;

import static webdebugger.WebInputDebugger.*;

public class GroupAdder extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String group = request.getParameter("group");

        String message = "Проверяю переданные параметры...<br>";
        if (group.isEmpty()) {
            message += printMessage(2, "Переданы пустые значения.");
            response.getWriter().write(message);
        } else {
            boolean check = true;
            message += "Значения не пустые " + printMessage(1, "OK.") +
                    "Проверяю корректность введенных параметров...<br>";

            message += "Группа: " + group;
            int number = 0;
            GroupDAO groupDAO = new GroupDAO();
            try {
                number = Integer.parseInt(group);
                Group groupObject = checkGroup(number, groupDAO);
                if (!Objects.isNull(groupObject)) {
                    message += printMessage(2, "Ошибка: такая группа уже сущесвует.");
                    check = false;
                } else {
                    message += printMessage(1, "OK.");
                }
            } catch (NumberFormatException e) {
                message += printMessage(2, "Ошибка ввода.");
                check = false;
            }

            message += "Пытаюсь добавить... ";
            if (!check) {
                message += printMessage(2, "Ошибка. Одно или несколько полей не прошли проверку.");
                response.getWriter().write(message);
            } else {
                Group newGroup = new Group(
                        number
                );
                groupDAO.add(newGroup);
                response.getWriter().write("Запись <span class=\"lastId\">" + newGroup.getId() + "</span> добавлена.");
                groupDAO.closeEM();
            }
        }
    }
}
