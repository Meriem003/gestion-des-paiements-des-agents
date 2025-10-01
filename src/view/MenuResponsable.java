package view;
import model.Agent;
import controller.ResponsableController;
import service.IResponsableService;
import java.util.Scanner;

public class MenuResponsable {
    private Scanner scanner;
    private Agent responsableConnecte;
    private ResponsableController responsableController;
    
    public MenuResponsable(Agent responsableConnecte, IResponsableService responsableService) {
        this.scanner = new Scanner(System.in);
        this.responsableConnecte = responsableConnecte;
        this.responsableController = new ResponsableController(responsableService);
    }

    public void afficherMenu() {
        int choix = 0;
        do {
            System.out.println("==== MENU RESPONSABLE DE DÉPARTEMENT ===");
            System.out.println("Connecté en tant que: " + responsableConnecte.getNom() + " " + responsableConnecte.getPrenom());
            System.out.println("Département: " + (responsableConnecte.getDepartement() != null ? responsableConnecte.getDepartement().getNom() : "Non assigné"));
            System.out.println("===== Mes options personnelles ========");
            System.out.println("--- Mes options personnelles ---");
            System.out.println("1. Accéder à mes informations");
            System.out.println("=======================================");
            System.out.println("========= Gestion des agents ==========");
            System.out.println("2. Ajouter un agent");
            System.out.println("3. Modifier un agent");
            System.out.println("4. Supprimer un agent");
            System.out.println("5. Affecter un agent à un département");
            System.out.println("=======================================");
            System.out.println("======== Gestion des paiements ========");
            System.out.println("6. Ajouter un paiement à un agent");
            System.out.println("7. Consulter les paiements d'un agent");
            System.out.println("8. Consulter les paiements de tout le département");
            System.out.println("9. Filtrer et trier les paiements du département");
            System.out.println("=======================================");
            System.out.println("====== Statistiques département =======");
            System.out.println("10. Calculer les statistiques du département");
            System.out.println("11. Classement des agents (du plus payé au moins payé)");
            System.out.println("12. Identifier les paiements inhabituels");
            System.out.println("=========================================");
            System.out.println("0. Déconnexion");
            System.out.print("Choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();
            
            switch (choix) {
                case 1:
                    responsableController.accederMenuAgent(responsableConnecte);
                    break;
                case 2:
                    responsableController.ajouterAgent(responsableConnecte.getId());
                    attendreEntree();
                    break;
                case 3:
                    responsableController.modifierAgent(responsableConnecte.getId());
                    attendreEntree();
                    break;
                case 4:
                    responsableController.supprimerAgent(responsableConnecte.getId());
                    attendreEntree();
                    break;
                case 5:
                    responsableController.affecterAgentDepartement(responsableConnecte.getId());
                    attendreEntree();
                    break;
                case 6:
                    responsableController.ajouterPaiementAgent(responsableConnecte.getId());
                    attendreEntree();
                    break;
                case 7:
                    responsableController.consulterPaiementsAgent(responsableConnecte.getId());
                    attendreEntree();
                    break;
                case 8:
                    int departementId = responsableConnecte.getDepartement() != null ? 
                        responsableConnecte.getDepartement().getId() : 0;
                    if (departementId > 0) {
                        responsableController.consulterPaiementsDepartement(responsableConnecte.getId(), departementId);
                    } else {
                        System.out.println("❌ Vous n'êtes assigné à aucun département.");
                    }
                    attendreEntree();
                    break;
                case 9:
                    int deptId = responsableConnecte.getDepartement() != null ? 
                        responsableConnecte.getDepartement().getId() : 0;
                    if (deptId > 0) {
                        responsableController.filtrerTrierPaiementsDepartement(responsableConnecte.getId(), deptId);
                    } else {
                        System.out.println("❌ Vous n'êtes assigné à aucun département.");
                    }
                    attendreEntree();
                    break;
                case 10:
                    int depId = responsableConnecte.getDepartement() != null ? 
                        responsableConnecte.getDepartement().getId() : 0;
                    responsableController.calculerStatistiquesDepartement(responsableConnecte.getId(), depId);
                    attendreEntree();
                    break;
                case 11:
                    int dId = responsableConnecte.getDepartement() != null ? 
                        responsableConnecte.getDepartement().getId() : 0;
                    responsableController.classementAgents(responsableConnecte.getId(), dId);
                    attendreEntree();
                    break;
                case 12:
                    int deptID = responsableConnecte.getDepartement() != null ? 
                        responsableConnecte.getDepartement().getId() : 0;
                    responsableController.identifierPaiementsInhabituels(responsableConnecte.getId(), deptID);
                    attendreEntree();
                    break;
                case 0:
                    System.out.println("Merci d'avoir utilisé notre application - Déconnexion...");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez choisir un nombre entre 0 et 12.");
            }
        } while (choix != 0);
    }

    private void attendreEntree() {
        System.out.println("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    // Getters et setters
    public Agent getResponsableConnecte() {
        return responsableConnecte;
    }

    public void setResponsableConnecte(Agent responsableConnecte) {
        this.responsableConnecte = responsableConnecte;
    }
}