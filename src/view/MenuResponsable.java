package view;
import model.Agent;

import java.util.Scanner;

public class MenuResponsable {
    private Scanner scanner;
    private Agent responsableConnecte;
    
    public MenuResponsable(Agent responsableConnecte) {
        this.scanner = new Scanner(System.in);
        this.responsableConnecte = responsableConnecte;
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
                    // menuAgent.afficherMenu();
                    break;
                case 2:
                    // ajouterAgent();
                    break;
                case 3:
                    // modifierAgent();
                    break;
                case 4:
                    // supprimerAgent();
                    break;
                case 5:
                    // affecterAgentDepartement();
                    break;
                case 6:
                    // ajouterPaiementAgent();
                    break;
                case 7:
                    // consulterPaiementsAgent();
                    break;
                case 8:
                    // consulterPaiementsDepartement();
                    break;
                case 9:
                    // filtrerTrierPaiementsDepartement();
                    break;
                case 10:
                    // calculerStatistiquesDepartement();
                    break;
                case 11:
                    // classementAgents();
                    break;
                case 12:
                    // identifierPaiementsInhabituels();
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