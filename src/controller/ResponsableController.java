package controller;

import model.Agent;
import model.Departement;
import model.Paiement;
import model.TypeAgent;
import model.TypePaiement;
import service.IResponsableService;
import service.IAgentService;
import view.MenuAgent;
import service.IPaiementService;
import service.Iimpl.PaiementServiceImpl;
import service.Iimpl.ResponsableServiceImpl;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.math.BigDecimal;

public class ResponsableController {
    private IResponsableService responsableService;
    private IPaiementService paiementService;
    private Scanner scanner;

    public ResponsableController(IResponsableService responsableService, IPaiementService paiementService) {
        this.responsableService = responsableService;
        this.paiementService = paiementService;
        this.scanner = new Scanner(System.in);
    }

    public ResponsableController(IResponsableService responsableService) {
        this.responsableService = responsableService;
        this.paiementService = new PaiementServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    public void accederMenuAgent(Agent responsable) {
        try {
            System.out.println("\n=== ACCÈS AU MENU AGENT ===");
            System.out.println("Vous accédez maintenant à vos informations personnelles...");

            MenuAgent menuAgent = new MenuAgent(responsable, (IAgentService) responsableService);
            menuAgent.afficherMenu();

        } catch (Exception e) {
            System.err.println("Erreur lors de l'accès au menu agent : " + e.getMessage());
        }
    }

    //menu responsable choix 2
    public void ajouterAgent(int responsableId) {
        try {
            System.out.println("\n=== AJOUT D'UN NOUVEL AGENT ===");

            System.out.print("Nom de l'agent : ");
            String nom = scanner.nextLine().trim();

            System.out.print("Prénom de l'agent : ");
            String prenom = scanner.nextLine().trim();

            System.out.print("Email de l'agent : ");
            String email = scanner.nextLine().trim();

            System.out.print("Mot de passe de l'agent : ");
            String motDePasse = scanner.nextLine().trim();

            System.out.println("Types d'agents disponibles :");
            System.out.println("1. OUVRIER");
            System.out.println("2. STAGIAIRE");
            System.out.print("Choisissez le type d'agent (1-2) : ");

            int choixType = scanner.nextInt();
            scanner.nextLine(); 

            TypeAgent typeAgent;
            switch (choixType) {
                case 1:
                    typeAgent = TypeAgent.OUVRIER;
                    break;
                case 2:
                    typeAgent = TypeAgent.STAGIAIRE;
                    break;
                default:
                    System.out.println("Choix invalide. Type OUVRIER sélectionné par défaut.");
                    typeAgent = TypeAgent.OUVRIER;
                    break;
            }

            Agent nouvelAgent = new Agent();
            nouvelAgent.setNom(nom);
            nouvelAgent.setPrenom(prenom);
            nouvelAgent.setEmail(email);
            nouvelAgent.setMotDePasse(motDePasse);
            nouvelAgent.setTypeAgent(typeAgent);

            System.out.println("Note: L'agent sera automatiquement affecté à votre département.");

            Agent agentAjoute = responsableService.ajouterAgent(nouvelAgent, responsableId);

            if (agentAjoute != null) {
                System.out.println("Agent ajouté avec succès !");
                System.out.println("ID : " + agentAjoute.getId());
                System.out.println("Nom complet : " + agentAjoute.getPrenom() + " " + agentAjoute.getNom());
                if (agentAjoute.getDepartement() != null) {
                    System.out.println("Département : " + agentAjoute.getDepartement().getNom());
                }
            } else {
                System.out.println("Erreur lors de l'ajout de l'agent.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de l'agent : " + e.getMessage());
            scanner.nextLine(); 
        }
    }

    //menu responsable choix 3
    public void modifierAgent(int responsableId) {
        try {
            System.out.println("\n=== MODIFICATION D'UN AGENT ===");

            System.out.print("ID de l'agent à modifier : ");
            int agentId = scanner.nextInt();
            scanner.nextLine(); 
            Agent agentExistant = responsableService.obtenirInformationsAgent(agentId);
            if (agentExistant == null) {
                System.out.println("Agent introuvable avec l'ID : " + agentId);
                return;
            }
            System.out.println("\nNouvelles informations (appuyez sur Entrée pour conserver la valeur actuelle) :");

            System.out.print("Nouveau nom : ");
            String nouveauNom = scanner.nextLine().trim();
            if (!nouveauNom.isEmpty()) {
                agentExistant.setNom(nouveauNom);
            }

            System.out.print("Nouveau prénom : ");
            String nouveauPrenom = scanner.nextLine().trim();
            if (!nouveauPrenom.isEmpty()) {
                agentExistant.setPrenom(nouveauPrenom);
            }

            System.out.print("Nouvel email : ");
            String nouvelEmail = scanner.nextLine().trim();
            if (!nouvelEmail.isEmpty()) {
                agentExistant.setEmail(nouvelEmail);
            }

            Agent agentModifie = responsableService.modifierAgent(agentExistant, responsableId);

            if (agentModifie != null) {
                System.out.println("Agent modifié avec succès !");
            } else {
                System.out.println("Erreur lors de la modification de l'agent.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la modification de l'agent : " + e.getMessage());
            scanner.nextLine();
        }
    }

    //menu responsable choix 4
    public void supprimerAgent(int responsableId) {
        try {
            System.out.println("\n=== SUPPRESSION D'UN AGENT ===");

            System.out.print("ID de l'agent à supprimer : ");
            int agentId = scanner.nextInt();
            scanner.nextLine();
            Agent agent = responsableService.obtenirInformationsAgent(agentId);
            if (agent == null) {
                System.out.println("Agent introuvable avec l'ID : " + agentId);
                return;
            }

            System.out.println("Agent à supprimer :");
            System.out.println("Nom complet : " + agent.getPrenom() + " " + agent.getNom());
            System.out.println("Email : " + agent.getEmail());
            System.out.println("Type : " + agent.getTypeAgent());

            System.out.print("Êtes-vous sûr de vouloir supprimer cet agent ? (oui/non) : ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("oui") || confirmation.equals("o")) {
                boolean supprime = responsableService.supprimerAgent(agentId, responsableId);

                if (supprime) {
                    System.out.println("Agent supprimé avec succès !");
                } else {
                    System.out.println("Erreur lors de la suppression de l'agent.");
                }
            } else {
                System.out.println("Suppression annulée.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de l'agent : " + e.getMessage());
            scanner.nextLine(); 
        }
    }

    //menu responsable choix 6
    public void ajouterSalaire(int responsableId) {
        try {
            System.out.println("\n=== AJOUT D'UN SALAIRE ===");

            System.out.print("ID de l'agent : ");
            int agentId = scanner.nextInt();

            System.out.print("Montant du salaire : ");
            double montant = scanner.nextDouble();
            scanner.nextLine(); 

            if (((service.Iimpl.ResponsableServiceImpl) responsableService)
                    .traiterPaiementAvecControles(agentId, TypePaiement.SALAIRE, montant, responsableId) != null) {
                System.out.println("Salaire ajouté avec succès !");
                System.out.println("Montant : " + montant + " DH");
            } else {
                System.out.println("Erreur lors de l'ajout du salaire.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout du salaire : " + e.getMessage());
            scanner.nextLine();
        }
    }

    //menu responsable choix 7
    public void ajouterPrime(int responsableId) {
        try {
            System.out.println("\n=== AJOUT D'UNE PRIME ===");

            System.out.print("ID de l'agent : ");
            int agentId = scanner.nextInt();

            System.out.print("Montant de la prime : ");
            double montant = scanner.nextDouble();
            scanner.nextLine(); 

            if (((service.Iimpl.ResponsableServiceImpl) responsableService)
                    .traiterPaiementAvecControles(agentId, TypePaiement.PRIME, montant, responsableId) != null) {
                System.out.println("Prime ajoutée avec succès !");
                System.out.println("Montant : " + montant + " DH");
            } else {
                System.out.println("Erreur lors de l'ajout de la prime.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de la prime : " + e.getMessage());
            scanner.nextLine();
        }
    }
    
    //menu responsable choix 8
    public void consulterPaiementsDepartement(int responsableId) {
        try {
            Agent responsable = responsableService.obtenirInformationsAgent(responsableId);
            if (responsable == null || responsable.getDepartement() == null) {
                System.err.println("Responsable introuvable ou pas de département assigné");
                return;
            }
            
            int departementId = responsable.getDepartement().getId();
            List<Paiement> paiements = responsableService.consulterTousPaiementsDepartement(departementId, responsableId);
            
            if (paiements.isEmpty()) {
                System.out.println("Aucun paiement trouvé pour ce département.");
                return;
            }
            
            System.out.println("\n=== PAIEMENTS DU DÉPARTEMENT ===");
            System.out.printf("%-5s %-20s %-15s %-12s %-15s %-8s%n",
                            "ID", "Agent", "Type", "Montant", "Date", "Validé");
            System.out.println("=".repeat(80));
            
            for (Paiement p : paiements) {
                String agentNom = p.getAgent().getPrenom() + " " + p.getAgent().getNom();
                System.out.printf("%-5d %-20s %-15s %-12.2f %-15s %-8s%n",
                    p.getId(),
                    agentNom.length() > 18 ? agentNom.substring(0, 18) + ".." : agentNom,
                    p.getTypePaiement(),
                    p.getMontant(),
                    p.getDatePaiement(),
                    p.isConditionValidee() ? "✅" : "⏳"
                );
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation : " + e.getMessage());
        }
    }

    //menu responsable choix 10
    public void calculerStatistiquesDepartement(int responsableId) {
        try {
            Agent responsable = responsableService.obtenirInformationsAgent(responsableId);
            int departementId = responsable.getDepartement().getId();
            Map<String, Object> statistiques = responsableService.calculerStatistiquesDepartement(departementId, responsableId);
            
            System.out.println("\n=== STATISTIQUES DU DÉPARTEMENT ===");
            for (Map.Entry<String, Object> entry : statistiques.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul des statistiques : " + e.getMessage());
        }
    }
    
    //menu responsable choix 11
    public void classementAgentsParPaiements(int responsableId) {
        try {
            Agent responsable = responsableService.obtenirInformationsAgent(responsableId);
            if (responsable == null || responsable.getDepartement() == null) {
                System.out.println("Responsable introuvable ou pas de département assigné");
                return;
            }
            int departementId = responsable.getDepartement().getId();
            List<Agent> classement = responsableService.classementAgentsParPaiement(departementId, responsableId);
            if (classement.isEmpty()) {
                System.out.println("Aucun agent trouvé pour le classement.");
                return;
            }
            System.out.println("\n=== CLASSEMENT DES AGENTS PAR MONTANT TOTAL ===");
            System.out.printf("%-4s %-25s %-15s%n", "#", "Agent", "Total(DH)");
            System.out.println("-".repeat(50));
            for (int i = 0; i < classement.size(); i++) {
                Agent agent = classement.get(i);
                List<Paiement> paiementsAgent = paiementService.obtenirPaiementsParAgent(agent.getId());
                java.math.BigDecimal montant = paiementsAgent.stream()
                        .map(Paiement::getMontant)
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                System.out.printf("%-4d %-25s %-15s%n", i + 1, agent.getPrenom() + " " + agent.getNom(), montant + " DH");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du classement : " + e.getMessage());
        }
    }

    //menu responsable choix 5
    public void listerAgentsMonDepartement(int responsableId) {
        try {
            System.out.println("\n=== LISTE DES AGENTS DE MON DÉPARTEMENT ===");
            
            List<Agent> agents = responsableService.listerAgentsMonDepartement(responsableId);
            if (agents.isEmpty()) {
                System.out.println("Aucun agent trouvé dans votre département.");
                return;
            }
            
            System.out.println("Total des agents : " + agents.size());
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.printf("%-5s %-25s %-20s %-15s%n", "ID", "Nom Complet", "Email", "Type");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            for (Agent agent : agents) {
                System.out.printf("%-5d %-25s %-20s %-15s%n",
                    agent.getId(),
                    agent.getPrenom() + " " + agent.getNom(),
                    agent.getEmail(),
                    agent.getTypeAgent()
                );
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des agents : " + e.getMessage());
        }
    }

}