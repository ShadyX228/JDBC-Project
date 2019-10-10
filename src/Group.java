import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Group extends DBInfo implements SQLOperations<Integer> {
    private static int nextId = 0;
    private static String tableName = "group";
    private int group_id;
    private int number;

    Group(int number) throws SQLException {
        group_id = ++nextId;
        this.number = number;

        String query = "INSERT INTO " + getDBName() + ".group " +
                "(group_id, Number) " +
                "VALUES (?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, group_id);
        statement.setInt(2, this.number);
        statement.executeUpdate();
    }

    public ArrayList<Integer> selectByCriteria(String criteria, Integer critValue) throws SQLException {
        String query = "SELECT * FROM " + getDBName() + "." + tableName + " " +
                "WHERE " + tableName + "." + criteria + " = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setInt(1, critValue);

        ArrayList<Integer> list = new ArrayList<>();
        ResultSet res = statement.executeQuery();
        /*while(res.next()) {
            for(int i = 1; i < 2; i++) {
                System.out.print(res.getString(i) + " ");
            }
            System.out.println();
        }*/

        statement.close();
        return list;
    }
    public void updateByCriteria(String criteria, Integer critValue) throws SQLException {
        String query = "UPDATE " + getDBName() + ".group " +
                "SET " + criteria + " = ? WHERE group.group_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, critValue);
        statement.setInt(2, group_id);
        statement.executeUpdate();
        statement.close();
    }
    public void deleteByCriteria(String criteria, Integer critValue) throws SQLException {
        String query = "DELETE FROM " + getDBName() + "." + tableName + " " +
                "WHERE " + tableName + "." + criteria + " = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, critValue);
        statement.executeUpdate();
        statement.close();
        nextId--;
    }
    public String toString() {
        return "Group: " + number;
    }
}
