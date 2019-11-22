package servlets;

import dbmodules.dao.StudentDAO;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static webinputdebugger.WebInputDebugger.*;


public class StudentSelector extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        List<Student> list = new ArrayList<>();
        StudentDAO studentDAO = new StudentDAO();

        String criteriaString = request.getParameter("criteria");
        String criteriaValue = request.getParameter("criteriaValue");
        String message = "Проверяю переданные параметры... <br>Критерий: ";

        Criteria criteria = checkCriteria(criteriaString);
        if(Objects.isNull(criteria)) {
            message += criteriaString + " - Ошибка. " +
                    "Нет такого критерия.<br>";
        } else {
            message += criteria + " - OK.<br>Значение критерия: ";
            String criteriaValueParsed = parseCriteria(criteria, criteriaValue);
            if(Objects.isNull(criteriaValueParsed)) {
                message += criteriaValueParsed + "- Ошибка. " +
                        "Неверное значение для введенного критерия.";
            } else {
                //list.add(studentDAO.selectById(Integer.parseInt(criteriaValue)));
                /*switch (criteria) {
                    case ID: {
                        list.add(studentDAO.selectById(Integer.parseInt(criteriaValue)));
                        break;
                    }
                    case NAME: {

                        break;
                    }
                    case GENDER: {

                        break;
                    }
                    case GROUP: {

                        break;
                    }
                    case BIRTH: {

                        break;
                    }
                    case ALL: {

                        break;
                    }
                }*/
            }
        }

        response.getWriter().write(message);
        for(Student student : list) {
            response.getWriter().write(student.toString());
        }
    }
}
