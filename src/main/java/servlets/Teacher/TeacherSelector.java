package servlets.Teacher;

import dbmodules.service.dao.TeacherDAO;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static dbmodules.types.Criteria.*;
import static webdebugger.WebInputDebugger.*;

public class TeacherSelector extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        setQueryParametres(request,response);

        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String birthString = request.getParameter("birth");
        String genderString = request.getParameter("gender");

        idString = parseCriteria(ID, idString);
        birthString = parseCriteria(BIRTH, birthString);
        genderString = parseCriteria(GENDER, genderString);

        HashMap<Criteria, String> map = new HashMap<>();

        JSONObject jsonObject = new JSONObject();

        List<Teacher> teachers;
        List<Integer> errors = new ArrayList<>();

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

        jsonObject.accumulate("teachers", teachers);
        jsonObject.accumulate("errors", errors);

        teacherDAO.closeEntityManager();
        response.getWriter().write(jsonObject.toString());
    }
}
