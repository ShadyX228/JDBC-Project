package servlets.Group;

import dbmodules.dao.GroupDAO;
import dbmodules.tables.Group;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static webdebugger.WebInputDebugger.generateGroupTable;
import static webdebugger.WebInputDebugger.setQueryParametres;

public class GroupAllSelector extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);
        GroupDAO groupDAO = new GroupDAO();
        List<Group> list = groupDAO.selectAll();
        response.getWriter().write(generateGroupTable(list));
        groupDAO.closeEntityManager();
    }
}
