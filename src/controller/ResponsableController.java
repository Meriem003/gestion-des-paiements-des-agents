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

    public void ajouterAgent(int responsableId) {
        try {
            System.out.println("\n=== AJOUT D'UN NOUVEL AGENT ===");

            System.out.print("Nom de l'agent : ");
            String nom = scanner.nextLine().trim();

            System.out.print("Prénom de l'agent : ");
            String prenom = scanner.nextLine().trim();

            System.out.print("Email de l'agent : ");
            String email = scanner.nextLine().trim();

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

            System.out.print("ID du département (optionnel, 0 pour ignorer) : ");
            int departementId = scanner.nextInt();
            scanner.nextLine(); 

            Agent nouvelAgent = new Agent();
            nouvelAgent.setNom(nom);
            nouvelAgent.setPrenom(prenom);
            nouvelAgent.setEmail(email);
            nouvelAgent.setTypeAgent(typeAgent);

            if (departementId > 0) {
                Departement dept = new Departement();
                dept.setId(departementId);
                nouvelAgent.setDepartement(dept);
            }

            Agent agentAjoute = responsableService.ajouterAgent(nouvelAgent, responsableId);

            if (agentAjoute != null) {
                System.out.println("Agent ajouté avec succès !");
                System.out.println("ID : " + agentAjoute.getId());
                System.out.println("Nom complet : " + agentAjoute.getPrenom() + " " + agentAjoute.getNom());
            } else {
                System.out.println("Erreur lors de l'ajout de l'agent.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de l'agent : " + e.getMessage());
            scanner.nextLine(); 
        }
    }

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

            System.out.println("Informations actuelles de l'agent :");
            System.out.println("Nom : " + agentExistant.getNom());
            System.out.println("Prénom : " + agentExistant.getPrenom());
            System.out.println("Email : " + agentExistant.getEmail());
            System.out.println("Type : " + agentExistant.getTypeAgent());

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

    public void affecterAgentDepartement(int responsableId) {
        try {
            System.out.println("\n=== AFFECTATION D'UN AGENT À UN DÉPARTEMENT ===");

            System.out.print("ID de l'agent : ");
            int agentId = scanner.nextInt();

            System.out.print("ID du département : ");
            int departementId = scanner.nextInt();
            scanner.nextLine(); 
            boolean affecte = ((ResponsableServiceImpl) responsableService).affecterAgentDepartement(agentId, departementId, responsableId);

            if (affecte) {
                System.out.println("Agent affecté au département avec succès !");
            } else {
                System.out.println("Erreur lors de l'affectation de l'agent au département.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'affectation : " + e.getMessage());
            scanner.nextLine();
        }
    }

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

    public void afficherStatistiquesDepartement(int responsableId) {
        try {
            System.out.println("\n=== STATISTIQUES DU DÉPARTEMENT ===");
            
            System.out.print("ID du département à analyser : ");
            int departementId = scanner.nextInt();
            scanner.nextLine();
            
            Map<String, Object> statistiques = responsableService.calculerStatistiquesDepartement(departementId, responsableId);
            
            if (statistiques.isEmpty()) {
                System.out.println("Aucune statistique disponible ou accès refusé.");
                return;
            }
            
            System.out.println("\n📊 === RAPPORT STATISTIQUES DÉPARTEMENT === 📊");
            System.out.println("┌─────────────────────────────────────────────┐");
            System.out.println("│            INFORMATIONS GÉNÉRALES          │");
            System.out.println("├─────────────────────────────────────────────┤");
            System.out.println("│ Nombre d'agents: " + statistiques.get("nombreAgents"));
            System.out.println("│ Nombre de paiements: " + statistiques.get("nombrePaiements"));
            System.out.println("│ Montant total: " + statistiques.get("montantTotal") + " DH");
            System.out.println("│ Montant moyen: " + statistiques.get("montantMoyen") + " DH");
            System.out.println("└─────────────────────────────────────────────┘");
            
            // Affichage des répartitions par type
            @SuppressWarnings("unchecked")
            Map<TypePaiement, Integer> repartitionParType = (Map<TypePaiement, Integer>) statistiques.get("repartitionParType");
            @SuppressWarnings("unchecked")
            Map<TypePaiement, BigDecimal> montantParType = (Map<TypePaiement, BigDecimal>) statistiques.get("montantParType");
            
            System.out.println("\n💰 RÉPARTITION PAR TYPE DE PAIEMENT:");
            for (TypePaiement type : TypePaiement.values()) {
                int nombre = repartitionParType.getOrDefault(type, 0);
                BigDecimal montant = montantParType.getOrDefault(type, BigDecimal.ZERO);
                System.out.println("  " + type + ": " + nombre + " paiements (" + montant + " DH)");
            }
            
            // Affichage du top 5 des agents
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> montantParAgent = (Map<String, BigDecimal>) statistiques.get("montantParAgent");
            
            System.out.println("\n🏆 TOP 5 AGENTS PAR MONTANT:");
            montantParAgent.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .forEach(entry -> System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " DH"));
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage des statistiques : " + e.getMessage());
            scanner.nextLine();
        }
    }

    /**
     * Affiche le classement complet des agents par montant total de paiements
     */
    public void afficherClassementAgents(int responsableId) {
        try {
            System.out.println("\n=== CLASSEMENT DES AGENTS ===");
            
            System.out.print("ID du département à analyser : ");
            int departementId = scanner.nextInt();
            scanner.nextLine();
            
            List<Agent> classement = responsableService.classementAgentsParPaiement(departementId, responsableId);
            
            if (classement.isEmpty()) {
                System.out.println("Aucun agent trouvé ou accès refusé.");
                return;
            }
            
            System.out.println("\n🏆 === CLASSEMENT DES AGENTS PAR PAIEMENTS === 🏆");
            System.out.println("┌──────┬─────────────────────────┬─────────────────┬──────────────┐");
            System.out.println("│ Rang │         Agent           │      Type       │    E-mail    │");
            System.out.println("├──────┼─────────────────────────┼─────────────────┼──────────────┤");
            
            for (int i = 0; i < classement.size(); i++) {
                Agent agent = classement.get(i);
                String rang = String.format("%2d", i + 1);
                String nom = String.format("%-23s", agent.getPrenom() + " " + agent.getNom());
                String type = String.format("%-15s", agent.getTypeAgent().toString());
                String email = String.format("%-12s", agent.getEmail());
                
                System.out.println("│  " + rang + "  │ " + nom + " │ " + type + " │ " + email + " │");
            }
            
            System.out.println("└──────┴─────────────────────────┴─────────────────┴──────────────┘");
            System.out.println("Classement basé sur le montant total des paiements reçus");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage du classement : " + e.getMessage());
            scanner.nextLine();
        }
    }
    
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
    
    public void calculerStatistiquesDepartement(int responsableId) {
        try {
            Agent responsable = responsableService.obtenirInformationsAgent(responsableId);
            if (responsable == null || responsable.getDepartement() == null) {
                System.err.println("Responsable introuvable ou pas de département assigné");
                return;
            }
            
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
    
    public void classementAgentsParPaiements(int responsableId) {
        try {
            Agent responsable = responsableService.obtenirInformationsAgent(responsableId);
            if (responsable == null || responsable.getDepartement() == null) {
                System.err.println("Responsable introuvable ou pas de département assigné");
                return;
            }
            
            int departementId = responsable.getDepartement().getId();
            List<Agent> classement = responsableService.classementAgentsParPaiement(departementId, responsableId);
            
            if (classement.isEmpty()) {
                System.out.println("Aucun agent trouvé pour le classement.");
                return;
            }
            
            System.out.println("\n=== CLASSEMENT DES AGENTS PAR PAIEMENTS ===");
            System.out.printf("%-5s %-25s %-15s %-12s%n", "Rang", "Agent", "Type", "Montant Total");
            System.out.println("=".repeat(65));
            
            for (int i = 0; i < classement.size(); i++) {
                Agent agent = classement.get(i);
                double total = responsableService.calculerTotalPaiements(agent.getId());
                System.out.printf("%-5d %-25s %-15s %-12.2f%n",
                    i + 1,
                    agent.getPrenom() + " " + agent.getNom(),
                    agent.getTypeAgent(),
                    total
                );
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors du classement : " + e.getMessage());
        }
    }

    public void listerAgentsMonDepartement(int responsableId) {
        try {
            System.out.println("\n=== LISTE DES AGENTS DE MON DÉPARTEMENT ===");
            
            List<Agent> agents = responsableService.listerAgentsMonDepartement(responsableId);
            if (agents.isEmpty()) {
                System.out.println("Aucun agent trouvé dans votre département.");
                return;
            }
            
            System.out.println("Total des agents : " + agents.size());
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.printf("%-5s %-25s %-20s %-15s%n", "ID", "Nom Complet", "Email", "Type");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
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

    public void filtrerPaiementsDepartement(int responsableId) {
        try {
            System.out.println("\n=== FILTRER LES PAIEMENTS DU DÉPARTEMENT ===");
            
            // Récupérer les informations du responsable pour obtenir son département
            System.out.print("ID du département : ");
            int departementId = scanner.nextInt();
            scanner.nextLine();
            
            System.out.println("Types de paiement disponibles :");
            System.out.println("1. SALAIRE");
            System.out.println("2. PRIME");
            System.out.println("3. BONUS");
            System.out.println("4. Tous les types");
            
            System.out.print("Choix du type : ");
            int choixType = scanner.nextInt();
            scanner.nextLine();
            
            TypePaiement typePaiement = null;
            switch (choixType) {
                case 1:
                    typePaiement = TypePaiement.SALAIRE;
                    break;
                case 2:
                    typePaiement = TypePaiement.PRIME;
                    break;
                case 3:
                    typePaiement = TypePaiement.BONUS;
                    break;
                case 4:
                    typePaiement = null;
                    break;
                default:
                    System.out.println("Choix invalide, affichage de tous les types.");
                    typePaiement = null;
            }
            
            System.out.print("Trier par montant ? (oui/non) : ");
            boolean triParMontant = scanner.nextLine().equalsIgnoreCase("oui");
            
            System.out.print("Trier par date ? (oui/non) : ");
            boolean triParDate = scanner.nextLine().equalsIgnoreCase("oui");
            
            List<Paiement> paiementsFiltres = responsableService.filtrerPaiementsDepartement(
                departementId, typePaiement, triParMontant, triParDate, responsableId);
            
            if (paiementsFiltres.isEmpty()) {
                System.out.println("Aucun paiement trouvé avec les critères spécifiés.");
                return;
            }
            
            System.out.println("\n📊 RÉSULTATS DU FILTRAGE");
            System.out.println("Total des paiements : " + paiementsFiltres.size());
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.printf("%-5s %-15s %-12s %-12s %-10s%n", "ID", "Type", "Montant", "Date", "Statut");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            for (Paiement p : paiementsFiltres) {
                System.out.printf("%-5d %-15s %-12.2f %-12s %-10s%n",
                    p.getId(),
                    p.getTypePaiement(),
                    p.getMontant(),
                    p.getDatePaiement().toString(),
                    p.isConditionValidee() ? "✅" : "⏳"
                );
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors du filtrage : " + e.getMessage());
            scanner.nextLine();
        }
    }
}