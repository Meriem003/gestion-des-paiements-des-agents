package dao;

import model.Agent;
import java.util.List;

public interface IAgentDao {
    
    void creer(Agent agent);
    Agent lireParId(int id);
    List<Agent> lireTous();
    void mettreAJour(Agent agent);
    void supprimer(int id);
}