package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_paiements";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    private static DBConnection instance = null;
    private Connection connection = null;
    
    private DBConnection() {
        try {
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erreur de connexion MySQL :");
            e.printStackTrace();
        }
    }
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée");
            }
        } catch (SQLException e) {
            System.err.println("Erreur fermeture : " + e.getMessage());
        }
    }
}