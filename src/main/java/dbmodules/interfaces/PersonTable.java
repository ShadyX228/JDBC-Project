package dbmodules.interfaces;

import dbmodules.tables.Table;
import dbmodules.types.Criteria;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public interface PersonTable<T extends Table> {
    void add(T person) throws SQLException;
    T selectById(int id) throws SQLException;
    List<T> select(Criteria criteria, String value) throws SQLException, IOException;
    void update(T person, Criteria criteria, String value) throws SQLException, IOException;
    void delete(Criteria criteria, String value) throws SQLException, IOException;
}
