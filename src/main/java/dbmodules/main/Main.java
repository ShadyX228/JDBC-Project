package dbmodules.main;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.dao.TeacherDAO;
import dbmodules.types.*;
import dbmodules.tables.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.util.*;

import static dbmodules.types.Criteria.*;
import static dbmodules.types.TableType.STUDENT;

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
                        System.out.println("Operation: \t add \t select \t update \t delete");
                        System.out.println("Code: \t\t 0 \t\t 1 \t\t\t 2 \t\t\t 3 \n");
                        System.out.print("Enter operation code (int): ");
                        int opCode = in.nextInt();
                        in.nextLine();
                        switch (opCode) {
                            case 0 : {
                                System.out.print("Adding student. \nEnter name: ");
                                String name = in.nextLine();

                                System.out.print("Entering birthday. Enter year: ");
                                int year = in.nextInt();

                                System.out.print("Enter month: ");
                                int month = in.nextInt();

                                System.out.print("Enter day: ");
                                int day = in.nextInt();

                                if(!checkBirth(year, month, day)) {
                                    break;
                                }

                                System.out.print("Enter gender (MALE/FEMALE): ");
                                String genderInput = in.next();
                                Gender gender;
                                try {
                                    gender = Gender.valueOf(genderInput);
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Invalid gender. Try again.");
                                    break;
                                }

                                System.out.print("Enter student's group number: ");
                                int number = in.nextInt();
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
                            case 1 : {
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
                                if (criteria.equals(GENDER)) {
                                    try {
                                        Gender.valueOf(critVal);
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid gender. Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(GROUP)) {
                                    Group group_check = gd.select(Integer.parseInt(critVal));
                                    if (Objects.isNull(group_check)) {
                                        System.out.println("No group with given number. Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(BIRTH)) {
                                    LocalDate d = LocalDate.parse(critVal);
                                    if(!checkBirth(
                                            d.getYear(),
                                            d.getMonth().getValue(),
                                            d.getDayOfMonth())) {
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
                            case 2 : {
                                System.out.print("Updating student. \nEnter id: ");
                                int id = in.nextInt();
                                Student student = sd.selectById(id);

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
                                    Group group_check = gd.select(
                                            Integer.parseInt(critVal)
                                    );
                                    if (Objects.isNull(group_check)) {
                                        System.out.println("No group with given number." +
                                                " Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(BIRTH)) {
                                    LocalDate d = LocalDate.parse(critVal);
                                    if(!checkBirth(
                                            d.getYear(),
                                            d.getMonth().getValue(),
                                            d.getDayOfMonth())) {
                                        break;
                                    }
                                }
                                sd.update(student, criteria, critVal);
                                break;
                            }
                            case 3 : {
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

                                if (criteria.equals(GENDER)) {
                                    try {
                                        Gender.valueOf(critVal);
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid gender. Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(GROUP)) {
                                    Group group_check = gd.select(Integer.parseInt(critVal));
                                    if (Objects.isNull(group_check)) {
                                        System.out.println("No group with given number. Try again.");
                                        break;
                                    }
                                }
                                if (criteria.equals(BIRTH)) {
                                    LocalDate d = LocalDate.parse(critVal);
                                    if(!checkBirth(
                                            d.getYear(),
                                            d.getMonth().getValue(),
                                            d.getDayOfMonth())) {
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
                }






            }

        } catch (Exception e) {
            System.out.println("Some error occured.");
            e.printStackTrace();
        }












        /*try {
            Database DB = new Database();
            Scanner input = new Scanner(System.in);

            getMethods("Student");
            getMethods("Teacher");
            getMethods("Group");

            String methodName = "";
            while (!methodName.equals("exit")) {
                System.out.print("\nEnter method's name or \"exit\" to stop: ");
                methodName = input.next();
                input.nextLine();
                switch (methodName) {
                    // student methods begin
                    case "addStudent":
                        System.out.print("Adding student. \nEnter name: ");
                        String name = input.nextLine();

                        System.out.print("Entering birthday. Enter year: ");
                        int year = input.nextInt();

                        System.out.print("Enter month: ");
                        int month = input.nextInt();

                        System.out.print("Enter day: ");
                        int day = input.nextInt();

                        if(!checkBirth(year, month, day)) {
                            break;
                        }

                        System.out.print("Enter gender (MALE/FEMALE): ");
                        String genderInput = input.next();
                        Gender gender;
                        try {
                            gender = Gender.valueOf(genderInput);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid gender. Try again.");
                            break;
                        }

                        System.out.print("Enter student's group number: ");
                        int number = input.nextInt();
                        Group group = DB.selectGroup(number);
                        if (Objects.isNull(group)) {
                            System.out.println("No group with given number. Try again.");
                            break;
                        }
                        int group_id = group.getId();

                        DB.addStudent(new Student(
                                name,
                                year, month, day,
                                gender,
                                group_id));
                        break;

                    case "select":
                        System.out.print("Selecting students. \n"
                                + "Enter criteria. Available variants: ");
                        for (Criteria criteria1 : Criteria.values()) {
                            System.out.print(criteria1 + " ");
                        }
                        System.out.print(": ");
                        String crit = input.next();
                        input.nextLine();
                        if(!checkCriteria(crit)) {
                            break;
                        }
                        Criteria criteria = Criteria.valueOf(crit);
                        String critVal;

                        if (!criteria.equals(Criteria.ALL)) {
                            System.out.print("Enter criteria value: ");
                            critVal = input.nextLine();
                        } else {
                            critVal = "";
                        }
                        if (criteria.equals(Criteria.GENDER)) {
                            try {
                                Gender.valueOf(critVal);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid gender. Try again.");
                                break;
                            }
                        }
                        if (criteria.equals(Criteria.GROUP)) {
                            Group group_check = DB.selectGroup(Integer.parseInt(critVal));
                            if (Objects.isNull(group_check)) {
                                System.out.println("No group with given number. Try again.");
                                break;
                            }
                        }
                        if (criteria.equals(Criteria.BIRTH)) {
                            LocalDate d = LocalDate.parse(critVal);
                            if(!checkBirth(
                                    d.getYear(),
                                    d.getDayOfMonth(),
                                    d.getDayOfMonth())) {
                                break;
                            }
                        }
                        for (Student student
                                : DB.selectStudent(criteria, critVal)
                        ) {
                            System.out.println(student);
                        }
                        break;

                    case "deleteStudent":
                        System.out.print("Deleting studetns. "
                                + "\nEnter criteria. Available variants: ");
                        for (Criteria criteria2 : Criteria.values()) {
                            System.out.print(criteria2 + " ");
                        }
                        System.out.print(": ");
                        crit = input.next();
                        input.nextLine();
                        if(!checkCriteria(crit)) {
                            break;
                        }
                        criteria = Criteria.valueOf(crit);

                        System.out.print("Enter criteria value: ");
                        critVal = input.nextLine();

                        if (criteria.equals(Criteria.GENDER)) {
                            try {
                                Gender.valueOf(critVal);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid gender. Try again.");
                                break;
                            }
                        }
                        if (criteria.equals(Criteria.GROUP)) {
                            Group group_check = DB.selectGroup(Integer.parseInt(critVal));
                            if (Objects.isNull(group_check)) {
                                System.out.println("No group with given number. Try again.");
                                break;
                            }
                        }
                        if (criteria.equals(Criteria.BIRTH)) {
                            LocalDate d = LocalDate.parse(critVal);
                            if(!checkBirth(
                                    d.getYear(),
                                    d.getDayOfMonth(),
                                    d.getDayOfMonth())) {
                                break;
                            }
                        }
                        DB.deleteStudent(criteria, critVal);
                        break;

                    case "updateStudent":
                        System.out.print("Updating student. \nEnter id: ");
                        int id = input.nextInt();

                        System.out.print("Enter criteria " +
                                "wich need to update. Available variants: ");
                        for (Criteria cr : Criteria.values()) {
                            if (!cr.equals(Criteria.ALL) && !cr.equals(Criteria.ID)) {
                                System.out.print(cr + " ");
                            }
                        }
                        System.out.print(": ");
                        crit = input.next();
                        input.nextLine();
                        if(!checkCriteria(crit)) {
                            break;
                        }
                        criteria = Criteria.valueOf(crit);

                        System.out.print("Enter criteria value: ");
                        critVal = input.nextLine();

                        if (criteria.equals(Criteria.GENDER)) {
                            try {
                                Gender.valueOf(critVal);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid gender. Try again.");
                                break;
                            }
                        }
                        if (criteria.equals(Criteria.GROUP)) {
                            Group group_check = DB.selectGroup(
                                    Integer.parseInt(critVal));
                            if (Objects.isNull(group_check)) {
                                System.out.println("No group with given number." +
                                        " Try again.");
                                break;
                            }
                        }
                        if (criteria.equals(Criteria.BIRTH)) {
                            LocalDate d = LocalDate.parse(critVal);
                            if(!checkBirth(
                                    d.getYear(),
                                    d.getDayOfMonth(),
                                    d.getDayOfMonth())) {
                                break;
                            }
                        }
                        DB.updateStudent(id, criteria, critVal);
                        break;
                    // student methods end


                    // teacher methods begin
                    case "addTeacher":
                        System.out.print("Adding teacher. \nEnter name: ");
                        name = input.nextLine();

                        System.out.print("Entering birthday. Enter year: ");
                        year = input.nextInt();

                        System.out.print("Enter month: ");
                        month = input.nextInt();

                        System.out.print("Enter day: ");
                        day = input.nextInt();

                        if(!checkBirth(year, month, day)) {
                            break;
                        }

                        System.out.print("Enter gender (MALE/FEMALE): ");
                        genderInput = input.next();
                        try {
                            gender = Gender.valueOf(genderInput);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid gender. Try again.");
                            break;
                        }

                        DB.addTeacher(new Teacher(name,
                                year, month, day,
                                gender)
                        );
                        break;

                    case "selectTeacher":
                        System.out.print("Selecting teachers. " +
                                "\nEnter criteria. Available variants: ");
                        for (Criteria criteria1 : Criteria.values()) {
                            if (!criteria1.equals(Criteria.GROUP)) {
                                System.out.print(criteria1 + " ");
                            }
                        }
                        System.out.print(": ");
                        crit = input.next();
                        input.nextLine();
                        if(!checkCriteria(crit)) {
                            break;
                        }
                        criteria = Criteria.valueOf(crit);

                        if (!criteria.equals(Criteria.ALL)) {
                            System.out.print("Enter criteria value: ");
                            critVal = input.nextLine();
                        } else {
                            critVal = "";
                        }
                        if (criteria.equals(Criteria.GENDER)) {
                            try {
                                Gender.valueOf(critVal);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid gender. Try again.");
                                break;
                            }
                        }
                        if (criteria.equals(Criteria.BIRTH)) {
                            try {
                                LocalDate.parse(critVal);
                            } catch (DateTimeException e) {
                                System.out.println("Invalid birthday. Try again.");
                                break;
                            }
                        }
                        for (Teacher teacher : DB.selectTeacher(
                                criteria,
                                critVal)
                        ) {
                            System.out.println(teacher);
                        }
                        break;

                    case "deleteTeacher":
                        System.out.print("Deleting teachers. " +
                                "\nEnter criteria. Available variants: ");
                        for (Criteria criteria2 : Criteria.values()) {
                            if (!criteria2.equals(Criteria.GROUP)) {
                                System.out.print(criteria2 + " ");
                            }
                        }
                        System.out.print(": ");
                        crit = input.next();
                        input.nextLine();
                        if(!checkCriteria(crit)) {
                            break;
                        }
                        criteria = Criteria.valueOf(crit);

                        System.out.print("Enter criteria value: ");
                        critVal = input.nextLine();



                        if (criteria.equals(Criteria.GENDER)) {
                            try {
                                Gender.valueOf(critVal);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid gender. Try again.");
                                break;
                            }
                        }
                        if (criteria.equals(Criteria.BIRTH)) {
                            try {
                                LocalDate.parse(critVal);
                            } catch (DateTimeException e) {
                                System.out.println("Invalid birthday. Try again.");
                                break;
                            }
                        }
                        DB.deleteTeacher(criteria, critVal);
                        break;

                    case "updateTeacher":
                        System.out.print("Updating teacher. \nEnter id: ");
                        id = input.nextInt();

                        System.out.print("Enter criteria " +
                                "wich need to update. Available variants: ");
                        for (Criteria cr : Criteria.values()) {
                            if (!cr.equals(Criteria.GROUP)
                                    && !cr.equals(Criteria.ID)
                                    && !cr.equals(Criteria.ALL)) {
                                System.out.print(cr + " ");
                            }
                        }
                        System.out.print(": ");
                        crit = input.next();
                        if(!checkCriteria(crit)) {
                            break;
                        }
                        criteria = Criteria.valueOf(crit);

                        System.out.print("Enter criteria value: ");
                        critVal = input.next();

                        if (criteria.equals(Criteria.GENDER)) {
                            try {
                                Gender.valueOf(critVal);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid gender. Try again.");
                                break;
                            }
                        }
                        if (criteria.equals(Criteria.BIRTH)) {
                            try {
                                LocalDate.parse(critVal);
                            } catch (DateTimeException e) {
                                System.out.println("Invalid birthday. Try again.");
                                break;
                            }
                        }
                        DB.updateTeacher(id, criteria, critVal);
                        break;

                    case "selectTeachersGroup":
                        System.out.print("Selecting teacher's groups." +
                                " \nEnter teacher id: ");
                        id = input.nextInt();
                        for (Group gr : DB.selectTeachersGroup(id)) {
                            System.out.println(gr);
                        }
                        break;

                    case "putTeacherInGroup":
                        System.out.print("Putting teacher in group." +
                                " \nEnter teacher id: ");
                        id = input.nextInt();
                        System.out.print("Enter group number: ");
                        number = input.nextInt();

                        if (Objects.isNull(DB.selectTeacher(Criteria.ID,
                                Integer.toString(id)).get(0))) {
                            System.out.println("Invalid teacher id. Try again.");
                            break;
                        }
                        if (Objects.isNull(DB.selectGroup(number))) {
                            System.out.println("No group with given number. " +
                                    "Try again.");
                            break;
                        }
                        DB.putTeacherInGroup(id, number);
                        break;

                    case "deleteTeacherFromGroup":
                        System.out.print("Putting teacher in group." +
                                " \nEnter teacher id: ");
                        id = input.nextInt();
                        System.out.print("Enter group number: ");
                        number = input.nextInt();
                        if (Objects.isNull(DB.selectTeacher(Criteria.ID,
                                Integer.toString(id)).get(0))) {
                            System.out.println("Invalid teacher id. Try again.");
                            break;
                        }
                        if (Objects.isNull(DB.selectGroup(number))) {
                            System.out.println("No group with given number. Try again.");
                            break;
                        }
                        DB.deleteTeacherFromGroup(id, number);

                        break;
                    // teacher methods end


                    // group methods begin
                    case "addGroup":
                        System.out.print("Adding group. " +
                                "\nEnter group number: ");
                        number = input.nextInt();
                        DB.addGroup(new Group(number));
                        break;

                    case "selectGroup":
                        System.out.print("Selecting group. " +
                                "\nEnter group number: ");
                        number = input.nextInt();

                        System.out.println(DB.selectGroup(number));
                        break;
                    case "selectAllGroups":
                        System.out.println("Selecting all groups.");
                        for (Group gr : DB.selectAllGroups()) {
                            System.out.println(gr);
                        }
                        break;
                    case "deleteGroup":
                        System.out.print("Deleting group. Enter group number: ");
                        number = input.nextInt();
                        Group g = DB.selectGroup(number);
                        if(Objects.isNull(g)) {
                            System.out.println("Invalid group. Try again.");
                            break;
                        }
                        DB.deleteGroup(g);
                        break;
                    // group methods end


                    // general methods begin
                    case "exit":
                        System.out.print("Shutting down.");
                        break;

                    default:
                        System.out.println("No method with selected name.");
                        // general methods end
                }
            }
        } catch (Exception e) {
            System.out.println("Some error occured.");
            e.printStackTrace();
        }*/
    }
}