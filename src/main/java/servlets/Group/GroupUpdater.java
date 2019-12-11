package servlets.Group;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.dao.TeacherDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static dbmodules.types.Criteria.*;
import static dbmodules.types.Criteria.GROUP;
import static webdebugger.WebInputDebugger.*;
import static webdebugger.WebInputDebugger.printMessage;

public class GroupUpdater extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String idString = request.getParameter("id");
        String groupString = request.getParameter("group");
        String message = "Проверяю переданные параметры...<br>";
        idString = parseCriteria(ID, idString);
        groupString = parseCriteria(ID, groupString);
        if((!Objects.isNull(idString)
                && !Objects.isNull(groupString))
                && (!idString.isEmpty()
                && !groupString.isEmpty())) {
            int id = Integer.parseInt(idString);
            GroupDAO groupDAO = new GroupDAO();
            Group group = groupDAO.selectById(id);
            int number = Integer.parseInt(groupString);
            List<Group> groupCheck = groupDAO.selectAll();
            boolean check = true;
            for(Group groupObject : groupCheck) {
                if(groupObject.getNumber() == number) {
                    check = false;
                    break;
                }
            }
            if(!check) {
                message += "Группа уже существует: <span class=\"error\">-1</span>";
                response.getWriter().write(message);
            } else {
                groupDAO.update(group, number);
                groupDAO.closeEM();
                response.getWriter().write("Запись <span class=\"lastId\">" + group.getId()  + "</span> обновлена.");
            }
            groupDAO.closeEM();
        } else {
            message += "Переданы пустые параметры либо они некорректны: <span class=\"error\">-1</span>";
            response.getWriter().write(message);
        }
    }
}
