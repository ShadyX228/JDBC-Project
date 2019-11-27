package servlets;

import dbmodules.dao.GroupDAO;
import dbmodules.tables.Student;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupSelector extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        List<Student> list = new ArrayList<>();

        GroupDAO groupDAO = new GroupDAO();

        String numberString = request.getParameter("number");
        String checkAll = request.getParameter("checkAll");
        String message = "Проверяю переданные параметры...<br>";

        if(checkAll.equals("false")) {
            message += "Выбираю все группы...";
        } else if(checkAll.equals("true")) {
            message += "Выбираю группу " + numberString;
        } else {
            message += "Переданы некорректные параметры.";
        }

        response.getWriter().write(message);
    }
}
