import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        Database database = new Database();
        database.addGroup(421);
        database.selectGroup("group_id", 1);
        System.out.print("\nTrying to access to empty students table: ");
        database.selectStudent("student_id", "1");

        System.out.print("\nAdding students and teachers... ");
        database.addStudent("Ilya Ivanov", "1998-04-09", 'M', 1);
        database.addStudent("Surkhay Petrovich",  "1997-05-22", 'M', 1);
        database.addStudent("Elena Viktorovna", "1999-02-01", 'F', 1);
        database.addStudent("Ivan Ivanov", "1998-01-01", 'M', 1);
        database.addStudent("Andrei Petrov", "1998-05-12", 'M', 1);
        database.addStudent("Alexey Zhukov", "1998-02-03", 'M', 1);

        database.addTeacher("Nikolai Voronov", "1956-01-05", 'M');
        database.addTeacher("Sergey Dragunov", "1972-02-05", 'M');
        database.addTeacher("Michael Kalashnikov", "1981-05-05", 'M');
        System.out.println("Success.");

        System.out.println("\nAll students selecting: ");
        for (int i = 1; i <= 6; i++) {
            database.selectStudent("student_id", String.valueOf(i));
        }

        System.out.println("\nAll teachers selecting: ");
        for (int i = 2; i < 4; i++) {
            database.selectTeacher("teacher_id", String.valueOf(i));
        }

        System.out.println("\nDeleting students: ");
        for (int i = 3; i < 6; i++) {
            database.deleteStudent(i);
        }

        System.out.println("\nDeleting teachers: ");
        for (int i = 1; i <= 2; i++) {
            database.deleteTeacher(i);
        }

        System.out.println("\nPrinting all data data: ");
        database.printTables();

        System.out.print("\nAdding more students and teachers...");
        database.addStudent("Mahmud Petrov", "1998-01-01", 'M', 1);
        database.addStudent("Mamed Petrovas", "1998-05-12", 'M', 1);
        database.addStudent("Tagir Petrov", "1998-02-03", 'M', 1);
        System.out.println("Success.");

        System.out.println("\nSome students selecting: ");
        for (int i = 1; i <= 6; i++) {
            database.selectStudent("student_id", String.valueOf(i));
        }

        System.out.println("\nSome teachers selecting: ");
        for (int i = 2; i < 4; i++) {
            database.selectTeacher("teacher_id", String.valueOf(i));
        }


        System.out.println("\nUpdating info about teacher and student: ");
        database.updateStudent("Name", "Andre Miheev", 6);
        database.updateTeacher("Name", "Michael Shumaher", 3);
        database.selectStudent("student_id", "6");
        database.selectTeacher("teacher_id", "3");

        System.out.println("\nPrinting all data data: ");
        database.printTables();

        System.out.println("\nUpdating group number... ");
        database.updateGroup(1, 422);

        System.out.println("\nDeleting all data data... ");
        for (int i = 1; i <= 9; i++) {
            if (i != 3 && i != 4 && i != 5) {
                database.deleteStudent(i);
            }
        }


        database.deleteTeacher(3);
        database.deleteGroup(1);

        database.printTables();

    }
}
