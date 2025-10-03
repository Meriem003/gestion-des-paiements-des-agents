package view;

import model.Agent;
import controller.DirecteurController;
import controller.PaiementController;
import service.IDirecteurService;
import service.IPaiementService;
import service.Iimpl.PaiementServiceImpl;
import java.util.Scanner;

public class MenuDirecteur {
    private Scanner scanner;
    private Agent directeurConnecte;
    private DirecteurController directeurController;
    private PaiementController paiementController;

    public MenuDirecteur(Agent directeurConnecte, IDirecteurService directeurService,
            IPaiementService paiementService) {
        this.scanner = new Scanner(System.in);
        this.directeurConnecte = directeurConnecte;
        this.directeurController = new DirecteurController(directeurService);
        this.paiementController = new PaiementController(paiementService, (service.IAgentService) directeurService);
    }

    public MenuDirecteur(Agent directeurConnecte, IDirecteurService directeurService) {
        this(directeurConnecte, directeurService, new PaiementServiceImpl());
    }

    public void afficherMenu() {
        int choix = 0;
        do {
            System.out.println("=============== MENU DIRECTEUR ===============");
            System.out.println(
                    "Connecté en tant que: " + directeurConnecte.getNom() + " " + directeurConnecte.getPrenom());
            System.out.println("1. Créer un bonus pour un responsable");
            System.out.println("2. Consulter tous les paiements");
            System.out.println("3. Générer un rapport global des paiements");
            System.out.println("4. Consulter le rapport global de l'entreprise");
            System.out.println("5. Statistiques par département");
            System.out.println("6. Top des agents les mieux payés");
            System.out.println("7. Répartition des paiements par type");
            System.out.println("8. Créer un département");
            System.out.println("9. Modifier un département");
            System.out.println("10. Supprimer un département");
            System.out.println("11. Associer un responsable à un département");
            System.out.println("12. Lister tous les départements");
            System.out.println("13. Créer un responsable de département");
            System.out.println("14. Top des agents les mieux payés");
            System.out.println("15. Répartition des paiements par type");
            System.out.println("16. Audit des paiements");
            System.out.println("===========================");
            System.out.println("0. Déconnexion");
            System.out.print("Choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    directeurController.accederMenuAgent(directeurConnecte);
                    break;
                case 2:
                    paiementController.creerBonusDirectPourResponsable(directeurConnecte.getId());
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 3:
                    paiementController.consulterTousLesPaiements();
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 4:
                    paiementController.genererRapportGlobal();
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 5:
                    directeurController.genererRapportGlobalEntreprise(directeurConnecte.getId());
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 7:
                    directeurController.genererTopAgentsMieuxPayes(directeurConnecte.getId());
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 8:
                    directeurController.calculerRepartitionPaiementsParType(directeurConnecte.getId());
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 9:
                    directeurController.creerDepartement(directeurConnecte.getId());
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 10:
                    directeurController.modifierDepartement(directeurConnecte.getId());
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 11:
                    directeurController.supprimerDepartement(directeurConnecte.getId());
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 12:
                    directeurController.associerResponsableDepartement(directeurConnecte.getId());
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 13:
                    directeurController.listerTousDepartements(directeurConnecte.getId());
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 14:
                    directeurController.creerUtilisateurAvecDepartement(directeurConnecte.getId());
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 15:
                    directeurController.genererTopAgentsMieuxPayes(directeurConnecte.getId());
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 16:
                    directeurController.calculerRepartitionPaiementsParType(directeurConnecte.getId());
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    break;
                case 0:
                    System.out.println("Merci d'avoir utilisé notre application - Déconnexion...");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez choisir un nombre entre 0 et 16.");
            }
        } while (choix != 0);
    }

    public Agent getDirecteurConnecte() {
        return directeurConnecte;
    }

    public void setDirecteurConnecte(Agent directeurConnecte) {
        this.directeurConnecte = directeurConnecte;
    }
}