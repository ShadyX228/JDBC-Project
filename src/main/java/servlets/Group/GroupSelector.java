package servlets.Group;

import dbmodules.dao.GroupDAO;
import dbmodules.tables.Group;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static dbmodules.types.Criteria.*;
import static webdebugger.WebInputDebugger.*;

public class GroupSelector extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);


        String groupString = request.getParameter("group");

        groupString = parseCriteria(ID, groupString);
        GroupDAO groupDAO = new GroupDAO();

        if(!Objects.isNull(groupString)) {
            List<Group> list = new ArrayList<>();
            Group group = checkGroup(Integer.parseInt(groupString), groupDAO);
            if(!Objects.isNull(group)) {
                list.add(group);
                response.getWriter().write(generateGroupTable(list));
            } else {
                generateGroupsNullTable(response);
            }
            groupDAO.closeEntityManager();
        } else {
            generateGroupsNullTable(response);
        }

    }
    private void generateGroupsNullTable(HttpServletResponse response)
            throws IOException {
        response.getWriter().write("\t<tr>\n" +
                "\t\t<td>ID</td>\n" +
                "\t\t<td>Группа</td>\n" +
                "\t\t<td>Операции</td>\n" +
                "\t</tr>");
        response.getWriter().write("<tr><td colspan=\"3\">Нет записей." +
                " <span class=\"error\">-1</span></tr></td>");
    }
}
