package view;

import model.Agent;
import java.util.Scanner;

public class MenuDirecteur {
    private Scanner scanner;
    private Agent directeurConnecte;
    
    public MenuDirecteur(Agent directeurConnecte) {
        this.scanner = new Scanner(System.in);
        this.directeurConnecte = directeurConnecte;
    }

    public void afficherMenu() {
        int choix = 0;
        do {
            System.out.println("\n=== MENU DIRECTEUR ===");
            System.out.println("Connecté en tant que: " + directeurConnecte.getNom() + " " + directeurConnecte.getPrenom());
            System.out.println("Statut: DIRECTEUR");
            System.out.println("--- Mes options personnelles ---");
            System.out.println("1. Accéder au menu Agent (mes informations)");
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
                    // menuAgent.afficherMenu();
                    break;
                case 2:
                    // validerAjoutBonus();
                    break;
                case 3:
                    // validerAjoutIndemnites();
                    break;
                case 4:
                    // consulterDemandesEnAttente();
                    break;
                case 5:
                    // consulterRapportGlobal();
                    break;
                case 6:
                    // genererStatistiquesDepartements();
                    break;
                case 7:
                    // genererTopAgentsMieuxPayes();
                    break;
                case 8:
                    // analyserRepartitionPaiementsParType();
                    break;
                case 9:
                        // creerDepartement();
                    break;
                case 10:
                    // modifierDepartement();
                    break;
                case 11:
                    // supprimerDepartement();
                    break;
                case 12:
                    // associerResponsableDepartement();
                    break;
                case 13:
                    // listerTousDepartements();
                    break;
                case 14:
                    // gererUtilisateurs();
                    break;
                case 15:
                    // effectuerAuditPaiements();
                    break;
                case 16:
                    // gererParametresSysteme();
                    break;
                case 0:
                    System.out.println("Merci d'avoir utilisé notre app - Déconnexion...");
                    break;
                default:
                    System.out.println("choix invalide");
            }
        } while (choix != 0);
    }
}