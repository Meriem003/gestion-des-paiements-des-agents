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
            System.out.println("\n============ MENU RESPONSABLE DE DÉPARTEMENT ============");
            System.out.println("👤 Responsable connecté: " + responsable.getPrenom() + " " + responsable.getNom());
            System.out.println("🏢 Département: " + (responsable.getDepartement() != null ? responsable.getDepartement().getNom() : "Non assigné"));
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("========= Mes options personnelles =========");
            System.out.println("1. 👤 Accéder à mes informations personnelles");
            System.out.println("═══════════ Gestion des agents ═════════════");
            System.out.println("2. Ajouter un agent");
            System.out.println("3. Modifier un agent");
            System.out.println("4. Supprimer un agent");
            System.out.println("5. Affecter un agent à un département");
            System.out.println("6. Lister les agents de mon département");
            System.out.println("══════════ Gestion des paiements ═══════════");
            System.out.println("7. Ajouter un salaire");
            System.out.println("8. Ajouter une prime");
            System.out.println("9. Consulter paiements d'un agent");
            System.out.println("10. Consulter tous les paiements du département");
            System.out.println("11. Filtrer les paiements du département");
            System.out.println("═══════════════ Statistiques ═══════════════");
            System.out.println("12. Calculer les statistiques du département");
            System.out.println("13. Classement des agents par paiements");
            System.out.println("14. Afficher statistiques détaillées");
            System.out.println("15. Afficher classement des agents");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("0. Déconnexion");
            System.out.print("Votre choix : ");

            try {
                choix = scanner.nextInt();
                scanner.nextLine(); // Consommer le retour à la ligne

                switch (choix) {
                    case 1:
                        responsableController.accederMenuAgent(responsable);
                        break;
                    case 2:
                        responsableController.ajouterAgent(responsable.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 3:
                        responsableController.modifierAgent(responsable.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 4:
                        responsableController.supprimerAgent(responsable.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 5:
                        responsableController.affecterAgentDepartement(responsable.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 6:
                        responsableController.listerAgentsMonDepartement(responsable.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 7:
                        responsableController.ajouterSalaire(responsable.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 8:
                        responsableController.ajouterPrime(responsable.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 9:
                        paiementController.consulterPaiementsAgent();
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 10:
                        responsableController.consulterPaiementsDepartement(responsable.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 11:
                        responsableController.filtrerPaiementsDepartement(responsable.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 12:
                        responsableController.calculerStatistiquesDepartement(responsable.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 13:
                        responsableController.classementAgentsParPaiements(responsable.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 14:
                        responsableController.afficherStatistiquesDepartement(responsable.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 15:
                        responsableController.afficherClassementAgents(responsable.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 0:
                        System.out.println("Merci d'avoir utilisé notre application - Déconnexion...");
                        break;
                    default:
                        System.out.println("Choix invalide. Veuillez choisir un nombre entre 0 et 15.");
                }
            } catch (Exception e) {
                System.err.println("Erreur de saisie. Veuillez entrer un nombre valide.");
                scanner.nextLine();
            }

        } while (choix != 0);
    }

    public Agent getResponsable() {
        return responsable;
    }

    public void setResponsable(Agent responsable) {
        this.responsable = responsable;
    }
}
