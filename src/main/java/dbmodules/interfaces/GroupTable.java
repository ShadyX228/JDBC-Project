package dbmodules.interfaces;

import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface GroupTable {
    void add(Group group);
    Group selectById(int id);
    Group select(int number);
    List<Group> selectAll();
    void delete(Group group);
}
