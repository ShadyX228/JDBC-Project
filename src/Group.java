import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Group extends Table {
    private int id;
    private int number;


    Group(int number) throws SQLException, IOException {
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

    public String toString() {
        return "Group: " + number;
    }
}
