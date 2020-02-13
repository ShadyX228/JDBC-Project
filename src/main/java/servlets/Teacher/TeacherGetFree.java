package servlets.Teacher;

import dbmodules.service.dao.TeacherDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;
import org.json.JSONObject;

import static webdebugger.WebInputDebugger.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class TeacherGetFree extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        setQueryParametres(request,response);

        String groupIdString = request.getParameter("id");

        groupIdString = parseCriteria(Criteria.ID, groupIdString);

        JSONObject jsonObject = new JSONObject();
        List<Integer> errors = new ArrayList<>();

        if(!Objects.isNull(groupIdString)) {
            int groupId = Integer.parseInt(groupIdString);
            TeacherDAO teacherDAO = new TeacherDAO();
            List<Teacher> teachers = teacherDAO.select(Criteria.ALL,"");
            List<Teacher> freeTeachers = new ArrayList<>();
            boolean check = true;
            for(Teacher teacher : teachers) {
                System.out.println(teacher);
                if(!teacher.getGroups().isEmpty()) {
                    for (Group group : teacher.getGroups()) {
                        if (group.getId() == groupId) {
                            check = false;
                            System.out.println(group);
                            break;
                        }
                    }
                }
                if(check) {
                    freeTeachers.add(teacher);
                }
                check = true;
            }
            if(freeTeachers.isEmpty()) {
                errors.add(0);
            } else {
                jsonObject.accumulate("teachers", freeTeachers);
            }
            teacherDAO.closeEntityManager();
        }
        jsonObject.accumulate("errors", errors);
        response.getWriter().write(jsonObject.toString());
    }
}
