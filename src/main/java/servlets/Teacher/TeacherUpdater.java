package servlets.Teacher;

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
import static webdebugger.WebInputDebugger.*;
import static webdebugger.WebInputDebugger.printMessage;

public class TeacherUpdater extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String birthString = request.getParameter("birth");
        String genderString = request.getParameter("gender");
        String message = "Проверяю переданные параметры...<br> ";


        idString = parseCriteria(ID, idString);
        birthString = parseCriteria(BIRTH, birthString);
        genderString = parseCriteria(GENDER, genderString);
        if((!Objects.isNull(idString)
                && !Objects.isNull(genderString)
                && !Objects.isNull(birthString))
                && (!idString.isEmpty()
                && !genderString.isEmpty()
                && !birthString.isEmpty())) {
            int id = Integer.parseInt(idString);
            Gender gender = checkGender(genderString);
            LocalDate birth = LocalDate.parse(birthString);

            TeacherDAO teacherDAO = new TeacherDAO();
            Teacher teacher = teacherDAO.selectById(id);
            teacherDAO.update(teacher, name, birth, gender);
            teacherDAO.closeEM();
            response.getWriter().write("Запись <span class=\"lastId\">" + teacher.getId()  + "</span> обновлена.");
        } else {
            message += "Переданы пустые параметры либо они некорректны: <span class=\"error\">-1</span>";
            response.getWriter().write(message);
        }
    }
}
