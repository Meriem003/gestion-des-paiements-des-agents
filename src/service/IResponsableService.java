package service;
import model.Agent;
import model.Paiement;
import model.TypePaiement;
import java.util.List;

public interface IResponsableService extends IAgentService {
    
    Agent ajouterAgent(Agent agent, int responsableId);
    Agent modifierAgent(Agent agent, int responsableId);
    boolean supprimerAgent(int agentId, int responsableId);
    boolean affecterAgentDepartement(int agentId, int departementId, int responsableId);

    Paiement ajouterSalaire(int agentId, double montant, int responsableId);
    Paiement ajouterPrime(int agentId, double montant, int responsableId);
    int demanderBonus(int agentId, double montant, int responsableId);
    int demanderIndemnite(int agentId, double montant, int responsableId);

    List<Paiement> consulterPaiementsAgent(int agentId, int responsableId);
    List<Paiement> consulterPaiementsDepartement(int departementId, int responsableId);
    List<Paiement> filtrerTrierPaiementsDepartement(int departementId, TypePaiement typePaiement, 
                boolean triParMontant, boolean triParDate, int responsableId);

    // Map<String, Object> calculerStatistiquesDepartement(int departementId, int responsableId);
    // List<Agent> classementAgentsParPaiement(int departementId, int responsableId);
    // Map<Paiement, String> identifierPaiementsInhabituels(int departementId, int responsableId);
    // boolean verifierPermissionsResponsable(int responsableId, int departementId);
}