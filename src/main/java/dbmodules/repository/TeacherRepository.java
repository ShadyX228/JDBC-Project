package dbmodules.repository;

import dbmodules.tables.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}