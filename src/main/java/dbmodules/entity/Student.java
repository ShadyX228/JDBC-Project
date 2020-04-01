package dbmodules.entity;

import dbmodules.types.*;

import javax.persistence.*;
import java.time.*;

@Entity
@javax.persistence.Table (catalog = "studentgroupteacher", name = "student")
public class Student extends PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="birthday")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name="gender")
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public Student() {}
    public Student(String name, LocalDate birthday,
            Gender gender, Group group) {
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.group = group;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public LocalDate getBirth() {
        return birthday;
    }
    public Group getGroup() {
        return group;
    }
    public Gender getGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return  "student_id: " + id + "; name: " + name + "; " +
                "birthday: " + birthday + "; gender: " + gender +
                "; group_id: " + group.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != student.id) return false;
        if (name != null ? !name.equals(student.name) : student.name != null)
            return false;
        if (birthday != null ?
                !birthday.equals(student.birthday)
                : student.birthday != null)
            return false;
        if (gender != student.gender) return false;
        return group != null ?
                group.equals(student.group)
                : student.group == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }
}
