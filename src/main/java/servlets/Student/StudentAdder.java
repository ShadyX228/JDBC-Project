package servlets.Student;

import dbmodules.dao.GroupDAOimpl;
import dbmodules.dao.StudentDAOimpl;
import dbmodules.entity.Group;
import dbmodules.entity.Student;
import dbmodules.daointerfaces.GroupDAO;
import dbmodules.daointerfaces.PersonDAO;
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
        List<String> errors = new ArrayList<>();

        if (name.isEmpty()
                || birth.isEmpty()
                || gender.isEmpty()
                || group.isEmpty()) {
            errors.add("Переданы пустые значения.");
        } else {
            boolean check = true;

            LocalDate birthday = LocalDate.of(2000,01,01);
            try {
                birthday = LocalDate.parse(birth);

            } catch (DateTimeParseException | InputMismatchException e) {
                errors.add("Дата имеет некорректный формат.");
                check = false;
                e.printStackTrace();
            }


            Gender genderParsed = checkGender(gender);
            if(Objects.isNull(genderParsed)) {
                errors.add("Пол имеет некорректный формат");
                check = false;
            }

            int number;
            Group groupObject = new Group(0);
            GroupDAO groupDAO = new GroupDAOimpl();
            try {
                number = Integer.parseInt(group);
                groupObject = checkGroup(number, groupDAO);
                if(Objects.isNull(groupObject)) {
                    errors.add("Группы с указанным номером нет.");
                    check = false;
                }
            } catch (NumberFormatException e) {
                errors.add("Номер группы некорректный.");
                check = false;
                e.printStackTrace();
            }

            if(!check) {
                errors.add("Внутренняя ошибка.");
            } else {
                Student student = new Student(
                        name,
                        birthday.getYear(),
                        birthday.getMonthValue(),
                        birthday.getDayOfMonth(),
                        genderParsed,
                        groupObject
                );
                PersonDAO<Student> studentDAO = new StudentDAOimpl();
                studentDAO.add(student);
                studentDAO.closeEntityManager();
                groupDAO.closeEntityManager();
                jsonObject.put("lastId", student.getId());
                jsonObject.put("group", student.getGroup().getNumber());
            }
        }
        jsonObject.accumulate("errors", errors);
        response.getWriter().write(jsonObject.toString());
        System.out.println(errors);
    }
}