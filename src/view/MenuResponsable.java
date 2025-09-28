package view;

import model.Agent;
import model.Departement;
import model.Paiement;
import model.TypePaiement;
import java.util.Scanner;

public class MenuResponsable {
    private Scanner scanner;
    private Agent responsableConnecte;
    private MenuAgent menuAgent;
    
    public MenuResponsable(Agent responsableConnecte) {
        this.scanner = new Scanner(System.in);
        this.responsableConnecte = responsableConnecte;
        this.menuAgent = new MenuAgent(responsableConnecte);
    }

    public void afficherMenu() {
        int choix = 0;
        do {
            System.out.println("\n=== MENU RESPONSABLE DE DÉPARTEMENT ===");
            System.out.println("Connecté en tant que: " + responsableConnecte.getNom() + " " + responsableConnecte.getPrenom());
            System.out.println("Département: " + (responsableConnecte.getDepartement() != null ? responsableConnecte.getDepartement().getNom() : "Non assigné"));
            System.out.println("--- Mes options personnelles ---");
            System.out.println("1. Accéder au menu Agent (mes informations)");
            System.out.println("--- Gestion des agents ---");
            System.out.println("2. Ajouter un agent");
            System.out.println("3. Modifier un agent");
            System.out.println("4. Supprimer un agent");
            System.out.println("5. Affecter un agent à un département");
            System.out.println("--- Gestion des paiements ---");
            System.out.println("6. Ajouter un paiement à un agent");
            System.out.println("7. Consulter les paiements d'un agent");
            System.out.println("8. Consulter les paiements de tout le département");
            System.out.println("9. Filtrer et trier les paiements du département");
            System.out.println("--- Statistiques département ---");
            System.out.println("10. Calculer les statistiques du département");
            System.out.println("11. Classement des agents (du plus payé au moins payé)");
            System.out.println("12. Identifier les paiements inhabituels");
            System.out.println("===========================");
            System.out.println("0. Déconnexion");
            System.out.print("Choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();
            
            switch (choix) {
                case 1:
                    menuAgent.afficherMenu();
                    break;
                case 2:
                    ajouterAgent();
                    break;
                case 3:
                    modifierAgent();
                    break;
                case 4:
                    supprimerAgent();
                    break;
                case 5:
                    affecterAgentDepartement();
                    break;
                case 6:
                    ajouterPaiementAgent();
                    break;
                case 7:
                    consulterPaiementsAgent();
                    break;
                case 8:
                    consulterPaiementsDepartement();
                    break;
                case 9:
                    filtrerTrierPaiementsDepartement();
                    break;
                case 10:
                    calculerStatistiquesDepartement();
                    break;
                case 11:
                    classementAgents();
                    break;
                case 12:
                    identifierPaiementsInhabituels();
                    break;
                case 0:
                    System.out.println("Merci d'avoir utilisé notre app - Déconnexion...");
                    break;
                default:
                    System.out.println("choix invalide");
            }
        } while (choix != 0);
    }

    private void ajouterAgent() {
        System.out.println("\n=== AJOUTER UN AGENT ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void modifierAgent() {
        System.out.println("\n=== MODIFIER UN AGENT ===");
        System.out.print("ID de l'agent à modifier: ");
        int idAgent = scanner.nextInt();
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void supprimerAgent() {
        System.out.println("\n=== SUPPRIMER UN AGENT ===");
        System.out.print("ID de l'agent à supprimer: ");
        int idAgent = scanner.nextInt();
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }

    private void affecterAgentDepartement() {
        System.out.println("\n=== AFFECTER UN AGENT À UN DÉPARTEMENT ===");
        System.out.print("ID de l'agent: ");
        int idAgent = scanner.nextInt();
        System.out.print("ID du département: ");
        int idDepartement = scanner.nextInt();
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void ajouterPaiementAgent() {
        System.out.println("\n=== AJOUTER UN PAIEMENT À UN AGENT ===");
        System.out.print("ID de l'agent: ");
        int idAgent = scanner.nextInt();
        
        System.out.println("\nType de paiement:");
        System.out.println("1. Salaire (pour tout agent)");
        System.out.println("2. Prime (pour tout agent)");
        System.out.println("3. Bonus (si agent éligible)");
        System.out.println("4. Indemnité (si agent éligible)");
        System.out.print("Votre choix: ");
        int typePaiement = scanner.nextInt();
        
        System.out.print("Montant: ");
        double montant = scanner.nextDouble();
        
        switch (typePaiement) {
            case 1:
                ajouterSalaire(idAgent, montant);
                break;
            case 2:
                ajouterPrime(idAgent, montant);
                break;
            case 3:
                ajouterBonus(idAgent, montant);
                break;
            case 4:
                ajouterIndemnite(idAgent, montant);
                break;
            default:
                System.out.println("Type de paiement invalide");
        }
        attendreEntree();
    }
    

    private void ajouterSalaire(int idAgent, double montant) {
        System.out.println("Ajout de salaire pour l'agent " + idAgent + ": " + montant + "€");
    }
    
    private void ajouterPrime(int idAgent, double montant) {
        System.out.println("Ajout de prime pour l'agent " + idAgent + ": " + montant + "€");
    }

    private void ajouterBonus(int idAgent, double montant) {
        System.out.println("Ajout de bonus pour l'agent " + idAgent + ": " + montant + "€");
    }

    private void ajouterIndemnite(int idAgent, double montant) {
        System.out.println("Ajout d'indemnité pour l'agent " + idAgent + ": " + montant + "€");
    }

    private void consulterPaiementsAgent() {
        System.out.println("\n=== PAIEMENTS D'UN AGENT ===");
        System.out.print("ID de l'agent: ");
        int idAgent = scanner.nextInt();
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }

    private void consulterPaiementsDepartement() {
        System.out.println("\n=== PAIEMENTS DU DÉPARTEMENT ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    private void filtrerTrierPaiementsDepartement() {
        System.out.println("\n=== FILTRER ET TRIER LES PAIEMENTS DU DÉPARTEMENT ===");
        System.out.println("1. Filtrer par type de paiement");
        System.out.println("2. Filtrer par agent");
        System.out.println("3. Filtrer par période");
        System.out.println("4. Trier par montant");
        System.out.println("5. Trier par date");
        System.out.print("Votre choix: ");
        
        int choixFiltre = scanner.nextInt();
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void calculerStatistiquesDepartement() {
        System.out.println("\n=== STATISTIQUES DU DÉPARTEMENT ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }

    private void classementAgents() {
        System.out.println("\n=== CLASSEMENT DES AGENTS (DU PLUS PAYÉ AU MOINS PAYÉ) ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }

    private void identifierPaiementsInhabituels() {
        System.out.println("\n=== PAIEMENTS INHABITUELS ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }

    private void attendreEntree() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
        scanner.nextLine();
    }
}