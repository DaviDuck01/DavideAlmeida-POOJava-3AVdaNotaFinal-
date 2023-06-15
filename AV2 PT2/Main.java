import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
        
public class Main {
    static final String DRIVER = "jdbc:postgresql://localhost:5432/DadosGerais";
    
    public static void main(String[] args) {
        Statement st;
        try (Connection conn = DriverManager.getConnection(DRIVER, "postgres", "12345")) {
            if (conn != null) {
                System.out.println("Connected to the database!");
                st = conn.createStatement();
                Application app = new Application(conn, st);
                app.FrontEnd();
                conn.close();
                st.close();
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
}
