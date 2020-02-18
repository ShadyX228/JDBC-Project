package servlets.Group;

import dbmodules.dao.GroupDAOimpl;
import dbmodules.entity.Group;
import dbmodules.daointerfaces.GroupDAO;
import org.json.JSONObject;

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
        GroupDAO groupDAO = new GroupDAOimpl();
        JSONObject jsonObject = new JSONObject();
        List<String> errors = new ArrayList<>();

        if(!Objects.isNull(groupString)) {
            List<Group> groups = new ArrayList<>();
            Group group = checkGroup(Integer.parseInt(groupString), groupDAO);
            if(!Objects.isNull(group)) {
                groups.add(group);
                jsonObject.accumulate("groups", groups);
            } else {
                errors.add("Группы не существует.");
            }
            groupDAO.closeEntityManager();
        } else {
            errors.add("Переданы пустые значения.");
        }
        jsonObject.accumulate("errors", errors);
        response.getWriter().write(jsonObject.toString());
        System.out.println(errors);
    }

}
