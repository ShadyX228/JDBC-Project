package dbmodules.tables;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@javax.persistence.Table (name = "group", schema = "studentgroupteacher")
public class Group extends Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="group_id")
    private int id;

    @Column(name = "number")
    private int number;


    @ManyToMany(cascade = {
            CascadeType.ALL
    }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "groupteacher",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "teacher_id")}
    )
    private List<Teacher> teachers = new ArrayList<>();

    public Group() {}
    public Group(int number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }
    public int getNumber() {
        return number;
    }
    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }
    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
    }


    public String toString() {
        return "group_id: " + getId() + "; number: " + getNumber();
    }
}
