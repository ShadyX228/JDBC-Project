package servlets.Group;

import dbmodules.dao.GroupDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.tables.Teacher;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import static dbmodules.types.Criteria.ID;
import static webdebugger.WebInputDebugger.*;

public class GroupGetInfo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        GroupDAO groupDAO = new GroupDAO();

        JSONObject jsonObject = new JSONObject();
        List<Integer> errors = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();
        List<String> students = new ArrayList<>();

        String idString = request.getParameter("id");


        if(Objects.isNull(idString)) {
            errors.add(0);
        } else {
            idString = parseCriteria(ID, idString);
            if(!Objects.isNull(idString)) {
                int id = Integer.parseInt(idString);
                Group group = groupDAO.selectById(id);

                if(!group.getTeachers().isEmpty()) {
                    teachers = group.getTeachers();
                }

                if(!group.getStudents().isEmpty()) {
                    for(Student student : group.getStudents()) {
                        students.add(student.getName());
                    }
                }
                groupDAO.closeEntityManager();
            }
        }

        jsonObject.accumulate("errors", errors);
        jsonObject.accumulate("students", students);
        jsonObject.accumulate("teachers", teachers);
        response.getWriter().write(jsonObject.toString());
    }
}
