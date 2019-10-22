import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static ArrayList<Method> getMethods(String table) {
        System.out.print("Available methods for " + table + "'s table: ");
        ArrayList<Method> result = new ArrayList<>();
        for (Method method : Database.class.getDeclaredMethods()) {
            if (Modifier.toString(method.getModifiers()).contains("public")
                    && method.getName().contains(table)) {
                System.out.print(method.getName() + " ");
                result.add(method);
            }
        }
        System.out.println();
        return result;
    }
    public static void main(String[] args) throws SQLException, IOException {
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

                    try {
                        LocalDate.of(year, month, day);
                    } catch (DateTimeException e) {
                        System.out.println("Invalid birthday. Try again.");
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
                    if(Objects.isNull(group)) {
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

                case "selectStudent":
                    System.out.print("Selecting students. \n"
                            + "Enter criteria. Available variants: ");
                    for (Criteria criteria1 : Criteria.values()) {
                        System.out.print(criteria1 + " ");
                    }
                    System.out.print(": ");
                    String crit = input.next();
                    input.nextLine();
                    Criteria criteria = Criteria.valueOf(crit);
                    String critVal;
                    if(!criteria.equals(Criteria.ALL)) {
                        System.out.print("Enter criteria value: ");
                        critVal = input.nextLine();
                    } else {
                        critVal = "";
                    }
                    if(criteria.equals(Criteria.GENDER)) {
                        try {
                            Gender.valueOf(critVal);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid gender. Try again.");
                            break;
                        }
                    }
                    if(criteria.equals(Criteria.GROUP)) {
                        Group group_check = DB.selectGroup(Integer.parseInt(critVal));
                        if(Objects.isNull(group_check)) {
                            System.out.println("No group with given number. Try again.");
                            break;
                        }
                    }
                    if(criteria.equals(Criteria.BIRTH)) {
                        try {
                            LocalDate.parse(critVal);
                        } catch (DateTimeException e) {
                            System.out.println("Invalid birthday. Try again.");
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
                    criteria = Criteria.valueOf(crit);

                    System.out.print("Enter criteria value: ");
                    critVal = input.nextLine();

                    if(criteria.equals(Criteria.GENDER)) {
                        try {
                            Gender.valueOf(critVal);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid gender. Try again.");
                            break;
                        }
                    }
                    if(criteria.equals(Criteria.GROUP)) {
                        Group group_check = DB.selectGroup(Integer.parseInt(critVal));
                        if(Objects.isNull(group_check)) {
                            System.out.println("No group with given number. Try again.");
                            break;
                        }
                    }
                    if(criteria.equals(Criteria.BIRTH)) {
                        try {
                            LocalDate.parse(critVal);
                        } catch (DateTimeException e) {
                            System.out.println("Invalid birthday. Try again.");
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
                        if(!cr.equals(Criteria.ALL) && !cr.equals(Criteria.ID)) {
                            System.out.print(cr + " ");
                        }
                    }
                    System.out.println(": ");
                    crit = input.next();
                    input.nextLine();
                    criteria = Criteria.valueOf(crit);

                    System.out.print("Enter criteria value: ");
                    critVal = input.nextLine();

                    if(criteria.equals(Criteria.GENDER)) {
                        try {
                            Gender.valueOf(critVal);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid gender. Try again.");
                            break;
                        }
                    }
                    if(criteria.equals(Criteria.GROUP)) {
                        Group group_check = DB.selectGroup(Integer.parseInt(critVal));
                        if(Objects.isNull(group_check)) {
                            System.out.println("No group with given number. Try again.");
                            break;
                        }
                    }
                    if(criteria.equals(Criteria.BIRTH)) {
                        try {
                            LocalDate.parse(critVal);
                        } catch (DateTimeException e) {
                            System.out.println("Invalid birthday. Try again.");
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

                    try {
                        LocalDate.of(year, month, day);
                    } catch (DateTimeException e) {
                        System.out.println("Invalid birthday. Try again.");
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
                     for(Criteria criteria1 : Criteria.values()) {
                         if(!criteria1.equals(Criteria.GROUP)) {
                             System.out.print(criteria1 + " ");
                         }
                     }
                     System.out.print(": ");
                     crit = input.next();
                     input.nextLine();
                     criteria = Criteria.valueOf(crit);

                     if(!criteria.equals(Criteria.ALL)) {
                         System.out.print("Enter criteria value: ");
                         critVal = input.nextLine();
                     } else {
                         critVal = "";
                     }
                     if(criteria.equals(Criteria.GENDER)) {
                         try {
                             Gender.valueOf(critVal);
                         } catch (IllegalArgumentException e) {
                             System.out.println("Invalid gender. Try again.");
                             break;
                         }
                     }
                     if(criteria.equals(Criteria.BIRTH)) {
                         try {
                             LocalDate.parse(critVal);
                         } catch (DateTimeException e) {
                             System.out.println("Invalid birthday. Try again.");
                             break;
                         }
                     }
                     for(Teacher teacher : DB.selectTeacher(
                             criteria,
                             critVal)
                     ) {
                         System.out.println(teacher);
                     }
                     break;

                case "deleteTeacher" :
                    System.out.print("Deleting teachers. " +
                            "\nEnter criteria. Available variants: ");
                    for(Criteria criteria2 : Criteria.values()) {
                        if(!criteria2.equals(Criteria.GROUP)) {
                            System.out.print(criteria2 + " ");
                        }
                    }
                    System.out.print(": ");
                    crit = input.next();
                    input.nextLine();
                    criteria = Criteria.valueOf(crit);

                    System.out.print("Enter criteria value: ");
                    critVal = input.nextLine();

                    if(criteria.equals(Criteria.GENDER)) {
                        try {
                            Gender.valueOf(critVal);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid gender. Try again.");
                            break;
                        }
                    }
                    if(criteria.equals(Criteria.BIRTH)) {
                        try {
                            LocalDate.parse(critVal);
                        } catch (DateTimeException e) {
                            System.out.println("Invalid birthday. Try again.");
                            break;
                        }
                    }
                    DB.deleteTeacher(criteria, critVal);
                    break;

                case "updateTeacher" :
                    System.out.print("Updating teacher. \nEnter id: ");
                    id = input.nextInt();

                    System.out.print("Enter criteria " +
                            "wich need to update. Available variants: ");
                    for(Criteria cr : Criteria.values()) {
                        if(!cr.equals(Criteria.GROUP)
                                && !cr.equals(Criteria.ID)
                                && !cr.equals(Criteria.ALL)) {
                            System.out.print(cr + " ");
                        }
                    }
                    System.out.print( ": ");
                    crit = input.next();
                    criteria = Criteria.valueOf(crit);

                    System.out.print("Enter criteria value: ");
                    critVal = input.next();

                    if(criteria.equals(Criteria.GENDER)) {
                        try {
                            Gender.valueOf(critVal);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid gender. Try again.");
                            break;
                        }
                    }
                    if(criteria.equals(Criteria.BIRTH)) {
                        try {
                            LocalDate.parse(critVal);
                        } catch (DateTimeException e) {
                            System.out.println("Invalid birthday. Try again.");
                            break;
                        }
                    }
                    DB.updateTeacher(id, criteria, critVal);
                    break;

                case "selectTeachersGroup" :
                    System.out.print("Selecting teacher's groups." +
                            " \nEnter teacher id: ");
                    id = input.nextInt();
                    for(Group gr : DB.selectTeachersGroup(id)) {
                        System.out.println(gr);
                    }
                    break;

                case "putTeacherInGroup" :
                    System.out.print("Putting teacher in group." +
                            " \nEnter teacher id: ");
                    id = input.nextInt();
                    System.out.print("Enter group number: ");
                    number = input.nextInt();

                    if(Objects.isNull(DB.selectTeacher(Criteria.ID,Integer.toString(number)).get(0))) {
                        System.out.println("Invalid teacher id. Try again.");
                        break;
                    }
                    if(Objects.isNull(DB.selectGroup(number))) {
                        System.out.println("No group with given number. Try again.");
                        break;
                    }
                    DB.putTeacherInGroup(id, number);
                    break;
                // teacher methods end



                // group methods begin
                case "addGroup" :
                    System.out.print("Adding group. " +
                            "\nEnter group number: ");
                    number = input.nextInt();
                    DB.addGroup(new Group(number));
                    break;

                case "selectGroup" :
                    System.out.print("Selecting group. " +
                            "\nEnter group number: ");
                    number = input.nextInt();

                    System.out.println(DB.selectGroup(number));
                    break;
                case "selectAllGroups" :
                    System.out.println("Selecting all groups.");
                    for(Group gr : DB.selectAllGroups()) {
                        System.out.println(gr);
                    }
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
    }
}