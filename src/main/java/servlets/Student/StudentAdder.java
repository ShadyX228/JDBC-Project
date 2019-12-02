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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

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
        } else {
            boolean check = true;
            message += "Значения не пустые " + printMessage(1, "OK.") +
                    "Проверяю корректность введенных параметров...<br>";
            message += "Имя: " + name +  printMessage(1, "OK.");

            message += "День рождения: " + birth;
            LocalDate birthday = LocalDate.of(2000,01,01);
            try {
                birthday = LocalDate.parse(birth);
                message +=  printMessage(1, "OK.");;
            } catch (DateTimeParseException | InputMismatchException e) {
                message +=  printMessage(2, "Ошибка: некорректный ввод.");;
                check = false;
            }

            message += "Пол: " + gender;
            Gender genderParsed = checkGender(gender);
            if(Objects.isNull(genderParsed)) {
                message += printMessage(2, "Ошибка: некорректный ввод.");
                check = false;
            } else {
                message += printMessage(1, "OK.");
            }

            message += "Группа: " + group;
            int number;
            Group groupObject = new Group(0);
            try {
                number = Integer.parseInt(group);
                groupObject = checkGroup(number, new GroupDAO());
                if(Objects.isNull(groupObject)) {
                    message += printMessage(2, "Ошибка: такой группы нет.");

                    check = false;
                } else {
                    message += printMessage(1, "OK.");
                }
            } catch (NumberFormatException e) {
                message += printMessage(2, "Ошибка ввода.");
                check = false;
            }

            message += "Пытаюсь добавить... ";
            if(!check) {
                message += printMessage(2,"Ошибка. Одно или несколько полей не прошли проверку.");
            } else {
                StudentDAO studentDAO = new StudentDAO();
                studentDAO.add(new Student(
                        name,
                        birthday.getYear(),
                        birthday.getMonthValue(),
                        birthday.getDayOfMonth(),
                        genderParsed,
                        groupObject
                ));
                message += printMessage(1,"Запись добавлена.");
            }


        }
        response.getWriter().write(message);
    }
}
