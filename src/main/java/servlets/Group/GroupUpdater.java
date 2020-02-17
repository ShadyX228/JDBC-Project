package servlets.Group;

import dbmodules.dao.GroupDAO;
import dbmodules.entity.Group;
import dbmodules.service.GroupService;
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

public class GroupUpdater extends HttpServlet {
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        String idString = request.getParameter("id");
        String groupString = request.getParameter("group");

        JSONObject jsonObject = new JSONObject();
        List<String> errors = new ArrayList<>();

        idString = parseCriteria(ID, idString);
        groupString = parseCriteria(ID, groupString);
        if((!Objects.isNull(idString)
                && !Objects.isNull(groupString))
                && (!idString.isEmpty()
                && !groupString.isEmpty())) {
            int id = Integer.parseInt(idString);
            GroupService groupDAO = new GroupDAO();
            Group group = groupDAO.selectById(id);
            int number = Integer.parseInt(groupString);
            List<Group> groupCheck = groupDAO.selectAll();
            boolean check = true;
            for(Group groupObject : groupCheck) {
                if(groupObject.getNumber() == number) {
                    check = false;
                    break;
                }
            }
            if(!check) {
                errors.add("Ошибка обновления.");
            } else {
                groupDAO.update(group, number);
            }
            groupDAO.closeEntityManager();
        } else {
            errors.add("Переданы пустые значения.");
        }
        jsonObject.accumulate("errors", errors);
        response.getWriter().write(jsonObject.toString());
        System.out.println(errors);
    }
}
