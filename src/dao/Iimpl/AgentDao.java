package dao.Iimpl;

import dao.IAgentDao;
import model.Agent;
import model.TypeAgent;
import model.Departement;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import utils.DBConnection;

public class AgentDao implements IAgentDao {
    private Connection connection;
    
    public AgentDao() {
        this.connection = DBConnection.getInstance().getConnection();
    }
    
    public AgentDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void creer(Agent agent){
        String sql = "INSERT INTO agent (nom, prenom, email, mot_de_passe, type_agent, departement_id, est_responsable_departement) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, agent.getNom());
            stmt.setString(2, agent.getPrenom());
            stmt.setString(3, agent.getEmail());
            stmt.setString(4, agent.getMotDePasse());
            stmt.setString(5, agent.getTypeAgent().name());
            
            if (agent.getDepartement() != null) {
                stmt.setInt(6, agent.getDepartement().getId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.setBoolean(7, agent.isEstResponsableDepartement());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                agent.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'agent: " + e.getMessage(), e);
        }
    }

    @Override
    public Agent lireParId(int id){
        String sql = "SELECT * FROM agent WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapperAgent(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la lecture de l'agent avec l'ID " + id + ": " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Agent> lireTous(){
        List<Agent> agents = new ArrayList<>();
        String sql = "SELECT * FROM agent ORDER BY nom, prenom";
        
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                agents.add(mapperAgent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la lecture de tous les agents: " + e.getMessage(), e);
        }
        return agents;
    }

    @Override
    public void mettreAJour(Agent agent){
        String sql = "UPDATE agent SET nom = ?, prenom = ?, email = ?, mot_de_passe = ?, type_agent = ?, departement_id = ?, est_responsable_departement = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agent.getNom());
            stmt.setString(2, agent.getPrenom());
            stmt.setString(3, agent.getEmail());
            stmt.setString(4, agent.getMotDePasse());
            stmt.setString(5, agent.getTypeAgent().name());
            
            if (agent.getDepartement() != null) {
                stmt.setInt(6, agent.getDepartement().getId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.setBoolean(7, agent.isEstResponsableDepartement());
            stmt.setInt(8, agent.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'agent: " + e.getMessage(), e);
        }
    }

    @Override
    public void supprimer(int id){
        String sql = "DELETE FROM agent WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'agent avec l'ID " + id + ": " + e.getMessage(), e);
        }
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
            agent.setTypeAgent(TypeAgent.valueOf(typeAgentStr));
        }
        
        agent.setEstResponsableDepartement(rs.getBoolean("est_responsable_departement"));
        
        int departementId = rs.getInt("departement_id");
        if (!rs.wasNull()) {
            String sqlDept = "SELECT nom FROM departement WHERE id = ?";
            try (PreparedStatement stmtDept = connection.prepareStatement(sqlDept)) {
                stmtDept.setInt(1, departementId);
                ResultSet rsDept = stmtDept.executeQuery();
                if (rsDept.next()) {
                    Departement departement = new Departement();
                    departement.setId(departementId);
                    departement.setNom(rsDept.getString("nom"));
                    agent.setDepartement(departement);
                }
            } catch (SQLException e) {
                // Log l'erreur mais continue le mapping de l'agent
                System.err.println("Erreur lors du chargement du département: " + e.getMessage());
            }
        }
        
        return agent;
    }
    
    @Override
    public Agent authentifier(String email, String motDePasse) {
        String sql = "SELECT a.id, a.nom, a.prenom, a.email, a.mot_de_passe, a.type_agent, a.est_responsable_departement, " +
                     "d.id as dept_id, d.nom as dept_nom " +
                     "FROM agent a " +
                     "LEFT JOIN departement d ON a.departement_id = d.id " +
                     "WHERE a.email = ? AND a.mot_de_passe = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, motDePasse);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Agent agent = new Agent();
                agent.setId(rs.getInt("id"));
                agent.setNom(rs.getString("nom"));
                agent.setPrenom(rs.getString("prenom"));
                agent.setEmail(rs.getString("email"));
                agent.setMotDePasse(rs.getString("mot_de_passe"));
                agent.setTypeAgent(TypeAgent.valueOf(rs.getString("type_agent")));
                agent.setEstResponsableDepartement(rs.getBoolean("est_responsable_departement"));
                
                // Charger le département si il existe
                if (rs.getInt("dept_id") != 0) {
                    Departement dept = new Departement();
                    dept.setId(rs.getInt("dept_id"));
                    dept.setNom(rs.getString("dept_nom"));
                    agent.setDepartement(dept);
                }
                
                return agent;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'authentification: " + e.getMessage(), e);
        }
    }
}