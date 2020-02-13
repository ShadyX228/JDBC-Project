package servlets.Teacher;

import dbmodules.service.dao.TeacherDAO;
import dbmodules.tables.Teacher;
import dbmodules.types.Gender;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;

import static webdebugger.WebInputDebugger.*;

public class TeacherAdder extends HttpServlet {
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        String name = request.getParameter("name");
        String birth = request.getParameter("birth");
        String gender = request.getParameter("gender");

        JSONObject jsonObject = new JSONObject();
        List<Integer> errors = new ArrayList<>();

        if (name.isEmpty()
                || birth.isEmpty()
                || gender.isEmpty()) {
            errors.add(0);
        } else {
            boolean check = true;

            LocalDate birthday = LocalDate.of(2000, 01, 01);
            try {
                birthday = LocalDate.parse(birth);
            } catch (DateTimeParseException | InputMismatchException e) {
                errors.add(-1);
                check = false;
            }

            Gender genderParsed = checkGender(gender);
            if (Objects.isNull(genderParsed)) {
                errors.add(-2);
                check = false;
            }

            if (!check) {
                errors.add(-3);
            } else {
                Teacher teacher = new Teacher(
                        name,
                        birthday.getYear(),
                        birthday.getMonthValue(),
                        birthday.getDayOfMonth(),
                        genderParsed
                );
                TeacherDAO teacherDAO = new TeacherDAO();
                teacherDAO.add(teacher);
                teacherDAO.closeEntityManager();
                jsonObject.put("lastId", teacher.getId());
            }
        }
        jsonObject.accumulate("errors", errors);
        response.getWriter().write(jsonObject.toString());
    }
}
