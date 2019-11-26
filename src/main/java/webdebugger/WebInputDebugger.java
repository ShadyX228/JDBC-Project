package webdebugger;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

import static dbmodules.types.Criteria.*;

public class WebInputDebugger {
    static LocalDate checkBirth
            (Scanner input) {
        try {
            System.out.print("Enter year: ");
            int year = input.nextInt();

            System.out.print("Enter month: ");
            int month = input.nextInt();

            System.out.print("Enter day: ");
            int day = input.nextInt();

            input.nextLine();

            return LocalDate.of(year, month, day);
        } catch (DateTimeParseException e) {
            input.nextLine();
            System.out.print("Invalid birthday. Try again: ");
            return checkBirth(input);
        } catch (InputMismatchException e) {
            input.nextLine();
            System.out.print("Int required in date. Try again: ");
            return checkBirth(input);
        }

    }
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
        for(Gender gender : Gender.values()) {
            if(gender.getValue().equals(genderInput)) {
                return Gender.valueOf(genderInput);
            }
        }
        return null;
    }
    public static Group checkGroup
            (int number, GroupDAO groupDAO) {
        try {
            return groupDAO.select(number);
        } catch (InputMismatchException e) {
            return null;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    public static String parseCriteria
            (Criteria criteria, String critVal) {
        switch (criteria) {
            case ID :  {
                try {
                    Integer.parseInt(critVal);
                    return critVal;
                } catch (NumberFormatException e) {
                    return null;
                }

            }
            case NAME : {
                return critVal;
            }
            case GENDER : {
                try {
                    Gender.valueOf(critVal.toUpperCase());
                    return critVal.toUpperCase();
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
            case GROUP :  {
                try {
                    Group group = new GroupDAO().select(Integer.parseInt(critVal));
                    return Integer.toString(group.getNumber());
                } catch (InputMismatchException e) {
                    return null;
                } catch (IndexOutOfBoundsException e) {
                    return null;
                }
            }
            case BIRTH : {
                try {
                    LocalDate.parse(critVal);
                    return critVal;
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
            case ALL : {
                return "";
            }
        }
        return null;
    }

    public static String printMessage(int status, String message) {
        switch(status) {
            case 1 : {
                return " - <span class=\"OK\">" + message + "</span><br>";
            }
            case 2 : {
                return " - <span class=\"error\">" + message + "</span><br>";
            }
            default: {
                return " - " + message + "<br>";
            }
        }
    }

}
