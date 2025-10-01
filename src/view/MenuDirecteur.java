package view;

import model.Agent;
import controller.DirecteurController;
import service.IDirecteurService;
import java.util.Scanner;

public class MenuDirecteur {
    private Scanner scanner;
    private Agent directeurConnecte;
    private DirecteurController directeurController;
    
    public MenuDirecteur(Agent directeurConnecte, IDirecteurService directeurService) {
        this.scanner = new Scanner(System.in);
        this.directeurConnecte = directeurConnecte;
        this.directeurController = new DirecteurController(directeurService);
    }

    public void afficherMenu() {
        int choix = 0;
        do {
            System.out.println("=============== MENU DIRECTEUR ===============");
            System.out.println("Connecté en tant que: " + directeurConnecte.getNom() + " " + directeurConnecte.getPrenom());
            System.out.println("Statut: DIRECTEUR");
            System.out.println("--- Mes options personnelles ---");
            System.out.println("1. Accéder a mes informations ");
            System.out.println("--- Validation des paiements ---");
            System.out.println("2. Valider l'ajout de bonus");
            System.out.println("3. Valider l'ajout d'indemnités");
            System.out.println("4. Consulter les demandes en attente");
            System.out.println("--- Rapports globaux ---");
            System.out.println("5. Consulter le rapport global de l'entreprise");
            System.out.println("6. Statistiques par département");
            System.out.println("7. Top des agents les mieux payés");
            System.out.println("8. Répartition des paiements par type");
            System.out.println("--- Gestion des départements ---");
            System.out.println("9. Créer un département");
            System.out.println("10. Modifier un département");
            System.out.println("11. Supprimer un département");
            System.out.println("12. Associer un responsable à un département");
            System.out.println("13. Lister tous les départements");
            System.out.println("--- Administration ---");
            System.out.println("14. Gestion des utilisateurs (agents/responsables)");
            System.out.println("15. Audit des paiements");
            System.out.println("16. Paramètres système");
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
                    directeurController.validerAjoutBonus(directeurConnecte.getId());
                    attendreEntree();
                    break;
                case 3:
                    directeurController.validerAjoutIndemnites(directeurConnecte.getId());
                    attendreEntree();
                    break;
                case 4:
                    directeurController.consulterDemandesEnAttente(directeurConnecte.getId());
                    attendreEntree();
                    break;
                case 5:
                    System.out.println("⚠️ Fonctionnalité en développement : Rapport global");
                    attendreEntree();
                    break;
                case 6:
                    System.out.println("⚠️ Fonctionnalité en développement : Statistiques par département");
                    attendreEntree();
                    break;
                case 7:
                    System.out.println("⚠️ Fonctionnalité en développement : Top des agents les mieux payés");
                    attendreEntree();
                    break;
                case 8:
                    System.out.println("⚠️ Fonctionnalité en développement : Répartition des paiements par type");
                    attendreEntree();
                    break;
                case 9:
                    directeurController.creerDepartement(directeurConnecte.getId());
                    attendreEntree();
                    break;
                case 10:
                    directeurController.modifierDepartement(directeurConnecte.getId());
                    attendreEntree();
                    break;
                case 11:
                    directeurController.supprimerDepartement(directeurConnecte.getId());
                    attendreEntree();
                    break;
                case 12:
                    directeurController.associerResponsableDepartement(directeurConnecte.getId());
                    attendreEntree();
                    break;
                case 13:
                    directeurController.listerTousDepartements(directeurConnecte.getId());
                    attendreEntree();
                    break;
                case 14:
                    directeurController.gererUtilisateurs(directeurConnecte.getId());
                    attendreEntree();
                    break;
                case 15:
                    System.out.println("⚠️ Fonctionnalité en développement : Audit des paiements");
                    attendreEntree();
                    break;
                case 16:
                    System.out.println("⚠️ Fonctionnalité en développement : Paramètres système");
                    attendreEntree();
                    break;
                case 0:
                    System.out.println("Merci d'avoir utilisé notre application - Déconnexion...");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez choisir un nombre entre 0 et 16.");
            }
        } while (choix != 0);
    }

    private void attendreEntree() {
        System.out.println("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    // Getters et setters
    public Agent getDirecteurConnecte() {
        return directeurConnecte;
    }

    public void setDirecteurConnecte(Agent directeurConnecte) {
        this.directeurConnecte = directeurConnecte;
    }
}