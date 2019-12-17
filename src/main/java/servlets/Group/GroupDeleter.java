package servlets.Group;

import dbmodules.dao.GroupDAO;
import dbmodules.tables.Group;
import dbmodules.types.Criteria;
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

        String message = "Проверяю переданные параметры... <br>Критерий: ";
        Criteria criteria = checkCriteria(criteriaString);
        if(Objects.isNull(criteria) || criteria.equals(ALL)) {
            message += criteriaString;
            if(!Objects.isNull(criteria) && criteria.equals(ALL)) {
                message += printMessage(2,"Ошибка. Нельзя удалить всех сразу.");
                response.getWriter().write(message);
            } else {
                message += printMessage(2,"Ошибка. Нет такого критерия.");
                response.getWriter().write(message);
            }
        } else {
            message += criteria + printMessage(1,"OK.");
            String criteriaValueParsed;
            if(!criteria.equals(ALL)) {
                message += "Значение критерия: " + criteriaValue;
                criteriaValueParsed = parseCriteria(criteria, criteriaValue);
            } else {
                criteriaValueParsed = "";
            }
            if(Objects.isNull(criteriaValueParsed)) {
                message += printMessage(2,
                        "Ошибка. Неверное значение для введенного критерия.");
                response.getWriter().write(message);
            } else {
                message += printMessage(1,"OK.");
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
                        message += "Статус" + printMessage(2,
                                "Ошибка. Пустое значение критерия.");
                        response.getWriter().write(message);
                    }
                }

                for (Group group : list) {
                    if(group.getTeachers().isEmpty() && group.getStudents().isEmpty()) {
                        groupDAO.delete(group);
                        response.getWriter().write("<br>Удалено "
                                + list.size() + " записей.");
                    } else {
                        response.getWriter().write(
                                "В группе есть преподаватели или студенты. " +
                                        "<span class=\"error\">-1</span><br>");
                    }
                }
                groupDAO.closeEntityManager();
            }
        }
    }
}
