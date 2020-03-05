package webdebugger;


import dbmodules.dao.GroupDAOimpl;
import dbmodules.entity.Group;
import dbmodules.entity.Student;
import dbmodules.daointerfaces.GroupDAO;
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
    public static LocalDate checkBirth(String birth) {
        try {;
            return LocalDate.parse(birth);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Group checkGroup
            (int number, GroupDAO groupDAO) {
        try {
            return groupDAO.select(number);
        } catch (InputMismatchException | IndexOutOfBoundsException e) {
            e.printStackTrace();
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
                    e.printStackTrace();
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
                    e.printStackTrace();
                    return null;
                }
            }
            case GROUP: {
                GroupDAOimpl groupDAO = new GroupDAOimpl();
                try {
                    Group group = groupDAO.select(Integer.parseInt(critVal));
                    if (!Objects.isNull(group)) {
                        return Integer.toString(group.getNumber());
                    } else {
                        return null;
                    }
                } catch (InputMismatchException
                        | IndexOutOfBoundsException
                        | NumberFormatException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            case BIRTH: {
                try {
                    LocalDate.parse(critVal);
                    return critVal;
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
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
                                                List<String> errors, JSONObject jsonObject) {
       if(!students.isEmpty()) {
           jsonObject.accumulate("students", students);
           for(Student student : students) {
               groups.put(student.getId(), student.getGroup().getNumber());
           }
           jsonObject.accumulate("groups", groups);
       } else {
           errors.add("Список пуст.");
       }
    }

    public static void setQueryParametres(HttpServletRequest request,
                                          HttpServletResponse response)
            throws UnsupportedEncodingException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
    }
}
