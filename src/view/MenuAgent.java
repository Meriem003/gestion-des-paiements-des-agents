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
            System.out.println("2. Modifier mes informations personnelles");
            System.out.println("3. Consulter mon département");
            System.out.println("--- Mes paiements ---");
            System.out.println("4. Consulter l'historique de mes paiements");
            System.out.println("5. Filtrer et trier mes paiements");
            System.out.println("6. Calculer le total de mes paiements");
            System.out.println("--- Mes statistiques ---");
            System.out.println("7. Consulter mon salaire annuel total");
            System.out.println("8. Consulter le nombre de primes/bonus/indemnités reçus");
            System.out.println("9. Voir mon paiement le plus élevé et le plus faible");
            System.out.println("10. Afficher mes statistiques complètes");
            System.out.println("===========================");
            System.out.println("0. Déconnexion");
            System.out.print("Choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();
            
            switch (choix) {
                case 1:
                    // consulterInformationsPersonnelles();
                    break;
                case 2:
                    // modifierInformationsPersonnelles();
                    break;
                case 3:
                    // consulterDepartement();
                    break;
                case 4:
                    // consulterHistoriquePaiements();
                    break;
                case 5:
                    // filtrerEtTrierPaiements();
                    break;
                case 6:
                    // calculerTotalPaiements();
                    break;
                case 7:
                    // consulterSalaireAnnuel();
                    break;
                case 8:
                    // consulterNombrePrimesBonus();
                    break;
                case 9:
                    // consulterPaiementExtremes();
                    break;
                case 10:
                    // afficherStatistiquesCompletes();
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