package servlets.Group;

import dbmodules.dao.GroupDAO;
import dbmodules.tables.Group;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;

import static webdebugger.WebInputDebugger.setQueryParametres;

public class GroupAllSelector extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);
        JSONObject jsonObject = new JSONObject();
        GroupDAO groupDAO = new GroupDAO();
        List<Group> groups = groupDAO.selectAll();
        List<Integer> errors = new ArrayList<>();
        if(!groups.isEmpty()) {
            jsonObject.accumulate("groups", groups);
        } else {
            errors.add(0);
        }
        jsonObject.accumulate("errors",errors);
        groupDAO.closeEntityManager();
        response.getWriter().write(jsonObject.toString());
    }
}
