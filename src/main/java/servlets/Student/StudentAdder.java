package servlets.Student;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.types.Gender;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
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

        String message = "Проверяю переданные параметры...<br>";
        if (name.isEmpty()
                || birth.isEmpty()
                || gender.isEmpty()
                || group.isEmpty()) {
            message += printMessage(2,"Переданы пустые значения.");
            response.getWriter().write(message);
        } else {
            boolean check = true;
            message += "Значения не пустые " + printMessage(1, "OK.");
            message += "Имя: " + name + printMessage(1, " - OK.");

            message += "День рождения: " + birth;
            LocalDate birthday = LocalDate.of(2000,01,01);
            try {
                birthday = LocalDate.parse(birth);
                message +=  printMessage(1, " - OK.");
            } catch (DateTimeParseException | InputMismatchException e) {
                message +=  printMessage(2, " - Ошибка: некорректный ввод.");
                check = false;
            }

            message += "Пол: " + gender;
            Gender genderParsed = checkGender(gender);
            if(Objects.isNull(genderParsed)) {
                message += printMessage(2, " - Ошибка: некорректный ввод.");
                check = false;
            } else {
                message += printMessage(1, " - OK.");
            }

            message += "Группа: " + group;
            int number;
            Group groupObject = new Group(0);
            GroupDAO groupDAO = new GroupDAO();
            try {
                number = Integer.parseInt(group);
                groupObject = checkGroup(number, groupDAO);
                if(Objects.isNull(groupObject)) {
                    message += printMessage(2, " - Ошибка: такой группы нет.");

                    check = false;
                } else {
                    message += printMessage(1, " - OK.");
                }
            } catch (NumberFormatException e) {
                message += printMessage(2, " - Ошибка ввода.");
                check = false;
            }

            message += "Пытаюсь добавить... ";
            if(!check) {
                message += printMessage(2,"Ошибка. " +
                        "Одно или несколько полей не прошли проверку.");
                response.getWriter().write(message);
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
                response.getWriter().write("Запись <span class=\"lastId\">"
                        + student.getId()  + "</span> добавлена.");
                groupDAO.closeEntityManager();
            }
        }
    }
}