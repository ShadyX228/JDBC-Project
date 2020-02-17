package servlets.Student;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.entity.Group;
import dbmodules.entity.Student;
import dbmodules.service.PersonService;
import dbmodules.types.Gender;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dbmodules.types.Criteria.*;
import static webdebugger.WebInputDebugger.*;

public class StudentUpdater extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String birthString = request.getParameter("birth");
        String genderString = request.getParameter("gender");
        String groupString = request.getParameter("group");


        JSONObject jsonObject = new JSONObject();
        List<Integer> errors = new ArrayList<>();

        idString = parseCriteria(ID, idString);
        birthString = parseCriteria(BIRTH, birthString);
        genderString = parseCriteria(GENDER, genderString);
        groupString = parseCriteria(GROUP, groupString);
        if((!Objects.isNull(idString)
                && !Objects.isNull(genderString)
                && !Objects.isNull(birthString)
                && !Objects.isNull(groupString))
                && (!idString.isEmpty()
                && !genderString.isEmpty()
                && !birthString.isEmpty()
                && !groupString.isEmpty())) {
            int id = Integer.parseInt(idString);
            Gender gender = checkGender(genderString);
            LocalDate birth = LocalDate.parse(birthString);
            GroupDAO groupDAO = new GroupDAO();
            Group group = checkGroup(Integer.parseInt(groupString), groupDAO);

            if(Objects.isNull(group)) {
                errors.add(0);
            } else {
                PersonService<Student> studentDAO = new StudentDAO();
                Student student = studentDAO.selectById(id);
                StudentDAO studentUpdater = (StudentDAO) studentDAO;
                studentUpdater .update(student, name, birth, gender, group);
                studentUpdater.closeEntityManager();
                studentDAO.closeEntityManager();
            }

        } else {
            errors.add(-1);
        }
        jsonObject.accumulate("errors", errors);
        response.getWriter().write(jsonObject.toString());
    }
}