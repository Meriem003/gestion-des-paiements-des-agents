package view;

import model.Agent;
import java.util.Scanner;

public class MenuAgent {
    private Scanner scanner;
    private Agent agentConnecte;
    
    public MenuAgent(Agent agentConnecte) {
        this.scanner = new Scanner(System.in);
        this.agentConnecte = agentConnecte;
    }
    public void afficherMenu() {
        int choix = 0;
        do {
            System.out.println("\n=== MENU AGENT ===");
            System.out.println("Connecté en tant que: " + agentConnecte.getNom() + " " + agentConnecte.getPrenom());
            System.out.println("Type: " + agentConnecte.getTypeAgent());
            System.out.println("--- Mes informations ---");
            System.out.println("1. Consulter mes informations personnelles");
            System.out.println("2. Consulter mon département");
            System.out.println("--- Mes paiements ---");
            System.out.println("3. Consulter l'historique de mes paiements");
            System.out.println("4. Filtrer et trier mes paiements");
            System.out.println("5. Calculer le total de mes paiements");
            System.out.println("--- Mes statistiques ---");
            System.out.println("6. Consulter mon salaire annuel total");
            System.out.println("7. Consulter le nombre de primes/bonus/indemnités reçus");
            System.out.println("8. Voir mon paiement le plus élevé et le plus faible");
            System.out.println("9. Afficher mes statistiques complètes");
            System.out.println("===========================");
            System.out.println("0. Déconnexion");
            System.out.print("Choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();
            
            switch (choix) {
                case 1:
                    consulterInformationsPersonnelles();
                    break;
                case 2:
                    consulterDepartement();
                    break;
                case 3:
                    consulterHistoriquePaiements();
                    break;
                case 4:
                    filtrerEtTrierPaiements();
                    break;
                case 5:
                    calculerTotalPaiements();
                    break;
                case 6:
                    consulterSalaireAnnuel();
                    break;
                case 7:
                    consulterNombrePrimesBonus();
                    break;
                case 8:
                    consulterPaiementExtremes();
                    break;
                case 9:
                    afficherStatistiquesCompletes();
                    break;
                case 0:
                    System.out.println("Merci d'avoir utilisé notre app - Déconnexion...");
                    break;
                default:
                    System.out.println("choix invalide");
            }
        } while (choix != 0);
    }
    
    private void consulterInformationsPersonnelles() {
        System.out.println("\n=== MES INFORMATIONS PERSONNELLES ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void consulterDepartement() {
        System.out.println("\n=== MON DÉPARTEMENT ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void consulterHistoriquePaiements() {
        System.out.println("\n=== HISTORIQUE DE MES PAIEMENTS ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void filtrerEtTrierPaiements() {
        System.out.println("\n=== FILTRER ET TRIER MES PAIEMENTS ===");
        System.out.println("1. Filtrer par type (salaire, prime, bonus, indemnité)");
        System.out.println("2. Trier par montant (croissant/décroissant)");
        System.out.println("3. Trier par date (plus récent/plus ancien)");
        System.out.print("Votre choix: ");
        
        try {
            int choixFiltre = scanner.nextInt();
            System.out.println("Fonctionnalité en cours de développement...");
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Choix invalide.");
        }
        attendreEntree();
    }
    
    private void calculerTotalPaiements() {
        System.out.println("\n=== TOTAL DE MES PAIEMENTS ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void consulterSalaireAnnuel() {
        System.out.println("\n=== MON SALAIRE ANNUEL TOTAL ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void consulterNombrePrimesBonus() {
        System.out.println("\n=== NOMBRE DE PRIMES/BONUS/INDEMNITÉS ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void consulterPaiementExtremes() {
        System.out.println("\n=== PAIEMENTS EXTRÊMES ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void afficherStatistiquesCompletes() {
        System.out.println("\n=== MES STATISTIQUES COMPLÈTES ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void attendreEntree() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
        scanner.nextLine();
    }
}