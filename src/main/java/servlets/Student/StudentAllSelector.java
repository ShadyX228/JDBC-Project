package servlets.Student;

import dbmodules.dao.StudentDAO;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;
import servlets.Main.MainServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentAllSelector extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        StudentDAO studentDAO = new StudentDAO();
        List<Student> list = studentDAO.select(Criteria.ALL,"");
        if(!list.isEmpty()) {
            response.getWriter().write("\t<tr>\n" +
                    "\t\t<td>ID</td>\n" +
                    "\t\t<td>ФИО</td>\n" +
                    "\t\t<td>День рождения</td>\n" +
                    "\t\t<td>Пол</td>\n" +
                    "\t\t<td>Группа</td>\n" +
                    "\t\t<td>Операции</td>\n" +
                    "\t</tr>");
            for (Student student : list) {
                response.getWriter().write("<tr id=\"student" + student.getId() + "\">\n");
                response.getWriter().write("<td class=\"id\">" + student.getId() + "</td>");
                response.getWriter().write("<td class=\"name\">" + student.getName() + "</td>");
                response.getWriter().write("<td class=\"birth\">" + student.getBirth() + "</td>");
                response.getWriter().write("<td class=\"gender\">" + student.getGender() + "</td>");
                response.getWriter().write("<td class=\"group\">" + student.getGroup().getNumber() + "</td>");
                response.getWriter().write("<td class=\"operations\">" +
                        "<a class=\"delete\" href=\"#deleteStudent" + student.getId() + "\">Удалить</a><br>" +
                        "<a class=\"update\" href=\"#updateStudent" + student.getId() + "\">Изменить</a>" +
                        "</td>");
                response.getWriter().write("</tr>");
            }
        } else {
            response.getWriter().write("Нет записей.");
        }
        studentDAO.closeEM();
    }
}
