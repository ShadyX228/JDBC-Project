package webdebugger;

import com.sun.deploy.net.HttpResponse;
import dbmodules.dao.GroupDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
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
                    groupDAO.closeEntityManager();
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
    public static String printMessage(int status, String message) {
        switch (status) {
            case 1: {
                return "<span class=\"OK\">" + message + "</span><br>";
            }
            case 2: {
                return "<span class=\"error\">" + message + "</span><br>";
            }
            default: {
                return message + "<br>";
            }
        }
    }

    public static String generateStudentsTable(List<Student> list) {
        StringBuilder output = new StringBuilder();
        output.append("\t<thead><tr>\n" +
                "\t\t<th>ID</th>\n" +
                "\t\t<th>ФИО</th>\n" +
                "\t\t<th>День рождения</th>\n" +
                "\t\t<th>Пол</th>\n" +
                "\t\t<th>Группа</th>\n" +
                "\t\t<th>Операции</th>\n" +
                "\t</tr></thead><tbody>\n");
        if (!list.isEmpty()) {
            for (Student student : list) {
                output.append("<tr id=\"student")
                        .append(student.getId())
                        .append("\">\n").append(
                "<td class=\"id\">").append(student.getId()).append("</td>")
                        .append("<td class=\"name\">")
                        .append(student.getName()).append("</td>")
                        .append("<td class=\"birth\">")
                        .append(student.getBirth()).append("</td>")
                        .append("<td class=\"gender\">")
                        .append(student.getGender()).append("</td>")
                        .append("<td class=\"group\">")
                        .append(student.getGroup().getNumber())
                        .append("</td>")
                        .append("<td class=\"operations\">").append(
                        "<a class=\"delete\" href=\"#deleteStudent")
                        .append(student.getId())
                        .append("\">Удалить</a><br>").append(
                        "<a class=\"update\" href=\"#updateStudent")
                        .append(student.getId()).append("\">Изменить</a>")
                        .append("</td>").append("</tr>");
            }
            output.append("</tbody>");
        } else {
            output.append("<tr><td colspan=\"6\">Нет записей.</tr></td>");
        }
        return output.toString();
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
    public static String generateGroupTable(List<Group> list) {
        StringBuilder output = new StringBuilder();
        output.append("\t<tr>\n" +
                "\t\t<td>ID</td>\n" +
                "\t\t<td>Группа</td>\n" +
                "\t\t<td>Операции</td>\n" +
                "\t</tr>");
        if(!list.isEmpty()) {
            for (Group group : list) {
                output
                        .append("<tr id=\"group")
                        .append(group.getId())
                        .append("\">\n")
                        .append("<td class=\"id\">").append(group.getId()).append("</td>")
                        .append("<td class=\"group\">").append(group.getNumber()).append( "</td>")
                        .append("<td class=\"operations\">")
                        .append("<a class=\"delete\" href=\"#deleteGroup")
                        .append(group.getId()).append("\">Удалить</a><br>")
                        .append("<a class=\"update\" href=\"#updateGroup")
                        .append(group.getId()).append("\">Изменить</a><br>")
                        .append("<a class=\"getInfo\" href=\"#getInfoGroup")
                        .append(group.getId() )
                        .append( "\">Информация</a><br>")
                        .append("<a class=\"putTeacherInGroup\"")
                        .append("href=\"#putTeacherInGroup")
                        .append(group.getId())
                        .append("\">Назначить преподавателя</a>")
                        .append("</td>")
                        .append("</tr>");
            }
        } else {
            output.append("<tr><td colspan=\"3\">Нет записей.</tr></td>");
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
