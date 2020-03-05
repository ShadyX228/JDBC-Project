package servlets.Teacher;

import dbmodules.dao.GroupDAOimpl;
import dbmodules.dao.TeacherDAOimpl;
import dbmodules.entity.Group;
import dbmodules.entity.Teacher;
import dbmodules.daointerfaces.GroupDAO;
import dbmodules.daointerfaces.TeacherDAO;
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
        List<String> errors = new ArrayList<>();

        if(teacherIdString.isEmpty() || groupIdString.isEmpty()) {
            errors.add("Переданы пустые значения.");
        } else {
            TeacherDAO teacherDAO = new TeacherDAOimpl();
            GroupDAO groupDAO = new GroupDAOimpl();

            Teacher teacher = teacherDAO
                    .selectById(Integer.parseInt(teacherIdString));
            Group group = groupDAO.selectById(Integer.parseInt(groupIdString));
            if(!Objects.isNull(teacher) && !Objects.isNull(group)) {
                teacherDAO.removeTeacherFromGroup(teacher,group);
            } else {
                errors.add("Внутреняя ошибка.");
            }

        }
        jsonObject.accumulate("errors", errors);
        response.getWriter().write(jsonObject.toString());
        System.out.println(errors);
    }
}
