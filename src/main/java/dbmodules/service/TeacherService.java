package dbmodules.service;

import dbmodules.daointerfaces.TeacherDAO;
import dbmodules.entity.Group;
import dbmodules.entity.Teacher;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TeacherService implements BaseService {
    private TeacherDAO teacherDAO;

    @Autowired
    public TeacherService(TeacherDAO teacherDAO) {
        this.teacherDAO = teacherDAO;
    }

    @Transactional
    public void add(Teacher entity) {
        teacherDAO.add(entity);
    }
    @Transactional
    public Teacher selectById(int id) {
        return teacherDAO.selectById(id);
    }
    @Transactional
    public List<Teacher> select(String name, LocalDate birth, Gender gender) {
        return teacherDAO.select(name, birth, gender);
    }
    @Transactional
    public void update(Teacher person, Criteria criteria, String value) {
        teacherDAO.update(person, criteria, value);
    }
    @Transactional
    public void update(Teacher person, String name, LocalDate birth, Gender gender) {
        teacherDAO.update(person, name, birth, gender);
    }
    @Transactional
    public void putTeacherInGroup(Teacher teacher, Group group) {
        teacherDAO.putTeacherInGroup(teacher, group);
    }
    @Transactional
    public List<Group> getTeacherGroups(int id) {
        return teacherDAO.getTeacherGroups(id);
    }
    @Transactional
    public void removeTeacherFromGroup(Teacher teacher, Group group) {
        teacherDAO.removeTeacherFromGroup(teacher, group);
    }
    @Transactional
    public void delete(Teacher entity) {
        teacherDAO.delete(entity);
    }
    @Transactional
    public List<Teacher> selectFree(int groupId){
        List<Teacher> freeTeachers = new ArrayList<>();
        List<Teacher> allTeachers = teacherDAO.selectAll();

        System.out.println("Checking teachers list...\n" + allTeachers);
        Boolean check = true;
        for(Teacher teacher : allTeachers) {
            System.out.println(teacher.getName() + " " + teacher.getGroups());
            if(teacher.getGroups().isEmpty()) {
                freeTeachers.add(teacher);
                System.out.println("No groups found. " + teacher.getName() + " added.");
            } else {
                for(Group group : teacher.getGroups()) {
                    if(group.getId() == groupId) {
                        System.out.println("Found group. Cant add " + teacher.getName());
                        System.out.println(teacher.getGroups());
                        check = false;
                        break;
                    }
                }
                if(check) {
                    freeTeachers.add(teacher);
                    System.out.println(teacher.getName() + " added.");
                    System.out.println(teacher.getGroups());
                }
                check = true;
            }
        }
        return freeTeachers;
    }
    @Transactional
    public List<Teacher> selectAll() {
        return teacherDAO.selectAll();
    }
}