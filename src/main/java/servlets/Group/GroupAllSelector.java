package servlets.Group;

import dbmodules.service.dao.GroupDAO;
import dbmodules.tables.Group;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static webdebugger.WebInputDebugger.setQueryParametres;

public class GroupAllSelector extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);
        JSONObject jsonObject = new JSONObject();
        GroupDAO groupDAO = new GroupDAO();
        //String draw = request.getParameter("draw");

        List<Group> groups = groupDAO.selectAll();
        List<Integer> errors = new ArrayList<>();
        if(!groups.isEmpty()) {
            jsonObject.accumulate("groups", groups);
        } else {
            errors.add(0);
        }

        jsonObject.accumulate("errors", errors);
        //jsonObject.append("draw", draw);
        //jsonObject.append("recordsTotal", groups.size());
        //jsonObject.append("recordsFiltered", groups.size());

        response.getWriter().write(jsonObject.toString());
    }

}
