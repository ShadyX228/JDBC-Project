package servlets.Teacher;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.TeacherDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static webdebugger.WebInputDebugger.*;

public class TeacherPutInGroup extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String idString = request.getParameter("id");
        String numberString = request.getParameter("number");

        String message = "Проверяю переданные параметры...<br>";
        if(idString.isEmpty() || numberString.isEmpty()) {
            message += "Статус" + printMessage(2,"Переданы пустые значения.");
        } else {
            idString = parseCriteria(Criteria.ID, idString);
            numberString = parseCriteria(Criteria.ID, numberString);
            if(!Objects.isNull(idString) && !Objects.isNull(numberString)) {
                message += "Значения не пусты" + printMessage(1, "OK.");
                message += "Проверяю корректность введенных параметров...<br>";
                int id = Integer.parseInt(idString);
                int number = Integer.parseInt(numberString);
                TeacherDAO teacherDAO = new TeacherDAO();
                GroupDAO groupDAO = new GroupDAO();

                Teacher teacher = teacherDAO.selectById(id);
                Group group = checkGroup(number, groupDAO);
                if(Objects.isNull(teacher) || Objects.isNull(group)) {
                    message += "Статус" + printMessage(2,"нет такой группы или преподавателя.");
                } else {
                    List<Group> groups = teacher.getGroups();

                    if(groups.contains(group)) {
                        message += "Cтатус" + printMessage(2,"Преподаватель уже преподает в этой группе.");
                    } else {
                        message += "Статус" + printMessage(1,"OK.");
                        message += "Добаляю...";
                        teacherDAO.putTeacherInGroup(teacher, group);
                        message += printMessage(1,"OK.");
                    }
                }
            } else {
                message += printMessage(2,"Переданы некорректные значения.");
            }
        }
        response.getWriter().write(message);
    }
}
