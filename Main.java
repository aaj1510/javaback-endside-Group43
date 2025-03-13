import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:/Users/Alaina/javabackside.db"; //change path to ur db path

        //create table statement
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "email TEXT NOT NULL, "
                + "avatar TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "points_earned INTEGER DEFAULT 0, "
                + "task_id INTEGER, "
                + "total_bosses INTEGER DEFAULT 0, "
                + "inventory_id INTEGER, "
                + "FOREIGN KEY (task_id) REFERENCES Tasks(task_id), "
                + "FOREIGN KEY (inventory_id) REFERENCES Inventory(inventory_id)"
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
