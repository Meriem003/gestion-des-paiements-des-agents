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
            System.out.println("\n============== MENU DIRECTEUR ==============");
            System.out.println("🎯 Directeur connecté: " + directeurConnecte.getPrenom() + " " + directeurConnecte.getNom());
            System.out.println("═════════ Gestion des paiements ════════════");
            System.out.println("2. Créer un bonus direct pour responsable");
            System.out.println("3. Consulter tous les paiements");
            System.out.println("4. Générer rapport global des paiements");
            System.out.println("5. Rapport global entreprise");
            System.out.println("6. Audit des paiements");
            System.out.println("7. Top des agents les mieux payés");
            System.out.println("8. Répartition des paiements par type");
            System.out.println("══════════ Gestion départements ═══════════");
            System.out.println("9. Créer un département");
            System.out.println("10. Modifier un département");
            System.out.println("11. Supprimer un département");
            System.out.println("12. Lister tous les départements");
            System.out.println("═══════════ Gestion des agents ════════════");
            System.out.println("13. Créer un utilisateur avec département");
            System.out.println("14. Associer un responsable à un département");
            System.out.println("15. Modifier un responsable");
            System.out.println("16. Supprimer un responsable");
            System.out.println("17. Lister tous les responsables");
            System.out.println("═══════════════ Statistiques ══════════════");
            System.out.println("18. Nombre total d'agents");
            System.out.println("19. Nombre total de départements");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
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
                        directeurController.calculerRepartitionPaiementsParType(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 8:
                        directeurController.creerDepartement(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 9:
                        directeurController.modifierDepartement(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 10:
                        directeurController.supprimerDepartement(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 11:
                        directeurController.listerTousDepartements(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 12:
                        directeurController.creerUtilisateurAvecDepartement(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 13:
                        directeurController.associerResponsableDepartement(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 14:
                        directeurController.modifierResponsable(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 15:
                        directeurController.supprimerResponsable(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 16:
                        directeurController.listerTousResponsables(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 17:
                        directeurController.obtenirNombreTotalAgents(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 18:
                        directeurController.obtenirNombreTotalDepartements(directeurConnecte.getId());
                        System.out.println("\nAppuyez sur Entrée pour continuer...");
                        scanner.nextLine();
                        break;
                    case 0:
                        System.out.println("Merci d'avoir utilisé notre application - Déconnexion...");
                        break;
                    default:
                        System.out.println("Choix invalide. Veuillez choisir un nombre entre 0 et 19.");
                }
            } catch (Exception e) {
                System.err.println("Erreur de saisie. Veuillez entrer un nombre valide.");
                scanner.nextLine(); 
        } while (choix != 0);
    }

    public Agent getDirecteurConnecte() {
        return directeurConnecte;
    }

    public void setDirecteurConnecte(Agent directeurConnecte) {
        this.directeurConnecte = directeurConnecte;
    }
}
