package dbmodules.tables;

import dbmodules.types.*;

import javax.persistence.*;
import java.time.*;

@Entity
@javax.persistence.Table (name = "studentgroupteacher.student")
public class Student extends Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_id")
    private int id;

    @Column(name="Name")
    private String name;

    @Column(name="Birthday")
    private LocalDate birthday;

    @Column(name="Gender")
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student() {}
    public Student(int id, String name, int year, int month, int day,
            Gender gender, Group group) {
        this.id = id;
        this.name = name;
        birthday = LocalDate.of(year,month,day);
        this.gender = gender;
        this.group = group;
    }
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


    public String toString() {
        return  "student_id: " + id + "; name: " + name + "; " +
                "birthday: " + birthday + "; gender: " + gender +
                "; group_id: " + group.getId();
    }

}
