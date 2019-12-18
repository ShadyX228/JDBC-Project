package servlets.Teacher;

import dbmodules.dao.TeacherDAO;
import dbmodules.tables.Teacher;
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

public class TeacherUpdater extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setQueryParametres(request,response);

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
            if(!Objects.isNull(teacher)) {
                teacherDAO.update(teacher, name, birth, gender);
                response.getWriter().write("Запись <span class=\"lastId\">"
                        + teacher.getId()  + "</span> обновлена.");
            } else {
                message += "Такого преподавателя нет.." +
                        " <span class=\"error\">-1</span>";
                response.getWriter().write(message);
            }
            teacherDAO.closeEntityManager();
        } else {
            message += "Переданы пустые параметры либо они некорректны." +
                    " <span class=\"error\">-1</span>";
            response.getWriter().write(message);
        }
    }
}
