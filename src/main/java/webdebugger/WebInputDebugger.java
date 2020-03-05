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
}
