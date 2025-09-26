package dao;

import model.Agent;
import java.sql.SQLException;
import java.util.List;

public interface IAgentDao {
    
    void creer(Agent agent) throws SQLException;
    Agent lireParId(int id) throws SQLException;
    List<Agent> lireTous() throws SQLException;
    void mettreAJour(Agent agent) throws SQLException;
    void supprimer(int id) throws SQLException;
}