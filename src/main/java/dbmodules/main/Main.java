package dbmodules.main;

import dbmodules.dao.*;
import dbmodules.types.*;
import dbmodules.tables.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

import static dbmodules.types.Criteria.*;

public class Main {
    // исправить null-pointer в последних методах учителя.
    private static LocalDate checkBirth(Scanner input) {
        try {
            System.out.print("Enter year: ");
            int year = input.nextInt();

            System.out.print("Enter month: ");
            int month = input.nextInt();

            System.out.print("Enter day: ");
            int day = input.nextInt();

            input.nextLine();

            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            input.nextLine();
            System.out.println("Invalid birthday. Try again.");
            return checkBirth(input);
        }
    }
    private static boolean checkCriteria(String crit) {
        try {
            Criteria.valueOf(crit);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid criteria. Try again.");
            return false;
        }
    }
    public static int getOpCode(Scanner input) {
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


    public static void main(String[] args) {
        try {
            StudentDAO studentDAO = new StudentDAO();
            TeacherDAO teacherDAO = new TeacherDAO();
            GroupDAO groupDAO = new GroupDAO();
            Scanner input = new Scanner(System.in);
            String tableName;

            while(true) {
                System.out.print("Enter table name or \"e\" " +
                        "to stop (string): ");
                tableName = input.next();
                input.nextLine();
                TableType table;
                if(tableName.equals("e")) {
                    System.out.println("Shutting down.");
                    System.exit(0);
                }
                try {
                    table = TableType.valueOf(tableName.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("No table with entered name. " +
                            "Try again.");
                    continue;
                }

                switch (table) {
                    case STUDENT : {
                        System.out.println("Operation: \t add \t select " +
                                "\t update \t delete");
                        System.out.println("Code: \t\t 0 \t\t 1 " +
                                "\t\t\t 2 \t\t\t 3");
                        System.out.print("Enter operation code or -1 to exit: ");
                        int opCode = getOpCode(input);

                        while(opCode != -1) {
                            switch (opCode) {
                                case 0: {
                                    System.out.print("Adding student. \nEnter name: ");
                                    String name = input.nextLine();

                                    System.out.println("Entering birthday.");
                                    LocalDate birthday = checkBirth(input);

                                    System.out.print("Enter gender (MALE/FEMALE): ");
                                    String genderInput = input.next();
                                    Gender gender;
                                    try {
                                        gender = Gender.valueOf(genderInput.toUpperCase());
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid gender. Try again.");
                                        break;
                                    }

                                    System.out.print("Enter student's group number: ");
                                    int number = 0;
                                    try {
                                        number = input.nextInt();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid group number. Try again.");
                                        input.nextLine();
                                        break;
                                    }
                                    Group group = groupDAO.select(number);
                                    if (Objects.isNull(group)) {
                                        System.out.println("No group with given number. Try again.");
                                        break;
                                    }

                                    studentDAO.add(new Student(
                                            name,
                                            birthday.getYear(),
                                            birthday.getMonth().getValue(),
                                            birthday.getDayOfMonth(),
                                            gender,
                                            group));
                                    System.out.println("Student added.");
                                    break;
                                }
                                case 1: {
                                    System.out.print("Selecting students. \n"
                                            + "Enter criteria ( ");
                                    for (Criteria criteria : values()) {
                                        System.out.print(criteria + " ");
                                    }
                                    System.out.print("): ");
                                    String crit = input.next().toUpperCase();
                                    input.nextLine();
                                    if (!checkCriteria(crit)) {
                                        break;
                                    }
                                    Criteria criteria = valueOf(crit);
                                    String critVal;

                                    if (!criteria.equals(ALL)) {
                                        System.out.print("Enter criteria value: ");
                                        critVal = input.nextLine();
                                    } else {
                                        critVal = "";
                                    }
                                    if (criteria.equals(ID)) {
                                        try {
                                            Integer.parseInt(critVal);
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid id. " +
                                                    "Try again");
                                            break;
                                        }
                                    }
                                    if (criteria.equals(GENDER)) {
                                        try {
                                            Gender.valueOf(critVal.toUpperCase());
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Invalid gender. " +
                                                    "Try again.");
                                            break;
                                        }
                                    }
                                    if (criteria.equals(GROUP)) {
                                        try {
                                            Group groupCheck = groupDAO.select(
                                                    Integer.parseInt(critVal));
                                            if (Objects.isNull(groupCheck)) {
                                                System.out.println("No group with " +
                                                        "given number. Try again.");
                                                break;
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid group" +
                                                    " number. Try again.");
                                            break;
                                        }
                                    }
                                    if (criteria.equals(BIRTH)) {
                                        try {
                                            LocalDate d = LocalDate.parse(critVal);
                                            //if (!checkBirth(
                                            //        d.getYear(),
                                            //        d.getMonth().getValue(),
                                            //        d.getDayOfMonth())) {
                                            //    break;
                                            //}
                                        } catch (DateTimeParseException e) {
                                            System.out.println("Invalid " +
                                                    "birthday. Try again.");
                                            break;
                                        }

                                    }
                                    for (Student student
                                            : studentDAO.select(criteria, critVal)
                                    ) {
                                        System.out.println(student);
                                    }
                                    break;
                                }
                                case 2: {
                                    System.out.print("Updating student. " +
                                            "\nEnter id: ");
                                    int id;
                                    try {
                                        id = input.nextInt();
                                        input.nextLine();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid id. Try again");
                                        input.nextLine();
                                        break;
                                    }
                                    Student student = studentDAO.selectById(id);

                                    if (Objects.isNull(student)) {
                                        System.out.println("No student " +
                                                "with given id. Try again.");
                                        break;
                                    }

                                    System.out.print("Enter criteria " +
                                            "wich need to update ( ");
                                    for (Criteria criteria : values()) {
                                        if (!criteria.equals(ALL)
                                                && !criteria.equals(ID)) {
                                            System.out.print(criteria + " ");
                                        }
                                    }
                                    System.out.print("): ");
                                    String crit = input.next().toUpperCase();
                                    input.nextLine();
                                    if (!checkCriteria(crit)) {
                                        break;
                                    }
                                    Criteria criteria = valueOf(crit);

                                    System.out.print("Enter criteria value: ");
                                    String critVal = input.nextLine();

                                    if (criteria.equals(GENDER)) {
                                        try {
                                            Gender.valueOf(critVal.toUpperCase());
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Invalid" +
                                                    " gender. Try again.");
                                            break;
                                        }
                                    }
                                    if (criteria.equals(GROUP)) {
                                        try {
                                            Group groupCheck = groupDAO.select(
                                                    Integer.parseInt(critVal));
                                        } catch (Exception e) {
                                            System.out.println("Invalid group " +
                                                    "number or " +
                                                    "group with given number doesn't exist. Try again.");
                                            break;
                                        }
                                    }
                                    if (criteria.equals(BIRTH)) {
                                        try {
                                            LocalDate d = LocalDate.parse(critVal);
                                            //if (!checkBirth(
                                            //        d.getYear(),
                                            //        d.getMonth().getValue(),
                                            //        d.getDayOfMonth())) {
                                            //    break;
                                            //}
                                        } catch (DateTimeParseException e) {
                                            System.out.println("Invalid " +
                                                    "birthday. Try again.");
                                            break;
                                        }
                                    }
                                    studentDAO.update(student, criteria, critVal);
                                    System.out.println("Student updated.");
                                    break;
                                }
                                case 3:  {
                                    System.out.print("Deleting studetns. "
                                            + "\nEnter criteria ( ");
                                    for (Criteria criteria : values()) {
                                        System.out.print(criteria + " ");
                                    }
                                    System.out.print(") ");
                                    String crit = input.next().toUpperCase();
                                    input.nextLine();
                                    if (!checkCriteria(crit)) {
                                        break;
                                    }
                                    Criteria criteria = valueOf(crit);

                                    System.out.print("Enter criteria value: ");
                                    String critVal = input.nextLine();

                                    if (criteria.equals(ID)) {
                                        try {
                                            Integer.parseInt(critVal);
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid " +
                                                    "id. Try again");
                                            break;
                                        }
                                    }
                                    if (criteria.equals(GENDER)) {
                                        try {
                                            Gender.valueOf(critVal.toUpperCase());
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Invalid " +
                                                    "gender. Try again.");
                                            break;
                                        }
                                    }
                                    if (criteria.equals(GROUP)) {
                                        try {
                                            Group groupCheck = groupDAO.select(
                                                    Integer.parseInt(critVal));
                                            if (Objects.isNull(groupCheck)) {
                                                System.out.println("No group with " +
                                                        "given number. Try again.");
                                                break;
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid group number. " +
                                                    "Try again.");
                                            break;
                                        }
                                    }
                                    if (criteria.equals(BIRTH)) {
                                        try {
                                            LocalDate d = LocalDate.parse(critVal);
                                            //if (!checkBirth(
                                            //        d.getYear(),
                                            //        d.getMonth().getValue(),
                                            //        d.getDayOfMonth())) {
                                            //    break;
                                            //}
                                        } catch (DateTimeParseException e) {
                                            System.out.println("Invalid birthday." +
                                                    " Try again.");
                                            break;
                                        }
                                    }
                                    studentDAO.delete(criteria, critVal);
                                    System.out.println("Student deleted.");
                                    break;
                                }
                                case -1: {
                                    break;
                                }
                                default: {
                                    System.out.println("Invalid " +
                                            "operation code. Try again.");
                                    break;
                                }
                            }
                            System.out.print("\nEnter operation code or -1 to exit (int): ");
                            try {
                                opCode = input.nextInt();
                                input.nextLine();
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid operation code. Try again.");
                                input.nextLine();
                                break;
                            }
                            System.out.println();
                        }
                        break;
                    }
                    case TEACHER : {
                        System.out.println("Operation: \t add \t select " +
                                "\t update \t delete " +
                                "\t putTeacherInGroup \t " +
                                "\t getTeachersGroup \t" +
                                "removeTeacherFromGroup");
                        System.out.println("Code: \t\t 0 " +
                                "\t\t 1 \t\t\t 2 \t\t\t 3 " +
                                "\t\t\t 4 \t\t\t\t\t\t 5" +
                                "\t\t\t\t\t6");
                        System.out.print("Enter operation code: ");
                        int opCode = getOpCode(input);

                        while(opCode != -1) {
                            switch (opCode) {
                                case 0 :  {
                                    System.out.print("Adding teacher. " +
                                            "\nEnter name: ");
                                    String name = input.nextLine();

                                    System.out.print("Entering birthday. " +
                                            "Enter year: ");

                                    LocalDate birthday = checkBirth(input);

                                    System.out.print("Enter gender " +
                                            "(MALE/FEMALE): ");
                                    String genderInput = input.next();
                                    Gender gender;
                                    try {
                                        gender = Gender.valueOf(genderInput.toUpperCase());
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid gender. Try again.");
                                        break;
                                    }

                                    teacherDAO.add(new Teacher(
                                            name,
                                            birthday.getYear(),
                                            birthday.getMonth().getValue(),
                                            birthday.getDayOfMonth(),
                                            gender));
                                    System.out.println("Teacher added.");
                                    break;
                                }
                                case 1 :  {
                                    System.out.print("Selecting teachers. \n"
                                            + "Enter criteria ( ");
                                    for (Criteria criteria : values()) {
                                        if(!criteria.equals(GROUP)) {
                                            System.out.print(criteria + " ");
                                        }
                                    }
                                    System.out.print("): ");
                                    String crit = input.next().toUpperCase();
                                    input.nextLine();
                                    if(!checkCriteria(crit)) {
                                        break;
                                    }
                                    Criteria criteria = valueOf(crit);
                                    String critVal;

                                    if (!criteria.equals(ALL)) {
                                        System.out.print("Enter criteria value: ");
                                        critVal = input.nextLine();
                                    } else {
                                        critVal = "";
                                    }
                                    if (criteria.equals(ID)) {
                                        try {
                                            Integer.parseInt(critVal);
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid " +
                                                    "id. Try again");
                                            break;
                                        }
                                    }
                                    if (criteria.equals(GENDER)) {
                                        try {
                                            critVal = critVal.toUpperCase();
                                            Gender.valueOf(critVal.toUpperCase());

                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Invalid gender. " +
                                                    "Try again.");
                                            break;
                                        }
                                    }
                                    if (criteria.equals(BIRTH)) {
                                        try {
                                            LocalDate d = LocalDate.parse(critVal);
                                            //if (!checkBirth(
                                            //        d.getYear(),
                                            //        d.getMonth().getValue(),
                                            //        d.getDayOfMonth())) {
                                            //    break;
                                            //}
                                        } catch (DateTimeParseException e ) {
                                            System.out.println("Invalid " +
                                                    "birthday. Try again.");
                                            break;
                                        }

                                    }
                                    for (Teacher teacher
                                            : teacherDAO.select(criteria, critVal)
                                    ) {
                                        System.out.println(teacher);
                                    }
                                    break;
                                }
                                case 2 :  {
                                    System.out.print("Updating teacher. " +
                                            "\nEnter id: ");
                                    int id;
                                    try {
                                        id = input.nextInt();
                                        input.nextLine();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid id. " +
                                                "Try again");
                                        input.nextLine();
                                        break;
                                    }
                                    Teacher teacher = teacherDAO.selectById(id);

                                    if(Objects.isNull(teacher)) {
                                        System.out.println("No teacher " +
                                                "with given id. Try again.");
                                        break;
                                    }

                                    System.out.print("Enter criteria " +
                                            "wich need to update ( ");
                                    for (Criteria criteria : values()) {
                                        if (!criteria.equals(ALL)
                                                && !criteria.equals(ID)
                                                && !criteria.equals(GROUP)) {
                                            System.out.print(criteria + " ");
                                        }
                                    }
                                    System.out.print("): ");
                                    String crit = input.next().toUpperCase();
                                    input.nextLine();
                                    if(!checkCriteria(crit)) {
                                        break;
                                    }
                                    Criteria criteria = valueOf(crit);

                                    System.out.print("Enter criteria value: ");
                                    String critVal = input.nextLine();

                                    if (criteria.equals(GENDER)) {
                                        try {
                                            Gender.valueOf(critVal.toUpperCase());
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Invalid " +
                                                    "gender. Try again.");
                                            break;
                                        }
                                    }
                                    if (criteria.equals(BIRTH)) {
                                        try {
                                            LocalDate d = LocalDate.parse(critVal);
                                            //if (!checkBirth(
                                            //        d.getYear(),
                                            //        d.getMonth().getValue(),
                                            //        d.getDayOfMonth())) {
                                            //    break;
                                            //}
                                        } catch (DateTimeParseException e ) {
                                            System.out.println("Invalid " +
                                                    "birthday. Try again.");
                                            break;
                                        }
                                    }
                                    teacherDAO.update(teacher, criteria, critVal);
                                    System.out.println("Teacher updated.");
                                    break;
                                }
                                case 3 :  {
                                    System.out.print("Deleting teachers. "
                                            + "\nEnter criteria ( ");
                                    for (Criteria criteria : values()) {
                                        if(!criteria.equals(GROUP)) {
                                            System.out.print(criteria + " ");
                                        }
                                    }
                                    System.out.print(") ");
                                    String crit = input.next().toUpperCase();
                                    input.nextLine();
                                    if(!checkCriteria(crit)) {
                                        break;
                                    }
                                    Criteria criteria = valueOf(crit);

                                    System.out.print("Enter criteria value: ");
                                    String critVal = input.nextLine();

                                    if (criteria.equals(ID)) {
                                        try {
                                            Integer.parseInt(critVal);
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid id. Try again");
                                            break;
                                        }
                                    }
                                    if (criteria.equals(GENDER)) {
                                        try {
                                            Gender.valueOf(critVal.toUpperCase());
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Invalid gender. Try again.");
                                            break;
                                        }
                                    }
                                    if (criteria.equals(BIRTH)) {
                                        try {
                                            LocalDate d = LocalDate.parse(critVal);
                                            //if (!checkBirth(
                                            //        d.getYear(),
                                            //        d.getMonth().getValue(),
                                            //        d.getDayOfMonth())) {
                                            //    break;
                                            //}
                                        } catch (DateTimeParseException e ) {
                                            System.out.println("Invalid birthday. Try again.");
                                            break;
                                        }
                                    }
                                    studentDAO.delete(criteria, critVal);
                                    System.out.println("Teacher deleted.");
                                    break;
                                }
                                case 4 :  {
                                    System.out.print("Putting teacher in group." +
                                            " \nEnter teacher id: ");
                                    int id;
                                    try {
                                        id = input.nextInt();
                                        input.nextLine();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid id. Try again");
                                        input.nextLine();
                                        break;
                                    }

                                    System.out.print("Enter group number: ");
                                    int number;
                                    try {
                                        number = input.nextInt();
                                        input.nextLine();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid group number. Try again");
                                        input.nextLine();
                                        break;
                                    }

                                    Teacher teacher = teacherDAO.selectById(id);
                                    Group group = groupDAO.select(number);

                                    if (Objects.isNull(teacher)) {
                                        System.out.println("Invalid teacher id. Try again.");
                                        break;
                                    }
                                    if (Objects.isNull(group)) {
                                        System.out.println("No group with given number. " +
                                                "Try again.");
                                        break;
                                    }
                                    teacherDAO.putTeacherInGroup(teacher, group);
                                    break;
                                }
                                case 5 :  {
                                    System.out.print("Getting teacher groups." +
                                            " \nEnter teacher id: ");
                                    int id;
                                    try {
                                        id = input.nextInt();
                                        input.nextLine();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid id. Try again");
                                        input.nextLine();
                                        break;
                                    }
                                    List<Group> list = teacherDAO.getTeacherGroups(id);
                                    for(Group group : list) {
                                        System.out.println(group);
                                    }
                                    break;
                                }
                                case 6 :  {
                                    System.out.print("Putting teacher in group." +
                                            " \nEnter teacher id: ");
                                    int id;
                                    try {
                                        id = input.nextInt();
                                        input.nextLine();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid id. Try again");
                                        input.nextLine();
                                        break;
                                    }

                                    System.out.print("Enter group number: ");
                                    int number;
                                    try {
                                        number = input.nextInt();
                                        input.nextLine();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid group number. Try again");
                                        input.nextLine();
                                        break;
                                    }


                                    try {
                                        Teacher teacher = teacherDAO.selectById(id);
                                        Group group = groupDAO.select(number);
                                        teacherDAO.removeTeacherFromGroup(teacher, group);
                                    } catch (NullPointerException e) {
                                        System.out.println("No teacher or group. Try again.");
                                    }

                                    break;
                                }
                                default : {
                                    System.out.println("Invalid operation code. Try again.");
                                    break;
                                }
                            }
                            System.out.print("\nEnter operation code or -1 to exit (int): ");
                            try {
                                opCode = input.nextInt();
                                input.nextLine();
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid operation code. Returning to table select..");
                                input.nextLine();
                                break;
                            }
                            System.out.println();
                        }
                        System.out.println();
                        break;
                    }
                    case GROUP   : {
                        System.out.println("Operation: \t add \t select " +
                                "\t selectAll \t delete");
                        System.out.println("Code: \t\t 0 " +
                                "\t\t 1 \t\t\t 2 \t\t\t 3");
                        System.out.print("Enter operation code (int): ");
                        int opCode = getOpCode(input);

                        while(opCode != -1) {
                            switch (opCode) {
                                case 0 :  {
                                    System.out.print("Adding Group. \nEnter number: ");
                                    int number = 0;
                                    try {
                                        number = input.nextInt();
                                        input.nextLine();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid group number value. Try again.");
                                        input.nextLine();
                                        break;
                                    }


                                    Group groupCheck = groupDAO.select(number);
                                    if(!Objects.isNull(groupCheck)) {
                                        System.out.println("Group with given number is exist." +
                                                " Select another number.");
                                        break;
                                    }

                                    groupDAO.add(new Group(number));
                                    System.out.println("Group added.");
                                    break;
                                }
                                case 1 :  {
                                    System.out.print("Selecting group. \n"
                                            + "Enter group number: ");

                                    int number = 0;
                                    try {
                                        number = input.nextInt();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid group number value. Try again.");
                                        input.nextLine();
                                        break;
                                    }
                                    input.nextLine();
                                    try {
                                        System.out.println(groupDAO.select(number));
                                    } catch (IndexOutOfBoundsException e ) {
                                        System.out.println("Group with given number doesn't exist." +
                                                " Select another number.");
                                    }
                                    break;
                                }
                                case 2 :  {
                                    System.out.println("Selecting all...");
                                    for(Group group : groupDAO.selectAll()) {
                                        System.out.println(group);
                                    }
                                    break;
                                }
                                case 3 :  {
                                    System.out.print("Deleting group. "
                                            + "\nEnter number: ");

                                    int number = 0;
                                    try {
                                        number = input.nextInt();
                                        input.nextLine();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid group number value. Try again.");
                                        input.nextLine();
                                        break;
                                    }

                                    Group group = null;
                                    try {
                                        group = groupDAO.select(number);
                                    } catch (NullPointerException e) {
                                        System.out.println("No group with given number. Try again.");
                                        break;
                                    }

                                    if(!studentDAO.select(GROUP, Integer.toString(number)).isEmpty()) {
                                        System.out.println("Cannot delete group. Some students is in." +
                                                " Unbind them first.");
                                        break;
                                    }

                                    groupDAO.delete(group);
                                    System.out.print("Group deleted.");
                                    break;
                                }
                                default : {
                                    System.out.println("Invalid operation code. Try again.");
                                    break;
                                }
                            }
                            System.out.print("\nEnter operation code or -1 to exit (int): ");
                            try {
                                opCode = input.nextInt();
                                input.nextLine();
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid operation code. Try again.");
                                input.nextLine();
                                break;
                            }
                            System.out.println();
                        }
                        System.out.println();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Some error occured.");
            e.printStackTrace();
        }
    }
}