package service;

import model.Agent;
import model.Departement;
import model.Paiement;
import model.TypePaiement;
import java.util.List;
import java.util.Map;


public interface IAgentService {

    Agent obtenirInformationsAgent(int agentId);//valide
    List<Paiement> obtenirHistoriquePaiements(int agentId);
    List<Paiement> filtrerPaiementsParType(int agentId, TypePaiement typePaiement);
    List<Paiement> trierPaiementsParMontant(int agentId, boolean croissant);
    List<Paiement> trierPaiementsParDate(int agentId, boolean plusRecent);
    double calculerTotalPaiements(int agentId);
    double calculerSalaireAnnuel(int agentId, int annee);
    Map<TypePaiement, Integer> compterPrimesBonus(int agentId);
    Map<String, Paiement> obtenirPaiementsExtremes(int agentId);
    Map<String, Object> genererStatistiquesCompletes(int agentId);
    Agent authentifier(String email, String motDePasse);
}