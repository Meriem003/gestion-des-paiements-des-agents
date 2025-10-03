package dao.Iimpl;

import dao.IDepartementDao;
import model.Departement;
import model.Agent;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import utils.DBConnection;

public class DepartementDao implements IDepartementDao {
    private Connection connection;
    
    public DepartementDao() {
        this.connection = DBConnection.getInstance().getConnection();
    }
    
    public DepartementDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void creer(Departement departement){
        String sql = "INSERT INTO departement (nom) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, departement.getNom());
            stmt.executeUpdate();            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                departement.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du département: " + e.getMessage(), e);
        }
    }

    @Override
    public Departement lireParId(int id){
        String sql = "SELECT * FROM departement WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Departement departement = new Departement();
                departement.setId(rs.getInt("id"));
                departement.setNom(rs.getString("nom"));                
                Agent responsable = getResponsableDepartement(id);
                departement.setResponsable(responsable);                
                List<Agent> agents = getAgentsDuDepartement(id);
                departement.setAgents(agents);
                
                return departement;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la lecture du département avec l'ID " + id + ": " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Departement> lireTous(){
        List<Departement> departements = new ArrayList<>();
        String sql = "SELECT * FROM departement ORDER BY nom";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Departement departement = new Departement();
                departement.setId(rs.getInt("id"));
                departement.setNom(rs.getString("nom"));                
                Agent responsable = getResponsableDepartement(departement.getId());
                departement.setResponsable(responsable);                
                List<Agent> agents = getAgentsDuDepartement(departement.getId());
                departement.setAgents(agents);
                
                departements.add(departement);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la lecture de tous les départements: " + e.getMessage(), e);
        }
        return departements;
    }

    @Override
    public void mettreAJour(Departement departement){
        String sql = "UPDATE departement SET nom = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, departement.getNom());
            stmt.setInt(2, departement.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du département: " + e.getMessage(), e);
        }
    }

    @Override
    public void supprimer(int id){
        String sql = "DELETE FROM departement WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du département avec l'ID " + id + ": " + e.getMessage(), e);
        }
    }

    private Agent getResponsableDepartement(int departementId){
        String sql = "SELECT * FROM agent WHERE departement_id = ? AND est_responsable_departement = TRUE";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, departementId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapperAgent(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du responsable du département: " + e.getMessage());
        }
        return null;
    }

    private List<Agent> getAgentsDuDepartement(int departementId){
        List<Agent> agents = new ArrayList<>();
        String sql = "SELECT * FROM agent WHERE departement_id = ? ORDER BY nom, prenom";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, departementId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                agents.add(mapperAgent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des agents du département: " + e.getMessage());
        }
        return agents;
    }
    
    private Agent mapperAgent(ResultSet rs) throws SQLException {
        Agent agent = new Agent();
        agent.setId(rs.getInt("id"));
        agent.setNom(rs.getString("nom"));
        agent.setPrenom(rs.getString("prenom"));
        agent.setEmail(rs.getString("email"));
        agent.setMotDePasse(rs.getString("mot_de_passe"));        
        String typeAgentStr = rs.getString("type_agent");
        if (typeAgentStr != null) {
            agent.setTypeAgent(model.TypeAgent.valueOf(typeAgentStr));
        }
        
        agent.setEstResponsableDepartement(rs.getBoolean("est_responsable_departement"));
        
        return agent;
    }
}