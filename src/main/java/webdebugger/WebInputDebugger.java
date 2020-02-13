package webdebugger;


import dbmodules.service.dao.GroupDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import org.json.JSONObject;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static dbmodules.types.Criteria.*;

public class WebInputDebugger {
    public static Criteria checkCriteria(String crit) {
        switch (crit.toUpperCase()) {
            case "ID": {
                return ID;
            }
            case "NAME": {
                return NAME;
            }
            case "BIRTH": {
                return BIRTH;
            }
            case "GENDER": {
                return GENDER;
            }
            case "GROUP": {
                return GROUP;
            }
            case "ALL": {
                return ALL;
            }
        }
        return null;
    }
    public static Gender checkGender(String genderInput) {
        for (Gender gender : Gender.values()) {
            if (gender.getValue().equals(genderInput)) {
                return Gender.valueOf(genderInput);
            }
        }
        return null;
    }
    public static Group checkGroup
            (int number, GroupDAO groupDAO) {
        try {
            return groupDAO.select(number);
        } catch (InputMismatchException | IndexOutOfBoundsException e) {
            return null;
        }
    }
    public static String parseCriteria
            (Criteria criteria, String critVal) {
        switch (criteria) {
            case ID: {
                try {
                    Integer.parseInt(critVal);
                    return critVal;
                } catch (InputMismatchException | NumberFormatException e) {
                    return null;
                }
            }
            case NAME: {
                return critVal;
            }
            case GENDER: {
                try {
                    Gender.valueOf(critVal.toUpperCase());
                    return critVal.toUpperCase();
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
            case GROUP: {
                try {
                    GroupDAO groupDAO = new GroupDAO();
                    Group group = groupDAO.select(Integer.parseInt(critVal));
                    if (!Objects.isNull(group)) {
                        return Integer.toString(group.getNumber());
                    } else {
                        return null;
                    }
                } catch (InputMismatchException
                        | IndexOutOfBoundsException
                        | NumberFormatException e) {
                    return null;
                }
            }
            case BIRTH: {
                try {
                    LocalDate.parse(critVal);
                    return critVal;
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
            case ALL: {
                return "";
            }
        }
        return null;
    }

   public static void setStudentJSONObjectState(List<Student> students,
                                                Map<Integer, Integer> groups,
                                                List<Integer> errors, JSONObject jsonObject) {
       if(!students.isEmpty()) {
           jsonObject.accumulate("students", students);
           for(Student student : students) {
               groups.put(student.getId(), student.getGroup().getNumber());
           }
           jsonObject.accumulate("groups", groups);
       } else {
           errors.add(0);
       }
    }
    public static String generateTeacherTable(List<Teacher> list) {
        StringBuilder output = new StringBuilder();
        output.append("\t<tr>\n" +
                "\t\t<td>ID</td>\n" +
                "\t\t<td>ФИО</td>\n" +
                "\t\t<td>День рождения</td>\n" +
                "\t\t<td>Пол</td>\n" +
                "\t\t<td>Операции</td>\n" +
                "\t</tr>");
        if(!list.isEmpty()) {
            for (Teacher teacher : list) {
                output
                        .append("<tr id=\"teacher")
                        .append( teacher.getId() ).append( "\">\n" )
                        .append("<td class=\"id\">")
                        .append(teacher.getId())
                        .append("</td>" )
                        .append("<td class=\"name\">")
                        .append(teacher.getName() )
                        .append("</td>" )
                        .append("<td class=\"birth\">")
                        .append(teacher.getBirth())
                        .append("</td>")
                        .append("<td class=\"gender\">")
                        .append(teacher.getGender())
                        .append( "</td>" )
                        .append("<td class=\"operations\">" )
                        .append("<a class=\"delete\" href=\"#deleteTeacher")
                        .append( teacher.getId() ).append("\">Удалить</a><br>")
                        .append("<a class=\"update\" href=\"#updateTeacher")
                        .append( teacher.getId() ).append( "\">Изменить</a><br>" )
                        .append("<a class=\"getInfo\" href=\"#getInfoTeacher")
                        .append(teacher.getId())
                        .append("\">Информация</a>")
                        .append("</td>")
                        .append("</tr>");
            }
        } else {
            output.append("<tr><td colspan=\"6\">Нет записей.</tr></td>");
        }
        return output.toString();
    }

    public static void setQueryParametres(HttpServletRequest request,
                                   HttpServletResponse response)
            throws UnsupportedEncodingException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
    }
}
