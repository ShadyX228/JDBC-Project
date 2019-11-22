package webinputdebugger;

import dbmodules.dao.GroupDAO;
import dbmodules.tables.Group;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
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

    static Gender checkGender(Scanner input) {
        String genderInput = input.next().toUpperCase();
        for(Gender gender : Gender.values()) {
            if(gender.getValue().equals(genderInput)) {
                return Gender.valueOf(genderInput);
            }
        }
        System.out.print("Invalid gender. Try again: ");
        return checkGender(input);
    }
    protected static Group checkGroup
            (Scanner input, GroupDAO groupDAO) {
        try {
            int number = input.nextInt();
            return groupDAO.select(number);
        } catch (InputMismatchException e) {
            System.out.print("Invalid group number. Try again: ");
            input.nextLine();
            return checkGroup(input, groupDAO);
        } catch (IndexOutOfBoundsException e) {
            System.out.print("Group is not exist. Try again: ");
            input.nextLine();
            return checkGroup(input, groupDAO);
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
                    return Integer.toString(Integer.parseInt(critVal));
                } catch (InputMismatchException e) {
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
        }
        return null;
    }

}
