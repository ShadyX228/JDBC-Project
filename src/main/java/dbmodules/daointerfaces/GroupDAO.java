package dbmodules.daointerfaces;

import dbmodules.entity.Group;
import dbmodules.entity.Teacher;

import java.util.List;

public interface GroupDAO extends BaseDAO {
    Group selectById(int id);
    void add(Group entity);
    Group select(int number);
    List<Group> selectAll();
    List<Teacher> selectGroupTeachers(Group group);
    void update(Group group, int number);
    void delete(Group entity);
}