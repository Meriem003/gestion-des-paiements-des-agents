package view;

import model.Agent;
import model.Departement;
import model.Paiement;
import model.TypeAgent;
import model.TypePaiement;
import java.util.Scanner;

/**
 * Menu console pour le directeur
 * Hérite des fonctionnalités Agent avec des options spécifiques de direction
 */
public class MenuDirecteur {
    private Scanner scanner;
    private Agent directeurConnecte;
    private MenuAgent menuAgent;
    
    public MenuDirecteur(Agent directeurConnecte) {
        this.scanner = new Scanner(System.in);
        this.directeurConnecte = directeurConnecte;
        this.menuAgent = new MenuAgent(directeurConnecte);
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
                    menuAgent.afficherMenu();
                    break;
                case 2:
                    validerAjoutBonus();
                    break;
                case 3:
                    validerAjoutIndemnites();
                    break;
                case 4:
                    consulterDemandesEnAttente();
                    break;
                case 5:
                    consulterRapportGlobal();
                    break;
                case 6:
                    statistiquesParDepartement();
                    break;
                case 7:
                    topAgentsMieuxPayes();
                    break;
                case 8:
                    repartitionPaiementsParType();
                    break;
                case 9:
                    creerDepartement();
                    break;
                case 10:
                    modifierDepartement();
                    break;
                case 11:
                    supprimerDepartement();
                    break;
                case 12:
                    associerResponsableDepartement();
                    break;
                case 13:
                    listerDepartements();
                    break;
                case 14:
                    gestionUtilisateurs();
                    break;
                case 15:
                    auditPaiements();
                    break;
                case 16:
                    parametresSysteme();
                    break;
                case 0:
                    System.out.println("Merci d'avoir utilisé notre app - Déconnexion...");
                    break;
                default:
                    System.out.println("choix invalide");
            }
        } while (choix != 0);
    }

    private void validerAjoutBonus() {
        System.out.println("\n=== VALIDATION DE BONUS ===");
        System.out.println("Liste des demandes de bonus en attente:");        
        System.out.print("ID de la demande à valider (0 pour annuler): ");
        int idDemande = scanner.nextInt();
        
        if (idDemande != 0) {
            System.out.println("1. Approuver");
            System.out.println("2. Rejeter");
            System.out.print("Votre décision: ");
            int decision = scanner.nextInt();
            
            if (decision == 1) {
                System.out.println("Bonus approuvé et traité.");
            } else if (decision == 2) {
                System.out.print("Motif du rejet: ");
                scanner.nextLine();
                String motif = scanner.nextLine();
                System.out.println("Bonus rejeté. Motif: " + motif);
            }
        }
        attendreEntree();
    }

    private void validerAjoutIndemnites() {
        System.out.println("\n=== VALIDATION D'INDEMNITÉS ===");
        System.out.println("Liste des demandes d'indemnités en attente:");
        System.out.print("ID de la demande à valider (0 pour annuler): ");
        int idDemande = scanner.nextInt();
        
        if (idDemande != 0) {
            System.out.println("1. Approuver");
            System.out.println("2. Rejeter");
            System.out.print("Votre décision: ");
            int decision = scanner.nextInt();            
            if (decision == 1) {
                System.out.println("Indemnité approuvée et traitée.");
            } else if (decision == 2) {
                System.out.print("Motif du rejet: ");
                scanner.nextLine();
                String motif = scanner.nextLine();
                System.out.println("Indemnité rejetée. Motif: " + motif);
            }
        }
        attendreEntree();
    }
    
    private void consulterDemandesEnAttente() {
        System.out.println("\n=== DEMANDES EN ATTENTE ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }

    private void consulterRapportGlobal() {
        System.out.println("\n=== RAPPORT GLOBAL DE L'ENTREPRISE ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void statistiquesParDepartement() {
        System.out.println("\n=== STATISTIQUES PAR DÉPARTEMENT ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void topAgentsMieuxPayes() {
        System.out.println("\n=== TOP DES AGENTS LES MIEUX PAYÉS ===");
        System.out.print("Nombre d'agents à afficher (par défaut 10): ");
        int nombre = scanner.nextInt();
        if (nombre <= 0) nombre = 10;        
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void repartitionPaiementsParType() {
        System.out.println("\n=== RÉPARTITION DES PAIEMENTS PAR TYPE ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void creerDepartement() {
        System.out.println("\n=== CRÉER UN DÉPARTEMENT ===");
        System.out.print("Nom du département: ");
        scanner.nextLine();
        String nomDepartement = scanner.nextLine();        
        System.out.println("Département '" + nomDepartement + "' créé avec succès.");
        attendreEntree();
    }
    
    private void modifierDepartement() {
        System.out.println("\n=== MODIFIER UN DÉPARTEMENT ===");
        System.out.print("ID du département à modifier: ");
        int idDepartement = scanner.nextInt();
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void supprimerDepartement() {
        System.out.println("\n=== SUPPRIMER UN DÉPARTEMENT ===");
        System.out.print("ID du département à supprimer: ");
        int idDepartement = scanner.nextInt();
        
        System.out.print("Confirmez-vous la suppression ? (O/N): ");
        scanner.nextLine();
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("O") || confirmation.equalsIgnoreCase("OUI")) {
            System.out.println("Département supprimé avec succès.");
        } else {
            System.out.println("Suppression annulée.");
        }
        attendreEntree();
    }
    
    private void associerResponsableDepartement() {
        System.out.println("\n=== ASSOCIER UN RESPONSABLE À UN DÉPARTEMENT ===");
        System.out.print("ID du département: ");
        int idDepartement = scanner.nextInt();
        System.out.print("ID du responsable: ");
        int idResponsable = scanner.nextInt();        
        System.out.println("Responsable associé avec succès au département.");
        attendreEntree();
    }
    
    private void listerDepartements() {
        System.out.println("\n=== LISTE DES DÉPARTEMENTS ===");
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void gestionUtilisateurs() {
        System.out.println("\n=== GESTION DES UTILISATEURS ===");
        System.out.println("1. Créer un nouvel agent/responsable");
        System.out.println("2. Modifier les droits d'un utilisateur");
        System.out.println("3. Désactiver/Activer un utilisateur");
        System.out.println("4. Réinitialiser le mot de passe");
        System.out.print("Votre choix: ");
        
        int choix = scanner.nextInt();
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void auditPaiements() {
        System.out.println("\n=== AUDIT DES PAIEMENTS ===");
        System.out.println("1. Paiements suspects ou anormaux");
        System.out.println("2. Historique des modifications");
        System.out.println("3. Paiements par période");
        System.out.println("4. Rapport d'audit complet");
        System.out.print("Votre choix: ");
        
        int choix = scanner.nextInt();
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void parametresSysteme() {
        System.out.println("\n=== PARAMÈTRES SYSTÈME ===");
        System.out.println("1. Seuils de validation des paiements");
        System.out.println("2. Configuration des notifications");
        System.out.println("3. Sauvegarde/Restauration");
        System.out.println("4. Paramètres de sécurité");
        System.out.print("Votre choix: ");
        
        int choix = scanner.nextInt();
        System.out.println("Fonctionnalité en cours de développement...");
        attendreEntree();
    }
    
    private void attendreEntree() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
        scanner.nextLine();
    }
}