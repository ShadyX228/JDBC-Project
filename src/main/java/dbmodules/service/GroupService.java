package dbmodules.service;

import dbmodules.entity.Group;

import java.util.List;

public interface GroupService extends BaseService {
    Group selectById(int id);
    void add(Group entity);
    Group select(int number);
    List<Group> selectAll();
    void update(Group group, int number);
    void delete(Group entity);
}