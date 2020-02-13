package servlets.Teacher;

import dbmodules.service.dao.TeacherDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Teacher;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static dbmodules.types.Criteria.ID;
import static webdebugger.WebInputDebugger.*;

public class TeacherGetInfo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        TeacherDAO teacherDAO = new TeacherDAO();


        String idString = request.getParameter("id");

        JSONObject jsonObject = new JSONObject();
        List<Integer> errors = new ArrayList<>();
        Map<Integer, Integer> groups = new HashMap<>();

        if (Objects.isNull(idString)) {
            errors.add(0);
        } else {
            idString = parseCriteria(ID, idString);
            if (!Objects.isNull(idString)) {
                int id = Integer.parseInt(idString);
                Teacher teacher = teacherDAO.selectById(id);
                for(Group group : teacher.getGroups()) {
                    groups.put(group.getId(), group.getNumber());
                }

            }
        }
        teacherDAO.closeEntityManager();


        jsonObject.accumulate("errors",errors);
        jsonObject.accumulate("groups", groups);
        response.getWriter().write(jsonObject.toString());
    }
}

