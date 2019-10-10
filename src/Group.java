import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Group extends Table {
    private static int nextId = 0;
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
    public String toString() {
        return "Group: " + number;
    }
}
