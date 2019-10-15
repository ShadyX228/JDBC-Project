import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Group extends DBInfo {
    private int group_id;
    private int number;

    Group(int number) throws SQLException, IOException {
        this.number = number;

        // inserting in db
        String query = "INSERT INTO " + getDBName() + ".group " +
                "(Number) " +
                "VALUES (?)";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, number);
        statement.executeUpdate();

        // get id from db
        query = "SELECT * FROM studentgroupteacher.group ORDER BY group.group_id DESC LIMIT 1";
        statement = getConnection().prepareStatement(query);
        ResultSet res = statement.executeQuery();
        if(res.next()) {
            group_id = res.getInt(1);
        }
        statement.close();
    }

    public int getId() {
        return group_id;
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String toString() {
        return "Group: " + number;
    }
}
