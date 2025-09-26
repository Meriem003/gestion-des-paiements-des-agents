package dao.Iimpl;

import dao.IDepartementDao;
import model.Departement;
import model.Agent;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class DepartementDao implements IDepartementDao {
    private Connection connection;
    public DepartementDao(Connection connection) {
        this.connection = connection;
    }

    public void creer(Departement departement) throws SQLException {
        String sql = "INSERT INTO departement (nom) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, departement.getNom());
            stmt.executeUpdate();            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                departement.setId(rs.getInt(1));
            }
        }
    }

    public Departement lireParId(int id) throws SQLException {
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
        }
        return null;
    }

    public List<Departement> lireTous() throws SQLException {
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
        }
        return departements;
    }

    public void mettreAJour(Departement departement) throws SQLException {
        String sql = "UPDATE departement SET nom = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, departement.getNom());
            stmt.setInt(2, departement.getId());
            stmt.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM departement WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Agent getResponsableDepartement(int departementId) throws SQLException {
        String sql = "SELECT * FROM agent WHERE departement_id = ? AND est_responsable_departement = TRUE";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, departementId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapperAgent(rs);
            }
        }
        return null;
    }

    private List<Agent> getAgentsDuDepartement(int departementId) throws SQLException {
        List<Agent> agents = new ArrayList<>();
        String sql = "SELECT * FROM agent WHERE departement_id = ? ORDER BY nom, prenom";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, departementId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                agents.add(mapperAgent(rs));
            }
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