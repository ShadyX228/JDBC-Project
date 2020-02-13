package dbmodules.service.dao;

import dbmodules.service.GroupSerivce;
import dbmodules.tables.Group;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import utilfactories.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Repository
public class GroupDAO  implements GroupSerivce {
    @PersistenceContext
    private EntityManager entityManager;

    public void add(Group entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }
    public Group selectById(int id) {
        return entityManager.find(Group.class, id);
    }
    public Group select(int number)  {
        return (Group) entityManager
                .createQuery("FROM Group WHERE number = :number")
                .setParameter("number",number)
                .getResultList().get(0);
    }
    public List<Group> selectAll() {
        return  entityManager.createQuery("FROM Group").getResultList();
    }
    public void update(Group group, int number) {
        entityManager.getTransaction().begin();
        group.setNumber(number);
        entityManager.persist(group);
        entityManager.getTransaction().commit();
    }
    public void delete(Group entity) {
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }
}