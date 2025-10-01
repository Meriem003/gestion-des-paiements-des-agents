package controller;

import model.Agent;
import model.Departement;
import model.Paiement;
import model.TypeAgent;
import model.TypePaiement;
import service.IResponsableService;
import service.IAgentService;
import view.MenuAgent;
import java.util.List;
import java.util.Scanner;
import java.math.BigDecimal;

public class ResponsableController {
    private IResponsableService responsableService;
    private Scanner scanner;

    public ResponsableController(IResponsableService responsableService) {
        this.responsableService = responsableService;
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
            boolean affecte = responsableService.affecterAgentDepartement(agentId, departementId, responsableId);

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
            Paiement salaire = responsableService.ajouterSalaire(agentId, montant, responsableId);

            if (salaire != null) {
                System.out.println("Salaire ajouté avec succès !");
                System.out.println("ID du paiement : " + salaire.getId());
                System.out.println("Montant : " + salaire.getMontant() + " DH");
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
            Paiement prime = responsableService.ajouterPrime(agentId, montant, responsableId);

            if (prime != null) {
                System.out.println("Prime ajoutée avec succès !");
                System.out.println("ID du paiement : " + prime.getId());
                System.out.println("Montant : " + prime.getMontant() + " DH");
            } else {
                System.out.println("Erreur lors de l'ajout de la prime.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de la prime : " + e.getMessage());
            scanner.nextLine();
        }
    }

    public void demanderBonus(int responsableId) {
        try {
            System.out.println("\n=== DEMANDE DE BONUS ===");

            System.out.print("ID de l'agent : ");
            int agentId = scanner.nextInt();

            System.out.print("Montant du bonus demandé : ");
            double montant = scanner.nextDouble();
            scanner.nextLine(); 
            int demandeId = responsableService.demanderBonus(agentId, montant, responsableId);

            if (demandeId > 0) {
                System.out.println("Demande de bonus soumise avec succès !");
                System.out.println("ID de la demande : " + demandeId);
                System.out.println("Montant demandé : " + montant + " DH");
                System.out.println("Statut : En attente de validation");
            } else {
                System.out.println("Erreur lors de la soumission de la demande de bonus.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la demande de bonus : " + e.getMessage());
            scanner.nextLine();
        }
    }

    public void demanderIndemnite(int responsableId) {
        try {
            System.out.println("\n=== DEMANDE D'INDEMNITÉ ===");

            System.out.print("ID de l'agent : ");
            int agentId = scanner.nextInt();

            System.out.print("Montant de l'indemnité demandée : ");
            double montant = scanner.nextDouble();
            scanner.nextLine(); 
            int demandeId = responsableService.demanderIndemnite(agentId, montant, responsableId);

            if (demandeId > 0) {
                System.out.println("Demande d'indemnité soumise avec succès !");
                System.out.println("ID de la demande : " + demandeId);
                System.out.println("Montant demandé : " + montant + " DH");
                System.out.println("Statut : En attente de validation");
            } else {
                System.out.println("Erreur lors de la soumission de la demande d'indemnité.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la demande d'indemnité : " + e.getMessage());
            scanner.nextLine();
        }
    }

    public void consulterPaiementsAgent(int responsableId) {
        try {
            System.out.println("\n=== CONSULTATION DES PAIEMENTS D'UN AGENT ===");

            System.out.print("ID de l'agent : ");
            int agentId = scanner.nextInt();
            scanner.nextLine(); 
            List<Paiement> paiements = responsableService.consulterPaiementsAgent(agentId, responsableId);

            if (paiements != null && !paiements.isEmpty()) {
                System.out.println("\nPaiements de l'agent ID " + agentId + " :");
                System.out.println("+---------+---------------+-----------+------------+");
                System.out.println("| ID      | Type          | Montant   | Date       |");
                System.out.println("+---------+---------------+-----------+------------+");

                for (Paiement paiement : paiements) {
                    System.out.printf("| %-7d | %-13s | %-9.2f | %-10s |\n",
                            paiement.getId(),
                            paiement.getTypePaiement(),
                            paiement.getMontant(),
                            paiement.getDatePaiement()
                    );
                }
                System.out.println("+---------+---------------+-----------+------------+");

                BigDecimal total = paiements.stream()
                        .map(Paiement::getMontant)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                System.out.println("Total des paiements : " + total + " DH");
            } else {
                System.out.println("Aucun paiement trouvé pour cet agent.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des paiements : " + e.getMessage());
            scanner.nextLine();
        }
    }

    public void consulterPaiementsDepartement(int responsableId) {
        try {
            System.out.println("\n=== CONSULTATION DES PAIEMENTS DU DÉPARTEMENT ===");

            System.out.print("ID du département : ");
            int departementId = scanner.nextInt();
            scanner.nextLine(); 
            List<Paiement> paiements = responsableService.consulterPaiementsDepartement(departementId, responsableId);

            if (paiements != null && !paiements.isEmpty()) {
                System.out.println("\nPaiements du département ID " + departementId + " :");
                System.out.println("+---------+----------+---------------+-----------+------------+");
                System.out.println("| ID      | Agent ID | Type          | Montant   | Date       |");
                System.out.println("+---------+----------+---------------+-----------+------------+");

                for (Paiement paiement : paiements) {
                    System.out.printf("| %-7d | %-8d | %-13s | %-9.2f | %-10s |\n",
                            paiement.getId(),
                            paiement.getAgent().getId(),
                            paiement.getTypePaiement(),
                            paiement.getMontant(),
                            paiement.getDatePaiement()
                    );
                }
                System.out.println("+---------+----------+---------------+-----------+------------+");

                BigDecimal total = paiements.stream()
                        .map(Paiement::getMontant)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                System.out.println("Total des paiements du département : " + total + " DH");
            } else {
                System.out.println("Aucun paiement trouvé pour ce département.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des paiements : " + e.getMessage());
            scanner.nextLine();
        }
    }

    public void filtrerTrierPaiementsDepartement(int responsableId) {
        try {
            System.out.println("\n=== FILTRAGE ET TRI DES PAIEMENTS DU DÉPARTEMENT ===");

            System.out.print("ID du département : ");
            int departementId = scanner.nextInt();
            scanner.nextLine(); 
            System.out.println("Types de paiements disponibles :");
            System.out.println("1. SALAIRE");
            System.out.println("2. PRIME");
            System.out.println("3. BONUS");
            System.out.println("4. INDEMNITE");
            System.out.println("0. Tous les types");
            System.out.print("Choisissez le type de paiement (0-4) : ");

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
                    typePaiement = TypePaiement.INDEMNITE;
                    break;
                case 0:
                default:
                    typePaiement = null; // Tous les types
                    break;
            }

            System.out.print("Trier par montant ? (oui/non) : ");
            boolean triParMontant = scanner.nextLine().trim().toLowerCase().startsWith("o");

            System.out.print("Trier par date ? (oui/non) : ");
            boolean triParDate = scanner.nextLine().trim().toLowerCase().startsWith("o");

            List<Paiement> paiements = responsableService.filtrerTrierPaiementsDepartement(
                    departementId, typePaiement, triParMontant, triParDate, responsableId);

            if (paiements != null && !paiements.isEmpty()) {
                System.out.println("\nPaiements filtrés et triés du département ID " + departementId + " :");
                if (typePaiement != null) {
                    System.out.println("Filtre : " + typePaiement);
                }
                if (triParMontant) {
                    System.out.println("Trié par : Montant");
                }
                if (triParDate) {
                    System.out.println("Trié par : Date");
                }

                System.out.println("+---------+----------+---------------+-----------+------------+");
                System.out.println("| ID      | Agent ID | Type          | Montant   | Date       |");
                System.out.println("+---------+----------+---------------+-----------+------------+");

                for (Paiement paiement : paiements) {
                    System.out.printf("| %-7d | %-8d | %-13s | %-9.2f | %-10s |\n",
                            paiement.getId(),
                            paiement.getAgent().getId(),
                            paiement.getTypePaiement(),
                            paiement.getMontant(),
                            paiement.getDatePaiement()
                    );
                }
                System.out.println("+---------+----------+---------------+-----------+------------+");

                BigDecimal total = paiements.stream()
                        .map(Paiement::getMontant)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                System.out.println("Total des paiements filtrés : " + total + " DH");
            } else {
                System.out.println("Aucun paiement trouvé avec les critères spécifiés.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du filtrage et tri des paiements : " + e.getMessage());
            scanner.nextLine();
        }
    }

    public void ajouterPaiementAgent(int responsableId) {
        try {
            System.out.println("\n=== AJOUT D'UN PAIEMENT À UN AGENT ===");

            System.out.print("ID de l'agent : ");
            int agentId = scanner.nextInt();
            scanner.nextLine(); 
            System.out.println("Types de paiements disponibles :");
            System.out.println("1. SALAIRE");
            System.out.println("2. PRIME");
            System.out.println("3. Demander BONUS (nécessite validation)");
            System.out.println("4. Demander INDEMNITÉ (nécessite validation)");
            System.out.print("Choisissez le type de paiement (1-4) : ");

            int choixType = scanner.nextInt();
            scanner.nextLine(); 
            System.out.print("Montant : ");
            double montant = scanner.nextDouble();
            scanner.nextLine(); 
            switch (choixType) {
                case 1:
                    responsableService.ajouterSalaire(agentId, montant, responsableId);
                    System.out.println("Salaire ajouté avec succès !");
                    break;
                case 2:
                    responsableService.ajouterPrime(agentId, montant, responsableId);
                    System.out.println("Prime ajoutée avec succès !");
                    break;
                case 3:
                    int bonusId = responsableService.demanderBonus(agentId, montant, responsableId);
                    if (bonusId > 0) {
                        System.out.println("Demande de bonus soumise avec succès ! ID: " + bonusId);
                    }
                    break;
                case 4:
                    int indemniteId = responsableService.demanderIndemnite(agentId, montant, responsableId);
                    if (indemniteId > 0) {
                        System.out.println("Demande d'indemnité soumise avec succès ! ID: " + indemniteId);
                    }
                    break;
                default:
                    System.out.println("Choix invalide.");
                    break;
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout du paiement : " + e.getMessage());
            scanner.nextLine();
        }
    }

    public void consulterPaiementsDepartement(int responsableId, int departementId) {
        try {
            System.out.println("\n=== CONSULTATION DES PAIEMENTS DU DÉPARTEMENT ===");

            List<Paiement> paiements = responsableService.consulterPaiementsDepartement(departementId, responsableId);

            if (paiements != null && !paiements.isEmpty()) {
                System.out.println("\nPaiements du département ID " + departementId + " :");
                System.out.println("+---------+----------+---------------+-----------+------------+");
                System.out.println("| ID      | Agent ID | Type          | Montant   | Date       |");
                System.out.println("+---------+----------+---------------+-----------+------------+");

                for (Paiement paiement : paiements) {
                    System.out.printf("| %-7d | %-8d | %-13s | %-9.2f | %-10s |\n",
                            paiement.getId(),
                            paiement.getAgent().getId(),
                            paiement.getTypePaiement(),
                            paiement.getMontant(),
                            paiement.getDatePaiement()
                    );
                }
                System.out.println("+---------+----------+---------------+-----------+------------+");

                BigDecimal total = paiements.stream()
                        .map(Paiement::getMontant)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                System.out.println("Total des paiements du département : " + total + " DH");
            } else {
                System.out.println("Aucun paiement trouvé pour ce département.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des paiements : " + e.getMessage());
        }
    }

    public void filtrerTrierPaiementsDepartement(int responsableId, int departementId) {
        try {
            System.out.println("\n=== FILTRAGE ET TRI DES PAIEMENTS DU DÉPARTEMENT ===");

            System.out.println("Types de paiements disponibles :");
            System.out.println("1. SALAIRE");
            System.out.println("2. PRIME");
            System.out.println("3. BONUS");
            System.out.println("4. INDEMNITE");
            System.out.println("0. Tous les types");
            System.out.print("Choisissez le type de paiement (0-4) : ");

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
                    typePaiement = TypePaiement.INDEMNITE;
                    break;
                case 0:
                default:
                    typePaiement = null;
                    break;
            }

            System.out.print("Trier par montant ? (oui/non) : ");
            boolean triParMontant = scanner.nextLine().trim().toLowerCase().startsWith("o");

            System.out.print("Trier par date ? (oui/non) : ");
            boolean triParDate = scanner.nextLine().trim().toLowerCase().startsWith("o");

            List<Paiement> paiements = responsableService.filtrerTrierPaiementsDepartement(
                    departementId, typePaiement, triParMontant, triParDate, responsableId);

            if (paiements != null && !paiements.isEmpty()) {
                System.out.println("\nPaiements filtrés et triés du département ID " + departementId + " :");
                if (typePaiement != null) {
                    System.out.println("Filtre : " + typePaiement);
                }
                if (triParMontant) {
                    System.out.println("Trié par : Montant");
                }
                if (triParDate) {
                    System.out.println("Trié par : Date");
                }

                System.out.println("+---------+----------+---------------+-----------+------------+");
                System.out.println("| ID      | Agent ID | Type          | Montant   | Date       |");
                System.out.println("+---------+----------+---------------+-----------+------------+");

                for (Paiement paiement : paiements) {
                    System.out.printf("| %-7d | %-8d | %-13s | %-9.2f | %-10s |\n",
                            paiement.getId(),
                            paiement.getAgent().getId(),
                            paiement.getTypePaiement(),
                            paiement.getMontant(),
                            paiement.getDatePaiement()
                    );
                }
                System.out.println("+---------+----------+---------------+-----------+------------+");

                BigDecimal total = paiements.stream()
                        .map(Paiement::getMontant)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                System.out.println("Total des paiements filtrés : " + total + " DH");
            } else {
                System.out.println("Aucun paiement trouvé avec les critères spécifiés.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du filtrage et tri des paiements : " + e.getMessage());
            scanner.nextLine();
        }
    }

    public void calculerStatistiquesDepartement(int responsableId, int departementId) {
        try {
            System.out.println("\n=== STATISTIQUES DU DÉPARTEMENT ===");
            System.out.println("Fonctionnalité en cours de développement...");
            // TODO: Implémenter les statistiques quand les méthodes seront disponibles dans le service
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul des statistiques : " + e.getMessage());
        }
    }

    public void classementAgents(int responsableId, int departementId) {
        try {
            System.out.println("\n=== CLASSEMENT DES AGENTS ===");
            System.out.println("Fonctionnalité en cours de développement...");
            // TODO: Implémenter le classement quand les méthodes seront disponibles dans le service
        } catch (Exception e) {
            System.err.println("Erreur lors du classement des agents : " + e.getMessage());
        }
    }

    public void identifierPaiementsInhabituels(int responsableId, int departementId) {
        try {
            System.out.println("\n=== IDENTIFICATION DES PAIEMENTS INHABITUELS ===");
            System.out.println("Fonctionnalité en cours de développement...");
            // TODO: Implémenter l'identification quand les méthodes seront disponibles dans le service
        } catch (Exception e) {
            System.err.println("Erreur lors de l'identification des paiements inhabituels : " + e.getMessage());
        }
    }
}