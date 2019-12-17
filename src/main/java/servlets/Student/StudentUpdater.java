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
import java.util.Objects;

import static dbmodules.types.Criteria.*;
import static webdebugger.WebInputDebugger.*;

public class StudentUpdater extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

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
                message += "Группа не существует. " +
                        "<span class=\"error\">-1</span>";
                response.getWriter().write(message);
            } else {
                StudentDAO studentDAO = new StudentDAO();
                Student student = studentDAO.selectById(id);
                studentDAO.update(student, name, birth, gender, group);
                studentDAO.closeEntityManager();
                response.getWriter().write("Запись <span class=\"lastId\">"
                        + student.getId()  + "</span> обновлена.");
            }
            groupDAO.closeEntityManager();
        } else {
            message += "Переданы пустые параметры либо они некорректны." +
                    " <span class=\"error\">-1</span>";
            response.getWriter().write(message);
        }
    }
}