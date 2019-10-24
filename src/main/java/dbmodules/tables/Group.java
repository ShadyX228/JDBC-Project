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
        setTableName(TableType.GROUP);

    }

    public Group(int number) throws SQLException, IOException {
        this.number = number;
        setTableName(TableType.GROUP);

        // get id from db
        id = setId();
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
    public void add() throws SQLException {
        String query = "INSERT INTO " + getDBName() + "." + getTableName() +
                "(Number) " +
                "VALUES (?)";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, number);
        statement.executeUpdate();
        statement.close();
    }
    public void delete() throws SQLException {
        String query = "DELETE FROM studentgroupteacher.group " +
                "WHERE group_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1,id);
        statement.executeUpdate();
    }

    @Override
    protected int setId() throws SQLException {
        String query = "SELECT * FROM " + getDBName() + "."
                + getTableName() + " WHERE " + getTableName() + ".Number = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1,number);
        ResultSet res = statement.executeQuery();
        if(res.next()) {
            return res.getInt(1);
        }
        return 0;
    }

    public String toString() {
        return "group_id: " + getId() + "; number: " + getNumber();
    }
}
