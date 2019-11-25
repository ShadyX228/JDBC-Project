package servlets;

import dbmodules.dao.GroupDAO;
import dbmodules.tables.Group;
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
            message += "Переданы пустные значения.";
        } else {
            boolean check = true;
            message += "Значение не пустые. ОК.<br>" +
                    "Проверяю корректность введенных параметров...<br>";
            message += "Имя: " + name + " - OK.<br>";

            message += "День рождения: " + birth;
            LocalDate birthday;
            try {
                birthday = LocalDate.parse(birth);
                message += " - OK.<br>";
            } catch (DateTimeParseException e) {
                message += " - Ошибка: некорректный ввод.<br>";
            } catch (InputMismatchException e) {
                message += " - Ошибка: некорректный ввод.<br>";
                check = false;
            }

            message += "Пол: " + gender;
            Gender genderParsed = checkGender(gender);
            if(Objects.isNull(genderParsed)) {
                message += " - Ошибка: некорректный ввод.<br>";
                check = false;
            } else {
                message += " - OK.<br>";
            }

            message += "Группа: " + group;
            int number = Integer.parseInt(group);
            Group groupObject = checkGroup(number, new GroupDAO());
            if(Objects.isNull(groupObject)) {
                message += " - Ошибка: такой группы нет.<br>";
                check = false;
            } else {
                message += " - OK.<br>";
            }


        }

        response.getWriter().write(message);

    }
}
