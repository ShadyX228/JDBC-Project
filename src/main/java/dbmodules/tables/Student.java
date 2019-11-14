package dbmodules.tables;

import dbmodules.types.*;

import javax.persistence.*;
import java.time.*;

@Entity
@javax.persistence.Table (catalog = "studentgroupteacher", name = "student")
public class Student extends Table {
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
    public Student(String name, int year, int month, int day,
            Gender gender, Group group) {
        this.name = name;
        birthday = LocalDate.of(year,month,day);
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
}
