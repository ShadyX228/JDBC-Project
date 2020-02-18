package servlets.Student;

import dbmodules.dao.StudentDAOimpl;
import dbmodules.entity.Student;
import dbmodules.daointerfaces.PersonDAO;
import dbmodules.types.Criteria;
import org.json.JSONObject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static webdebugger.WebInputDebugger.setStudentJSONObjectState;
import static webdebugger.WebInputDebugger.setQueryParametres;

public class StudentAllSelector extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);
        PersonDAO<Student> studentDAO = new StudentDAOimpl();
        JSONObject jsonObject = new JSONObject();

        List<Student> students = studentDAO.select(Criteria.ALL,"");
        Map<Integer, Integer> groups = new HashMap<>();
        List<String> errors = new ArrayList<>();

        setStudentJSONObjectState(students, groups, errors, jsonObject);
        jsonObject.accumulate("errors", errors);

        studentDAO.closeEntityManager();
        response.getWriter().write(jsonObject.toString());
        System.out.println(errors);
    }
}
