package controller;

import model.Agent;
import model.Departement;
import model.TypePaiement;
import service.IDirecteurService;
import service.IAgentService;
import view.MenuAgent;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class DirecteurController {
    private IDirecteurService directeurService;
    private Scanner scanner;

    public DirecteurController(IDirecteurService directeurService) {
        this.directeurService = directeurService;
        this.scanner = new Scanner(System.in);
    }

    public void accederMenuAgent(Agent directeur) {
        try {
            System.out.println("\n=== ACCÈS AU MENU AGENT ===");
            System.out.println("Vous accédez maintenant à vos informations personnelles...");

            MenuAgent menuAgent = new MenuAgent(directeur, (IAgentService) directeurService);
            menuAgent.afficherMenu();

        } catch (Exception e) {
            System.err.println("Erreur lors de l'accès au menu agent : " + e.getMessage());
        }
    }

    public void creerDepartement(int directeurId) {
        try {
            System.out.println("\n=== CRÉATION D'UN DÉPARTEMENT ===");

            System.out.print("Nom du département : ");
            String nom = scanner.nextLine();

            if (nom.trim().isEmpty()) {
                System.out.println("Le nom du département ne peut pas être vide.");
                return;
            }

            Departement nouveauDepartement = new Departement();
            nouveauDepartement.setNom(nom);

            Departement departementCree = directeurService.creerDepartement(nouveauDepartement, directeurId);

            if (departementCree != null) {
                System.out.println("Département créé avec succès !");
                System.out.println("ID : " + departementCree.getId());
                System.out.println("Nom : " + departementCree.getNom());
            } else {
                System.out.println("Erreur lors de la création du département.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la création du département : " + e.getMessage());
        }
    }

    public void modifierDepartement(int directeurId) {
        try {
            System.out.println("\n=== MODIFICATION D'UN DÉPARTEMENT ===");
            listerTousDepartements(directeurId);
            System.out.print("ID du département à modifier : ");
            int departementId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Nouveau nom du département : ");
            String nouveauNom = scanner.nextLine();
            if (nouveauNom.trim().isEmpty()) {
                System.out.println("Le nom du département ne peut pas être vide.");
                return;
            }
            Departement departement = new Departement();
            departement.setId(departementId);
            departement.setNom(nouveauNom);

            Departement departementModifie = directeurService.modifierDepartement(departement, directeurId);

            if (departementModifie != null) {
                System.out.println("Département modifié avec succès !");
                System.out.println("Nouveau nom : " + departementModifie.getNom());
            } else {
                System.out.println("Erreur lors de la modification du département.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la modification du département : " + e.getMessage());
        }
    }

    public void supprimerDepartement(int directeurId) {
        try {
            System.out.println("\n=== SUPPRESSION D'UN DÉPARTEMENT ===");
            listerTousDepartements(directeurId);
            System.out.print("ID du département à supprimer : ");
            int departementId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Êtes-vous sûr de vouloir supprimer ce département ? (o/n) : ");
            String confirmation = scanner.nextLine().toLowerCase();

            if (!confirmation.equals("o") && !confirmation.equals("oui")) {
                System.out.println("Suppression annulée.");
                return;
            }

            boolean resultat = directeurService.supprimerDepartement(departementId, directeurId);

            if (resultat) {
                System.out.println("Département supprimé avec succès !");
            } else {
                System.out.println("Erreur lors de la suppression du département.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du département : " + e.getMessage());
        }
    }

    public void listerTousDepartements(int directeurId) {
        try {
            System.out.println("\n=== LISTE DES DÉPARTEMENTS ===");

            List<Departement> departements = directeurService.listerTousDepartements(directeurId);

            if (departements == null || departements.isEmpty()) {
                System.out.println("Aucun département trouvé.");
                return;
            }

            System.out.println("Nombre total de départements : " + departements.size());
            System.out.println("-------------------------------------------------------");

            for (Departement dept : departements) {
                System.out.println("ID : " + dept.getId());
                System.out.println("Nom : " + dept.getNom());
                if (dept.getResponsable() != null) {
                    System.out.println("Responsable : " + dept.getResponsable().getNom() + " " + dept.getResponsable().getPrenom());
                } else {
                    System.out.println("Responsable : Non assigné");
                }
                System.out.println("-------------------------------------------------------");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des départements : " + e.getMessage());
        }
    }

    //simplifier 
    public void creerUtilisateurAvecDepartement(int directeurId) {
        try {
            System.out.println("\n=== CRÉATION D'UN RESPONSABLE DE DÉPARTEMENT ===");
            
            listerTousDepartements(directeurId);
            
            System.out.print("ID du département d'assignation : ");
            int departementId = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Nom du responsable : ");
            String nom = scanner.nextLine();
            
            System.out.print("Prénom du responsable : ");
            String prenom = scanner.nextLine();
            
            System.out.print("Email du responsable : ");
            String email = scanner.nextLine();
            
            System.out.print("Mot de passe du responsable : ");
            String motDePasse = scanner.nextLine();
            
            Agent nouveauResponsable = new Agent();
            nouveauResponsable.setNom(nom);
            nouveauResponsable.setPrenom(prenom);
            nouveauResponsable.setEmail(email);
            nouveauResponsable.setMotDePasse(motDePasse);
            
            Agent responsableCree = directeurService.creerUtilisateurAvecDepartement(nouveauResponsable, departementId, directeurId);
            
            if (responsableCree != null) {
                System.out.println(" Responsable de département créé et assigné avec succès !");
                System.out.println(" Le nouveau responsable peut maintenant gérer son département.");
            } else {
                System.out.println(" Erreur lors de la création du responsable.");
            }
            
        } catch (Exception e) {
            System.err.println(" Erreur : " + e.getMessage());
        }
    }
    
    public void genererTopAgentsMieuxPayes(int directeurId) {
        try {
            System.out.println("\n=== TOP DES AGENTS LES MIEUX PAYÉS ===");
            
            System.out.print("Nombre d'agents à afficher dans le top : ");
            int nombreAgents = scanner.nextInt();
            scanner.nextLine();
            
            if (nombreAgents <= 0) {
                System.out.println(" Le nombre doit être positif.");
                return;
            }
            
            List<Agent> topAgents = directeurService.genererTopAgentsMieuxPayes(nombreAgents, directeurId);
            
            if (topAgents.isEmpty()) {
                System.out.println("Aucun agent trouvé.");
                return;
            }
            
            System.out.println(" TOP " + nombreAgents + " DES AGENTS LES MIEUX PAYÉS");
            System.out.println("=" + "=".repeat(80));
            System.out.printf("%-5s %-20s %-15s %-20s%n", "Rang", "Nom Complet", "Type", "Département");
            System.out.println("=" + "=".repeat(80));
            
            for (int i = 0; i < topAgents.size(); i++) {
                Agent agent = topAgents.get(i);
                String departement = agent.getDepartement() != null ? 
                    agent.getDepartement().getNom() : "Non assigné";
                
                System.out.printf("%-5d %-20s %-15s %-20s%n",
                    (i + 1),
                    agent.getNom() + " " + agent.getPrenom(),
                    agent.getTypeAgent(),
                    departement
                );
            }
            
        } catch (Exception e) {
            System.err.println(" Erreur : " + e.getMessage());
        }
    }
    

    public void genererRapportGlobalEntreprise(int directeurId) {
        try {
            Map<String, Object> statistiques = directeurService.genererRapportGlobalEntreprise(directeurId);
            if (statistiques != null && !statistiques.isEmpty()) {
                System.out.println("STATISTIQUES GÉNÉRALES :");
                System.out.println("   Nombre total d'agents     : " + statistiques.get("totalAgents"));
                System.out.println("   Nombre de départements    : " + statistiques.get("totalDepartements"));
                System.out.println("   Total des paiements       : " + statistiques.get("totalPaiements") + "dh");
                System.out.println("   Moyenne par agent         : " + statistiques.get("moyenneParAgent") + "dh");
                System.out.println("   Paiement le plus élevé    : " + statistiques.get("paiementMax") + "dh");
                System.out.println("   Paiement le plus faible   : " + statistiques.get("paiementMin") + "dh");
                
                System.out.println("RÉPARTITION PAR TYPE D'AGENT :");
                @SuppressWarnings("unchecked")
                Map<String, Object> repartitionAgents = (Map<String, Object>) statistiques.get("repartitionAgents");
                if (repartitionAgents != null) {
                    for (Map.Entry<String, Object> entry : repartitionAgents.entrySet()) {
                        System.out.println("   " + entry.getKey() + " : " + entry.getValue());
                    }
                }
            } else {
                System.out.println("Aucune donnée disponible pour le rapport global.");
            }
                        
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du rapport global : " + e.getMessage());
        }
    }
    
    public void calculerRepartitionPaiementsParType(int directeurId) {
        try {
            System.out.println("\n=== RÉPARTITION DES PAIEMENTS PAR TYPE ===");
            
            Map<String, Object> rapport = directeurService.genererRapportGlobalEntreprise(directeurId);
            
            if (rapport.containsKey("repartitionPaiements")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> repartition = (Map<String, Object>) rapport.get("repartitionPaiements");
                
                System.out.println("\nRépartition des paiements :");
                for (Map.Entry<String, Object> entry : repartition.entrySet()) {
                    System.out.printf("%-15s : %sdh%n", entry.getKey(), entry.getValue());
                }
            } else {
                System.out.println("Aucune donnée de répartition disponible.");
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul de la répartition : " + e.getMessage());
        }
    }
    
    public void associerResponsableDepartement(int directeurId) {
        try {
            System.out.println("\n=== ASSOCIATION RESPONSABLE-DÉPARTEMENT ===");
            
            List<Departement> departements = directeurService.listerTousDepartements(directeurId);
            if (departements.isEmpty()) {
                System.out.println("Aucun département disponible.");
                return;
            }
            
            System.out.println("Départements disponibles :");
            for (Departement dept : departements) {
                System.out.println(dept.getId() + ". " + dept.getNom());
            }
            
            System.out.print("ID du département : ");
            int departementId = scanner.nextInt();
            scanner.nextLine();
            
            List<Agent> responsables = directeurService.listerTousResponsables();
            if (responsables.isEmpty()) {
                System.out.println("Aucun responsable disponible.");
                return;
            }
            
            System.out.println("Responsables disponibles :");
            for (Agent resp : responsables) {
                System.out.println(resp.getId() + ". " + resp.getPrenom() + " " + resp.getNom());
            }
            
            System.out.print("ID du responsable : ");
            int responsableId = scanner.nextInt();
            scanner.nextLine();
            
            System.out.println("✅ Association effectuée avec succès !");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'association : " + e.getMessage());
            scanner.nextLine(); 
        }
    }
}