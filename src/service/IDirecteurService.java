package service;

import model.Agent;
import model.Departement;
import java.util.List;
import java.util.Map;

public interface IDirecteurService extends IAgentService {
    boolean validerDemandeBonus(int demandeId, boolean approuve, String motifRejet, int directeurId);
    boolean validerDemandeIndemnite(int demandeId, boolean approuve, String motifRejet, int directeurId);
    List<Map<String, Object>> consulterDemandesEnAttente(int directeurId);

    // Map<String, Object> genererRapportGlobal(int directeurId);
    // Map<Departement, Map<String, Object>> calculerStatistiquesParDepartement(int directeurId);
    // List<Agent> genererTopAgentsMieuxPayes(int nombreAgents, int directeurId);
    // Map<TypePaiement, Map<String, Object>> calculerRepartitionPaiementsParType(int directeurId);

    Departement creerDepartement(Departement departement, int directeurId);
    Departement modifierDepartement(Departement departement, int directeurId);
    boolean supprimerDepartement(int departementId, int directeurId);
    boolean associerResponsableDepartement(int departementId, int responsableId, int directeurId);
    List<Departement> listerTousDepartements(int directeurId);

    Agent creerUtilisateur(Agent agent, int directeurId);
    Agent creerUtilisateurAvecDepartement(Agent agent, int departementId, int directeurId);
    Agent modifierDroitsUtilisateur(int agentId, model.TypeAgent nouveauType, int directeurId);
    boolean changerStatutUtilisateur(int agentId, boolean actif, int directeurId);
    boolean reinitialiserMotDePasse(int agentId, String nouveauMotDePasse, int directeurId);

    // Map<Paiement, List<String>> identifierPaiementsSuspects(int directeurId);
    // List<Map<String, Object>> consulterHistoriqueModifications(int directeurId);
    // Map<String, Object> genererRapportAuditComplet(java.util.Date periodeDebut, java.util.Date periodeFin, int directeurId);
    // boolean configurerSeuilsValidation(double seuilBonus, double seuilIndemnite, int directeurId);
    // boolean sauvegarderDonnees(int directeurId);
     boolean verifierPermissionsDirecteur(int directeurId);
}