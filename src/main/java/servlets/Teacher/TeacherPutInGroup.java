package servlets.Teacher;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.TeacherDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Teacher;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static webdebugger.WebInputDebugger.*;

public class TeacherPutInGroup extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        String teacherIdString = request.getParameter("teacherId");
        String groupIdString = request.getParameter("groupId");

        JSONObject jsonObject = new JSONObject();
        List<Integer> errors = new ArrayList<>();

        if(Objects.isNull(teacherIdString) || Objects.isNull(groupIdString) ) {
            errors.add(0);
        } else {
            TeacherDAO teacherDAO = new TeacherDAO();
            GroupDAO groupDAO = new GroupDAO();

            Teacher teacher = teacherDAO.selectById(Integer
                    .parseInt(teacherIdString));
            Group group = groupDAO.selectById(Integer.parseInt(groupIdString));
            if(!Objects.isNull(teacher) && !Objects.isNull(group)) {
                teacherDAO.putTeacherInGroup(teacher,group);
            } else {
                errors.add(-1);
            }

            teacherDAO.closeEntityManager();
            groupDAO.closeEntityManager();
        }
        jsonObject.accumulate("errors", errors);
        response.getWriter().write(jsonObject.toString());
    }
}
