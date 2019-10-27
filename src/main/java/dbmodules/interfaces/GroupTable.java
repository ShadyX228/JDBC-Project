package dbmodules.interfaces;

import dbmodules.tables.Group;
import dbmodules.tables.Student;
import dbmodules.types.Criteria;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface GroupTable {
    void add(Group group) throws SQLException, IOException;
    Group selectById(int id) throws SQLException, IOException;
    Group select(int number) throws SQLException, IOException ;
    List<Group> selectAll() throws SQLException, IOException ;
    void delete(Group group) throws SQLException;
}
