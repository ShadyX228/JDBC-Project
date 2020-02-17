package servlets.Teacher;

import dbmodules.dao.TeacherDAO;
import dbmodules.entity.Teacher;
import dbmodules.service.PersonService;
import dbmodules.types.Gender;
import org.json.JSONObject;

import javax.servlet.ServletException;
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

public class TeacherUpdater extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        setQueryParametres(request,response);

        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String birthString = request.getParameter("birth");
        String genderString = request.getParameter("gender");

        JSONObject jsonObject = new JSONObject();
        List<Integer> errors = new ArrayList<>();

        idString = parseCriteria(ID, idString);
        birthString = parseCriteria(BIRTH, birthString);
        genderString = parseCriteria(GENDER, genderString);
        if((!Objects.isNull(idString)
                && !Objects.isNull(genderString)
                && !Objects.isNull(birthString))
                && (!idString.isEmpty()
                && !genderString.isEmpty()
                && !birthString.isEmpty())) {
            int id = Integer.parseInt(idString);
            Gender gender = checkGender(genderString);
            LocalDate birth = LocalDate.parse(birthString);

            PersonService<Teacher> teacherDAO = new TeacherDAO();
            Teacher teacher = teacherDAO.selectById(id);
            if(!Objects.isNull(teacher)) {
                TeacherDAO updateService = (TeacherDAO) teacherDAO;
                updateService.update(teacher, name, birth, gender);
            } else {
                errors.add(0);
            }
            teacherDAO.closeEntityManager();
        } else {
            errors.add(-1);
        }
        jsonObject.accumulate("errors", errors);

        response.getWriter().write(jsonObject.toString());
    }
}
