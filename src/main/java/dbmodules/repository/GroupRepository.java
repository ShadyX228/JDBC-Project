package dbmodules.repository;

import dbmodules.tables.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query("select g from Group g where g.number = :number")
    Group findByNumber(@Param("number") Integer number);
}