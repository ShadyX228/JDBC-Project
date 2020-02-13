package servlets.Teacher;

import dbmodules.service.dao.GroupDAO;
import dbmodules.service.dao.TeacherDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Teacher;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static webdebugger.WebInputDebugger.setQueryParametres;

public class TeacherDeleteGroup extends HttpServlet {
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        String teacherIdString = request.getParameter("teacherId");
        String groupIdString = request.getParameter("groupId");

        JSONObject jsonObject = new JSONObject();
        List<Integer> errors = new ArrayList<>();

        if(teacherIdString.isEmpty() || groupIdString.isEmpty()) {
            errors.add(0);
        } else {
            TeacherDAO teacherDAO = new TeacherDAO();
            GroupDAO groupDAO = new GroupDAO();

            Teacher teacher = teacherDAO
                    .selectById(Integer.parseInt(teacherIdString));
            Group group = groupDAO.selectById(Integer.parseInt(groupIdString));
            if(!Objects.isNull(teacher) && !Objects.isNull(group)) {
                teacherDAO.removeTeacherFromGroup(teacher,group);
            } else {
                errors.add(-1);
            }

            teacherDAO.closeEntityManager();

        }
        jsonObject.accumulate("errors", errors);
        response.getWriter().write(jsonObject.toString());
    }
}
