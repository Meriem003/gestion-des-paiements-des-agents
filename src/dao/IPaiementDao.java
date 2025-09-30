package dao;

import model.Paiement;
import java.util.List;

public interface IPaiementDao {
    
    void creer(Paiement paiement);
    Paiement lireParId(int id);
    List<Paiement> lireTous();
    List<Paiement> findPaiementsByAgentId(int agentId);
    void mettreAJour(Paiement paiement);
    void supprimer(int id);
}