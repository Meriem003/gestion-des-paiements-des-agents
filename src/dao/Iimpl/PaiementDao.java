package dao.Iimpl;

import dao.IPaiementDao;
import model.Paiement;
import model.TypePaiement;
import model.Agent;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PaiementDao implements IPaiementDao {
    private Connection connection;
    public PaiementDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void creer(Paiement paiement){
        String sql = "INSERT INTO paiement (type_paiement, montant, date_paiement, motif, condition_validee, agent_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, paiement.getTypePaiement().name());
            stmt.setBigDecimal(2, paiement.getMontant());
            stmt.setDate(3, Date.valueOf(paiement.getDatePaiement()));
            stmt.setString(4, paiement.getMotif());
            stmt.setBoolean(5, paiement.isConditionValidee());
            stmt.setInt(6, paiement.getAgent().getId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                paiement.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du paiement: " + e.getMessage(), e);
        }
    }

    @Override
    public Paiement lireParId(int id){
        String sql = "SELECT * FROM paiement WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapperPaiement(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la lecture du paiement avec l'ID " + id + ": " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Paiement> lireTous(){
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement ORDER BY date_paiement DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                paiements.add(mapperPaiement(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la lecture de tous les paiements: " + e.getMessage(), e);
        }
        return paiements;
    }

    @Override
    public void mettreAJour(Paiement paiement){
        String sql = "UPDATE paiement SET type_paiement = ?, montant = ?, date_paiement = ?, motif = ?, condition_validee = ?, agent_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, paiement.getTypePaiement().name());
            stmt.setBigDecimal(2, paiement.getMontant());
            stmt.setDate(3, Date.valueOf(paiement.getDatePaiement()));
            stmt.setString(4, paiement.getMotif());
            stmt.setBoolean(5, paiement.isConditionValidee());
            stmt.setInt(6, paiement.getAgent().getId());
            stmt.setInt(7, paiement.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du paiement: " + e.getMessage(), e);
        }
    }

    @Override
    public void supprimer(int id){
        String sql = "DELETE FROM paiement WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du paiement avec l'ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Paiement> findPaiementsByAgentId(int agentId) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement WHERE agent_id = ? ORDER BY date_paiement DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, agentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                paiements.add(mapperPaiement(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des paiements pour l'agent ID " + agentId + ": " + e.getMessage(), e);
        }
        return paiements;
    }

    private Paiement mapperPaiement(ResultSet rs) throws SQLException {
        Paiement paiement = new Paiement();
        paiement.setId(rs.getInt("id"));        
        String typePaiementStr = rs.getString("type_paiement");
        if (typePaiementStr != null) {
            paiement.setTypePaiement(TypePaiement.valueOf(typePaiementStr));
        }
        paiement.setMontant(rs.getBigDecimal("montant"));
        Date datePaiementSql = rs.getDate("date_paiement");
        if (datePaiementSql != null) {
            paiement.setDatePaiement(datePaiementSql.toLocalDate());
        }
        paiement.setMotif(rs.getString("motif"));
        paiement.setConditionValidee(rs.getBoolean("condition_validee"));
        int agentId = rs.getInt("agent_id");
        Agent agent = new Agent();
        agent.setId(agentId);
        paiement.setAgent(agent);
        return paiement;
    }
}