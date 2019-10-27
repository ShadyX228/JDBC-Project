package dbmodules.tables;

import dbmodules.types.Gender;
import dbmodules.types.TableType;
import dbmodules.types.Criteria;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class Teacher extends Table {
    private int id;
    private String name;
    private LocalDate birthday;
    private Gender gender;
    private List<Group> groups = new ArrayList<>();


    public Teacher(int id, String name, int year, int month, int day, Gender gender) {
        this.id = id;
        this.name = name;
        birthday = LocalDate.of(year,month,day);
        this.gender = gender;
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

    public void setName(String name) throws SQLException {
        this.name = name;
    }
    public void setBirthday(LocalDate birthday) throws SQLException {
        this.birthday = birthday;
    }
    public void setGender(Gender gender) throws SQLException {
        this.gender = gender;
    }

    public String toString() {
        return "teacher_id: " + id + "; name: " + name + "; " +
                "birthday: " + birthday + "; gender: " + gender;
    }
}
