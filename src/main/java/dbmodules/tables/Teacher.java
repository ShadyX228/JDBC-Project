package dbmodules.tables;

import dbmodules.types.Gender;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@javax.persistence.Table (catalog = "studentgroupteacher", name = "teacher")
public class Teacher extends Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="teacher_id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="birthday")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name="gender")
    private Gender gender;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(name = "groupteacher",
            joinColumns = {@JoinColumn(name = "teacher_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id")}
    )
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
    public void removeGroup(Group group) {
        groups.remove(group);
    }

    @Override
    public String toString() {
        return "teacher_id: " + id + "; name: " + name + "; " +
                "birthday: " + birthday + "; gender: " + gender;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return id == teacher.id &&
                name.equals(teacher.name) &&
                birthday.equals(teacher.birthday) &&
                gender == teacher.gender &&
                Objects.equals(groups, teacher.groups);
    }
}
