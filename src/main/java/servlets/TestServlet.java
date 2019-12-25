package servlets;

import com.google.gson.*;
import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import static dbmodules.types.Criteria.*;
import static webdebugger.WebInputDebugger.setQueryParametres;

public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        setQueryParametres(request,response);

        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject();

        StudentDAO sd = new StudentDAO();
        List<Student> students = sd.select(ALL,"");
        Map<Integer, Integer> groups = new HashMap<>();

        jsonObject.accumulate("students", students);
        for(Student student : students) {
            groups.put(student.getId(), student.getGroup().getNumber());
        }
        jsonObject.accumulate("groups", groups);
        sd.closeEntityManager();
        out.write(jsonObject.toString());
    }
}
