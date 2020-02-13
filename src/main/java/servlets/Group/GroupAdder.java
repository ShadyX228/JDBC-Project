package servlets.Group;

import dbmodules.service.dao.GroupDAO;
import dbmodules.tables.Group;
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
        List<Integer> errors = new ArrayList<>();

        if (group.isEmpty()) {
            errors.add(0);
        } else {
            boolean check = true;

            int number = 0;
            GroupDAO groupDAO = new GroupDAO();
            try {
                number = Integer.parseInt(group);
                Group groupObject = checkGroup(number, groupDAO);
                if (!Objects.isNull(groupObject)) {
                    errors.add(-1);
                    check = false;
                }
            } catch (NumberFormatException e) {
                errors.add(-2);
                check = false;
            }

            if (!check) {
                errors.add(-3);
            } else {
                Group newGroup = new Group(
                        number
                );
                groupDAO.add(newGroup);

                jsonObject.put("lastId", newGroup.getId());


            }
        }
        jsonObject.accumulate("errors", errors);
        response.getWriter().write(jsonObject.toString());
    }
}
