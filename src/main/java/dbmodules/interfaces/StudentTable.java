package dbmodules.interfaces;

import dbmodules.tables.Student;
import dbmodules.types.Criteria;

import java.sql.SQLException;
import java.util.List;


public interface StudentTable {
    List<Student> selectStudent(Criteria criteria, String value) throws SQLException;
    Student selectById(int id) throws SQLException;
    void addStudent(Student student) throws SQLException;
    void updateStudent(int id, Criteria criteria, String value) throws SQLException;
    void deleteStudent(Criteria criteria, String value) throws SQLException;
}