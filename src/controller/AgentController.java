package controller;

import model.Agent;
import model.Departement;
import model.Paiement;
import model.TypePaiement;
import service.IAgentService;
import service.Iimpl.AgentServiceImpl;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class AgentController {
    private IAgentService agentService;
    private Scanner scanner;

    public AgentController(IAgentService agentService) {
        this.agentService = agentService;
        this.scanner = new Scanner(System.in);
    }

    public void consulterInformationsPersonnelles(int agentId) {
        try {
            System.out.println("\n=== CONSULTATION DES INFORMATIONS PERSONNELLES ===");
            Agent agent = agentService.obtenirInformationsAgent(agentId);
            if (agent != null) {
                System.out.println("Informations affichées avec succès.");
            } else {
                System.out.println("Erreur : Impossible de récupérer les informations.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des informations : " + e.getMessage());
        }
    }


    public void consulterHistoriquePaiements(int agentId) {
        try {
            System.out.println("\n=== HISTORIQUE DES PAIEMENTS ===");
            List<Paiement> paiements = agentService.obtenirHistoriquePaiements(agentId);

            if (paiements != null && !paiements.isEmpty()) {
                System.out.println("Nombre total de paiements : " + paiements.size());
                System.out.println("\nDétail des paiements :");
                System.out.println("-------------------------------------------------------");

                for (Paiement paiement : paiements) {
                    System.out.println("ID : " + paiement.getId());
                    System.out.println("Type : " + paiement.getTypePaiement());
                    System.out.println("Montant : " + paiement.getMontant() + " €");
                    System.out.println("Date : " + paiement.getDatePaiement());
                    System.out.println("-------------------------------------------------------");
                }
            } else {
                System.out.println("Aucun paiement trouvé pour cet agent.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation de l'historique : " + e.getMessage());
        }
    }

    public void filtrerEtTrierPaiements(int agentId) {
        try {
            System.out.println("\n=== FILTRAGE ET TRI DES PAIEMENTS ===");
            System.out.println("1. Filtrer par type de paiement");
            System.out.println("2. Trier par montant (croissant)");
            System.out.println("3. Trier par montant (décroissant)");
            System.out.println("4. Trier par date (plus récent)");
            System.out.println("5. Trier par date (plus ancien)");
            System.out.print("Choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine();

            List<Paiement> paiements = null;

            switch (choix) {
                case 1:
                    System.out.println("Types de paiement disponibles :");
                    for (TypePaiement type : TypePaiement.values()) {
                        System.out.println("- " + type);
                    }
                    System.out.print("Entrez le type de paiement : ");
                    String typeStr = scanner.nextLine().toUpperCase();
                    try {
                        TypePaiement typePaiement = TypePaiement.valueOf(typeStr);
                        paiements = agentService.filtrerPaiementsParType(agentId, typePaiement);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Type de paiement invalide.");
                        return;
                    }
                    break;
                case 2:
                    paiements = agentService.trierPaiementsParMontant(agentId, true);
                    break;
                case 3:
                    paiements = agentService.trierPaiementsParMontant(agentId, false);
                    break;
                case 4:
                    paiements = agentService.trierPaiementsParDate(agentId, true);
                    break;
                case 5:
                    paiements = agentService.trierPaiementsParDate(agentId, false);
                    break;
                default:
                    System.out.println("Choix invalide.");
                    return;
            }

            if (paiements != null && !paiements.isEmpty()) {
                System.out.println("\nRésultats (" + paiements.size() + " paiement(s)) :");
                System.out.println("-------------------------------------------------------");
                for (Paiement paiement : paiements) {
                    System.out.println("Type : " + paiement.getTypePaiement() +
                            " | Montant : " + paiement.getMontant() + " €" +
                            " | Date : " + paiement.getDatePaiement());
                }
            } else {
                System.out.println("Aucun paiement trouvé pour les critères sélectionnés.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du filtrage/tri : " + e.getMessage());
        }
    }

    public void calculerTotalPaiements(int agentId) {
        try {
            System.out.println("\n=== CALCUL DU TOTAL DES PAIEMENTS ===");
            double total = agentService.calculerTotalPaiements(agentId);
            System.out.println("Total de tous vos paiements : " + total + " €");
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul du total : " + e.getMessage());
        }
    }

    public void consulterSalaireAnnuel(int agentId) {
        try {
            System.out.println("\n=== CONSULTATION DU SALAIRE ANNUEL ===");
            System.out.print("Entrez l'année (ex: 2023) : ");
            int annee = scanner.nextInt();

            double salaireAnnuel = agentService.calculerSalaireAnnuel(agentId, annee);
            System.out.println("Salaire total pour l'année " + annee + " : " + salaireAnnuel + " €");
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation du salaire annuel : " + e.getMessage());
        }
    }

    public void consulterNombrePrimesBonus(int agentId) {
        try {
            System.out.println("\n=== NOMBRE DE PRIMES ET BONUS ===");
            Map<TypePaiement, Integer> statistiques = agentService.compterPrimesBonus(agentId);

            if (statistiques != null && !statistiques.isEmpty()) {
                System.out.println("Répartition de vos paiements :");
                for (Map.Entry<TypePaiement, Integer> entry : statistiques.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue() + " paiement(s)");
                }
            } else {
                System.out.println("Aucune statistique disponible.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des primes/bonus : " + e.getMessage());
        }
    }
    public void consulterPaiementExtremes(int agentId) {
        try {
            System.out.println("\n=== PAIEMENTS EXTRÊMES ===");
            Map<String, Paiement> extremes = agentService.obtenirPaiementsExtremes(agentId);

            if (extremes != null && !extremes.isEmpty()) {
                Paiement plusEleve = extremes.get("max");
                Paiement plusFaible = extremes.get("min");

                if (plusEleve != null) {
                    System.out.println("Paiement le plus élevé :");
                    System.out.println("  Type : " + plusEleve.getTypePaiement());
                    System.out.println("  Montant : " + plusEleve.getMontant() + " €");
                    System.out.println("  Date : " + plusEleve.getDatePaiement());
                }

                if (plusFaible != null) {
                    System.out.println("\nPaiement le plus faible :");
                    System.out.println("  Type : " + plusFaible.getTypePaiement());
                    System.out.println("  Montant : " + plusFaible.getMontant() + " €");
                    System.out.println("  Date : " + plusFaible.getDatePaiement());
                }
            } else {
                System.out.println("Aucun paiement trouvé.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des paiements extrêmes : " + e.getMessage());
        }
    }

    public void afficherStatistiquesCompletes(int agentId) {
        try {
            System.out.println("\n=== STATISTIQUES COMPLÈTES ===");
            Map<String, Object> statistiques = agentService.genererStatistiquesCompletes(agentId);

            if (statistiques != null && !statistiques.isEmpty()) {
                System.out.println("Voici vos statistiques détaillées :");
                for (Map.Entry<String, Object> entry : statistiques.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
            } else {
                System.out.println("Aucune statistique disponible.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération des statistiques : " + e.getMessage());
        }
    }
}