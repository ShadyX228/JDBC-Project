import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Database {
    private ArrayList<Student> students;
    private ArrayList<Teacher> teachers;
    private ArrayList<Group> groups;
    private DBInfo dbinfo;

    Database() throws SQLException {
        students = new ArrayList<>();
        teachers = new ArrayList<>();
        groups = new ArrayList<>();
        dbinfo = new DBInfo();
    }

    public void addStudent(String name, String birth, char sex, int group_id)
            throws SQLException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-DD");
        LocalDate birthParsed = LocalDate.parse(birth);
        students.add(
                new Student(
                        name,
                        birthParsed,
                        sex,
                        group_id
                )
        );
    }
    public void addTeacher(String name, String birth, char sex)
            throws SQLException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-DD");
        LocalDate birthParsed = LocalDate.parse(birth);
        teachers.add(
                new Teacher(
                        name,
                        birthParsed,
                        sex
                )
        );
    }
    public void addGroup(int number)
            throws SQLException {
        groups.add(
                new Group(
                        number
                )
        );
    }

    public void selectStudent(String criteria, String critValue)
            throws SQLException {
        if(!students.isEmpty()) {
            //System.out.print("Selecting... ");
            ArrayList<Integer> indexes = students.get(students.size()-1).selectByCriteria(criteria, critValue);
            //System.out.println(indexes.size() + " rows found.");
            for(int index : indexes){
                System.out.println(students.get(index-1));
            }
        }
        else {
            System.out.println("No students in database.");
        }
    }
    public void selectTeacher(String criteria, String critValue)
            throws SQLException {
        if(!teachers.isEmpty()) {
            //System.out.print("Selecting... ");
            ArrayList<Integer> indexes = teachers.get(teachers.size()-1).
                    selectByCriteria(criteria, critValue);
            //System.out.println(indexes.size() + " rows found.");
            for(int index : indexes){
                System.out.println(teachers.get(index-1));
            }
        }
        else {
            System.out.println("No students in database.");
        }
    }
    public void selectGroup(String criteria, int critValue)
            throws SQLException {
        if(!groups.isEmpty()) {
            //System.out.print("Selecting... ");
            ArrayList<Integer> indexes = groups.get(groups.size()-1).
                    selectByCriteria(criteria, critValue);
            System.out.println(indexes.size() + " rows found.");
            for(int index : indexes){
                System.out.println(groups.get(index-1));
            }
        }
        else {
            System.out.println("No students in database.");
        }
    }

    public void updateStudent(String criteria, String critValue, int index)
            throws SQLException {
        System.out.print("Updating... ");
        students.get(index).updateByCriteria(criteria, critValue);
    }
    public void updateTeacher(String criteria, String critValue, int index)
            throws SQLException {
        System.out.print("Updating... ");
        teachers.get(index).updateByCriteria(criteria, critValue);
    }

    public void deleteStudent(int index)
            throws SQLException {
        Student student;
        if(!Objects.isNull(student = students.get(index-1))){
            System.out.println("Deleting...");
            student.deleteByCriteria("student_id", Integer.toString(index));
            students.set(index - 1, null);
        }
        else {
            System.err.println("Error.");
        }
    }
    public void deleteTeacher(int index)
            throws SQLException {
        Teacher teacher;
        if(!Objects.isNull(teacher = teachers.get(index-1))){
            System.out.println("Deleting...");
            teacher.deleteByCriteria("teacher_id", Integer.toString(index));
            teachers.set(index - 1, null);
        }
        else {
            System.err.println("Error.");
        }
    }

    public void printTables() {
        Iterator<Student> its = students.iterator();
        Iterator<Teacher> itt = teachers.iterator();
        Iterator<Group> itg = groups.iterator();
        while(its.hasNext()) {
            System.out.println(its.next());
        }
        System.out.println();
        while(itt.hasNext()) {
            System.out.println(itt.next());
        }
        System.out.println();
        while(itg.hasNext()) {
            System.out.print(itg.next());
        }
    }

}
