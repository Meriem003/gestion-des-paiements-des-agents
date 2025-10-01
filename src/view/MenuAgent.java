package view;
import model.Agent;
import controller.AgentController;
import service.IAgentService;
import java.util.Scanner;

public class MenuAgent {
    private Scanner scanner;
    private Agent agentConnecte;
    private AgentController agentController;
    
    public MenuAgent(Agent agentConnecte, IAgentService agentService) {
        this.scanner = new Scanner(System.in);
        this.agentConnecte = agentConnecte;
        this.agentController = new AgentController(agentService);
    }
    public void afficherMenu() {
        int choix = 0;
        do {
            try {
                afficherOptionsMenu();
                choix = obtenirChoixUtilisateur();
                traiterChoix(choix);
            } catch (Exception e) {
                System.err.println("Erreur dans le menu : " + e.getMessage());
                scanner.nextLine();
            }
        } while (choix != 0);
    }

    private void afficherOptionsMenu() {
        System.out.println("\n=== MENU AGENT ===");
        System.out.println("Connecté en tant que: " + agentConnecte.getNom() + " " + agentConnecte.getPrenom());
        System.out.println("Type: " + agentConnecte.getTypeAgent());
        System.out.println("--- Mes informations ---");
        System.out.println("1. Consulter mes informations personnelles");
        System.out.println("2. Modifier mes informations personnelles");
        System.out.println("3. Consulter mon département");
        System.out.println("--- Mes paiements ---");
        System.out.println("4. Consulter l'historique de mes paiements");
        System.out.println("5. Filtrer et trier mes paiements");
        System.out.println("6. Calculer le total de mes paiements");
        System.out.println("--- Mes statistiques ---");
        System.out.println("7. Consulter mon salaire annuel total");
        System.out.println("8. Consulter le nombre de primes/bonus/indemnités reçus");
        System.out.println("9. Voir mon paiement le plus élevé et le plus faible");
        System.out.println("10. Afficher mes statistiques complètes");
        System.out.println("===========================");
        System.out.println("0. Déconnexion");
        System.out.print("Choix : ");
    }

    private int obtenirChoixUtilisateur() {
        int choix = scanner.nextInt();
        scanner.nextLine(); 
        return choix;
    }

    private void traiterChoix(int choix) {
        switch (choix) {
            case 1:
                agentController.consulterInformationsPersonnelles(agentConnecte.getId());
                attendreEntree();
                break;
            case 2:
                agentController.modifierInformationsPersonnelles(agentConnecte.getId());
                rafraichirAgentConnecte();
                attendreEntree();
                break;
            case 3:
                agentController.consulterDepartement(agentConnecte.getId());
                attendreEntree();
                break;
            case 4:
                agentController.consulterHistoriquePaiements(agentConnecte.getId());
                attendreEntree();
                break;
            case 5:
                agentController.filtrerEtTrierPaiements(agentConnecte.getId());
                attendreEntree();
                break;
            case 6:
                agentController.calculerTotalPaiements(agentConnecte.getId());
                attendreEntree();
                break;
            case 7:
                agentController.consulterSalaireAnnuel(agentConnecte.getId());
                attendreEntree();
                break;
            case 8:
                agentController.consulterNombrePrimesBonus(agentConnecte.getId());
                attendreEntree();
                break;
            case 9:
                agentController.consulterPaiementExtremes(agentConnecte.getId());
                attendreEntree();
                break;
            case 10:
                agentController.afficherStatistiquesCompletes(agentConnecte.getId());
                attendreEntree();
                break;
            case 0:
                System.out.println("Merci d'avoir utilisé notre application - Déconnexion...");
                break;
            default:
                System.out.println("Choix invalide. Veuillez choisir un nombre entre 0 et 10.");
        }
    }

    private void attendreEntree() {
        System.out.println("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    private void rafraichirAgentConnecte() {
        System.out.println("Informations mises à jour.");
    }

    public Agent getAgentConnecte() {
        return agentConnecte;
    }

    public void setAgentConnecte(Agent agentConnecte) {
        this.agentConnecte = agentConnecte;
    }
}