package dbmodules.tables;

import dbmodules.types.*;

import java.time.*;

public class Student extends Table {
    private int id;
    private String name;
    private LocalDate birthday;
    private Gender gender;
    private int group_id;

    public Student(int id, String name, int year, int month, int day,
            Gender gender, int group_id) {
        this.id = id;
        this.name = name;
        birthday = LocalDate.of(year,month,day);
        this.gender = gender;
        this.group_id = group_id;
    }

    public Student(String name, int year, int month, int day,
            Gender gender, int group_id) {
        this.name = name;
        birthday = LocalDate.of(year,month,day);
        this.gender = gender;
        this.group_id = group_id;

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
    public int getGroup_id() {
        return group_id;
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
    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }


    public String toString() {
        return  "student_id: " + id + "; name: " + name + "; " +
                "birthday: " + birthday + "; gender: " + gender +
                "; group_id: " + group_id;
    }

}
