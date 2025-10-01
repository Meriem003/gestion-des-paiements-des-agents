package controller;

import model.Agent;
import model.Departement;
import model.TypeAgent;
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

    public void validerAjoutBonus(int directeurId) {
        try {
            System.out.println("\n=== VALIDATION DES BONUS ===");
            List<Map<String, Object>> demandes = directeurService.consulterDemandesEnAttente(directeurId);

            if (demandes == null || demandes.isEmpty()) {
                System.out.println("Aucune demande de bonus en attente.");
                return;
            }

            System.out.println("Demandes de bonus en attente :");
            for (int i = 0; i < demandes.size(); i++) {
                Map<String, Object> demande = demandes.get(i);
                System.out.println((i + 1) + ". ID: " + demande.get("id") +
                        " | Agent: " + demande.get("agent") +
                        " | Montant: " + demande.get("montant") + " €");
            }

            System.out.print("Entrez l'ID de la demande à traiter : ");
            int demandeId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Approuver cette demande ? (o/n) : ");
            String reponse = scanner.nextLine().toLowerCase();
            boolean approuve = reponse.equals("o") || reponse.equals("oui");

            String motifRejet = "";
            if (!approuve) {
                System.out.print("Motif du rejet : ");
                motifRejet = scanner.nextLine();
            }

            boolean resultat = directeurService.validerDemandeBonus(demandeId, approuve, motifRejet, directeurId);

            if (resultat) {
                System.out.println("Demande traitée avec succès !");
            } else {
                System.out.println("Erreur lors du traitement de la demande.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la validation des bonus : " + e.getMessage());
        }
    }

    public void validerAjoutIndemnites(int directeurId) {
        try {
            System.out.println("\n=== VALIDATION DES INDEMNITÉS ===");

            List<Map<String, Object>> demandes = directeurService.consulterDemandesEnAttente(directeurId);

            if (demandes == null || demandes.isEmpty()) {
                System.out.println("Aucune demande d'indemnité en attente.");
                return;
            }

            System.out.println("Demandes d'indemnités en attente :");
            for (int i = 0; i < demandes.size(); i++) {
                Map<String, Object> demande = demandes.get(i);
                System.out.println((i + 1) + ". ID: " + demande.get("id") +
                        " | Agent: " + demande.get("agent") +
                        " | Montant: " + demande.get("montant") + " €");
            }

            System.out.print("Entrez l'ID de la demande à traiter : ");
            int demandeId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Approuver cette demande ? (o/n) : ");
            String reponse = scanner.nextLine().toLowerCase();
            boolean approuve = reponse.equals("o") || reponse.equals("oui");

            String motifRejet = "";
            if (!approuve) {
                System.out.print("Motif du rejet : ");
                motifRejet = scanner.nextLine();
            }

            boolean resultat = directeurService.validerDemandeIndemnite(demandeId, approuve, motifRejet, directeurId);

            if (resultat) {
                System.out.println("Demande traitée avec succès !");
            } else {
                System.out.println("Erreur lors du traitement de la demande.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la validation des indemnités : " + e.getMessage());
        }
    }

    public void consulterDemandesEnAttente(int directeurId) {
        try {
            System.out.println("\n=== DEMANDES EN ATTENTE ===");

            List<Map<String, Object>> demandes = directeurService.consulterDemandesEnAttente(directeurId);

            if (demandes == null || demandes.isEmpty()) {
                System.out.println("Aucune demande en attente.");
                return;
            }

            System.out.println("Nombre total de demandes : " + demandes.size());
            System.out.println("-------------------------------------------------------");

            for (Map<String, Object> demande : demandes) {
                System.out.println("ID : " + demande.get("id"));
                System.out.println("Type : " + demande.get("type"));
                System.out.println("Agent : " + demande.get("agent"));
                System.out.println("Montant : " + demande.get("montant") + " €");
                System.out.println("Date de demande : " + demande.get("dateDemande"));
                System.out.println("Statut : " + demande.get("statut"));
                System.out.println("-------------------------------------------------------");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des demandes : " + e.getMessage());
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

    public void associerResponsableDepartement(int directeurId) {
        try {
            System.out.println("\n=== ASSOCIATION RESPONSABLE-DÉPARTEMENT ===");
            listerTousDepartements(directeurId);
            System.out.print("ID du département : ");
            int departementId = scanner.nextInt();
            System.out.print("ID du responsable (agent) : ");
            int responsableId = scanner.nextInt();
            scanner.nextLine();
            boolean resultat = directeurService.associerResponsableDepartement(departementId, responsableId, directeurId);
            if (resultat) {
                System.out.println("Responsable associé au département avec succès !");
            } else {
                System.out.println("Erreur lors de l'association.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'association : " + e.getMessage());
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

    public void gererUtilisateurs(int directeurId) {
        try {
            System.out.println("\n=== GESTION DES UTILISATEURS ===");
            System.out.println("1. Créer un nouvel utilisateur");
            System.out.println("2. Créer un utilisateur avec département");
            System.out.println("3. Modifier les droits d'un utilisateur");
            System.out.println("4. Changer le statut d'un utilisateur");
            System.out.println("5. Réinitialiser un mot de passe");
            System.out.println("0. Retour");
            System.out.print("Choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    creerUtilisateur(directeurId);
                    break;
                case 2:
                    creerUtilisateurAvecDepartement(directeurId);
                    break;
                case 3:
                    modifierDroitsUtilisateur(directeurId);
                    break;
                case 4:
                    changerStatutUtilisateur(directeurId);
                    break;
                case 5:
                    reinitialiserMotDePasse(directeurId);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Choix invalide.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la gestion des utilisateurs : " + e.getMessage());
        }
    }


    private void creerUtilisateur(int directeurId) {
        try {
            System.out.println("\n=== CRÉATION D'UN UTILISATEUR ===");

            System.out.print("Nom : ");
            String nom = scanner.nextLine();

            System.out.print("Prénom : ");
            String prenom = scanner.nextLine();

            System.out.print("Email : ");
            String email = scanner.nextLine();

            System.out.print("Mot de passe : ");
            String motDePasse = scanner.nextLine();

            System.out.println("Type d'agent :");
            for (TypeAgent type : TypeAgent.values()) {
                System.out.println("- " + type);
            }
            System.out.print("Type : ");
            String typeStr = scanner.nextLine().toUpperCase();

            try {
                TypeAgent typeAgent = TypeAgent.valueOf(typeStr);

                Agent nouvelAgent = new Agent();
                nouvelAgent.setNom(nom);
                nouvelAgent.setPrenom(prenom);
                nouvelAgent.setEmail(email);
                nouvelAgent.setMotDePasse(motDePasse);
                nouvelAgent.setTypeAgent(typeAgent);

                Agent agentCree = directeurService.creerUtilisateur(nouvelAgent, directeurId);

                if (agentCree != null) {
                    System.out.println("Utilisateur créé avec succès !");
                    System.out.println("ID : " + agentCree.getId());
                    System.out.println("Nom complet : " + agentCree.getNom() + " " + agentCree.getPrenom());
                } else {
                    System.out.println("Erreur lors de la création de l'utilisateur.");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Type d'agent invalide.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
        }
    }

    private void creerUtilisateurAvecDepartement(int directeurId) {
        try {
            System.out.println("\n=== CRÉATION D'UN UTILISATEUR AVEC DÉPARTEMENT ===");
            listerTousDepartements(directeurId);
            System.out.print("ID du département : ");
            int departementId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Nom : ");
            String nom = scanner.nextLine();

            System.out.print("Prénom : ");
            String prenom = scanner.nextLine();

            System.out.print("Email : ");
            String email = scanner.nextLine();

            System.out.print("Mot de passe : ");
            String motDePasse = scanner.nextLine();

            System.out.println("Type d'agent :");
            for (TypeAgent type : TypeAgent.values()) {
                System.out.println("- " + type);
            }
            System.out.print("Type : ");
            String typeStr = scanner.nextLine().toUpperCase();

            try {
                TypeAgent typeAgent = TypeAgent.valueOf(typeStr);

                Agent nouvelAgent = new Agent();
                nouvelAgent.setNom(nom);
                nouvelAgent.setPrenom(prenom);
                nouvelAgent.setEmail(email);
                nouvelAgent.setMotDePasse(motDePasse);
                nouvelAgent.setTypeAgent(typeAgent);

                Agent agentCree = directeurService.creerUtilisateurAvecDepartement(nouvelAgent, departementId, directeurId);

                if (agentCree != null) {
                    System.out.println("Utilisateur créé avec département avec succès !");
                    System.out.println("ID : " + agentCree.getId());
                    System.out.println("Nom complet : " + agentCree.getNom() + " " + agentCree.getPrenom());
                } else {
                    System.out.println("Erreur lors de la création de l'utilisateur.");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Type d'agent invalide.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
        }
    }

    private void modifierDroitsUtilisateur(int directeurId) {
        try {
            System.out.println("\n=== MODIFICATION DES DROITS ===");

            System.out.print("ID de l'agent : ");
            int agentId = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Nouveau type d'agent :");
            for (TypeAgent type : TypeAgent.values()) {
                System.out.println("- " + type);
            }
            System.out.print("Type : ");
            String typeStr = scanner.nextLine().toUpperCase();

            try {
                TypeAgent nouveauType = TypeAgent.valueOf(typeStr);

                Agent agentModifie = directeurService.modifierDroitsUtilisateur(agentId, nouveauType, directeurId);

                if (agentModifie != null) {
                    System.out.println("Droits modifiés avec succès !");
                    System.out.println("Nouveau type : " + agentModifie.getTypeAgent());
                } else {
                    System.out.println("Erreur lors de la modification des droits.");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Type d'agent invalide.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la modification des droits : " + e.getMessage());
        }
    }

    private void changerStatutUtilisateur(int directeurId) {
        try {
            System.out.println("\n=== CHANGEMENT DE STATUT ===");

            System.out.print("ID de l'agent : ");
            int agentId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Activer l'utilisateur ? (o/n) : ");
            String reponse = scanner.nextLine().toLowerCase();
            boolean actif = reponse.equals("o") || reponse.equals("oui");

            boolean resultat = directeurService.changerStatutUtilisateur(agentId, actif, directeurId);

            if (resultat) {
                System.out.println("Statut modifié avec succès !");
                System.out.println("Nouveau statut : " + (actif ? "Actif" : "Inactif"));
            } else {
                System.out.println("Erreur lors du changement de statut.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du changement de statut : " + e.getMessage());
        }
    }

    private void reinitialiserMotDePasse(int directeurId) {
        try {
            System.out.println("\n=== RÉINITIALISATION DE MOT DE PASSE ===");

            System.out.print("ID de l'agent : ");
            int agentId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Nouveau mot de passe : ");
            String nouveauMotDePasse = scanner.nextLine();

            boolean resultat = directeurService.reinitialiserMotDePasse(agentId, nouveauMotDePasse, directeurId);

            if (resultat) {
                System.out.println("Mot de passe réinitialisé avec succès !");
            } else {
                System.out.println("Erreur lors de la réinitialisation du mot de passe.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la réinitialisation : " + e.getMessage());
        }
    }
    
    public boolean verifierPermissions(int directeurId) {
        try {
            return directeurService.verifierPermissionsDirecteur(directeurId);
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification des permissions : " + e.getMessage());
            return false;
        }
    }
}