package servlets.Teacher;

import dbmodules.dao.TeacherDAO;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static dbmodules.types.Criteria.*;
import static dbmodules.types.Criteria.GROUP;
import static webdebugger.WebInputDebugger.*;
import static webdebugger.WebInputDebugger.printMessage;

public class TeacherSelector extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String birthString = request.getParameter("birth");
        String genderString = request.getParameter("gender");

        idString = parseCriteria(ID, idString);
        birthString = parseCriteria(BIRTH, birthString);
        genderString = parseCriteria(GENDER, genderString);

        HashMap<Criteria, String> map = new HashMap<>();

        List<Teacher> teachers;
        if (!Objects.isNull(idString)) {
            map.put(ID, idString);
        } else {
            if (!name.isEmpty()) {
                map.put(NAME, name);
            }

            if (!Objects.isNull(birthString)) {
                if(!birthString.isEmpty()) {
                    map.put(BIRTH, birthString);
                }
            }

            if (!Objects.isNull(genderString)) {
                if(!genderString.isEmpty()) {
                    map.put(GENDER, genderString);
                }
            }
        }

        TeacherDAO teacherDAO = new TeacherDAO();
        if(map.isEmpty()) {
            map.put(ALL, " ");
        }
        teachers = teacherDAO.select(map);

        response.getWriter().write(generateTeacherTable(teachers));
        teacherDAO.closeEM();
    }
}
