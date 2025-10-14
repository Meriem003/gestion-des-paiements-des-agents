package controller;

import model.Agent;
import model.Departement;
import model.Paiement;
import model.TypePaiement;
import service.IAgentService;
import service.Iimpl.AgentServiceImpl;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class AgentController {
    private IAgentService agentService;
    private Scanner scanner;

    public AgentController(IAgentService agentService) {
        this.agentService = agentService;
        this.scanner = new Scanner(System.in);
    }

    //menu agent choix 1
    public void consulterInformationsPersonnelles(int agentId) {
        try {
            Agent agent = agentService.obtenirInformationsAgent(agentId);
            if (agent != null) {
                System.out.println("Nom: " + agent.getNom());
                System.out.println("Prénom: " + agent.getPrenom());
                System.out.println("Email: " + agent.getEmail());
                System.out.println("Type: " + agent.getTypeAgent());
                if (agent.getDepartement() != null) {
                    System.out.println("Département: " + agent.getDepartement().getNom());
                } else {
                    System.out.println("Département: -");
                }
                System.out.println("Informations affichées avec succès.");
            } else {
                System.out.println("Erreur : Impossible de récupérer les informations de l'agent.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des informations : " + e.getMessage());
        }
    }

    //menu agent choix 6
    public void consulterNombrePrimesBonus(int agentId) {
        try {
            Map<TypePaiement, Integer> statistiques = agentService.compterPrimesBonus(agentId);
            if (statistiques == null || statistiques.isEmpty()) {
                System.out.println("Aucun paiement trouvé pour cet agent ou agent introuvable.");
                return;
            }
            System.out.println("=== COMPTAGE DES PAIEMENTS POUR L'AGENT ID " + agentId + " ===");
            Integer salaires = statistiques.getOrDefault(TypePaiement.SALAIRE, 0);
            Integer primes = statistiques.getOrDefault(TypePaiement.PRIME, 0);
            Integer bonus = statistiques.getOrDefault(TypePaiement.BONUS, 0);
            Integer indem = statistiques.getOrDefault(TypePaiement.INDEMNITE, 0);
            int total = salaires + primes + bonus + indem;
            System.out.println("Salaires: " + salaires);
            System.out.println("Primes: " + primes);
            System.out.println("Bonus: " + bonus);
            System.out.println("Indemnités: " + indem);
            System.out.println("Total paiements: " + total);
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des primes/bonus : " + e.getMessage());
        }
    }
}