package servlets.Teacher;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.dao.TeacherDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.tables.Teacher;
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

public class TeacherAdder extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String birth = request.getParameter("birth");
        String gender = request.getParameter("gender");

        String message = "Проверяю переданные параметры...<br>";
        if (name.isEmpty()
                || birth.isEmpty()
                || gender.isEmpty()) {
            message += printMessage(2,"Переданы пустые значения.");
            response.getWriter().write(message);
        } else {
            boolean check = true;
            message += "Значения не пустые " + printMessage(1, "OK.") +
                    "Проверяю корректность введенных параметров...<br>";
            message += "Имя: " + name + printMessage(1, "OK.");

            message += "День рождения: " + birth;
            LocalDate birthday = LocalDate.of(2000, 01, 01);
            try {
                birthday = LocalDate.parse(birth);
                message += printMessage(1, "OK.");
            } catch (DateTimeParseException | InputMismatchException e) {
                message += printMessage(2, "Ошибка: некорректный ввод.");
                check = false;
            }

            message += "Пол: " + gender;
            Gender genderParsed = checkGender(gender);
            if (Objects.isNull(genderParsed)) {
                message += printMessage(2, "Ошибка: некорректный ввод.");
                check = false;
            } else {
                message += printMessage(1, "OK.");
            }

            message += "Пытаюсь добавить... ";
            if (!check) {
                message += printMessage(2, "Ошибка. Одно или несколько полей не прошли проверку.");
                response.getWriter().write(message);
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
                teacherDAO.closeEM();
                response.getWriter().write("Запись <span class=\"lastId\">" + teacher.getId() + "</span> добавлена.");
            }
        }
    }
}
