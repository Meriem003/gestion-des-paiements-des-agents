package service;
import model.Agent;
import model.Paiement;
import model.TypePaiement;
import java.util.List;
import java.util.Map;

public interface IResponsableService extends IAgentService {
    Agent ajouterAgent(Agent agent, int responsableId);
    Agent modifierAgent(Agent agent, int responsableId);
    boolean supprimerAgent(int agentId, int responsableId);
    List<Agent> listerAgentsMonDepartement(int idResponsable);
    List<Paiement> consulterPaiementsAgentDepartement(int agentId, int responsableId);
    List<Paiement> consulterTousPaiementsDepartement(int departementId, int responsableId);
    List<Paiement> filtrerPaiementsDepartement(int departementId, TypePaiement typePaiement, 
                boolean triParMontant, boolean triParDate, int responsableId);
    Map<String, Object> calculerStatistiquesDepartement(int departementId, int responsableId);
    List<Agent> classementAgentsParPaiement(int departementId, int responsableId);
}