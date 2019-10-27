package dbmodules.tables;

import dbmodules.types.TableType;

import java.io.IOException;
import java.sql.*;


public class Group extends Table {
    private int id;
    private int number;

    public Group(int id, int number) throws SQLException, IOException {
        this.id = id;
        this.number = number;

    }

    public Group(int number) throws SQLException, IOException {
        this.number = number;
    }

    public int getId() {
        return id;
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String toString() {
        return "group_id: " + getId() + "; number: " + getNumber();
    }
}
