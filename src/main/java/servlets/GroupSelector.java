package servlets;

import dbmodules.dao.GroupDAO;
import dbmodules.tables.Group;
import dbmodules.types.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static webdebugger.WebInputDebugger.*;

public class GroupSelector extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");


        GroupDAO groupDAO = new GroupDAO();
        List<Group> list = new ArrayList<>();

        String numberString = request.getParameter("number");
        String checkAll = request.getParameter("checkAll");
        String message = "Проверяю переданные параметры...<br>";


        if(checkAll.equals("false")) {
            message += "Выбираю все группы...";
            list = groupDAO.selectAll();
        } else if(checkAll.equals("true")) {
            message += "Выбираю группу " + numberString;
            numberString = parseCriteria(Criteria.ID, numberString);
            if(!Objects.isNull(numberString)) {
                message += printMessage(1, "OK.");
                int number = Integer.parseInt(numberString);
                Group group = checkGroup(number, groupDAO);
                if(!Objects.isNull(group)) {
                    list.add(group);
                }
            } else {
                message += printMessage(2,"Ошибка. Некорректный номер");
            }
        } else {
            message += "Переданы некорректные параметры.";
        }

        response.getWriter().write(message);
        if(!list.isEmpty()) {
            response.getWriter().write("<table border=1>\n" +
                    "\t<tr>\n" +
                    "\t\t<td>ID</td>\n" +
                    "\t\t<td>Номер</td>\n");
            for (Group group : list) {
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + group.getId() + "</td>");
                response.getWriter().write("<td>" + group.getNumber() + "</td>");
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</table>");
        } else {
            response.getWriter().write("Нет записей.");
        }
    }
}
