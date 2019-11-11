package dbmodules.main;

import dbmodules.dao.*;
import dbmodules.types.*;
import dbmodules.tables.*;
import hibernate.JPAUtil;

import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

import static dbmodules.types.Criteria.*;

public class Main {
    private static LocalDate checkBirth
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
    private static Criteria checkCriteria(Scanner input) {
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
    private static int getOpCode(Scanner input) {
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
    private static Gender checkGender(Scanner input) {
        String genderInput = input.next().toUpperCase();
        for(Gender gender : Gender.values()) {
            if(gender.getValue().equals(genderInput)) {
                return Gender.valueOf(genderInput);
            }
        }
        System.out.print("Invalid gender. Try again: ");
        return checkGender(input);
    }
    private static Group checkGroup
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
    private static String parseCriteria
            (Criteria criteria, Scanner input, GroupDAO groupDAO) {

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
                    return parseCriteria(criteria, input, groupDAO);
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
                    return parseCriteria(criteria, input, groupDAO);
                }
            }
            case GROUP :  {
                try {
                    Integer.parseInt(critVal);
                    return critVal;
                } catch (InputMismatchException e) {
                    System.out.print("Invalid group" +
                            " number. Try again: ");
                    return parseCriteria(criteria, input, groupDAO);
                }
            }
            case BIRTH : {
                try {
                    LocalDate.parse(critVal);
                    return critVal;
                } catch (DateTimeParseException e) {
                    System.out.print("Invalid " +
                            "birthday. Try again: ");
                    return parseCriteria(criteria, input, groupDAO);
                }
            }
            default : {
                return "";
            }
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
                        "to stop (student/group/teacher): ");
                tableName = input.next();
                input.nextLine();
                TableType table;
                if(tableName.equals("e")) {
                    System.out.println("Shutting down.");
                    JPAUtil.close();
                    System.exit(0);
                }
                try {
                    table = TableType.valueOf(tableName.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("No table with entered name. " +
                            "Try again. ");
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
                                case 0:  {
                                    System.out.print("Adding student. \nEnter name: ");
                                    String name = input.nextLine();

                                    System.out.println("Entering birthday.");
                                    LocalDate birthday = checkBirth(input);

                                    System.out.print("Enter gender (MALE/FEMALE): ");
                                    Gender gender = checkGender(input);

                                    System.out.print("Enter student's group number: ");
                                    Group group = checkGroup(input, groupDAO);

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
                                case 1:  {
                                    System.out.print("Selecting students. \n"
                                            + "Enter criteria ( ");
                                    for (Criteria criteria : values()) {
                                        System.out.print(criteria + " ");
                                    }
                                    System.out.print("): ");
                                    Criteria criteria = checkCriteria(input);

                                    System.out.println("Enter criteria value: ");
                                    String critVal = parseCriteria(criteria, input, groupDAO);


                                    for (Student student
                                            : studentDAO.select(criteria, critVal)
                                    ) {
                                        System.out.println(student);
                                    }
                                    break;
                                }
                                case 2:  {
                                    System.out.print("Updating student. " +
                                            "\nEnter id: ");
                                    int id;
                                    try {
                                        id = input.nextInt();
                                        input.nextLine();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid id. Abort operation.");
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

                                    Criteria criteria = checkCriteria(input);

                                    if(criteria.equals(ID)
                                            || criteria.equals(ALL)) {
                                        System.out.println("Incorrect criteria. Abort operation.");
                                        break;
                                    }

                                    System.out.print("Enter criteria value: ");
                                    String critVal = parseCriteria(criteria, input, groupDAO);

                                    studentDAO.update(student, criteria, critVal);
                                    System.out.println("Student updated.");
                                    break;
                                }
                                case 3:  {
                                    System.out.print("Deleting studetns. "
                                            + "\nCriterias ( ");
                                    for (Criteria criteria : values()) {
                                        System.out.print(criteria + " ");
                                    }
                                    System.out.print(") ");
                                    Criteria criteria = checkCriteria(input);

                                    System.out.print("Enter criteria value: ");
                                    String critVal = parseCriteria(criteria, input, groupDAO);

                                    if(criteria.equals(ID)
                                            && (studentDAO
                                            .selectById(Integer
                                                    .parseInt(critVal)) == null)) {
                                        studentDAO.selectById(Integer.parseInt(critVal));
                                        System.out.println("No student with given id. " +
                                                    "Abort operation.");
                                        break;
                                    }

                                    List<Student> studentsToDelete = studentDAO.select(criteria, critVal);

                                    for(Student student : studentsToDelete) {
                                        studentDAO.delete(student);
                                    }
                                    System.out.println("Student deleted.");
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
                                    Gender gender = checkGender(input);

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
                                    Criteria criteria = checkCriteria(input);

                                    String critVal = parseCriteria(criteria, input, groupDAO);


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

                                    Criteria criteria = checkCriteria(input);
                                    if(criteria.equals(ID)
                                            || criteria.equals(GROUP)
                                            || criteria.equals(ALL)) {
                                        System.out.println("Incorrect criteria. Abort operation.");
                                        break;
                                    }

                                    System.out.print("Enter criteria value: ");
                                    String critVal =
                                            parseCriteria(criteria, input, groupDAO);

                                    teacherDAO
                                            .update(teacher, criteria, critVal);
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

                                    Criteria criteria = checkCriteria(input);

                                    String critVal =
                                            parseCriteria(criteria, input, groupDAO);

                                    List<Teacher> teachersToDelete =
                                            teacherDAO
                                                    .select(criteria, critVal);

                                    try {
                                        for(Teacher teacher : teachersToDelete) {
                                            teacherDAO.delete(teacher);
                                        }
                                        System.out.println("Teacher deleted.");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println("This teacher " +
                                                "is working in some groups. " +
                                                "Unbind them first.");
                                    }
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

                                    Teacher teacher = teacherDAO.selectById(id);
                                    if(Objects.isNull(teacher)) {
                                        System.out.println("No teacher " +
                                                "with given id. Abort.");
                                        break;
                                    }

                                    System.out.print("Enter group number: ");
                                    Group group = checkGroup(input, groupDAO);
                                    if (Objects.isNull(group)) {
                                        System.out.println("No " +
                                                "group with given number. " +
                                                "Try again.");
                                        break;
                                    }

                                    try {
                                        teacherDAO.putTeacherInGroup(teacher, group);
                                    } catch (IllegalStateException e) {
                                        System.out.println("This teacher " +
                                                "is already " +
                                                "teach this group.");
                                        break;
                                    }
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
                                        System.out.println("Invalid id. " +
                                                "Try again");
                                        input.nextLine();
                                        break;
                                    }

                                    List<Group> list;
                                    try {
                                        list = teacherDAO.getTeacherGroups(id);
                                    } catch (NullPointerException e) {
                                        System.out.println("No " +
                                                "teacher with given id.");
                                        break;
                                    }

                                    for(Group group : list) {
                                        System.out.println(group);
                                    }
                                    break;
                                }
                                case 6 :  {
                                    System.out.print("Removing " +
                                            "teacher from group." +
                                            " \nEnter teacher id: ");
                                    int id;
                                    try {
                                        id = input.nextInt();
                                        input.nextLine();
                                    } catch (InputMismatchException e) {
                                        System.out
                                                .println("Invalid id. Try again.");
                                        input.nextLine();
                                        break;
                                    }

                                    System.out.print("Enter group number: ");
                                    try {
                                        Group group = checkGroup(input, groupDAO);
                                        Teacher teacher = teacherDAO.
                                                selectById(id);
                                        teacherDAO.
                                                removeTeacherFromGroup(teacher, group);
                                    } catch (NullPointerException e) {
                                        System.out.println("No teacher " +
                                                "or group. Try again.");
                                        break;
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Query error. " +
                                                "Check entered group/teacher.");
                                        break;
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
                                case 0  :  {
                                    System.out.print("Adding Group. \nEnter number: ");
                                    int number;
                                    try {
                                        number = input.nextInt();
                                        input.nextLine();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid group number value. Try again.");
                                        input.nextLine();
                                        break;
                                    }


                                    try {
                                        Group groupCheck = groupDAO.select(number);
                                        System.out.print(groupCheck);
                                        if(!Objects.isNull(groupCheck)) {
                                            System.out.println("Group with given number is exist." +
                                                    " Select another number.");
                                            break;
                                        }
                                    } catch (IndexOutOfBoundsException e) {
                                        System.out.println("Correct.");
                                    }


                                    groupDAO.add(new Group(number));
                                    System.out.println("Group added.");
                                    break;
                                }
                                case 1  :  {
                                    System.out.print("Selecting group. \n"
                                            + "Enter group number: ");

                                    System.out.println(checkGroup(input, groupDAO));

                                    break;
                                }
                                case 2  :  {
                                    System.out.println("Selecting all...");
                                    for(Group group : groupDAO.selectAll()) {
                                        System.out.println(group);
                                    }
                                    break;
                                }
                                case 3  :  {
                                    System.out.print("Deleting group. "
                                            + "\nEnter number: ");

                                    int number;
                                    try {
                                        number = input.nextInt();
                                        input.nextLine();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid " +
                                                "group number value." +
                                                " Try again.");
                                        input.nextLine();
                                        break;
                                    }

                                    Group group;
                                    try {
                                        group = groupDAO.select(number);
                                    } catch (NullPointerException e) {
                                        System.out.println("No group " +
                                                "with given number. " +
                                                "Abort operation..");
                                        break;
                                    }

                                    if(!studentDAO.select(GROUP,
                                            Integer.toString(number))
                                            .isEmpty()) {
                                        System.out.println("Cannot " +
                                                "delete group. " +
                                                "Some students is in." +
                                                " Unbind them first.");
                                        break;
                                    }

                                    groupDAO.delete(group);
                                    System.out.print("Group deleted.");
                                    break;
                                }
                                default :  {
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
                        break;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Some error occured.");
            e.printStackTrace();
        } finally {
            JPAUtil.close();
        }
    }
}