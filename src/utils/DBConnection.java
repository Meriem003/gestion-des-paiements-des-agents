package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_paiements?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; 
    private static DBConnection instance = null;
    private Connection connection = null;
    
    private DBConnection() {
    }
    
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            // Vérifier si la connexion est fermée ou invalide
            if (connection == null || connection.isClosed() || !isConnectionValid()) {
                System.out.println("🔄 Établissement de la connexion à MySQL...");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✅ Connexion à MySQL réussie !");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion :");
            System.err.println("- Vérifiez que Laragon est démarré");
            System.err.println("- Vérifiez que MySQL fonctionne");
            System.err.println("- Vérifiez que la base 'gestion_paiements' existe");
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Vérifie si la connexion est valide
     */
    private boolean isConnectionValid() {
        try {
            return connection != null && connection.isValid(5); // Timeout de 5 secondes
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Force une nouvelle connexion
     */
    public Connection getNewConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de l'ancienne connexion: " + e.getMessage());
        }
        connection = null;
        return getConnection();
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
    
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && isConnectionValid();
        } catch (SQLException e) {
            return false;
        }
    }
}