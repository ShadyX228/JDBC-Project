package servlets.Student;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import servlets.Main.MainServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static dbmodules.types.Criteria.*;
import static webdebugger.WebInputDebugger.*;
import static webdebugger.WebInputDebugger.printMessage;

public class StudentUpdater extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String birthString = request.getParameter("birth");
        String genderString = request.getParameter("gender");
        String groupString = request.getParameter("group");
        String message = "Проверяю переданные параметры...<br> ";


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
                message += "Группа не существует: <span class=\"error\">-1</span>";
                response.getWriter().write(message);
            } else {
                StudentDAO studentDAO = new StudentDAO();
                Student student = studentDAO.selectById(id);
                studentDAO.update(student, name, birth, gender, group);
                studentDAO.closeEM();
                groupDAO.closeEM();
                response.getWriter().write("Запись <span class=\"lastId\">" + student.getId()  + "</span> обновлена.");
            }
        } else {
            message += "Переданы пустые параметры либо они некорректны: <span class=\"error\">-1</span>";
            response.getWriter().write(message);
        }
        /*if(!Objects.isNull(idParsed)) {
            Student student = studentDAO.selectById(Integer.parseInt(idParsed));
            if (Objects.isNull(student) || criteriaValue.isEmpty()) {
                message += printMessage(2, "Ошибка. Нет студента с таким ID, ID введен не верно или значение критерия пусто.");
            } else {
                message += printMessage(1, "OK.");
                message += "Поле, которое надо обновить: " + criteriaString;
                Criteria criteria = checkCriteria(criteriaString);
                if (Objects.isNull(criteria)
                        || criteria.equals(ALL)
                        || criteria.equals(ID)) {
                    message += printMessage(2, "Ошибка. Нет такого поля.");
                } else {
                    message += printMessage(1, "OK.");
                    message += "Новое значение: " + criteriaValue;
                    String criteriaValueParsed = parseCriteria(criteria, criteriaValue);

                    if (Objects.isNull(criteriaValueParsed)) {
                        message += printMessage(2, "Ошибка. Неверное значение для введенного поля.");
                    } else {
                        message += printMessage(1, "OK.");
                        studentDAO.update(student, criteria, criteriaValueParsed);
                        message += "Запись обновлена.";
                    }
                }
            }
        } else {
            message += printMessage(2,"Некорректно введенный id.");
        }*/
    }
}