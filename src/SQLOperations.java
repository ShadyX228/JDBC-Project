import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface SQLOperations<T> {
    // maybe can do it using array
    ArrayList<Integer> selectByCriteria(String criteria, T critValue) throws SQLException;
    void updateByCriteria(String criteria, T critValue) throws SQLException;
    void deleteByCriteria(String criteria, T critValue) throws SQLException;
}
