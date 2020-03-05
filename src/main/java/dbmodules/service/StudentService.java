package dbmodules.service;

import dbmodules.daointerfaces.StudentDAO;
import dbmodules.entity.Group;
import dbmodules.entity.Student;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ss")
public class StudentService implements BaseService {
    private StudentDAO studentDAO;

    @Autowired
    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Transactional
    public void add(Student entity) {
        studentDAO.add(entity);
    }
    @Transactional
    public Student selectById(int id) {
        return studentDAO.selectById(id);
    }
    @Transactional
    public List<Student> select(String name,
                                LocalDate birth,
                                Gender gender,
                                Group group) {
        return studentDAO.select(name, birth, gender, group);
    }
    @Transactional
    public List<Student> selectAll() {
        return studentDAO.selectAll();
    }
    @Transactional
    public List<Student> selectByGroup(Group group) {
        return studentDAO.selectByGroup(group);
    }
    @Transactional
    public void update(Student person, Criteria criteria, String value) {
        studentDAO.update(person, criteria, value);
    }
    @Transactional
    public void update(Student person, String name,
                       LocalDate birth, Gender gender, Group group) {
        studentDAO.update(person, name, birth, gender, group);
    }
    @Transactional
    public void delete(Student entity) {
        studentDAO.delete(entity);
    }
}