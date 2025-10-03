package view;

import model.Agent;
import model.TypeAgent;
import service.IAgentService;
import service.IDirecteurService;
import service.Iimpl.AgentServiceImpl;
import service.Iimpl.DirecteurServiceImpl;
import dao.Iimpl.AgentDao;
import dao.Iimpl.DepartementDao;
import dao.Iimpl.PaiementDao;

import java.util.Scanner;

public class MenuPrinciple {
    private Scanner scanner;
    private IAgentService agentService;
    private IDirecteurService directeurService;

    public MenuPrinciple() {
        this.scanner = new Scanner(System.in);
        
        // Initialisation des DAO
        AgentDao agentDao = new AgentDao();
        DepartementDao departementDao = new DepartementDao();
        PaiementDao paiementDao = new PaiementDao();
        
        // Initialisation des services (seulement pour l'administration)
        this.agentService = new AgentServiceImpl(agentDao, paiementDao, departementDao);
        this.directeurService = new DirecteurServiceImpl(agentDao, paiementDao, departementDao);
    }

    public void afficherMenuPrincipal() {
        int choix = 0;
        do {
            System.out.println("\n============= SYSTÈME DE GESTION DES PAIEMENTS =============");
            System.out.println("🏢 INTERFACE D'ADMINISTRATION");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("1. Connexion Directeur (Administration)");
            System.out.println("0. Quitter");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.print("Votre choix : ");
            
            try {
                choix = scanner.nextInt();
                scanner.nextLine(); // Consommer le retour à la ligne
                
                switch (choix) {
                    case 1:
                        connexionDirecteur();
                        break;
                    case 0:
                        System.out.println("👋 Au revoir !");
                        break;
                    default:
                        System.out.println("Choix invalide. Veuillez choisir 1 ou 0.");
                }
            } catch (Exception e) {
                System.out.println("Erreur de saisie. Veuillez entrer un nombre.");
                scanner.nextLine(); // Nettoyer le buffer
            }
        } while (choix != 0);
    }

    private void connexionDirecteur() {
        System.out.println("\n========== CONNEXION ADMINISTRATION ===========");
        System.out.println("Accès réservé aux directeurs uniquement");
        System.out.println("──────────────────────────────────────────────");
        System.out.print("Email administrateur : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();
        
        try {
            Agent agent = agentService.authentifier(email, motDePasse);
            if (agent == null) {
                System.out.println("Échec de l'authentification! Vérifiez vos identifiants.");
                System.out.println("Accès refusé - Identifiants incorrects");
                return;
            }
            
            if (!checkType(agent.getTypeAgent(), TypeAgent.DIRECTEUR)) {
                System.out.println("ACCÈS REFUSÉ!");
                System.out.println("Seuls les directeurs peuvent accéder à l'administration.");
                System.out.println("Votre type de compte: " + agent.getTypeAgent());
                return;
            }
            
            System.out.println("AUTHENTIFICATION RÉUSSIE!");
            System.out.println("Directeur connecté: " + agent.getPrenom() + " " + agent.getNom());
            System.out.println("Département: " + (agent.getDepartement() != null ? agent.getDepartement().getNom() : "Non assigné"));
            System.out.println("──────────────────────────────────────────────");
            System.out.println("Accès au panneau d'administration accordé");
            
            MenuDirecteur menuDirecteur = new MenuDirecteur(agent, directeurService);
            menuDirecteur.afficherMenu();
            
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion: " + e.getMessage());
            System.out.println("Veuillez contacter l'administrateur système si le problème persiste.");
        }
    }

    private boolean checkType(TypeAgent agentType, TypeAgent expectedType) {
        return agentType == expectedType;
    }

    public static void main(String[] args) {
        MenuPrinciple menuPrinciple = new MenuPrinciple();
        menuPrinciple.afficherMenuPrincipal();
    }
}
