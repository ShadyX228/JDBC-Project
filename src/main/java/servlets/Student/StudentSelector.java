package servlets.Student;

import dbmodules.service.dao.StudentDAO;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static dbmodules.types.Criteria.*;
import static webdebugger.WebInputDebugger.*;


public class StudentSelector extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        setQueryParametres(request,response);

        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String birthString = request.getParameter("birth");
        String genderString = request.getParameter("gender");
        String groupString = request.getParameter("group");

        idString = parseCriteria(ID, idString);
        birthString = parseCriteria(BIRTH, birthString);
        genderString = parseCriteria(GENDER, genderString);
        groupString = parseCriteria(ID, groupString);

        HashMap<Criteria, String> map = new HashMap<>();

        List<Student> students;
        Map<Integer, Integer> groups = new HashMap<>();
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

            if (!Objects.isNull(groupString)) {
                map.put(GROUP, groupString);
            }
        }

        StudentDAO studentDAO = new StudentDAO();
        if(map.isEmpty()) {
            map.put(ALL, " ");
        }
        students = studentDAO.select(map);

        JSONObject jsonObject = new JSONObject();
        setStudentJSONObjectState(students, groups, errors, jsonObject);
        jsonObject.accumulate("errors", errors);

        studentDAO.closeEntityManager();
        response.getWriter().write(jsonObject.toString());
    }
}
