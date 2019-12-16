package servlets.Teacher;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.TeacherDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Teacher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static webdebugger.WebInputDebugger.printMessage;

public class TeacherPutInGroupByNumber extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String teacherIdString = request.getParameter("teacherId");
        String groupNumberString = request.getParameter("groupNumber");

        String message = "Проверяю переданные параметры...<br>";
        if(Objects.isNull(teacherIdString) || Objects.isNull(groupNumberString) ) {
            message += printMessage(2,"Переданы пустые значения.");
        } else {
            TeacherDAO teacherDAO = new TeacherDAO();
            GroupDAO groupDAO = new GroupDAO();

            Teacher teacher = teacherDAO.selectById(Integer.parseInt(teacherIdString));
            Group group = groupDAO.select(Integer.parseInt(groupNumberString));
            if(!Objects.isNull(teacher) && !Objects.isNull(group)) {
                teacherDAO.putTeacherInGroup(teacher,group);
                response.getWriter().write("<span class=\"lastId\">" + group.getId() + "</span>");
            } else {
                printMessage(2,"Ошибка: нет такой группы или преподавателя.");
            }

            teacherDAO.closeEM();
            groupDAO.closeEM();
        }
        response.getWriter().write(message);
    }
}
