package servlets.Group;

import dbmodules.dao.GroupDAOimpl;
import dbmodules.entity.Group;
import dbmodules.daointerfaces.GroupDAO;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static webdebugger.WebInputDebugger.*;

public class GroupAdder extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        setQueryParametres(request,response);

        String group = request.getParameter("group");

        JSONObject jsonObject = new JSONObject();
        List<String> errors = new ArrayList<>();

        if (group.isEmpty()) {
            errors.add("Передано пустое значение.");
        } else {
            boolean check = true;

            int number = 0;
            GroupDAO groupDAO = new GroupDAOimpl();
            try {
                number = Integer.parseInt(group);
                Group groupObject = checkGroup(number, groupDAO);
                if (!Objects.isNull(groupObject)) {
                    errors.add("Группа уже существует.");
                    check = false;
                }
            } catch (NumberFormatException e) {
                errors.add("Передано некорректное значение.");
                check = false;
                e.printStackTrace();
            }

            if (!check) {
                errors.add("Ошибка добавления.");
            } else {
                Group newGroup = new Group(
                        number
                );
                groupDAO.add(newGroup);

                jsonObject.put("lastId", newGroup.getId());


            }
            groupDAO.closeEntityManager();
        }
        jsonObject.accumulate("errors", errors);
        response.getWriter().write(jsonObject.toString());
        System.out.println(errors);

    }
}
