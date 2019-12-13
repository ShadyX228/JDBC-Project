package servlets.Group;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static dbmodules.types.Criteria.*;
import static dbmodules.types.Criteria.GROUP;
import static webdebugger.WebInputDebugger.*;

public class GroupSelector extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");


        String groupString = request.getParameter("group");

        groupString = parseCriteria(ID, groupString);

        if(!Objects.isNull(groupString)) {
            response.getWriter().write("\t<tr>\n" +
                    "\t\t<td>ID</td>\n" +
                    "\t\t<td>Группа</td>\n" +
                    "\t\t<td>Операции</td>\n" +
                    "\t</tr>");
            GroupDAO groupDAO = new GroupDAO();
            Group group = checkGroup(Integer.parseInt(groupString), groupDAO);
            if(!Objects.isNull(group)) {
                response.getWriter().write("<tr id=\"group" + group.getId() + "\">\n");
                response.getWriter().write("<td class=\"id\">" + group.getId() + "</td>");
                response.getWriter().write("<td class=\"group\">" + group.getNumber() + "</td>");
                response.getWriter().write("<td class=\"operations\">" +
                        "<a class=\"delete\" href=\"#deleteGroup" + group.getId() + "\">Удалить</a><br>" +
                        "<a class=\"update\" href=\"#updateGroup" + group.getId() + "\">Изменить</a><br>" +
                        "<a class=\"getInfo\" href=\"#getInfoGroup" + group.getId() + "\">Информация</a><br>" +
                        "<a class=\"putTeacherInGroup\" href=\"#putTeacherInGroup" + group.getId() + "\">Назначить преподавателя</a>" +
                        "</td>");
                response.getWriter().write("</tr>");
            } else {
                response.getWriter().write("<tr><td colspan=\"3\">Нет записей: <span class=\"error\">-1</span></tr></td>");
            }

            groupDAO.closeEM();
        } else {
            response.getWriter().write("\t<tr>\n" +
                    "\t\t<td>ID</td>\n" +
                    "\t\t<td>Группа</td>\n" +
                    "\t\t<td>Операции</td>\n" +
                    "\t</tr>");
            response.getWriter().write("<tr><td colspan=\"3\">Нет записей: <span class=\"error\">-1</span></tr></td>");

        }

    }
}
