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
import java.util.List;
import java.util.Objects;

import static dbmodules.types.Criteria.*;
import static webdebugger.WebInputDebugger.*;


public class StudentDeleter extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        String criteriaString = request.getParameter("criteria");
        String criteriaValue = request.getParameter("criteriaValue");

        JSONObject jsonObject = new JSONObject();
        List<String> errors = new ArrayList<>();

        Criteria criteria = checkCriteria(criteriaString);
        if(Objects.isNull(criteria) || criteria.equals(ALL)) {
            if(!Objects.isNull(criteria) && criteria.equals(ALL)) {
                errors.add("Переданы не правильные параметры.");
            } else {
                errors.add("Переданы некорректные параметры.");
            }
        } else {
            String criteriaValueParsed;
            if(!criteria.equals(ALL)) {
                criteriaValueParsed = parseCriteria(criteria, criteriaValue);
            } else {
                criteriaValueParsed = "";
            }
            if(Objects.isNull(criteriaValueParsed)) {
                errors.add("Критерий имеет некорректный формат.");
            } else {
                List<Student> list = new ArrayList<>();
                PersonDAO<Student> studentDAO = new StudentDAOimpl();
                if(criteria.equals(ID)) {
                    Student student = studentDAO.selectById(Integer
                            .parseInt(criteriaValueParsed));
                    if(!Objects.isNull(student)) {
                        list.add(student);
                    }
                } else {
                    if(!criteriaValueParsed.isEmpty()) {
                        list = studentDAO.select(criteria, criteriaValueParsed);
                    } else {
                        errors.add("Критерий пуст.");
                    }
                }
                for (Student student : list) {
                    studentDAO.delete(student);
                }
                jsonObject.accumulate("errors", errors);
                jsonObject.put("deletedSize", list.size());
                studentDAO.closeEntityManager();

                response.getWriter().write(jsonObject.toString());
            }
        }
        System.out.println(errors);
    }
}
