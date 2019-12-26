package servlets.Group;

import dbmodules.dao.GroupDAO;
import dbmodules.tables.Group;
import dbmodules.types.Criteria;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dbmodules.types.Criteria.ALL;
import static dbmodules.types.Criteria.ID;
import static webdebugger.WebInputDebugger.*;

public class GroupDeleter extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        String criteriaString = request.getParameter("criteria");
        String criteriaValue = request.getParameter("criteriaValue");

        JSONObject jsonObject = new JSONObject();
        List<Integer> errors = new ArrayList<>();

        Criteria criteria = checkCriteria(criteriaString);
        if(Objects.isNull(criteria) || criteria.equals(ALL)) {
            if(!Objects.isNull(criteria) && criteria.equals(ALL)) {
                errors.add(0);
            } else {
                errors.add(-1);
            }
        } else {
            String criteriaValueParsed;
            if(!criteria.equals(ALL)) {
                criteriaValueParsed = parseCriteria(criteria, criteriaValue);
            } else {
                criteriaValueParsed = "";
            }
            if(Objects.isNull(criteriaValueParsed)) {
                errors.add(-2);
            } else {
                List<Group> list = new ArrayList<>();
                GroupDAO groupDAO = new GroupDAO();
                if(criteria.equals(ID)) {
                    Group group = groupDAO.selectById(Integer
                            .parseInt(criteriaValueParsed));
                    if(!Objects.isNull(group)) {
                        list.add(group);
                    }
                } else {
                    if(!criteriaValueParsed.isEmpty()) {
                        int number = Integer.parseInt(criteriaValueParsed);
                        list.add(groupDAO.select(number));
                    } else {
                        errors.add(-3);
                    }
                }

                for (Group group : list) {
                    if(group.getTeachers().isEmpty() && group.getStudents().isEmpty()) {
                        groupDAO.delete(group);
                    } else {
                        errors.add(-4);
                    }
                }
                groupDAO.closeEntityManager();

            }
        }
        jsonObject.accumulate("errors", errors);
        response.getWriter().write(jsonObject.toString());
    }
}
