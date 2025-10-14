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
            System.out.println("═════════════ MENU DIRECTEUR ═══════════════");
            System.out.println("1. Créer un bonus direct pour responsable");
            System.out.println("2. Consulter tous les paiements");
            System.out.println("3. Générer rapport global des paiements");
            System.out.println("4. Rapport global entreprise");
            System.out.println("5. Audit des paiements");
            System.out.println("6. Top des agents les mieux payés");
            System.out.println("══════════ Gestion départements ═══════════");
            System.out.println("7. Créer un département");
            System.out.println("8. Modifier un département");
            System.out.println("9. Supprimer un département");
            System.out.println("10. Lister tous les départements");
            System.out.println("═══════════ Gestion des agents ════════════");
            System.out.println("11. Créer un utilisateur avec département");
            System.out.println("12. Associer un responsable à un département");
            System.out.println("13. Modifier un responsable");
            System.out.println("14. Supprimer un responsable");
            System.out.println("15. Lister tous les responsables");
            System.out.println("═══════════════════════════════════════════════");

            System.out.println("0. Déconnexion");
            System.out.print("Votre choix : ");

            try {
                choix = scanner.nextInt();
                scanner.nextLine();

                switch (choix) {
                    case 1:
                        paiementController.creerBonusDirectPourResponsable(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 2:
                        paiementController.consulterTousLesPaiements();
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 3:
                        paiementController.genererRapportGlobal();
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 4:
                        directeurController.genererRapportGlobalEntreprise(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 5:
                        paiementController.effectuerAuditPaiements();
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 6:
                        directeurController.genererTopAgentsMieuxPayes(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 7:
                        directeurController.creerDepartement(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 8:
                        directeurController.modifierDepartement(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 9:
                        directeurController.supprimerDepartement(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 10:
                        directeurController.listerTousDepartements(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 11:
                        directeurController.creerUtilisateurAvecDepartement(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 12:
                        directeurController.associerResponsableDepartement(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 13:
                        directeurController.modifierResponsable(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 14:
                        directeurController.supprimerResponsable(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 15:
                        directeurController.listerTousResponsables(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 0:
                        System.out.println("Merci d'avoir utilisé notre application - Déconnexion...");
                        break;
                    default:
                        System.out.println("Choix invalide. Veuillez choisir un nombre entre 0 et 18.");
                }
            } catch (Exception e) {
                System.err.println("Erreur de saisie. Veuillez entrer un nombre valide.");
                scanner.nextLine();
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
