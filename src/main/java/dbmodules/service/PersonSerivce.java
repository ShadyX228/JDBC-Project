package dbmodules.service;

import dbmodules.tables.Table;
import dbmodules.types.Criteria;

import java.util.HashMap;
import java.util.List;


public interface PersonSerivce<T extends Table> {
    void add(T person);
    T selectById(int id);
    List<T> select(Criteria criteria, String value);
    List<T> select(HashMap<Criteria, String> criteriasMap);
    void update(T person, Criteria criteria, String value);
    void delete(T entity);
}
