package dbmodules.daointerfaces;

import dbmodules.entity.PersonEntity;
import dbmodules.types.Criteria;

import java.util.HashMap;
import java.util.List;

public interface PersonDAO<T extends PersonEntity> extends BaseDAO {
    T selectById(int id);
    void add(T entity);
    List<T> select(Criteria criteria, String value);
    List<T> select(HashMap<Criteria, String> criteriasMap);
    void update(T person, Criteria criteria, String value);
    void delete(T entity);
}