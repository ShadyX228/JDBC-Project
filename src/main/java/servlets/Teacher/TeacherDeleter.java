package servlets.Teacher;

import dbmodules.dao.TeacherDAOimpl;
import dbmodules.entity.Teacher;
import dbmodules.daointerfaces.TeacherDAO;
import dbmodules.types.Criteria;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static dbmodules.types.Criteria.ALL;
import static dbmodules.types.Criteria.ID;
import static webdebugger.WebInputDebugger.*;

public class TeacherDeleter extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        TeacherDAO teacherDAO = new TeacherDAOimpl();

        String criteriaString = request.getParameter("criteria");
        String criteriaValue = request.getParameter("criteriaValue");

        JSONObject jsonObject = new JSONObject();
        List<String> errors = new ArrayList<>();

        Criteria criteria = checkCriteria(criteriaString);
        if(Objects.isNull(criteria) || criteria.equals(ALL)) {
            if(!Objects.isNull(criteria) && criteria.equals(ALL)) {
                errors.add("Переданы не правильные значения.");
            } else {
                errors.add("Переданы некорректные значения.");
            }
        } else {
            String criteriaValueParsed;
            if(!criteria.equals(ALL)) {
                criteriaValueParsed = parseCriteria(criteria, criteriaValue);
            } else {
                criteriaValueParsed = "";
            }
            if(Objects.isNull(criteriaValueParsed)) {
                errors.add("Критерий некорректный.");
            } else {
                List<Teacher> list = new ArrayList<>();
                if(criteria.equals(ID)) {
                    Teacher teacher = teacherDAO.selectById(Integer
                            .parseInt(criteriaValueParsed));
                    if(!Objects.isNull(teacher)) {
                        list.add(teacher);
                    }
                } else {
                    if(!criteriaValueParsed.isEmpty()) {
                        list = teacherDAO.select(criteria, criteriaValueParsed);
                    } else {
                        errors.add("Критерий пуст.");
                    }
                }
                for (Iterator<Teacher> iterator = list.iterator();
                     iterator.hasNext();) {
                    Teacher teacher = iterator.next();
                    if(teacher.getGroups().isEmpty()) {
                        teacherDAO.delete(teacher);
                    } else {
                        iterator.remove();
                    }
                }
                if(!list.isEmpty()) {
                    jsonObject.put("deletedSize", list.size());
                } else {
                    errors.add("Список пуст.");
                }
            }
        }
        teacherDAO.closeEntityManager();
        jsonObject.accumulate("errors",errors);
        response.getWriter().write(jsonObject.toString());
        System.out.println(errors);
    }
}
