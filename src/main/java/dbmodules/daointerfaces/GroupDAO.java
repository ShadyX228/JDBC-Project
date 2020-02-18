package dbmodules.daointerfaces;

import dbmodules.entity.Group;

import java.util.List;

public interface GroupDAO extends BaseDAO {
    Group selectById(int id);
    void add(Group entity);
    Group select(int number);
    List<Group> selectAll();
    void update(Group group, int number);
    void delete(Group entity);
}