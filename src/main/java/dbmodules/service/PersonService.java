package dbmodules.service;

import dbmodules.entity.BaseEntity;
import dbmodules.entity.PersonEntity;
import dbmodules.entity.Teacher;
import dbmodules.types.Criteria;

import java.util.HashMap;
import java.util.List;

public interface PersonService<T extends PersonEntity> extends BaseService {
    T selectById(int id);
    void add(T entity);
    List<T> select(Criteria criteria, String value);
    List<T> select(HashMap<Criteria, String> criteriasMap);
    void update(T person, Criteria criteria, String value);
    void delete(T entity);
}