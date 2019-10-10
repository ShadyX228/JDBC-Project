import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Main {
    private static int nextId = 0;
    public static void main(String[] args) throws SQLException {
        Student s1 = new Student(
                "I I",
                LocalDate.of(2019, 1, 22),
                'M',
                1
        );
        s1.selectByCriteria("Birthday", "2019-01-22");
        s1.updateByCriteria("Birthday", "2019-02-22");
        s1.selectByCriteria("Birthday", "2019-02-22");
    }
}
