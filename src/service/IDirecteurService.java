package service;

import model.Agent;
import model.Departement;
import model.TypePaiement;
import java.util.List;
import java.util.Map;

public interface IDirecteurService extends IAgentService {
    Departement creerDepartement(Departement departement, int directeurId);
    Departement modifierDepartement(Departement departement, int directeurId);
    boolean supprimerDepartement(int departementId, int directeurId);
    List<Departement> listerTousDepartements(int directeurId);
    Agent creerUtilisateurAvecDepartement(Agent agent, int departementId, int directeurId);
    boolean modifierResponsable(int idResponsable, String nom, String prenom, String email);
    boolean supprimerResponsable(int idResponsable);
    List<Agent> listerTousResponsables();
    int obtenirNombreTotalAgents();
    int obtenirNombreTotalDepartements();
    List<Agent> genererTopAgentsMieuxPayes(int nombreAgents, int directeurId);
    Map<String, Object> genererRapportGlobalEntreprise(int directeurId);
}