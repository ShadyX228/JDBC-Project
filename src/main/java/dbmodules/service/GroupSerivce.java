package dbmodules.service;

import dbmodules.tables.Group;

import java.util.List;

public interface GroupSerivce {
    void add(Group group);
    Group selectById(int id);
    Group select(int number);
    List<Group> selectAll();
    void update(Group group, int number);
    void delete(Group group);
}
