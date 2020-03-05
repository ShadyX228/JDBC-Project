package dbmodules.daointerfaces;

import dbmodules.entity.Group;
import dbmodules.entity.PersonEntity;
import dbmodules.types.Criteria;
import dbmodules.types.Gender;

import java.util.HashMap;
import java.util.List;

public interface PersonDAO<T extends PersonEntity> extends BaseDAO {
    T selectById(int id);
    void add(T entity);
    List<T> selectAll();
    void update(T person, Criteria criteria, String value);
    void delete(T entity);
}