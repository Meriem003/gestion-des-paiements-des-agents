package view;
import model.Agent;
import controller.ResponsableController;
import controller.PaiementController;
import service.IResponsableService;
import service.IPaiementService;
import service.Iimpl.PaiementServiceImpl;
import java.util.Scanner;

public class MenuResponsable {
    private Scanner scanner;
    private ResponsableController responsableController;
    private PaiementController paiementController;
    private Agent responsable;

    public MenuResponsable(Agent responsable, IResponsableService responsableService) {
        this.responsable = responsable;
        this.responsableController = new ResponsableController(responsableService);
        
        // Création d'une instance du service de paiement
        IPaiementService paiementService = new PaiementServiceImpl();
        this.paiementController = new PaiementController(paiementService, (service.IAgentService) responsableService);
        this.scanner = new Scanner(System.in);
    }
    
    public MenuResponsable(Agent responsable, IResponsableService responsableService, IPaiementService paiementService) {
        this.responsable = responsable;
        this.responsableController = new ResponsableController(responsableService, paiementService);
        this.paiementController = new PaiementController(paiementService, (service.IAgentService) responsableService);
        this.scanner = new Scanner(System.in);
    }

    public void afficherMenu() {
        int choix = 0;
        do {
            System.out.println("\n==== MENU RESPONSABLE DE DÉPARTEMENT ===");
            System.out.println("Connecté en tant que: " + responsable.getPrenom() + " " + responsable.getNom());
            System.out.println("===== Mes options personnelles =========");
            System.out.println("1. Accéder à mes informations");
            System.out.println("=======================================");
            System.out.println("========= Gestion des agents ==========");
            System.out.println("2. Ajouter un agent");
            System.out.println("3. Modifier un agent");
            System.out.println("4. Supprimer un agent");
            System.out.println("5. Affecter un agent à un département");
            System.out.println("=======================================");
            System.out.println("======== Gestion des paiements ========");
            System.out.println("6. Ajouter un salaire");
            System.out.println("7. Ajouter une prime");
            System.out.println("8. Consulter paiements d'un agent");
            System.out.println("9. Consulter paiements du département");
            System.out.println("=======================================");
            System.out.println("====== Statistiques département =======");
            System.out.println("10. Calculer les statistiques du département");
            System.out.println("11. Classement des agents par paiements");
            System.out.println("=========================================");
            System.out.println("0. Déconnexion");
            System.out.print("Choix : ");

            try {
                choix = scanner.nextInt();
                scanner.nextLine();

                switch (choix) {
                    case 1:
                        responsableController.accederMenuAgent(responsable);
                        break;
                    case 2:
                        responsableController.ajouterAgent(responsable.getId());
                        break;
                    case 3:
                        responsableController.modifierAgent(responsable.getId());
                        break;
                    case 4:
                        responsableController.supprimerAgent(responsable.getId());
                        break;
                    case 5:
                        responsableController.affecterAgentDepartement(responsable.getId());
                        break;
                    case 6:
                        paiementController.ajouterSalaire();
                        break;
                    case 7:
                        paiementController.ajouterPrime();
                        break;
                    case 8:
                        paiementController.consulterPaiementsAgent();
                        break;
                    case 9:
                        responsableController.consulterPaiementsDepartement(responsable.getId());
                        break;
                    case 10:
                        responsableController.calculerStatistiquesDepartement(responsable.getId());
                        break;
                    case 11:
                        responsableController.classementAgentsParPaiements(responsable.getId());
                        break;
                    case 0:
                        System.out.println("Déconnexion...");
                        break;
                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");
                }

            } catch (Exception e) {
                System.err.println("Erreur: " + e.getMessage());
                scanner.nextLine();
            }

        } while (choix != 0);
    }
}