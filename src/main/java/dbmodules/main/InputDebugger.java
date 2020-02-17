package dbmodules.main;

import dbmodules.dao.GroupDAO;
import dbmodules.entity.Group;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static dbmodules.types.Criteria.*;
import static dbmodules.types.Criteria.ALL;

public class InputDebugger {
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
    static Criteria checkCriteria(Scanner input) {
        String crit = input.next();
        input.nextLine();
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

        System.out.print("Invalid criteria. Try again: ");
        return checkCriteria(input);
    }
    static int getOpCode(Scanner input) {
        try {
            int opCode = input.nextInt();
            input.nextLine();
            return opCode;
        } catch (InputMismatchException e) {
            System.out.print("Invalid operation code. Try again: ");
            input.nextLine();
            return getOpCode(input);
        }
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
    static String parseCriteria
            (Criteria criteria, Scanner input) {

        String critVal = input.next();
        input.nextLine();
        switch (criteria) {
            case ID :  {
                try {
                    Integer.parseInt(critVal);
                    return critVal;
                } catch (NumberFormatException e) {
                    System.out.print("Invalid id. " +
                            "Try again: ");
                    return parseCriteria(criteria, input);
                }
            }
            case NAME : {
                return critVal;
            }
            case GENDER : {
                try {
                    System.out.print("Enter gender: ");
                    Gender.valueOf(critVal.toUpperCase());
                    return critVal.toUpperCase();
                } catch (IllegalArgumentException e) {
                    System.out.print("Invalid gender. " +
                            "Try again: ");
                    return parseCriteria(criteria, input);
                }
            }
            case GROUP :  {
                try {
                    return Integer.toString(Integer.parseInt(critVal));
                } catch (InputMismatchException e) {
                    System.out.print("Invalid group" +
                            " number. Try again: ");
                    return parseCriteria(criteria, input);
                }
            }
            case BIRTH : {
                try {
                    LocalDate.parse(critVal);
                    return critVal;
                } catch (DateTimeParseException e) {
                    System.out.print("Invalid " +
                            "birthday. Try again: ");
                    return parseCriteria(criteria, input);
                }
            }
            default : {
                return "";
            }
        }
    }

}
