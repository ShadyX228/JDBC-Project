package servlets.Teacher;

import dbmodules.dao.TeacherDAOimpl;
import dbmodules.entity.Teacher;
import dbmodules.daointerfaces.TeacherDAO;
import dbmodules.types.Criteria;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static webdebugger.WebInputDebugger.setQueryParametres;

public class TeacherAllSelector extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        setQueryParametres(request,response);
        TeacherDAO teacherDAO = new TeacherDAOimpl();
        JSONObject jsonObject = new JSONObject();

        List<Teacher> list = teacherDAO.select(Criteria.ALL,"");
        List<String> errors = new ArrayList<>();

        jsonObject.accumulate("teachers", list);
        jsonObject.accumulate("errors", errors);
        teacherDAO.closeEntityManager();

        response.getWriter().write(jsonObject.toString());
        System.out.println(errors);
    }
}