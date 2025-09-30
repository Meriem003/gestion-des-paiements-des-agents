package service.Iimpl;
import dao.Iimpl.AgentDao;
import dao.Iimpl.PaiementDao;
import dao.Iimpl.DepartementDao;
import service.IAgentService;
import model.Agent;
import model.Departement;
import model.Paiement;
import model.TypePaiement;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class AgentServiceImpl implements IAgentService {
    private AgentDao agentDao;
    private PaiementDao paiementDao;
    private DepartementDao departementDao;

    public AgentServiceImpl(AgentDao agentDao, PaiementDao paiementDao, DepartementDao departementDao) {
        this.agentDao = agentDao;
        this.paiementDao = paiementDao;
        this.departementDao = departementDao;
    }

    @Override
    public Agent obtenirInformationsAgent(int agentId) {
        try {
            if (agentId <= 0) {
                System.err.println("ID d'agent invalide: " + agentId);
                return null;
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Agent introuvable avec l'ID: " + agentId);
                return null;
            }
            System.out.println("=== INFORMATIONS DE L'AGENT ===");
            System.out.println("ID: " + agent.getId());
            System.out.println("Nom: " + agent.getNom());
            System.out.println("Prénom: " + agent.getPrenom());
            System.out.println("Email: " + agent.getEmail());
            System.out.println("Type: " + agent.getTypeAgent());
            if (agent.getDepartement() != null) {
                System.out.println("Département: " + agent.getDepartement().getNom() + " (ID: " + agent.getDepartement().getId() + ")");
            } else {
                System.out.println("Département: Non assigné");
            }
            System.out.println("Responsable de département: " + (agent.isEstResponsableDepartement() ? "Oui" : "Non"));
            return agent;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des informations de l'agent: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Agent mettreAJourInformationsAgent(Agent agent) {
        try {
            if (agent == null) {
                System.err.println("Agent ne peut pas être null");
                return null;
            }
            if (agent.getId() <= 0) {
                System.err.println("ID d'agent invalide: " + agent.getId());
                return null;
            }
            Agent agentExistant = agentDao.lireParId(agent.getId());
            if (agentExistant == null) {
                System.err.println("Agent introuvable avec l'ID: " + agent.getId());
                return null;
            }
            if (agent.getNom() == null || agent.getNom().trim().isEmpty()) {
                System.err.println("Le nom de l'agent est obligatoire");
                return null;
            }
            if (agent.getPrenom() == null || agent.getPrenom().trim().isEmpty()) {
                System.err.println("Le prénom de l'agent est obligatoire");
                return null;
            }
            if (agent.getEmail() == null || agent.getEmail().trim().isEmpty()) {
                System.err.println("L'email de l'agent est obligatoire");
                return null;
            }
            agentDao.mettreAJour(agent);
            System.out.println("Informations de l'agent '" + agent.getPrenom() + " " + agent.getNom() + "' mises à jour avec succès");
            return agentDao.lireParId(agent.getId());
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des informations de l'agent: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Departement obtenirDepartementAgent(int agentId) {
        try {
            if (agentId <= 0) {
                System.err.println("ID d'agent invalide: " + agentId);
                return null;
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Agent introuvable avec l'ID: " + agentId);
                return null;
            }
            Departement departement = agent.getDepartement();
            if (departement == null) {
                System.out.println("=== DÉPARTEMENT DE L'AGENT ===");
                System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom());
                System.out.println("Département: Non assigné à un département");
                return null;
            }
            Departement departementComplet = departementDao.lireParId(departement.getId());
            if (departementComplet != null) {
                System.out.println("=== DÉPARTEMENT DE L'AGENT ===");
                System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom());
                System.out.println("Département: " + departementComplet.getNom() + " (ID: " + departementComplet.getId() + ")");

                if (departementComplet.getResponsable() != null) {
                    System.out.println("Responsable du département: " +
                            departementComplet.getResponsable().getPrenom() + " " +
                            departementComplet.getResponsable().getNom());
                } else {
                    System.out.println("Responsable du département: Aucun responsable assigné");
                }
                if (departementComplet.getAgents() != null) {
                    System.out.println("Nombre total d'agents dans le département: " + departementComplet.getAgents().size());
                } else {
                    System.out.println("Nombre total d'agents dans le département: 0");
                }
                return departementComplet;
            }
            return departement;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du département de l'agent: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Paiement> obtenirHistoriquePaiements(int agentId) {
        try {
            if (agentId <= 0) {
                System.err.println("ID d'agent invalide: " + agentId);
                return null;
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Agent introuvable avec l'ID: " + agentId);
                return null;
            }
            List<Paiement> paiements = paiementDao.findPaiementsByAgentId(agentId);

            if (paiements == null || paiements.isEmpty()) {
                System.out.println("=== HISTORIQUE DES PAIEMENTS ===");
                System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom());
                System.out.println("Aucun paiement trouvé pour cet agent");
                return paiements;
            }
            System.out.println("=== HISTORIQUE DES PAIEMENTS ===");
            System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom() + " (ID: " + agentId + ")");
            System.out.println("Nombre total de paiements: " + paiements.size());
            System.out.println();

            for (int i = 0; i < paiements.size(); i++) {
                Paiement paiement = paiements.get(i);
                System.out.println("--- Paiement " + (i + 1) + " ---");
                System.out.println("ID: " + paiement.getId());
                System.out.println("Type: " + paiement.getTypePaiement());
                System.out.println("Montant: " + paiement.getMontant() + " €");
                System.out.println("Date: " + paiement.getDatePaiement());
                System.out.println("Motif: " + (paiement.getMotif() != null ? paiement.getMotif() : "Non spécifié"));
                System.out.println("Condition validée: " + (paiement.isConditionValidee() ? "Oui" : "Non"));
                System.out.println();
            }

            return paiements;

        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de l'historique des paiements: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Paiement> filtrerPaiementsParType(int agentId, TypePaiement typePaiement) {
        try {
            if (agentId <= 0) {
                System.err.println("ID d'agent invalide: " + agentId);
                return null;
            }
            if (typePaiement == null) {
                System.err.println("Type de paiement ne peut pas être null");
                return null;
            }

            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Agent introuvable avec l'ID: " + agentId);
                return null;
            }

            List<Paiement> tousLesPaiements = paiementDao.findPaiementsByAgentId(agentId);
            if (tousLesPaiements == null || tousLesPaiements.isEmpty()) {
                System.out.println("=== FILTRAGE DES PAIEMENTS PAR TYPE ===");
                System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom());
                System.out.println("Type recherché: " + typePaiement);
                System.out.println("Aucun paiement trouvé pour cet agent");
                return tousLesPaiements;
            }

            List<Paiement> paiementsFiltres = tousLesPaiements.stream()
                    .filter(paiement -> paiement.getTypePaiement() == typePaiement)
                    .collect(Collectors.toList());

            System.out.println("=== FILTRAGE DES PAIEMENTS PAR TYPE ===");
            System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom() + " (ID: " + agentId + ")");
            System.out.println("Type recherché: " + typePaiement);
            System.out.println("Nombre total de paiements: " + tousLesPaiements.size());
            System.out.println("Nombre de paiements du type " + typePaiement + ": " + paiementsFiltres.size());
            System.out.println();

            for (int i = 0; i < paiementsFiltres.size(); i++) {
                Paiement paiement = paiementsFiltres.get(i);
                System.out.println("--- Paiement " + (i + 1) + " ---");
                System.out.println("ID: " + paiement.getId());
                System.out.println("Type: " + paiement.getTypePaiement());
                System.out.println("Montant: " + paiement.getMontant() + " €");
                System.out.println("Date: " + paiement.getDatePaiement());
                System.out.println("Motif: " + (paiement.getMotif() != null ? paiement.getMotif() : "Non spécifié"));
                System.out.println("Condition validée: " + (paiement.isConditionValidee() ? "Oui" : "Non"));
                System.out.println();
            }

            return paiementsFiltres;

        } catch (Exception e) {
            System.err.println("Erreur lors du filtrage des paiements par type: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Paiement> trierPaiementsParMontant(int agentId, boolean croissant) {
        try {
            if (agentId <= 0) {
                System.err.println("ID d'agent invalide: " + agentId);
                return null;
            }

            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Agent introuvable avec l'ID: " + agentId);
                return null;
            }

            List<Paiement> paiements = paiementDao.findPaiementsByAgentId(agentId);
            if (paiements == null || paiements.isEmpty()) {
                System.out.println("=== TRI DES PAIEMENTS PAR MONTANT ===");
                System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom());
                System.out.println("Ordre: " + (croissant ? "Croissant" : "Décroissant"));
                System.out.println("Aucun paiement trouvé pour cet agent");
                return paiements;
            }

            List<Paiement> paiementsTries = paiements.stream()
                    .sorted(croissant ? 
                            Comparator.comparing(Paiement::getMontant) : 
                            Comparator.comparing(Paiement::getMontant).reversed())
                    .collect(Collectors.toList());

            System.out.println("=== TRI DES PAIEMENTS PAR MONTANT ===");
            System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom() + " (ID: " + agentId + ")");
            System.out.println("Ordre: " + (croissant ? "Croissant (plus petit au plus grand)" : "Décroissant (plus grand au plus petit)"));
            System.out.println("Nombre total de paiements: " + paiementsTries.size());
            System.out.println();

            for (int i = 0; i < paiementsTries.size(); i++) {
                Paiement paiement = paiementsTries.get(i);
                System.out.println("--- Paiement " + (i + 1) + " ---");
                System.out.println("ID: " + paiement.getId());
                System.out.println("Type: " + paiement.getTypePaiement());
                System.out.println("Montant: " + paiement.getMontant() + " €");
                System.out.println("Date: " + paiement.getDatePaiement());
                System.out.println("Motif: " + (paiement.getMotif() != null ? paiement.getMotif() : "Non spécifié"));
                System.out.println("Condition validée: " + (paiement.isConditionValidee() ? "Oui" : "Non"));
                System.out.println();
            }

            return paiementsTries;

        } catch (Exception e) {
            System.err.println("Erreur lors du tri des paiements par montant: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Paiement> trierPaiementsParDate(int agentId, boolean plusRecent) {
        try {
            if (agentId <= 0) {
                System.err.println("ID d'agent invalide: " + agentId);
                return null;
            }

            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Agent introuvable avec l'ID: " + agentId);
                return null;
            }

            List<Paiement> paiements = paiementDao.findPaiementsByAgentId(agentId);
            if (paiements == null || paiements.isEmpty()) {
                System.out.println("=== TRI DES PAIEMENTS PAR DATE ===");
                System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom());
                System.out.println("Ordre: " + (plusRecent ? "Plus récent en premier" : "Plus ancien en premier"));
                System.out.println("Aucun paiement trouvé pour cet agent");
                return paiements;
            }

            List<Paiement> paiementsTries = paiements.stream()
                    .sorted(plusRecent ? 
                            Comparator.comparing(Paiement::getDatePaiement).reversed() : 
                            Comparator.comparing(Paiement::getDatePaiement))
                    .collect(Collectors.toList());

            System.out.println("=== TRI DES PAIEMENTS PAR DATE ===");
            System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom() + " (ID: " + agentId + ")");
            System.out.println("Ordre: " + (plusRecent ? "Plus récent en premier" : "Plus ancien en premier"));
            System.out.println("Nombre total de paiements: " + paiementsTries.size());
            System.out.println();

            for (int i = 0; i < paiementsTries.size(); i++) {
                Paiement paiement = paiementsTries.get(i);
                System.out.println("--- Paiement " + (i + 1) + " ---");
                System.out.println("ID: " + paiement.getId());
                System.out.println("Type: " + paiement.getTypePaiement());
                System.out.println("Montant: " + paiement.getMontant() + " €");
                System.out.println("Date: " + paiement.getDatePaiement());
                System.out.println("Motif: " + (paiement.getMotif() != null ? paiement.getMotif() : "Non spécifié"));
                System.out.println("Condition validée: " + (paiement.isConditionValidee() ? "Oui" : "Non"));
                System.out.println();
            }

            return paiementsTries;

        } catch (Exception e) {
            System.err.println("Erreur lors du tri des paiements par date: " + e.getMessage());
            return null;
        }
    }

    @Override
    public double calculerTotalPaiements(int agentId) {
        try {
            if (agentId <= 0) {
                System.err.println("ID d'agent invalide: " + agentId);
                return 0.0;
            }

            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Agent introuvable avec l'ID: " + agentId);
                return 0.0;
            }

            List<Paiement> paiements = paiementDao.findPaiementsByAgentId(agentId);
            if (paiements == null || paiements.isEmpty()) {
                System.out.println("=== CALCUL DU TOTAL DES PAIEMENTS ===");
                System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom());
                System.out.println("Aucun paiement trouvé pour cet agent");
                System.out.println("Total des paiements: 0.00 €");
                return 0.0;
            }

            double total = paiements.stream()
                    .filter(paiement -> paiement.getMontant() != null)
                    .mapToDouble(paiement -> paiement.getMontant().doubleValue())
                    .sum();

            System.out.println("=== CALCUL DU TOTAL DES PAIEMENTS ===");
            System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom() + " (ID: " + agentId + ")");
            System.out.println("Nombre total de paiements: " + paiements.size());
            System.out.println("Total des paiements: " + String.format("%.2f", total) + " €");
            
            System.out.println("\n--- Détail par type de paiement ---");
            for (TypePaiement type : TypePaiement.values()) {
                double totalParType = paiements.stream()
                        .filter(p -> p.getTypePaiement() == type && p.getMontant() != null)
                        .mapToDouble(p -> p.getMontant().doubleValue())
                        .sum();
                long nombreParType = paiements.stream()
                        .filter(p -> p.getTypePaiement() == type)
                        .count();
                
                if (nombreParType > 0) {
                    System.out.println(type + ": " + String.format("%.2f", totalParType) + " € (" + nombreParType + " paiement(s))");
                }
            }

            return total;

        } catch (Exception e) {
            System.err.println("Erreur lors du calcul du total des paiements: " + e.getMessage());
            return 0.0;
        }
    }

    @Override
    public double calculerSalaireAnnuel(int agentId, int annee) {
        try {
            if (agentId <= 0) {
                System.err.println("ID d'agent invalide: " + agentId);
                return 0.0;
            }
            if (annee < 1900 || annee > 2100) {
                System.err.println("Année invalide: " + annee);
                return 0.0;
            }

            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Agent introuvable avec l'ID: " + agentId);
                return 0.0;
            }

            List<Paiement> paiements = paiementDao.findPaiementsByAgentId(agentId);
            if (paiements == null || paiements.isEmpty()) {
                System.out.println("=== CALCUL DU SALAIRE ANNUEL ===");
                System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom());
                System.out.println("Année: " + annee);
                System.out.println("Aucun paiement trouvé pour cet agent");
                System.out.println("Salaire annuel: 0.00 €");
                return 0.0;
            }
            double salaireAnnuel = paiements.stream()
                    .filter(paiement -> paiement.getDatePaiement() != null && 
                                      paiement.getDatePaiement().getYear() == annee &&
                                      paiement.getTypePaiement() == TypePaiement.SALAIRE &&
                                      paiement.getMontant() != null)
                    .mapToDouble(paiement -> paiement.getMontant().doubleValue())
                    .sum();
            long nombreSalaires = paiements.stream()
                    .filter(paiement -> paiement.getDatePaiement() != null && 
                                      paiement.getDatePaiement().getYear() == annee &&
                                      paiement.getTypePaiement() == TypePaiement.SALAIRE)
                    .count();
            double totalAutresPaiements = paiements.stream()
                    .filter(paiement -> paiement.getDatePaiement() != null && 
                                      paiement.getDatePaiement().getYear() == annee &&
                                      paiement.getTypePaiement() != TypePaiement.SALAIRE &&
                                      paiement.getMontant() != null)
                    .mapToDouble(paiement -> paiement.getMontant().doubleValue())
                    .sum();

            System.out.println("=== CALCUL DU SALAIRE ANNUEL ===");
            System.out.println("Agent: " + agent.getPrenom() + " " + agent.getNom() + " (ID: " + agentId + ")");
            System.out.println("Année: " + annee);
            System.out.println("Nombre de paiements de salaire: " + nombreSalaires);
            System.out.println("Salaire annuel (SALAIRE uniquement): " + String.format("%.2f", salaireAnnuel) + " €");
            
            if (totalAutresPaiements > 0) {
                System.out.println("Autres paiements (PRIME, BONUS, INDEMNITE): " + String.format("%.2f", totalAutresPaiements) + " €");
                System.out.println("Total général pour " + annee + ": " + String.format("%.2f", salaireAnnuel + totalAutresPaiements) + " €");
            }

            return salaireAnnuel;

        } catch (Exception e) {
            System.err.println("Erreur lors du calcul du salaire annuel: " + e.getMessage());
            return 0.0;
        }
    }

    @Override
    public Map<TypePaiement, Integer> compterPrimesBonus(int agentId) {
        try {
            if (agentId <= 0) {
                System.err.println("ID d'agent invalide: " + agentId);
                return new HashMap<>();
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Agent introuvable avec l'ID: " + agentId);
                return new HashMap<>();
            }
            List<Paiement> paiements = paiementDao.findPaiementsByAgentId(agentId);
            Map<TypePaiement, Integer> compteurs = new HashMap<>();
            compteurs.put(TypePaiement.PRIME, 0);
            compteurs.put(TypePaiement.BONUS, 0);
            compteurs.put(TypePaiement.SALAIRE, 0);
            compteurs.put(TypePaiement.INDEMNITE, 0);
            for (Paiement paiement : paiements) {
                TypePaiement type = paiement.getTypePaiement();
                if (type != null) {
                    compteurs.put(type, compteurs.get(type) + 1);
                }
            }
            System.out.println("=== COMPTAGE DES PAIEMENTS POUR L'AGENT ID " + agentId + " ===");
            System.out.println("Salaires: " + compteurs.get(TypePaiement.SALAIRE));
            System.out.println("Primes: " + compteurs.get(TypePaiement.PRIME));
            System.out.println("Bonus: " + compteurs.get(TypePaiement.BONUS));
            System.out.println("Indemnités: " + compteurs.get(TypePaiement.INDEMNITE));
            System.out.println("Total paiements: " + paiements.size());
            return compteurs;
        } catch (Exception e) {
            System.err.println("Erreur lors du comptage des primes/bonus: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Paiement> obtenirPaiementsExtremes(int agentId) {
        try {
            if (agentId <= 0) {
                System.err.println("ID d'agent invalide: " + agentId);
                return new HashMap<>();
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Agent introuvable avec l'ID: " + agentId);
                return new HashMap<>();
            }
            List<Paiement> paiements = paiementDao.findPaiementsByAgentId(agentId);
            if (paiements.isEmpty()) {
                System.out.println("Aucun paiement trouvé pour l'agent ID " + agentId);
                return new HashMap<>();
            }
            Map<String, Paiement> extremes = new HashMap<>();
            Paiement paiementMax = paiements.stream()
                .max(Comparator.comparing(p -> p.getMontant()))
                .orElse(null);
            Paiement paiementMin = paiements.stream()
                .min(Comparator.comparing(p -> p.getMontant()))
                .orElse(null);
            Paiement paiementRecent = paiements.stream()
                .max(Comparator.comparing(p -> p.getDatePaiement()))
                .orElse(null);
            Paiement paiementAncien = paiements.stream()
                .min(Comparator.comparing(p -> p.getDatePaiement()))
                .orElse(null);
            if (paiementMax != null) {
                extremes.put("montantMax", paiementMax);
            }
            if (paiementMin != null) {
                extremes.put("montantMin", paiementMin);
            }
            if (paiementRecent != null) {
                extremes.put("plusRecent", paiementRecent);
            }
            if (paiementAncien != null) {
                extremes.put("plusAncien", paiementAncien);
            }
            System.out.println("=== PAIEMENTS EXTRÊMES POUR L'AGENT ID " + agentId + " ===");
            if (paiementMax != null) {
                System.out.println("Montant maximum: " + paiementMax.getMontant() + " € (" + 
                                 paiementMax.getTypePaiement() + " du " + paiementMax.getDatePaiement() + ")");
            }
            if (paiementMin != null) {
                System.out.println("Montant minimum: " + paiementMin.getMontant() + " € (" + 
                                 paiementMin.getTypePaiement() + " du " + paiementMin.getDatePaiement() + ")");
            }
            if (paiementRecent != null) {
                System.out.println("Plus récent: " + paiementRecent.getMontant() + " € (" + 
                                 paiementRecent.getTypePaiement() + " du " + paiementRecent.getDatePaiement() + ")");
            }
            if (paiementAncien != null) {
                System.out.println("Plus ancien: " + paiementAncien.getMontant() + " € (" + 
                                 paiementAncien.getTypePaiement() + " du " + paiementAncien.getDatePaiement() + ")");
            }
            return extremes;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'obtention des paiements extrêmes: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> genererStatistiquesCompletes(int agentId) {
        try {
            if (agentId <= 0) {
                System.err.println("ID d'agent invalide: " + agentId);
                return new HashMap<>();
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Agent introuvable avec l'ID: " + agentId);
                return new HashMap<>();
            }
            Map<String, Object> statistiques = new HashMap<>();
            Map<String, Object> infoAgent = new HashMap<>();
            infoAgent.put("id", agent.getId());
            infoAgent.put("nom", agent.getNom());
            infoAgent.put("prenom", agent.getPrenom());
            infoAgent.put("email", agent.getEmail());
            infoAgent.put("typeAgent", agent.getTypeAgent().toString());
            infoAgent.put("estResponsableDepartement", agent.isEstResponsableDepartement());
            
            if (agent.getDepartement() != null) {
                infoAgent.put("departementId", agent.getDepartement().getId());
                infoAgent.put("departementNom", agent.getDepartement().getNom());
            } else {
                infoAgent.put("departementId", null);
                infoAgent.put("departementNom", "Non assigné");
            }
            statistiques.put("agent", infoAgent);
            List<Paiement> paiements = paiementDao.findPaiementsByAgentId(agentId);
            statistiques.put("nombreTotalPaiements", paiements.size());
            if (paiements.isEmpty()) {
                statistiques.put("message", "Aucun paiement trouvé pour cet agent");
                return statistiques;
            }
            double totalPaiements = calculerTotalPaiements(agentId);
            statistiques.put("totalPaiements", totalPaiements);
            int anneeActuelle = LocalDate.now().getYear();
            double salaireAnnuel = calculerSalaireAnnuel(agentId, anneeActuelle);
            statistiques.put("salaireAnnuel" + anneeActuelle, salaireAnnuel);
            Map<String, Paiement> extremes = obtenirPaiementsExtremes(agentId);
            Map<String, Object> extremesSimples = new HashMap<>();
            for (Map.Entry<String, Paiement> entry : extremes.entrySet()) {
                Paiement p = entry.getValue();
                Map<String, Object> paiementInfo = new HashMap<>();
                paiementInfo.put("id", p.getId());
                paiementInfo.put("montant", p.getMontant());
                paiementInfo.put("type", p.getTypePaiement().toString());
                paiementInfo.put("date", p.getDatePaiement());
                paiementInfo.put("motif", p.getMotif());
                paiementInfo.put("valide", p.isConditionValidee());
                extremesSimples.put(entry.getKey(), paiementInfo);
            }
            statistiques.put("paiementsExtremes", extremesSimples);
            double moyenneMontants = paiements.stream()
                .mapToDouble(p -> p.getMontant().doubleValue())
                .average()
                .orElse(0.0);
            statistiques.put("moyenneMontants", moyenneMontants);
            List<Double> montantsTries = paiements.stream()
                .map(p -> p.getMontant().doubleValue())
                .sorted()
                .collect(Collectors.toList());
            double mediane = 0.0;
            int taille = montantsTries.size();
            if (taille > 0) {
                if (taille % 2 == 0) {
                    mediane = (montantsTries.get(taille/2 - 1) + montantsTries.get(taille/2)) / 2.0;
                } else {
                    mediane = montantsTries.get(taille/2);
                }
            }
            statistiques.put("medianeMontants", mediane);
            LocalDate premierPaiement = paiements.stream()
                .map(Paiement::getDatePaiement)
                .min(LocalDate::compareTo)
                .orElse(null);
            
            LocalDate dernierPaiement = paiements.stream()
                .map(Paiement::getDatePaiement)
                .max(LocalDate::compareTo)
                .orElse(null);

            statistiques.put("premierPaiementDate", premierPaiement);
            statistiques.put("dernierPaiementDate", dernierPaiement);

            long paiementsValides = paiements.stream()
                .mapToLong(p -> p.isConditionValidee() ? 1 : 0)
                .sum();
            
            long paiementsEnAttente = paiements.size() - paiementsValides;
            
            Map<String, Long> repartitionValidation = new HashMap<>();
            repartitionValidation.put("valides", paiementsValides);
            repartitionValidation.put("enAttente", paiementsEnAttente);
            statistiques.put("repartitionValidation", repartitionValidation);

            Map<String, Double> totauxParType = new HashMap<>();
            for (TypePaiement type : TypePaiement.values()) {
                double total = paiements.stream()
                    .filter(p -> p.getTypePaiement() == type)
                    .mapToDouble(p -> p.getMontant().doubleValue())
                    .sum();
                totauxParType.put(type.toString(), total);
            }
            statistiques.put("totauxParType", totauxParType);
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("dateGeneration", LocalDate.now());
            metadata.put("nombreIndicateurs", statistiques.size());
            metadata.put("periodeAnalysee", premierPaiement + " à " + dernierPaiement);
            statistiques.put("metadata", metadata);

            System.out.println("=== STATISTIQUES COMPLÈTES POUR " + agent.getPrenom() + " " + agent.getNom() + " ===");
            System.out.println("Total paiements: " + paiements.size());
            System.out.println("Montant total: " + String.format("%.2f", totalPaiements) + " €");
            System.out.println("Moyenne: " + String.format("%.2f", moyenneMontants) + " €");
            System.out.println("Médiane: " + String.format("%.2f", mediane) + " €");
            System.out.println("Période: " + premierPaiement + " à " + dernierPaiement);
            System.out.println("Paiements validés: " + paiementsValides + "/" + paiements.size());

            return statistiques;

        } catch (Exception e) {
            System.err.println("Erreur lors de la génération des statistiques complètes: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}