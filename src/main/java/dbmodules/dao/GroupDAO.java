package dbmodules.dao;

import dbmodules.interfaces.GroupTable;
import dbmodules.tables.Group;
import dbmodules.types.TableType;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class GroupDAO extends TableDAO implements GroupTable {
    public GroupDAO() throws IOException, SQLException {
        super(TableType.GROUP);
    }

    public void add(Group group) throws SQLException, IOException {
        if(Objects.isNull(select(group.getNumber()))) {
            String query = "INSERT INTO " + getDBName() + "."
                    + getTableName() + " " +
                    "(Number) " +
                    "VALUES (?)";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, group.getNumber());
            statement.executeUpdate();
            statement.close();
        }
    }
    public Group selectById(int id) throws SQLException, IOException {
        String query = "SELECT * FROM " + getDBName() + "."
                + getTableName() + " WHERE " + getTableName() + "_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet res = statement.executeQuery();
        if (res.next()) {
            int number = res.getInt(2);
            statement.close();
            return new Group(
                    id,
                    number
            );
        }
        return null;
    }
    public Group select(int number)  throws SQLException, IOException {
        String query = "SELECT * FROM " + getDBName() + "."
                + getTableName() + " WHERE "
                + getTableName() + ".Number = ? LIMIT 0,1";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, number);
        ResultSet res = statement.executeQuery();
        if(res.next()) {
            return new Group(
                    res.getInt(1),
                    number
            );
        }
        return null;
    }
    public void delete(Group group) throws SQLException {
        String query = "SELECT * FROM " + getDBName() + ".groupteacher" +
                 " WHERE groupteacher.group_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, group.getId());

        ResultSet res = statement.executeQuery();
        if(res.next()) {
            System.out.println("Cannot delete group. " +
                    "Some teachers teach in this group. " +
                    "Unbind them first.");
        } else {
            query = "DELETE FROM " + getDBName() + "."
                    + getTableName() +
                    " WHERE " + getTableName() + "."
                    + getTableName() + "_id = ?";
            statement = getConnection().prepareStatement(query);
            statement.setInt(1,group.getId());
            statement.executeUpdate();
        }
    }
}