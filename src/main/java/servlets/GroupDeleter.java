package servlets;

import dbmodules.dao.GroupDAO;
import dbmodules.tables.Group;
import dbmodules.types.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static webdebugger.WebInputDebugger.*;

public class GroupDeleter extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String numberString = request.getParameter("number");

        String message = "Проверяю переданные параметры...<br>";
        if(numberString.isEmpty()) {
            message += "Статус" + printMessage(2,"Передано пустое значение.");
        } else {
            message += "Значение не пусто " + printMessage(1, "OK.");
            message += "Номер группы: " + numberString;

            numberString = parseCriteria(Criteria.ID, numberString);
            if(!Objects.isNull(numberString)) {
                message += printMessage(1, "OK.");
                int number = Integer.parseInt(numberString);
                GroupDAO groupDAO = new GroupDAO();
                Group group = checkGroup(number,groupDAO);
                if(!Objects.isNull(group)) {
                    groupDAO.delete(group);
                    message += "Запись удалена.";
                } else {
                    message += printMessage(2,"Нет группы с таким номером.");
                }

            } else {
                message += printMessage(2,"Ошибка. Некорректный номер");
            }
        }
        response.getWriter().write(message);
    }
}
