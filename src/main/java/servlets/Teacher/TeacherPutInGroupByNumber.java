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
import static webdebugger.WebInputDebugger.setQueryParametres;

public class TeacherPutInGroupByNumber extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        String teacherIdString = request.getParameter("teacherId");
        String groupNumberString = request.getParameter("groupNumber");

        String message = "Проверяю переданные параметры...<br>";
        if(Objects.isNull(teacherIdString)
                || Objects.isNull(groupNumberString) ) {
            message += printMessage(2,"Переданы пустые значения.");
        } else {
            TeacherDAO teacherDAO = new TeacherDAO();
            GroupDAO groupDAO = new GroupDAO();

            Teacher teacher = teacherDAO.selectById(Integer
                    .parseInt(teacherIdString));
            Group group = groupDAO.select(Integer.parseInt(groupNumberString));
            if(!Objects.isNull(teacher) && !Objects.isNull(group)) {
                teacherDAO.putTeacherInGroup(teacher,group);
                response.getWriter().write("<span class=\"lastId\">"
                        + group.getId() + "</span>");
            } else {
                message += printMessage(2,"Ошибка: нет такой группы или преподавателя.");
                response.getWriter().write(message);
            }

            teacherDAO.closeEntityManager();
            groupDAO.closeEntityManager();
        }
        response.getWriter().write(message);
    }
}
