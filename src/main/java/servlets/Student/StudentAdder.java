package servlets.Student;

import dbmodules.service.dao.GroupDAO;
import dbmodules.service.dao.StudentDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
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


public class StudentAdder extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        String name = request.getParameter("name");
        String birth = request.getParameter("birth");
        String gender = request.getParameter("gender");
        String group = request.getParameter("group");

        JSONObject jsonObject = new JSONObject();
        List<Integer> errors = new ArrayList<>();

        if (name.isEmpty()
                || birth.isEmpty()
                || gender.isEmpty()
                || group.isEmpty()) {
            errors.add(0);
        } else {
            boolean check = true;

            LocalDate birthday = LocalDate.of(2000,01,01);
            try {
                birthday = LocalDate.parse(birth);

            } catch (DateTimeParseException | InputMismatchException e) {
                errors.add(-1);
                check = false;
            }


            Gender genderParsed = checkGender(gender);
            if(Objects.isNull(genderParsed)) {
                errors.add(-2);
                check = false;
            }

            int number;
            Group groupObject = new Group(0);
            GroupDAO groupDAO = new GroupDAO();
            try {
                number = Integer.parseInt(group);
                groupObject = checkGroup(number, groupDAO);
                if(Objects.isNull(groupObject)) {
                    errors.add(-3);
                    check = false;
                }
            } catch (NumberFormatException e) {
                errors.add(-4);
                check = false;
            }

            if(!check) {
                errors.add(-5);
            } else {
                Student student = new Student(
                        name,
                        birthday.getYear(),
                        birthday.getMonthValue(),
                        birthday.getDayOfMonth(),
                        genderParsed,
                        groupObject
                );
                StudentDAO studentDAO = new StudentDAO();
                studentDAO.add(student);
                studentDAO.closeEntityManager();
                jsonObject.put("lastId", student.getId());
                jsonObject.put("group", student.getGroup().getNumber());
            }
        }
        jsonObject.accumulate("errors", errors);
        response.getWriter().write(jsonObject.toString());
    }
}