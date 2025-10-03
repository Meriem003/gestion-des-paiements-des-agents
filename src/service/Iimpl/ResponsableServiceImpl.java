package service.Iimpl;

import dao.Iimpl.AgentDao;
import dao.Iimpl.PaiementDao;
import dao.Iimpl.DepartementDao;
import service.IResponsableService;
import model.Agent;
import model.Departement;
import model.Paiement;
import model.TypeAgent;
import model.TypePaiement;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import java.math.RoundingMode;
import service.IPaiementService;

public class ResponsableServiceImpl extends AgentServiceImpl implements IResponsableService {
    private AgentDao agentDao;
    private PaiementDao paiementDao;
    private DepartementDao departementDao;
    private IPaiementService paiementService; // Injection du service de paiement

    public ResponsableServiceImpl(AgentDao agentDao, PaiementDao paiementDao, DepartementDao departementDao) {
        super(agentDao, paiementDao, departementDao);
        this.agentDao = agentDao;
        this.paiementDao = paiementDao;
        this.departementDao = departementDao;
        this.paiementService = new PaiementServiceImpl(paiementDao, agentDao); // Utilisation du service existant
    }

    @Override
    public Agent ajouterAgent(Agent agent, int responsableId) {
        try {
            if (agent == null) {
                System.err.println("Erreur: L'agent ne peut pas être null");
                return null;
            }

            if (agent.getNom() == null || agent.getNom().trim().isEmpty()) {
                System.err.println("Erreur: Le nom de l'agent est obligatoire");
                return null;
            }

            if (agent.getPrenom() == null || agent.getPrenom().trim().isEmpty()) {
                System.err.println("Erreur: Le prénom de l'agent est obligatoire");
                return null;
            }

            if (agent.getEmail() == null || agent.getEmail().trim().isEmpty()) {
                System.err.println("Erreur: L'email de l'agent est obligatoire");
                return null;
            }

            if (agent.getTypeAgent() == null) {
                System.err.println("Erreur: Le type d'agent est obligatoire");
                return null;
            }

            if (agent.getTypeAgent() != TypeAgent.OUVRIER && 
                agent.getTypeAgent() != TypeAgent.STAGIAIRE) {
                System.err.println("Erreur: Un responsable ne peut créer que des agents de type OUVRIER ou STAGIAIRE");
                System.err.println("Type demandé: " + agent.getTypeAgent());
                return null;
            }

            List<Agent> existingAgents = agentDao.lireTous();
            for (Agent existingAgent : existingAgents) {
                if (existingAgent.getEmail().equals(agent.getEmail())) {
                    System.err.println("Erreur: Un agent avec cet email existe déjà");
                    return null;
                }
            }

            if (agent.getDepartement() != null && agent.getDepartement().getId() > 0) {
                Departement dept = departementDao.lireParId(agent.getDepartement().getId());
                if (dept == null) {
                    System.err.println("Erreur: Le département spécifié n'existe pas");
                    return null;
                }
                agent.setDepartement(dept);
            }
            agentDao.creer(agent);
            System.out.println("Agent ajouté avec succès par le responsable ID " + responsableId);
            return agent;

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de l'agent: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Agent modifierAgent(Agent agent, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return null;
            }
            if (agent == null || agent.getId() <= 0) {
                System.err.println("Erreur: Agent invalide ou ID manquant");
                return null;
            }
            Agent existingAgent = agentDao.lireParId(agent.getId());
            if (existingAgent == null) {
                System.err.println("Erreur: Agent non trouvé avec l'ID " + agent.getId());
                return null;
            }
            if (agent.getNom() == null || agent.getNom().trim().isEmpty()) {
                System.err.println("Erreur: Le nom de l'agent est obligatoire");
                return null;
            }

            if (agent.getPrenom() == null || agent.getPrenom().trim().isEmpty()) {
                System.err.println("Erreur: Le prénom de l'agent est obligatoire");
                return null;
            }

            if (agent.getEmail() == null || agent.getEmail().trim().isEmpty()) {
                System.err.println("Erreur: L'email de l'agent est obligatoire");
                return null;
            }

            if (agent.getTypeAgent() == null) {
                System.err.println("Erreur: Le type d'agent est obligatoire");
                return null;
            }
            if (agent.getTypeAgent() != TypeAgent.OUVRIER && 
                agent.getTypeAgent() != TypeAgent.STAGIAIRE) {
                System.err.println("Erreur: Un responsable ne peut gérer que des agents de type OUVRIER ou STAGIAIRE");
                System.err.println("Type demandé: " + agent.getTypeAgent());
                return null;
            }
            List<Agent> existingAgents = agentDao.lireTous();
            for (Agent otherAgent : existingAgents) {
                if (otherAgent.getId() != agent.getId() && otherAgent.getEmail().equals(agent.getEmail())) {
                    System.err.println("Erreur: Un autre agent avec cet email existe déjà");
                    return null;
                }
            }
            if (agent.getDepartement() != null && agent.getDepartement().getId() > 0) {
                Departement dept = departementDao.lireParId(agent.getDepartement().getId());
                if (dept == null) {
                    System.err.println("Erreur: Le département spécifié n'existe pas");
                    return null;
                }
                agent.setDepartement(dept);
            }

            agentDao.mettreAJour(agent);
            System.out.println("Agent modifié avec succès par le responsable ID " + responsableId);
            return agent;

        } catch (Exception e) {
            System.err.println("Erreur lors de la modification de l'agent: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean supprimerAgent(int agentId, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return false;
            }

            if (agentId <= 0) {
                System.err.println("Erreur: ID d'agent invalide");
                return false;
            }

            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Erreur: Agent non trouvé avec l'ID " + agentId);
                return false;
            }

            List<Paiement> paiements = paiementDao.findPaiementsByAgentId(agentId);
            if (paiements != null && !paiements.isEmpty()) {
                System.err.println("Attention: L'agent a des paiements associés. Considérez d'abord la gestion de ces paiements.");
            }
            agentDao.supprimer(agentId);
            System.out.println("Agent supprimé avec succès par le responsable ID " + responsableId);
            return true;

        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de l'agent: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean affecterAgentDepartement(int agentId, int departementId, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return false;
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Erreur: Agent non trouvé avec l'ID " + agentId);
                return false;
            }

            Departement departement = departementDao.lireParId(departementId);
            if (departement == null) {
                System.err.println("Erreur: Département non trouvé avec l'ID " + departementId);
                return false;
            }
            agent.setDepartement(departement);
            agentDao.mettreAJour(agent);

            System.out.println("Agent ID " + agentId + " affecté au département '" + 
                             departement.getNom() + "' par le responsable ID " + responsableId);
            return true;

        } catch (Exception e) {
            System.err.println("Erreur lors de l'affectation de l'agent au département: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    private boolean verifierPermissionsResponsable(int responsableId) {
        try {
            if (responsableId <= 0) {
                return false;
            }

            Agent responsable = agentDao.lireParId(responsableId);
            if (responsable == null) {
                System.err.println("Responsable non trouvé avec l'ID " + responsableId);
                return false;
            }
            if (!responsable.isEstResponsableDepartement()) {
                System.err.println("L'agent ID " + responsableId + " n'est pas un responsable de département");
                return false;
            }

            return true;

        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification des permissions: " + e.getMessage());
            return false;
        }
    }

    public Paiement traiterPaiementAvecControles(int agentId, TypePaiement typePaiement, 
                                                double montant, int responsableId) {
        try {
            if (!verifierAgentDansDepartementResponsable(agentId, responsableId)) {
                System.err.println("Erreur: L'agent n'appartient pas au département du responsable");
                return null;
            }
            String motif = String.format("%s ajouté par responsable ID %d", 
                                       typePaiement.toString().toLowerCase(), responsableId);
            
            return paiementService.traiterPaiement(agentId, typePaiement, 
                                                 new BigDecimal(montant), motif);

        } catch (Exception e) {
            System.err.println("Erreur lors du traitement du paiement: " + e.getMessage());
            return null;
        }
    }

    private boolean verifierAgentDansDepartementResponsable(int agentId, int responsableId) {
        try {
            Agent agent = agentDao.lireParId(agentId);
            Agent responsable = agentDao.lireParId(responsableId);
            
            if (agent == null || responsable == null) {
                return false;
            }
            
            if (agent.getDepartement() == null || responsable.getDepartement() == null) {
                return false;
            }
            
            return agent.getDepartement().getId() == responsable.getDepartement().getId();
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification du département: " + e.getMessage());
            return false;
        }
    }

    private boolean verifierResponsableDepartement(int responsableId, int departementId) {
        try {
            Agent responsable = agentDao.lireParId(responsableId);
            if (responsable == null || responsable.getDepartement() == null) {
                return false;
            }
            return responsable.getDepartement().getId() == departementId;
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification du département du responsable: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Paiement> consulterPaiementsAgentDepartement(int agentId, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return new ArrayList<>();
            }
            if (!verifierAgentDansDepartementResponsable(agentId, responsableId)) {
                System.err.println("Erreur: L'agent n'appartient pas au département du responsable");
                return new ArrayList<>();
            }
            List<Paiement> paiements = paiementService.obtenirPaiementsParAgent(agentId);
            System.out.println("Récupération de " + paiements.size() + " paiements pour l'agent ID " + agentId);
            return paiements;

        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des paiements de l'agent: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Paiement> consulterTousPaiementsDepartement(int departementId, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return new ArrayList<>();
            }
            if (!verifierResponsableDepartement(responsableId, departementId)) {
                System.err.println("Erreur: Le responsable n'appartient pas à ce département");
                return new ArrayList<>();
            }
            List<Agent> agentsDuDepartement = agentDao.lireTous().stream()
                .filter(agent -> agent.getDepartement() != null && 
                               agent.getDepartement().getId() == departementId)
                .collect(Collectors.toList());

            List<Paiement> tousLesPaiements = new ArrayList<>();
            for (Agent agent : agentsDuDepartement) {
                List<Paiement> paiementsAgent = paiementService.obtenirPaiementsParAgent(agent.getId());
                tousLesPaiements.addAll(paiementsAgent);
            }

            tousLesPaiements.sort(Comparator.comparing(Paiement::getDatePaiement).reversed());
            System.out.println("Récupération de " + tousLesPaiements.size() + 
                             " paiements pour le département ID " + departementId);
            return tousLesPaiements;

        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des paiements du département: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Paiement> filtrerPaiementsDepartement(int departementId, TypePaiement typePaiement, 
                                                      boolean triParMontant, boolean triParDate, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return new ArrayList<>();
            }

            List<Paiement> paiementsDepartement = consulterTousPaiementsDepartement(departementId, responsableId);
            if (paiementsDepartement.isEmpty()) {
                return paiementsDepartement;
            }

            List<Paiement> paiementsFiltres = paiementsDepartement;
            if (typePaiement != null) {
                paiementsFiltres = paiementsDepartement.stream()
                    .filter(paiement -> paiement.getTypePaiement() == typePaiement)
                    .collect(Collectors.toList());
                System.out.println("Filtrage par type " + typePaiement + ": " + 
                                 paiementsFiltres.size() + " paiements trouvés");
            }

            if (triParMontant && triParDate) {
                paiementsFiltres.sort(Comparator.comparing((Paiement p) -> p.getMontant())
                                               .thenComparing(Paiement::getDatePaiement).reversed());
                System.out.println("Tri appliqué: par montant puis par date");
            } else if (triParMontant) {
                paiementsFiltres.sort(Comparator.comparing((Paiement p) -> p.getMontant()).reversed());
                System.out.println("Tri appliqué: par montant (décroissant)");
            } else if (triParDate) {
                paiementsFiltres.sort(Comparator.comparing(Paiement::getDatePaiement).reversed());
                System.out.println("Tri appliqué: par date (plus récent d'abord)");
            }

            System.out.println("Résultat final: " + paiementsFiltres.size() + " paiements");
            return paiementsFiltres;

        } catch (Exception e) {
            System.err.println("Erreur lors du filtrage/tri des paiements du département: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> calculerStatistiquesDepartement(int departementId, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return new HashMap<>();
            }

            if (!verifierResponsableDepartement(responsableId, departementId)) {
                System.err.println("Erreur: Le responsable n'appartient pas à ce département");
                return new HashMap<>();
            }

            List<Paiement> paiementsDepartement = consulterTousPaiementsDepartement(departementId, responsableId);
            
            List<Agent> agentsDepartement = agentDao.lireTous().stream()
                .filter(agent -> agent.getDepartement() != null && 
                               agent.getDepartement().getId() == departementId)
                .collect(Collectors.toList());

            Map<String, Object> statistiques = new HashMap<>();

            statistiques.put("nombreAgents", agentsDepartement.size());
            statistiques.put("nombrePaiements", paiementsDepartement.size());

            BigDecimal montantTotal = paiementsDepartement.stream()
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            statistiques.put("montantTotal", montantTotal);

            if (!paiementsDepartement.isEmpty()) {
                BigDecimal montantMoyen = montantTotal.divide(
                    new BigDecimal(paiementsDepartement.size()), 2, RoundingMode.HALF_UP);
                statistiques.put("montantMoyen", montantMoyen);
            } else {
                statistiques.put("montantMoyen", BigDecimal.ZERO);
            }

            Map<TypePaiement, Integer> repartitionParType = new HashMap<>();
            Map<TypePaiement, BigDecimal> montantParType = new HashMap<>();
            
            for (TypePaiement type : TypePaiement.values()) {
                List<Paiement> paiementsType = paiementsDepartement.stream()
                    .filter(p -> p.getTypePaiement() == type)
                    .collect(Collectors.toList());
                
                repartitionParType.put(type, paiementsType.size());
                
                BigDecimal montantType = paiementsType.stream()
                    .map(Paiement::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                montantParType.put(type, montantType);
            }
            
            statistiques.put("repartitionParType", repartitionParType);
            statistiques.put("montantParType", montantParType);

            Map<String, Integer> paiementsParAgent = new HashMap<>();
            Map<String, BigDecimal> montantParAgent = new HashMap<>();
            
            for (Agent agent : agentsDepartement) {
                List<Paiement> paiementsAgent = paiementsDepartement.stream()
                    .filter(p -> p.getAgent() != null && p.getAgent().getId() == agent.getId())
                    .collect(Collectors.toList());
                
                String nomAgent = agent.getPrenom() + " " + agent.getNom();
                paiementsParAgent.put(nomAgent, paiementsAgent.size());
                
                BigDecimal montantAgent = paiementsAgent.stream()
                    .map(Paiement::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                montantParAgent.put(nomAgent, montantAgent);
            }
            
            statistiques.put("paiementsParAgent", paiementsParAgent);
            statistiques.put("montantParAgent", montantParAgent);

            System.out.println("Statistiques calculées avec succès pour le département ID " + departementId);
            return statistiques;

        } catch (Exception e) {
            System.err.println("Erreur lors du calcul des statistiques: " + e.getMessage());
            return new HashMap<>();
        }
    }

    @Override
    public List<Agent> classementAgentsParPaiement(int departementId, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return new ArrayList<>();
            }

            if (!verifierResponsableDepartement(responsableId, departementId)) {
                System.err.println("Erreur: Le responsable n'appartient pas à ce département");
                return new ArrayList<>();
            }

            List<Agent> agentsDepartement = agentDao.lireTous().stream()
                .filter(agent -> agent.getDepartement() != null && 
                               agent.getDepartement().getId() == departementId)
                .collect(Collectors.toList());

            List<Paiement> paiementsDepartement = consulterTousPaiementsDepartement(departementId, responsableId);

            Map<Integer, BigDecimal> montantParAgentId = new HashMap<>();
            
            for (Agent agent : agentsDepartement) {
                BigDecimal montantTotal = paiementsDepartement.stream()
                    .filter(p -> p.getAgent() != null && p.getAgent().getId() == agent.getId())
                    .map(Paiement::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                montantParAgentId.put(agent.getId(), montantTotal);
            }
            List<Agent> classement = agentsDepartement.stream()
                .sorted((a1, a2) -> {
                    BigDecimal montant1 = montantParAgentId.get(a1.getId());
                    BigDecimal montant2 = montantParAgentId.get(a2.getId());
                    return montant2.compareTo(montant1); 
                })
                .collect(Collectors.toList());

            System.out.println("Classement généré pour " + classement.size() + " agents du département");
            
            for (int i = 0; i < classement.size(); i++) {
                Agent agent = classement.get(i);
                BigDecimal montant = montantParAgentId.get(agent.getId());
                System.out.println((i + 1) + ". " + agent.getPrenom() + " " + agent.getNom() + 
                                 " - Montant total: " + montant + " DH");
            }

            return classement;

        } catch (Exception e) {
            System.err.println("Erreur lors du classement des agents: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Agent> listerAgentsMonDepartement(int idResponsable) {
        try {
            Agent responsable = agentDao.lireParId(idResponsable);
            if (responsable == null || responsable.getDepartement() == null) {
                System.err.println("Responsable introuvable ou pas de département assigné");
                return new ArrayList<>();
            }
            
            int departementId = responsable.getDepartement().getId();
            List<Agent> tousLesAgents = agentDao.lireTous();
            
            List<Agent> agentsDepartement = tousLesAgents.stream()
                .filter(agent -> agent.getDepartement() != null && 
                               agent.getDepartement().getId() == departementId)
                .collect(Collectors.toList());
                
            System.out.println("Agents du département " + responsable.getDepartement().getNom() + ": " + agentsDepartement.size());
            return agentsDepartement;
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la liste des agents du département: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}