package dbmodules.daointerfaces;

import dbmodules.entity.Group;
import dbmodules.entity.Student;
import dbmodules.types.Gender;
import net.bytebuddy.asm.Advice;

import java.time.LocalDate;
import java.util.List;

public interface StudentDAO extends PersonDAO<Student> {
     List<Student> select(String name, LocalDate birth, Gender gender, Group group);
     void update(Student person, String name,
                       LocalDate birth, Gender gender, Group group);
     List<Student> selectByGroup(Group group);
}