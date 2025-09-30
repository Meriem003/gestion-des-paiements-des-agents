package dao;

import model.Departement;
import java.util.List;

public interface IDepartementDao {
    
    void creer(Departement departement);
    
    Departement lireParId(int id);
    List<Departement> lireTous();
    
    void mettreAJour(Departement departement);
    
    void supprimer(int id);
}