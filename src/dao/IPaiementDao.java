package dao;

import model.Paiement;
import java.sql.SQLException;
import java.util.List;

public interface IPaiementDao {
    
    void creer(Paiement paiement);
    Paiement lireParId(int id);
    List<Paiement> lireTous();
    void mettreAJour(Paiement paiement);
    void supprimer(int id);
}