package controller;

import model.Agent;
import model.TypeAgent;
import service.Iimpl.LoginService;
import service.Iimpl.AgentServiceImpl;
import service.Iimpl.ResponsableServiceImpl;
import service.Iimpl.DirecteurServiceImpl;
import dao.Iimpl.AgentDao;
import dao.Iimpl.PaiementDao;
import dao.Iimpl.DepartementDao;
import view.MenuAgent;
import view.MenuResponsable;
import view.MenuDirecteur;
import java.util.Scanner;


public class AuthController {
    private LoginService loginService;
    private Scanner scanner;
    private AgentDao agentDao;
    private PaiementDao paiementDao;
    private DepartementDao departementDao;
    
    public AuthController() {
        this.loginService = new LoginService();
        this.scanner = new Scanner(System.in);
        
        this.agentDao = new AgentDao();
        this.paiementDao = new PaiementDao();
        this.departementDao = new DepartementDao();
    }
    
    public void afficherEcranLogin() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║               SYSTÈME DE GESTION DES PAIEMENTS           ║");
        System.out.println("║                     AUTHENTIFICATION                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        int tentatives = 0;
        final int MAX_TENTATIVES = 3;
        
        while (tentatives < MAX_TENTATIVES) {
            try {
                System.out.println("Veuillez vous connecter :");
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                System.out.print("Email : ");
                String email = scanner.nextLine().trim();
                if (!loginService.validerFormatEmail(email)) {
                    System.out.println("Format d'email invalide. Veuillez réessayer.");
                    tentatives++;
                    continue;
                }
                System.out.print("Mot de passe : ");
                String motDePasse = scanner.nextLine().trim();
                if (!loginService.validerMotDePasse(motDePasse)) {
                    System.out.println("Mot de passe invalide (minimum 4 caractères).");
                    tentatives++;
                    continue;
                }
                Agent agentAuthentifie = loginService.authentifier(email, motDePasse);
                if (agentAuthentifie != null) {
                    afficherSuccesAuthentification(agentAuthentifie);
                    redirigerSelonTypeAgent(agentAuthentifie);
                    return;
                } else {
                    tentatives++;
                    System.out.println("Email ou mot de passe incorrect.");
                    if (tentatives < MAX_TENTATIVES) {
                        System.out.println("entative " + tentatives + "/" + MAX_TENTATIVES +
                                         ". Il vous reste " + (MAX_TENTATIVES - tentatives) + " tentative(s).");
                    }
                }
                
            } catch (Exception e) {
                System.err.println("Erreur lors de la connexion : " + e.getMessage());
                tentatives++;
            }
        }
        System.out.println("\nACCÈS BLOQUÉ");
        System.out.println("Vous avez dépassé le nombre maximum de tentatives de connexion.");
        System.out.println("Veuillez contacter l'administrateur système.");
        System.exit(1);
    }
    private void afficherSuccesAuthentification(Agent agent) {
        System.out.println("AUTHENTIFICATION RÉUSSIE !");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("Bienvenue, " + agent.getPrenom() + " " + agent.getNom());
        System.out.println("Email : " + agent.getEmail());
        System.out.println("Type de compte : " + agent.getTypeAgent());
        
        if (agent.getDepartement() != null) {
            System.out.println("Département : " + agent.getDepartement().getNom());
        }
        
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    

    private void redirigerSelonTypeAgent(Agent agent) {
        switch (agent.getTypeAgent()) {
            case OUVRIER:
            case STAGIAIRE:
                accederMenuAgent(agent);
                break;
                
            case RESPONSABLE_DEPARTEMENT:
                accederMenuResponsable(agent);
                break;
                
            case DIRECTEUR:
                accederMenuDirecteur(agent);
                break;
                
            default:
                System.err.println("Type d'agent non reconnu : " + agent.getTypeAgent());
                System.out.println("Contactez l'administrateur système.");
        }
    }
    
    private void accederMenuAgent(Agent agent) {
        try {
            AgentServiceImpl agentService = new AgentServiceImpl(agentDao, paiementDao, departementDao);
            MenuAgent menuAgent = new MenuAgent(agent, agentService);
            menuAgent.afficherMenu();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'accès au menu agent : " + e.getMessage());
        }
    }

    private void accederMenuResponsable(Agent agent) {
        try {
            ResponsableServiceImpl responsableService = new ResponsableServiceImpl(agentDao, paiementDao, departementDao);
            MenuResponsable menuResponsable = new MenuResponsable(agent, responsableService);
            menuResponsable.afficherMenu();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'accès au menu responsable : " + e.getMessage());
        }
    }
    
    private void accederMenuDirecteur(Agent agent) {
        try {
            DirecteurServiceImpl directeurService = new DirecteurServiceImpl(agentDao, paiementDao, departementDao);
            MenuDirecteur menuDirecteur = new MenuDirecteur(agent, directeurService);
            menuDirecteur.afficherMenu();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'accès au menu directeur : " + e.getMessage());
        }
    }
}