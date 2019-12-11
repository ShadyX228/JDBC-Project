package servlets;

import dbmodules.tables.Student;
import dbmodules.types.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static dbmodules.types.Criteria.*;
import static dbmodules.types.Criteria.GENDER;
import static webdebugger.WebInputDebugger.parseCriteria;

public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String birthString = request.getParameter("birth");
        String genderString = request.getParameter("gender");
        String groupString = request.getParameter("group");

        idString = parseCriteria(ID, idString);
        birthString = parseCriteria(BIRTH, birthString);
        genderString = parseCriteria(GENDER, genderString);
        groupString = parseCriteria(GROUP, groupString);

        List<Object> valuesList = new ArrayList<>();
        List<Criteria> criteriaList = new ArrayList<>();
        HashMap<Criteria, String> map = new HashMap<>();

        Set<Student> students = new HashSet<>();
        if (!Objects.isNull(idString)) {
            valuesList.add(idString); // 0 - id
            criteriaList.add(ID);
            map.put(ID, idString);
        } else {
            if (!name.isEmpty()) {
                valuesList.add(name); // 1 - name
                criteriaList.add(NAME);
                map.put(NAME, name);
            }

            if (!Objects.isNull(birthString)) {
                if(!birthString.isEmpty()) {
                    valuesList.add(birthString); // 2 - birth
                    criteriaList.add(BIRTH);
                    map.put(BIRTH, birthString);
                }
            }

            if (!Objects.isNull(genderString)) {
                if(!genderString.isEmpty()) {
                    valuesList.add(genderString); // 3 - gender
                    criteriaList.add(GENDER);
                    map.put(GENDER, genderString);
                }
            }

            if (!Objects.isNull(groupString)) {
                valuesList.add(groupString); // 4 - group
                criteriaList.add(GROUP);
                map.put(GROUP, groupString);
            }
        }
        for(HashMap.Entry<Criteria, String> element : map.entrySet()) {
            Criteria criteria = element.getKey();
            String value = element.getValue();
            response.getWriter().write(criteria + " " + value);
        }
        //response.getWriter().write(criteriaList + "<br>");
        //response.getWriter().write(valuesList + "<br>");
    }
}
