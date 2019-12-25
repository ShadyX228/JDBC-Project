package servlets.Student;

import dbmodules.dao.StudentDAO;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;
import org.json.JSONObject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static webdebugger.WebInputDebugger.generateStudentsTable;
import static webdebugger.WebInputDebugger.setQueryParametres;

public class StudentAllSelector extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);
        StudentDAO studentDAO = new StudentDAO();
        JSONObject jsonObject = new JSONObject();

        List<Student> students = studentDAO.select(Criteria.ALL,"");
        Map<Integer, Integer> groups = new HashMap<>();

        jsonObject.accumulate("students", students);
        for(Student student : students) {
            groups.put(student.getId(), student.getGroup().getNumber());
        }
        jsonObject.accumulate("groups", groups);
        studentDAO.closeEntityManager();

        response.getWriter().write(jsonObject.toString());
    }
}
