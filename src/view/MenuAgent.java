package view;
import model.Agent;
import controller.AgentController;
import controller.PaiementController;
import service.IAgentService;
import service.IPaiementService;
import service.Iimpl.PaiementServiceImpl;
import java.util.Scanner;

public class MenuAgent {
    private Scanner scanner;
    private Agent agentConnecte;
    private AgentController agentController;
    private PaiementController paiementController;
    
    public MenuAgent(Agent agentConnecte, IAgentService agentService) {
        this.scanner = new Scanner(System.in);
        this.agentConnecte = agentConnecte;
        this.agentController = new AgentController(agentService);        
        this.paiementController = new PaiementController(new PaiementServiceImpl(), agentService);
    }
    public void afficherMenu() {
        int choix;
        do {
            afficherOptionsMenu();
            try {
                choix = scanner.nextInt();
                scanner.nextLine();
                traiterChoix(choix);
            } catch (Exception e) {
                System.out.println("❌ Erreur de saisie. Veuillez entrer un nombre valide.");
                scanner.nextLine();
                choix = -1;
            }
        } while (choix != 0);
    }

    private void afficherOptionsMenu() {
        System.out.println("\n============ MENU AGENT ============");
        System.out.println("👤 Connecté: " + agentConnecte.getNom() + " " + agentConnecte.getPrenom());
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("1. Mes informations");
        System.out.println("2. Mes paiements");
        System.out.println("3. Paiements par type");
        System.out.println("4. Calculer salaire");
        System.out.println("5. Trier paiements");
        System.out.println("6. Nombre de primes");
        System.out.println("7. Paiements extrêmes");
        System.out.println("8. Statistiques");
        System.out.println("0. Déconnexion");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.print("Votre choix : ");
    }

    private void traiterChoix(int choix) {
        try {
            switch (choix) {
                case 1:
                    agentController.consulterInformationsPersonnelles(agentConnecte.getId());
                    break;
                case 2:
                    paiementController.consulterMesPaiements(agentConnecte.getId());
                    break;
                case 3:
                    paiementController.consulterMesPaiementsParType(agentConnecte.getId());
                    break;
                case 4:
                    paiementController.calculerSalairePeriode(agentConnecte.getId());
                    break;
                case 5:
                    paiementController.trierMesPaiements(agentConnecte.getId());
                    break;
                case 6:
                    agentController.consulterNombrePrimesBonus(agentConnecte.getId());
                    break;
                case 7:
                    agentController.consulterPaiementExtremes(agentConnecte.getId());
                    break;
                case 8:
                    agentController.afficherStatistiquesCompletes(agentConnecte.getId());
                    break;
                case 0:
                    System.out.println("À bientôt " + agentConnecte.getPrenom() + " !");
                    break;
                default:
                    System.out.println("❌ Choix invalide. Veuillez choisir une option entre 0 et 8.");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'exécution de l'opération: " + e.getMessage());
            System.out.println("Veuillez réessayer ou contacter l'administrateur si le problème persiste.");
        }
        
        if (choix != 0) {
            System.out.println("\n⏎ Appuyez sur Entrée pour continuer...");
            scanner.nextLine();
        }
    }
}