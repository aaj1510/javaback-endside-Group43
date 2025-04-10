import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:/Users/Alaina/javabackside.db"; //change path to ur db path

        //create table statement
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Tasks ("
                + "task_id INTEGER PRIMARY KEY, "
                + "user_id INTEGER,"
                + "username TEXT,"
                + "taskname TEXT NOT NULL, "
                + "taskdescription TEXT, "
                + "taskdifficulty TEXT NOT NULL, "
                + "taskstatus INTEGER NOT NULL, "
                + "task_date_time_started INTEGER ,"
                + "task_date_time_completed INTEGER ,"
                + "task_deadline INTEGER,"
                + "FOREIGN KEY (user_id) REFERENCES Users(id), "
                + "FOREIGN KEY (username) REFERENCES Users(username)"
                + ");";
                

        try {
            Class.forName("org.sqlite.JDBC"); //driver load
            System.out.println("Driver loaded successfully!");

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {

                if (conn != null) {
                    System.out.println("Connected to the SQLite database successfully!");
                } else {
                    System.out.println("Connection failed!");
                    return;
                }

                //enable foreign keys: foreign keys are to reference another table
                stmt.execute("PRAGMA foreign_keys = ON;");

                //execute SQL statement
                stmt.execute(createTableSQL);
                System.out.println("Table created successfully!");

            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
}
