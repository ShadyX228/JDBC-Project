package dbmodules.tables;

import dbmodules.types.Gender;

import javax.persistence.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@javax.persistence.Table (name = "studentgroupteacher.teacher")
public class Teacher extends Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="teacher_id")
    private int id;

    @Column(name="Name")
    private String name;

    @Column(name="Birthday")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name="Gender")
    private Gender gender;

    @ManyToMany(mappedBy = "teachers", fetch = FetchType.LAZY)
    private List<Group> groups = new ArrayList<>();


    public Teacher() {
    }
    public Teacher(String name, int year, int month, int day, Gender gender) {
        this.name = name;
        birthday = LocalDate.of(year,month,day);
        this.gender = gender;
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
    public Gender getGender() {
        return gender;
    }
    public List<Group> getGroups() {
        return groups;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    public void setGender(Gender gender)  {
        this.gender = gender;
    }
    public void addGroup(Group group) {
        groups.add(group);
    }

    public String toString() {
        return "teacher_id: " + id + "; name: " + name + "; " +
                "birthday: " + birthday + "; gender: " + gender;
    }

    @Override
    public boolean equals(Object obj) {
        Teacher teacher = (Teacher) obj;
        return this.id == teacher.id;
    }
}
