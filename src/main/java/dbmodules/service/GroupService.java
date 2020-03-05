package dbmodules.service;

import dbmodules.daointerfaces.GroupDAO;
import dbmodules.daointerfaces.StudentDAO;
import dbmodules.daointerfaces.TeacherDAO;
import dbmodules.entity.Group;
import dbmodules.entity.Student;
import dbmodules.entity.Teacher;
import dbmodules.types.Criteria;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService implements BaseService {
    private GroupDAO groupDAO;
    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;

    @Autowired
    public GroupService(GroupDAO groupDAO, StudentDAO studentDAO, TeacherDAO teacherDAO) {
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
        this.teacherDAO = teacherDAO;
    }

    @Transactional
    public void add(Group entity) {
        groupDAO.add(entity);
    }
    @Transactional
    public Group selectById(int id) {
        return groupDAO.selectById(id);
    }
    @Transactional
    public Group select(int number) {
        return groupDAO.select(number);
    }
    @Transactional
    public List<Group> selectAll() {
        return groupDAO.selectAll();
    }
    @Transactional
    public void update(Group group, int number) {
        groupDAO.update(group, number);
    }
    @Transactional
    public void delete(Group entity) {
        groupDAO.delete(entity);
    }
    @Transactional
    public List<Student> getGroupStudents(Group group) {
        return studentDAO.selectByGroup(group);
    }
    @Transactional
    public List<Teacher> getGroupTeachers(Group group) {
        return groupDAO.selectGroupTeachers(group);
    }

}