import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database Connection Utility Class.
 * Provides JDBC Connection to MySQL database (or embedded fallback DB).
 */
public class DBConnection {
    // Configurable Database Credentials
    private static final String URL = "jdbc:mysql://localhost:3306/student_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    // Embedded Fallback Credentials for standalone execution without local MySQL instance running
    private static final String FALLBACK_URL = "jdbc:h2:mem:student_db;MODE=MySQL;DB_CLOSE_DELAY=-1";
    private static final String FALLBACK_USER = "sa";
    private static final String FALLBACK_PASS = "";

    private static boolean useFallback = false;

    /**
     * Obtains a Connection object to the database.
     */
    public static Connection getConnection() {
        if (!useFallback) {
            try {
                // Register MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                return DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                // Fallback if MySQL driver jar isn't on classpath or H2 fallback is activated
                return getFallbackConnection();
            } catch (SQLException e) {
                // If local MySQL server is not running, switch to embedded DB for standalone demonstration
                System.out.println("[INFO] Local MySQL server not reachable. Switching to embedded JDBC engine...");
                useFallback = true;
                return getFallbackConnection();
            }
        } else {
            return getFallbackConnection();
        }
    }

    private static Connection getFallbackConnection() {
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection(FALLBACK_URL, FALLBACK_USER, FALLBACK_PASS);
            initFallbackDatabase(conn);
            return conn;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to obtain Database Connection: " + e.getMessage());
            return null;
        }
    }

    private static boolean fallbackInitialized = false;

    private static synchronized void initFallbackDatabase(Connection conn) {
        if (fallbackInitialized) return;
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS students (id INT PRIMARY KEY, name VARCHAR(100) NOT NULL, age INT NOT NULL)");
            fallbackInitialized = true;
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to initialize fallback database schema: " + e.getMessage());
        }
    }
}
