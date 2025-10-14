package controller;

import model.Paiement;
import model.TypePaiement;
import service.IPaiementService;
import service.IAgentService;
import service.Iimpl.PaiementServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class PaiementController {
    private IPaiementService paiementService;
    private IAgentService agentService;
    private Scanner scanner;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PaiementController(IPaiementService paiementService, IAgentService agentService) {
        this.paiementService = paiementService;
        this.agentService = agentService;
        this.scanner = new Scanner(System.in);
    }

    public PaiementController(IAgentService agentService) {
        this.paiementService = new PaiementServiceImpl();
        this.agentService = agentService;
        this.scanner = new Scanner(System.in);
    }

    
    public void creerBonusDirectPourResponsable(int directeurId) {
        try {
            System.out.println("\n=== CRÉATION D'UN BONUS DIRECT ===");
            
            System.out.print("ID du responsable à qui accorder le bonus : ");
            int responsableId = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Montant du bonus : ");
            BigDecimal montant = scanner.nextBigDecimal();
            scanner.nextLine();
            
            System.out.print("Motif du bonus : ");
            String motif = scanner.nextLine();
            
            Paiement bonus = paiementService.creerBonusDirectParDirecteur(directeurId, responsableId, montant, motif);
            
            System.out.println("Bonus créé avec succès !");
            System.out.println("ID: " + bonus.getId());
            System.out.println("Montant: " + bonus.getMontant() + "DH");
            System.out.println("Bénéficiaire: " + bonus.getAgent().getNom() + " " + bonus.getAgent().getPrenom());
            System.out.println("Statut: Validé directement");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du bonus : " + e.getMessage());
        }
    }
    
    public void consulterTousLesPaiements() {
        try {
            System.out.println("\n=== CONSULTATION DE TOUS LES PAIEMENTS ===");
            
            List<Paiement> paiements = paiementService.obtenirTousLesPaiements();
            
            if (paiements.isEmpty()) {
                System.out.println("Aucun paiement trouvé.");
                return;
            }
            
            System.out.println("Total des paiements : " + paiements.size());
            System.out.println("\n" + "=".repeat(120));
            System.out.printf("%-5s %-20s %-15s %-12s %-15s %-8s %-30s%n", 
                            "ID", "Agent", "Type", "Montant", "Date", "Validé", "Motif");
            System.out.println("=".repeat(120));
            
            for (Paiement p : paiements) {
                System.out.printf("%-5d %-20s %-15s %-12.2f %-15s %-8s %-30s%n",
                    p.getId(),
                    p.getAgent().getNom() + " " + p.getAgent().getPrenom(),
                    p.getTypePaiement(),
                    p.getMontant(),
                    p.getDatePaiement().format(formatter),
                    p.isConditionValidee() ? "Oui" : "Non",
                    p.getMotif() != null ? (p.getMotif().length() > 25 ? 
                        p.getMotif().substring(0, 25) + "..." : p.getMotif()) : ""
                );
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation : " + e.getMessage());
        }
    }
    
    public void genererRapportGlobal() {
        try {
            System.out.println("\n=== RAPPORT GLOBAL DES PAIEMENTS ===");
            
            List<Paiement> tousLesPaiements = paiementService.obtenirTousLesPaiements();
            
            if (tousLesPaiements.isEmpty()) {
                System.out.println("Aucun paiement à analyser.");
                return;
            }
            
            long totalPaiements = tousLesPaiements.size();
            long paiementsValides = tousLesPaiements.stream()
                    .mapToLong(p -> p.isConditionValidee() ? 1 : 0).sum();
            
            BigDecimal montantTotal = tousLesPaiements.stream()
                    .filter(Paiement::isConditionValidee)
                    .map(Paiement::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            

            System.out.println("STATISTIQUES GÉNÉRALES");
            System.out.println("Total des paiements : " + totalPaiements);
            System.out.println("Paiements validés : " + paiementsValides);
            System.out.println("Paiements en attente : " + (totalPaiements - paiementsValides));
            System.out.println("Montant total validé : " + montantTotal + "DH");
            
            System.out.println("RÉPARTITION PAR TYPE");
            for (TypePaiement type : TypePaiement.values()) {
                long count = tousLesPaiements.stream()
                        .filter(p -> p.getTypePaiement() == type && p.isConditionValidee())
                        .count();
                BigDecimal montantType = tousLesPaiements.stream()
                        .filter(p -> p.getTypePaiement() == type && p.isConditionValidee())
                        .map(Paiement::getMontant)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                System.out.printf("%-12s : %3d paiements - %10.2fDH%n", type, count, montantType);
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du rapport : " + e.getMessage());
        }
    }
        
    public void consulterMesPaiements(int agentId) {
        try {
            System.out.println("\n=== MES PAIEMENTS ===");
            
            List<Paiement> mesPaiements = paiementService.obtenirPaiementsParAgent(agentId);
            
            if (mesPaiements.isEmpty()) {
                System.out.println("Vous n'avez encore aucun paiement enregistré.");
                return;
            }
            
            System.out.println("Total de vos paiements : " + mesPaiements.size());
            System.out.println("\n" + "=".repeat(100));
            System.out.printf("%-5s %-15s %-12s %-15s %-8s %-30s%n", 
                            "ID", "Type", "Montant", "Date", "Validé", "Motif");
            System.out.println("=".repeat(100));
            
            BigDecimal totalValide = BigDecimal.ZERO;
            
            for (Paiement p : mesPaiements) {
                System.out.printf("%-5d %-15s %-12.2f %-15s %-8s %-30s%n",
                    p.getId(),
                    p.getTypePaiement(),
                    p.getMontant(),
                    p.getDatePaiement().format(formatter),
                    p.isConditionValidee() ? "Oui" : "Non",
                    p.getMotif() != null ? (p.getMotif().length() > 25 ? 
                        p.getMotif().substring(0, 25) + "..." : p.getMotif()) : ""
                );
                
                if (p.isConditionValidee()) {
                    totalValide = totalValide.add(p.getMontant());
                }
            }
            
            System.out.println("=".repeat(100));
            System.out.println("Total des paiements validés : " + totalValide + "DH");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation : " + e.getMessage());
        }
    }
    
    //menu agent choix 3
    public void consulterMesPaiementsParType(int agentId) {
        try {
            System.out.println("\n=== MES PAIEMENTS PAR TYPE ===");
            
            System.out.println("Sélectionnez un type de paiement :");
            System.out.println("1. SALAIRE");
            System.out.println("2. PRIME");
            System.out.println("3. BONUS");
            System.out.println("4. INDEMNITE");
            System.out.print("Votre choix : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine();
            
            TypePaiement typeSelectionne;
            switch (choix) {
                case 1: typeSelectionne = TypePaiement.SALAIRE; break;
                case 2: typeSelectionne = TypePaiement.PRIME; break;
                case 3: typeSelectionne = TypePaiement.BONUS; break;
                case 4: typeSelectionne = TypePaiement.INDEMNITE; break;
                default:
                    System.out.println("Choix invalide.");
                    return;
            }
            
            List<Paiement> paiementsType = paiementService.obtenirHistoriquePaiementsParType(agentId, typeSelectionne);
            
            if (paiementsType.isEmpty()) {
                System.out.println("Aucun paiement de type " + typeSelectionne + " trouvé.");
                return;
            }
            
            System.out.println("Historique des " + typeSelectionne + " (" + paiementsType.size() + " paiements)");
            System.out.println("=".repeat(80));
            System.out.printf("%-5s %-12s %-15s %-8s %-30s%n", 
                            "ID", "Montant", "Date", "Validé", "Motif");
            System.out.println("=".repeat(80));
            
            BigDecimal totalType = BigDecimal.ZERO;
            
            for (Paiement p : paiementsType) {
                System.out.printf("%-5d %-12.2f %-15s %-8s %-30s%n",
                    p.getId(),
                    p.getMontant(),
                    p.getDatePaiement().format(formatter),
                    p.isConditionValidee() ? "Oui" : "Non",
                    p.getMotif() != null ? (p.getMotif().length() > 25 ? 
                        p.getMotif().substring(0, 25) + "..." : p.getMotif()) : ""
                );
                
                if (p.isConditionValidee()) {
                    totalType = totalType.add(p.getMontant());
                }
            }
            
            System.out.println("=".repeat(80));
            System.out.println("Total validé pour " + typeSelectionne + " : " + totalType + "DH");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation : " + e.getMessage());
        }
    }
    
    //menu agent choix 4
    public void calculerSalairePeriode(int agentId) {
        try {
            System.out.println("\n=== CALCUL SALAIRE SUR PÉRIODE ===");
            
            System.out.print("Date de début (dd/MM/yyyy) : ");
            String dateDebutStr = scanner.nextLine();
            
            System.out.print("Date de fin (dd/MM/yyyy) : ");
            String dateFinStr = scanner.nextLine();
            
            LocalDate dateDebut = LocalDate.parse(dateDebutStr, formatter);
            LocalDate dateFin = LocalDate.parse(dateFinStr, formatter);
            
            BigDecimal salaireTotal = paiementService.calculerSalaireTotalPeriode(agentId, dateDebut, dateFin);
            
            System.out.println("\nRÉSULTAT DU CALCUL");
            System.out.println("Période : " + dateDebut.format(formatter) + " au " + dateFin.format(formatter));
            System.out.println("Salaire total (validé) : " + salaireTotal + "DH");
            
            System.out.println("DÉTAIL PAR TYPE :");
            for (TypePaiement type : TypePaiement.values()) {
                List<Paiement> paiementsPeriode = paiementService.obtenirPaiementsParAgent(agentId).stream()
                        .filter(p -> p.getTypePaiement() == type &&
                                   p.getDatePaiement() != null &&
                                   !p.getDatePaiement().isBefore(dateDebut) &&
                                   !p.getDatePaiement().isAfter(dateFin) &&
                                   p.isConditionValidee())
                        .toList();
                
                BigDecimal montantType = paiementsPeriode.stream()
                        .map(Paiement::getMontant)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                if (montantType.compareTo(BigDecimal.ZERO) > 0) {
                    System.out.printf("  %-12s : %8.2fDH (%d paiement(s))%n", 
                                    type, montantType, paiementsPeriode.size());
                }
            }
            
        } catch (DateTimeParseException e) {
            System.err.println("Format de date invalide. Utilisez le format dd/MM/yyyy");
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul : " + e.getMessage());
        }
    }
    
    //menu agent choix 4
    public void trierMesPaiements(int agentId) {
        try {
            System.out.println("\n=== TRIER MES PAIEMENTS ===");
            
            System.out.println("Critère de tri :");
            System.out.println("1. Par date");
            System.out.println("2. Par montant");
            System.out.println("3. Par type");
            System.out.print("Votre choix : ");
            
            int choixCritere = scanner.nextInt();
            scanner.nextLine();
            
            String critere;
            switch (choixCritere) {
                case 1: critere = "date"; break;
                case 2: critere = "montant"; break;
                case 3: critere = "type"; break;
                default:
                    System.out.println("Choix invalide.");
                    return;
            }
            
            System.out.println("Ordre de tri :");
            System.out.println("1. Croissant (du plus petit au plus grand)");
            System.out.println("2. Décroissant (du plus grand au plus petit)");
            System.out.print("Votre choix : ");
            
            int choixOrdre = scanner.nextInt();
            scanner.nextLine();
            
            boolean ascendant = choixOrdre == 1;
            
            List<Paiement> paiementsTries = paiementService.trierPaiements(agentId, critere, ascendant);
            
            if (paiementsTries.isEmpty()) {
                System.out.println("Aucun paiement à trier.");
                return;
            }
            
            System.out.println("PAIEMENTS TRIÉS PAR " + critere.toUpperCase() + 
                             " (" + (ascendant ? "CROISSANT" : "DÉCROISSANT") + ")");
            System.out.println("=".repeat(100));
            System.out.printf("%-5s %-15s %-12s %-15s %-8s %-30s%n", 
                            "ID", "Type", "Montant", "Date", "Validé", "Motif");
            System.out.println("=".repeat(100));
            
            for (Paiement p : paiementsTries) {
                System.out.printf("%-5d %-15s %-12.2f %-15s %-8s %-30s%n",
                    p.getId(),
                    p.getTypePaiement(),
                    p.getMontant(),
                    p.getDatePaiement().format(formatter),
                    p.isConditionValidee() ? "✅" : "⏳",
                    p.getMotif() != null ? (p.getMotif().length() > 25 ? 
                        p.getMotif().substring(0, 25) + "..." : p.getMotif()) : ""
                );
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors du tri : " + e.getMessage());
        }
    }
    
    public void ajouterSalaire() {
        try {
            System.out.println("\n=== AJOUT DE SALAIRE ===");
            System.out.print("ID de l'agent : ");
            int agentId = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Montant du salaire : ");
            BigDecimal montant = scanner.nextBigDecimal();
            scanner.nextLine();
            
            System.out.print("Motif : ");
            String motif = scanner.nextLine();
            
            Paiement salaire = paiementService.traiterPaiement(agentId, TypePaiement.SALAIRE, montant, motif);
            if (salaire != null) {
                System.out.println("Salaire ajouté avec succès (ID: " + salaire.getId() + ")");
            } else {
                System.err.println("Erreur lors de l'ajout du salaire");
            }
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            scanner.nextLine();
        }
    }
    
    public void ajouterPrime() {
        try {
            System.out.println("\n=== AJOUT DE PRIME ===");
            System.out.print("ID de l'agent : ");
            int agentId = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Montant de la prime : ");
            BigDecimal montant = scanner.nextBigDecimal();
            scanner.nextLine();
            
            System.out.print("Motif : ");
            String motif = scanner.nextLine();
            
            Paiement prime = paiementService.traiterPaiement(agentId, TypePaiement.PRIME, montant, motif);
            if (prime != null) {
                System.out.println("Prime ajoutée avec succès (ID: " + prime.getId() + ")");
            } else {
                System.err.println("Erreur lors de l'ajout de la prime");
            }
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            scanner.nextLine();
        }
    }
    
    //menu responsable choix 8
    public void consulterPaiementsAgent() {
        try {
            System.out.println("\n=== CONSULTATION DES PAIEMENTS D'UN AGENT ===");
            System.out.print("ID de l'agent : ");
            int agentId = scanner.nextInt();
            scanner.nextLine();
            
            List<Paiement> paiements = paiementService.obtenirPaiementsParAgent(agentId);
            if (paiements.isEmpty()) {
                System.out.println("Aucun paiement trouvé pour cet agent.");
                return;
            }
            
            System.out.printf("%-5s %-15s %-12s %-15s %-8s %-30s%n",
                            "ID", "Type", "Montant", "Date", "Validé", "Motif");
            System.out.println("=".repeat(100));
            
            for (Paiement p : paiements) {
                System.out.printf("%-5d %-15s %-12.2f %-15s %-8s %-30s%n",
                    p.getId(),
                    p.getTypePaiement(),
                    p.getMontant(),
                    p.getDatePaiement().format(formatter),
                    p.isConditionValidee() ? "✅" : "⏳",
                    p.getMotif() != null ? (p.getMotif().length() > 25 ? 
                        p.getMotif().substring(0, 25) + "..." : p.getMotif()) : ""
                );
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation : " + e.getMessage());
        }
    }

    public void effectuerAuditPaiements() {
        try {            
            List<Paiement> tousPaiements = paiementService.obtenirTousLesPaiements();
            if (tousPaiements.isEmpty()) {
                System.out.println("Aucun paiement à auditer.");
                return;
            }
            
            int totalPaiements = tousPaiements.size();
            int paiementsValides = 0;
            int paiementsEnAttente = 0;
            BigDecimal montantTotal = BigDecimal.ZERO;
            BigDecimal montantValide = BigDecimal.ZERO;
            for (Paiement p : tousPaiements) {
                montantTotal = montantTotal.add(p.getMontant());
                
                if (p.isConditionValidee()) {
                    paiementsValides++;
                    montantValide = montantValide.add(p.getMontant());
                } else {
                    paiementsEnAttente++;
                }
            }
            
            System.out.println("RÉSULTATS DE L'AUDIT");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Total des paiements : " + totalPaiements);
            System.out.println("Paiements validés : " + paiementsValides );
            System.out.println("Paiements en attente : " + paiementsEnAttente);
            System.out.println("Taux de validation : " + String.format("%.2f%%", 
                (double) paiementsValides / totalPaiements * 100));
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Montant total des paiements : " + montantTotal + " DH");
            System.out.println("Montant validé : " + montantValide + " DH");
            System.out.println("Montant en attente : " + montantTotal.subtract(montantValide) + " DH");
            
            if (paiementsEnAttente > 0) {
                System.out.println("ATTENTION : Des paiements nécessitent une validation !");
                System.out.println("Veuillez traiter les paiements en attente.");
            } else {
                System.out.println("Tous les paiements sont validés !");
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'audit : " + e.getMessage());
        }
    }
}