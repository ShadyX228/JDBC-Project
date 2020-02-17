package servlets.Teacher;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.TeacherDAO;
import dbmodules.entity.Group;
import dbmodules.entity.Teacher;
import dbmodules.service.GroupService;
import dbmodules.service.PersonService;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static webdebugger.WebInputDebugger.setQueryParametres;

public class TeacherPutInGroupByNumber extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        String teacherIdString = request.getParameter("teacherId");
        String groupNumberString = request.getParameter("groupNumber");

        JSONObject jsonObject = new JSONObject();
        List<Integer> errors = new ArrayList<>();


        if(Objects.isNull(teacherIdString)
                || Objects.isNull(groupNumberString) ) {
            errors.add(0);
        } else {
            PersonService<Teacher> teacherDAO = new TeacherDAO();
            GroupService groupDAO = new GroupDAO();

            Teacher teacher = teacherDAO.selectById(Integer
                    .parseInt(teacherIdString));
            Group group = groupDAO.select(Integer.parseInt(groupNumberString));
            if(!Objects.isNull(teacher) && !Objects.isNull(group)) {
                TeacherDAO putService = (TeacherDAO) teacherDAO;
                putService.putTeacherInGroup(teacher,group);

                int id = group.getId();
                int number = group.getNumber();

                jsonObject.accumulate("errors",errors);
                jsonObject.put("groupId", id);
                jsonObject.put("groupNumber", number);
                putService.closeEntityManager();
            } else {
                errors.add(-1);
            }

            teacherDAO.closeEntityManager();
            groupDAO.closeEntityManager();
            response.getWriter().write(jsonObject.toString());
        }

    }
}
