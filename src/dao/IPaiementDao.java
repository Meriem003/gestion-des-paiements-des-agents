package dao;

import model.Paiement;
import java.sql.SQLException;
import java.util.List;

public interface IPaiementDao {
    
    void creer(Paiement paiement) throws SQLException;
    Paiement lireParId(int id) throws SQLException;
    List<Paiement> lireTous() throws SQLException;
    void mettreAJour(Paiement paiement) throws SQLException;
    void supprimer(int id) throws SQLException;
}