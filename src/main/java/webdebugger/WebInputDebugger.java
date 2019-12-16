package webdebugger;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.tables.Table;
import dbmodules.tables.Teacher;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import dbmodules.types.TableType;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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
                    groupDAO.closeEM();
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
                return " - <span class=\"OK\">" + message + "</span><br>";
            }
            case 2: {
                return " - <span class=\"error\">" + message + "</span><br>";
            }
            default: {
                return " - " + message + "<br>";
            }
        }
    }

    public static String generateStudentsTable(List<Student> list) {
        String output = "\t<tr>\n" +
                "\t\t<td>ID</td>\n" +
                "\t\t<td>ФИО</td>\n" +
                "\t\t<td>День рождения</td>\n" +
                "\t\t<td>Пол</td>\n" +
                "\t\t<td>Группа</td>\n" +
                "\t\t<td>Операции</td>\n" +
                "\t</tr>\n";
        if (!list.isEmpty()) {
            for (Student student : list) {
                output += "<tr id=\"student" + student.getId() + "\">\n";
                output += "<td class=\"id\">" + student.getId() + "</td>";
                output += "<td class=\"name\">" + student.getName() + "</td>";
                output += "<td class=\"birth\">" + student.getBirth() + "</td>";
                output += "<td class=\"gender\">" + student.getGender() + "</td>";
                output += "<td class=\"group\">" + student.getGroup().getNumber() + "</td>";
                output += "<td class=\"operations\">" +
                        "<a class=\"delete\" href=\"#deleteStudent" + student.getId() + "\">Удалить</a><br>" +
                        "<a class=\"update\" href=\"#updateStudent" + student.getId() + "\">Изменить</a>" +
                        "</td>";
                output += "</tr>";
            }
        } else {
            output += "<tr><td colspan=\"6\">Нет записей.</tr></td>";
        }
        return output;
    }
    public static String generateTeacherTable(List<Teacher> list) {
        String output = "\t<tr>\n" +
                "\t\t<td>ID</td>\n" +
                "\t\t<td>ФИО</td>\n" +
                "\t\t<td>День рождения</td>\n" +
                "\t\t<td>Пол</td>\n" +
                "\t\t<td>Операции</td>\n" +
                "\t</tr>";
        if(!list.isEmpty()) {
            for (Teacher teacher : list) {
                output += "<tr id=\"teacher" + teacher.getId() + "\">\n";
                output += "<td class=\"id\">" + teacher.getId() + "</td>";
                output += "<td class=\"name\">" + teacher.getName() + "</td>";
                output += "<td class=\"birth\">" + teacher.getBirth() + "</td>";
                output += "<td class=\"gender\">" + teacher.getGender() + "</td>";
                output += "<td class=\"operations\">" +
                        "<a class=\"delete\" href=\"#deleteTeacher" + teacher.getId() + "\">Удалить</a><br>" +
                        "<a class=\"update\" href=\"#updateTeacher" + teacher.getId() + "\">Изменить</a><br>" +
                        "<a class=\"getInfo\" href=\"#getInfoTeacher" + teacher.getId() + "\">Информация</a>" +
                        "</td>";
                output += "</tr>";
            }
        } else {
            output += "<tr><td colspan=\"6\">Нет записей.</tr></td>";
        }
        return output;
    }
}
