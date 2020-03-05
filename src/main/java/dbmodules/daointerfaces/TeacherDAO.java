package dbmodules.daointerfaces;

import dbmodules.entity.Group;
import dbmodules.entity.Teacher;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface TeacherDAO extends PersonDAO<Teacher> {
    List<Teacher> select(String name, LocalDate birth, Gender gender);
    void update(Teacher person, String name, LocalDate birth, Gender gender);
    void putTeacherInGroup(Teacher teacher, Group group);
    List<Group> getTeacherGroups(int id);
    void removeTeacherFromGroup(Teacher teacher, Group group);
}