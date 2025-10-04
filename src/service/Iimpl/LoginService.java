package service.Iimpl;

import model.Agent;
import model.TypeAgent;
import utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginService {
    private Connection connection;
    
    public LoginService() {
        this.connection = DBConnection.getInstance().getConnection();
    }
    
    public Agent authentifier(String email, String motDePasse) {
        String query = "SELECT id, nom, prenom, email, mot_de_passe, type_agent FROM agent WHERE email = ? AND mot_de_passe = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, motDePasse);
            
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                Agent agent = new Agent();
                agent.setId(resultSet.getInt("id"));
                agent.setNom(resultSet.getString("nom"));
                agent.setPrenom(resultSet.getString("prenom"));
                agent.setEmail(resultSet.getString("email"));
                agent.setMotDePasse(resultSet.getString("mot_de_passe"));                
                String typeAgentStr = resultSet.getString("type_agent");
                agent.setTypeAgent(TypeAgent.valueOf(typeAgentStr));
                
                return agent;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Type d'agent invalide dans la base de données : " + e.getMessage());
        }
        
        return null;
    }

    public boolean emailExiste(String email) {
        String query = "SELECT COUNT(*) FROM agent WHERE email = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'email : " + e.getMessage());
        }
        return false;
    }

    public Agent obtenirAgentParEmail(String email) {
        String query = "SELECT id, nom, prenom, email, type_agent FROM agent WHERE email = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                Agent agent = new Agent();
                agent.setId(resultSet.getInt("id"));
                agent.setNom(resultSet.getString("nom"));
                agent.setPrenom(resultSet.getString("prenom"));
                agent.setEmail(resultSet.getString("email"));
                
                String typeAgentStr = resultSet.getString("type_agent");
                agent.setTypeAgent(TypeAgent.valueOf(typeAgentStr));
                
                return agent;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'agent : " + e.getMessage());
        }
        
        return null;
    }
    public boolean validerFormatEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    public boolean validerMotDePasse(String motDePasse) {
        if (motDePasse == null || motDePasse.trim().isEmpty()) {
            return false;
        }
        return motDePasse.length() >= 4;
    }
}