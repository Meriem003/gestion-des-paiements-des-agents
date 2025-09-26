package dao;

import model.Departement;
import java.sql.SQLException;
import java.util.List;

public interface IDepartementDao {
    
    void creer(Departement departement) throws SQLException;
    
    Departement lireParId(int id) throws SQLException;
    List<Departement> lireTous() throws SQLException;
    
    void mettreAJour(Departement departement) throws SQLException;
    
    void supprimer(int id) throws SQLException;
}