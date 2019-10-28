package dbmodules.main;

import dbmodules.dao.*;
import dbmodules.types.*;
import dbmodules.tables.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

import static dbmodules.types.Criteria.*;

public class Main {
    private static boolean checkBirth(int year, int month, int day) {
        try {
            LocalDate.of(year, month, day);
            return true;
        } catch (DateTimeException e) {
            System.out.println("Invalid birthday. Try again.");
            return false;
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

    public static void main(String[] args) {
        try {
            StudentDAO sd = new StudentDAO();
            TeacherDAO td = new TeacherDAO();
            GroupDAO gd = new GroupDAO();
            Scanner in = new Scanner(System.in);

            String tableName = "";

            while(!tableName.equals("e")) {
                System.out.print("Enter table name (string): ");
                tableName = in.next();
                in.nextLine();
                TableType table;
                try {
                    table = TableType.valueOf(tableName.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("No table with entered name. Try again.");
                    continue;
                }


                switch (table) {
                    case STUDENT : {
                        System.out.println("Operation: \t add \t select " +
                                "\t update \t delete");
                        System.out.println("Code: \t\t 0 \t\t 1 " +
                                "\t\t\t 2 \t\t\t 3 \n");
                        System.out.print("Enter operation code (int): ");
                        int opCode = 0;
                        try {
                            opCode = in.nextInt();
                            in.nextLine();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid operation code. Try again.");
                            in.nextLine();
                            break;
                        }
                        switch (opCode) {
                            case 0 :  {
                                System.out.print("Adding student. \nEnter name: ");
                                String name = in.nextLine();

                                System.out.print("Entering birthday. Enter year: ");
                                int year;
                                int month;
                                int day;
                                try {
                                    year = in.nextInt();

                                    System.out.print("Enter month: ");
                                    month = in.nextInt();

                                    System.out.print("Enter day: ");
                                    day = in.nextInt();
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid birthday. Try again.");
                                    in.nextLine();
                                    break;
                                }

                                if(!checkBirth(year, month, day)) {
                                    break;
                                }

                                System.out.print("Enter gender (MALE/FEMALE): ");
                                String genderInput = in.next();
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
                                    number = in.nextInt();
                                } catch (InputMismatchException e ) {
                                    System.out.println("Invalid group number. Try again.");
                                    in.nextLine();
                                    break;
                                }
                                Group group = gd.select(number);
                                if (Objects.isNull(group)) {
                                    System.out.println("No group with given number. Try again.");
                                    break;
                                }
                                int group_id = group.getId();
                                sd.add(new Student(
                                        name,
                                        year, month, day,
                                        gender,
                                        group_id));
                                System.out.println("Student added.");
                                break;
                            }
                            case 1 :  {
                                System.out.print("Selecting students. \n"
                                        + "Enter criteria ( ");
                                for (Criteria criteria1 : values()) {
                                    System.out.print(criteria1 + " ");
                                }
                                System.out.print("): ");
                                String crit = in.next().toUpperCase();
                                in.nextLine();
                                if(!checkCriteria(crit)) {
                                    break;
                                }
                                Criteria criteria = valueOf(crit);
                                String critVal;

                                if (!criteria.equals(ALL)) {
                                    System.out.print("Enter criteria value: ");
                                    critVal = in.nextLine();
                                } else {
                                    critVal = "";
                                }
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
                                        Gender.valueOf(critVal);
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid gender. Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(GROUP)) {
                                    try {
                                        Group group_check = gd.select(Integer.parseInt(critVal));
                                        if (Objects.isNull(group_check)) {
                                            System.out.println("No group with given number. Try again.");
                                            break;
                                        }
                                    } catch (NumberFormatException e ) {
                                        System.out.println("Invalid group number. Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(BIRTH)) {
                                    try {
                                        LocalDate d = LocalDate.parse(critVal);
                                        if (!checkBirth(
                                                d.getYear(),
                                                d.getMonth().getValue(),
                                                d.getDayOfMonth())) {
                                            break;
                                        }
                                    } catch (DateTimeParseException e ) {
                                        System.out.println("Invalid birthday. Try again.");
                                        break;
                                    }

                                }
                                for (Student student
                                        : sd.select(criteria, critVal)
                                ) {
                                    System.out.println(student);
                                }
                                break;
                            }
                            case 2 :  {
                                System.out.print("Updating student. \nEnter id: ");
                                int id;
                                try {
                                    id = in.nextInt();
                                    in.nextLine();
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid id. Try again");
                                    in.nextLine();
                                    break;
                                }
                                Student student = sd.selectById(id);

                                if(Objects.isNull(student)) {
                                    System.out.println("No student with given id. Try again.");
                                    break;
                                }

                                System.out.print("Enter criteria " +
                                        "wich need to update ( ");
                                for (Criteria cr : values()) {
                                    if (!cr.equals(ALL) && !cr.equals(ID)) {
                                        System.out.print(cr + " ");
                                    }
                                }
                                System.out.print("): ");
                                String crit = in.next().toUpperCase();
                                in.nextLine();
                                if(!checkCriteria(crit)) {
                                    break;
                                }
                                Criteria criteria = valueOf(crit);

                                System.out.print("Enter criteria value: ");
                                String critVal = in.nextLine();

                                if (criteria.equals(GENDER)) {
                                    try {
                                        Gender.valueOf(critVal);
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid gender. Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(GROUP)) {
                                    try {
                                        Group group_check = gd.select(Integer.parseInt(critVal));
                                        if (Objects.isNull(group_check)) {
                                            System.out.println("No group with given number. Try again.");
                                            break;
                                        }
                                    } catch (NumberFormatException e ) {
                                        System.out.println("Invalid group number. Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(BIRTH)) {
                                    try {
                                        LocalDate d = LocalDate.parse(critVal);
                                        if (!checkBirth(
                                                d.getYear(),
                                                d.getMonth().getValue(),
                                                d.getDayOfMonth())) {
                                            break;
                                        }
                                    } catch (DateTimeParseException e ) {
                                        System.out.println("Invalid birthday. Try again.");
                                        break;
                                    }
                                }
                                sd.update(student, criteria, critVal);
                                System.out.println("Student updated.");
                                break;
                            }
                            case 3 :  {
                                System.out.print("Deleting studetns. "
                                        + "\nEnter criteria ( ");
                                for (Criteria criteria2 : values()) {
                                    System.out.print(criteria2 + " ");
                                }
                                System.out.print(") ");
                                String crit = in.next().toUpperCase();
                                in.nextLine();
                                if(!checkCriteria(crit)) {
                                    break;
                                }
                                Criteria criteria = valueOf(crit);

                                System.out.print("Enter criteria value: ");
                                String critVal = in.nextLine();

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
                                        Gender.valueOf(critVal);
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid gender. Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(GROUP)) {
                                    try {
                                        Group group_check = gd.select(Integer.parseInt(critVal));
                                        if (Objects.isNull(group_check)) {
                                            System.out.println("No group with given number. Try again.");
                                            break;
                                        }
                                    } catch (NumberFormatException e ) {
                                        System.out.println("Invalid group number. Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(BIRTH)) {
                                    try {
                                        LocalDate d = LocalDate.parse(critVal);
                                        if (!checkBirth(
                                                d.getYear(),
                                                d.getMonth().getValue(),
                                                d.getDayOfMonth())) {
                                            break;
                                        }
                                    } catch (DateTimeParseException e ) {
                                        System.out.println("Invalid birthday. Try again.");
                                        break;
                                    }
                                }
                                sd.delete(criteria, critVal);
                                System.out.println("Student deleted.");
                            }
                            default : {
                                System.out.println("Invalid operation code. Try again.");
                            }
                        }
                        System.out.println();
                    }
                    case TEACHER : {
                        System.out.println("Operation: \t add \t select " +
                                "\t update \t delete " +
                                "\t putTeacherInGroup \t removeTeacherFromGroup");
                        System.out.println("Code: \t\t 0 " +
                                "\t\t 1 \t\t\t 2 \t\t\t 3 " +
                                "\t\t\t 4 \t\t\t\t\t 5 \n");
                        System.out.print("Enter operation code (int): ");
                        int opCode = 0;
                        try {
                            opCode = in.nextInt();
                            in.nextLine();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid operation code. Try again.");
                            in.nextLine();
                            break;
                        }
                        switch (opCode) {
                            case 0 :  {
                                System.out.print("Adding teacher. \nEnter name: ");
                                String name = in.nextLine();

                                System.out.print("Entering birthday. Enter year: ");
                                int year;
                                int month;
                                int day;
                                try {
                                    year = in.nextInt();

                                    System.out.print("Enter month: ");
                                    month = in.nextInt();

                                    System.out.print("Enter day: ");
                                    day = in.nextInt();
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid birthday. Try again.");
                                    in.nextLine();
                                    break;
                                }

                                if(!checkBirth(year, month, day)) {
                                    break;
                                }

                                System.out.print("Enter gender (MALE/FEMALE): ");
                                String genderInput = in.next();
                                Gender gender;
                                try {
                                    gender = Gender.valueOf(genderInput.toUpperCase());
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Invalid gender. Try again.");
                                    break;
                                }

                                td.add(new Teacher(
                                        name,
                                        year, month, day,
                                        gender));
                                System.out.println("Teacher added.");
                                break;
                            }
                            case 1 :  {
                                System.out.print("Selecting teachers. \n"
                                        + "Enter criteria ( ");
                                for (Criteria criteria1 : values()) {
                                    if(!criteria1.equals(GROUP)) {
                                        System.out.print(criteria1 + " ");
                                    }
                                }
                                System.out.print("): ");
                                String crit = in.next().toUpperCase();
                                in.nextLine();
                                if(!checkCriteria(crit)) {
                                    break;
                                }
                                Criteria criteria = valueOf(crit);
                                String critVal;

                                if (!criteria.equals(ALL)) {
                                    System.out.print("Enter criteria value: ");
                                    critVal = in.nextLine();
                                } else {
                                    critVal = "";
                                }
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
                                        Gender.valueOf(critVal);
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid gender. Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(BIRTH)) {
                                    try {
                                        LocalDate d = LocalDate.parse(critVal);
                                        if (!checkBirth(
                                                d.getYear(),
                                                d.getMonth().getValue(),
                                                d.getDayOfMonth())) {
                                            break;
                                        }
                                    } catch (DateTimeParseException e ) {
                                        System.out.println("Invalid birthday. Try again.");
                                        break;
                                    }

                                }
                                for (Teacher teacher
                                        : td.select(criteria, critVal)
                                ) {
                                    System.out.println(teacher);
                                }
                                break;
                            }
                            case 2 :  {
                                System.out.print("Updating teacher. \nEnter id: ");
                                int id;
                                try {
                                    id = in.nextInt();
                                    in.nextLine();
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid id. Try again");
                                    in.nextLine();
                                    break;
                                }
                                Teacher teacher = td.selectById(id);

                                if(Objects.isNull(teacher)) {
                                    System.out.println("No teacher with given id. Try again.");
                                    break;
                                }

                                System.out.print("Enter criteria " +
                                        "wich need to update ( ");
                                for (Criteria cr : values()) {
                                    if (!cr.equals(ALL) && !cr.equals(ID) && !cr.equals(GROUP)) {
                                        System.out.print(cr + " ");
                                    }
                                }
                                System.out.print("): ");
                                String crit = in.next().toUpperCase();
                                in.nextLine();
                                if(!checkCriteria(crit)) {
                                    break;
                                }
                                Criteria criteria = valueOf(crit);

                                System.out.print("Enter criteria value: ");
                                String critVal = in.nextLine();

                                if (criteria.equals(GENDER)) {
                                    try {
                                        Gender.valueOf(critVal);
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid gender. Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(BIRTH)) {
                                    try {
                                        LocalDate d = LocalDate.parse(critVal);
                                        if (!checkBirth(
                                                d.getYear(),
                                                d.getMonth().getValue(),
                                                d.getDayOfMonth())) {
                                            break;
                                        }
                                    } catch (DateTimeParseException e ) {
                                        System.out.println("Invalid birthday. Try again.");
                                        break;
                                    }
                                }
                                td.update(teacher, criteria, critVal);
                                System.out.println("Teacher updated.");
                                break;
                            }
                            case 3 :  {
                                System.out.print("Deleting teachers. "
                                        + "\nEnter criteria ( ");
                                for (Criteria cr : values()) {
                                    if(!cr.equals(GROUP)) {
                                        System.out.print(cr + " ");
                                    }
                                }
                                System.out.print(") ");
                                String crit = in.next().toUpperCase();
                                in.nextLine();
                                if(!checkCriteria(crit)) {
                                    break;
                                }
                                Criteria criteria = valueOf(crit);

                                System.out.print("Enter criteria value: ");
                                String critVal = in.nextLine();

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
                                        Gender.valueOf(critVal);
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid gender. Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(BIRTH)) {
                                    try {
                                        LocalDate d = LocalDate.parse(critVal);
                                        if (!checkBirth(
                                                d.getYear(),
                                                d.getMonth().getValue(),
                                                d.getDayOfMonth())) {
                                            break;
                                        }
                                    } catch (DateTimeParseException e ) {
                                        System.out.println("Invalid birthday. Try again.");
                                        break;
                                    }
                                }
                                sd.delete(criteria, critVal);
                                System.out.println("Teacher deleted.");
                            }
                            case 4 :  {
                                System.out.print("Putting teacher in group." +
                                        " \nEnter teacher id: ");
                                int id = 0;
                                try {
                                    id = in.nextInt();
                                    in.nextLine();
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid id. Try again");
                                    in.nextLine();
                                    break;
                                }

                                System.out.print("Enter group number: ");
                                int number = 0;
                                try {
                                    number = in.nextInt();
                                    in.nextLine();
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid group number. Try again");
                                    in.nextLine();
                                    break;
                                }

                                Teacher teacher = td.selectById(id);
                                Group group = gd.select(number);

                                if (Objects.isNull(teacher)) {
                                    System.out.println("Invalid teacher id. Try again.");
                                    break;
                                }
                                if (Objects.isNull(group)) {
                                    System.out.println("No group with given number. " +
                                            "Try again.");
                                    break;
                                }
                                td.putTeacherInGroup(teacher, group);
                                break;
                            }
                            case 5 :  {
                                System.out.print("Putting teacher in group." +
                                        " \nEnter teacher id: ");
                                int id = 0;
                                try {
                                    id = in.nextInt();
                                    in.nextLine();
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid id. Try again");
                                    in.nextLine();
                                    break;
                                }

                                System.out.print("Enter group number: ");
                                int number = 0;
                                try {
                                    number = in.nextInt();
                                    in.nextLine();
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid group number. Try again");
                                    in.nextLine();
                                    break;
                                }

                                Teacher teacher = td.selectById(id);
                                Group group = gd.select(number);

                                if (Objects.isNull(teacher)) {
                                    System.out.println("Invalid teacher id. Try again.");
                                    break;
                                }
                                if (Objects.isNull(group)) {
                                    System.out.println("No group with given number. Try again.");
                                    break;
                                }
                                td.removeTeacherFromGroup(teacher, group);

                                break;
                            }
                            default : {
                                System.out.println("Invalid operation code. Try again.");
                            }
                        }
                        System.out.println();
                    }
                    case GROUP : {
                        System.out.println("Operation: \t add \t select " +
                                "\t selectAll \t delete");
                        System.out.println("Code: \t\t 0 " +
                                "\t\t 1 \t\t\t 2 \t\t\t 3\n");
                        System.out.print("Enter operation code (int): ");
                        int opCode = 0;
                        try {
                            opCode = in.nextInt();
                            in.nextLine();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid operation code. Try again.");
                            in.nextLine();
                            break;
                        }
                        switch (opCode) {
                            case 0 :  {
                                System.out.print("Adding Group. \nEnter number: ");
                                int number = 0;
                                try {
                                    number = in.nextInt();
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid group number value. Try again.");
                                    in.nextLine();
                                    break;
                                }
                                in.nextLine();

                                gd.add(new Group(number));
                                System.out.println("Group added.");
                                break;
                            }
                            case 1 :  {
                                System.out.print("Selecting group. \n"
                                        + "Enter group number: ");

                                int number = 0;
                                try {
                                    number = in.nextInt();
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid group number value. Try again.");
                                    in.nextLine();
                                    break;
                                }
                                in.nextLine();
                                System.out.println(gd.select(number));
                                break;
                            }
                            case 2 :  {
                                System.out.print("Selecting all...");
                                for(Group group : gd.selectAll()) {
                                    System.out.println(group);
                                }
                                break;
                            }
                            case 3 :  {
                                System.out.print("Deleting group. "
                                        + "\nEnter number: ");

                                int number = 0;
                                try {
                                    number = in.nextInt();
                                    in.nextLine();
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid group number value. Try again.");
                                    in.nextLine();
                                    break;
                                }

                                Group group = null;
                                try {
                                    group = gd.select(number);
                                } catch (NullPointerException e) {
                                    System.out.println("No group with given number. Try again.");
                                }

                                if(!sd.select(GROUP, Integer.toString(number)).isEmpty()) {
                                    System.out.println("Cannot delete group. Some students is in." +
                                            " Unbind them first.");
                                    break;
                                }

                                gd.delete(group);

                                break;
                            }
                            default : {
                                System.out.println("Invalid operation code. Try again.");
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Some error occured.");
            e.printStackTrace();
        }
    }
}