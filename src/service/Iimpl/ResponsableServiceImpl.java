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
    private IPaiementService paiementService; 

    public ResponsableServiceImpl(AgentDao agentDao, PaiementDao paiementDao, DepartementDao departementDao) {
        super(agentDao, paiementDao, departementDao);
        this.agentDao = agentDao;
        this.paiementDao = paiementDao;
        this.departementDao = departementDao;
        this.paiementService = new PaiementServiceImpl(paiementDao, agentDao); 
    }

    //menu responsable choix 2
    @Override
    public Agent ajouterAgent(Agent agent, int responsableId) {
        if (agent == null) {
            return null;
        }

        if (agent.getNom() == null || agent.getNom().trim().isEmpty()) {
            return null;
        }

        if (agent.getPrenom() == null || agent.getPrenom().trim().isEmpty()) {
            return null;
        }

        if (agent.getEmail() == null || agent.getEmail().trim().isEmpty()) {
            return null;
        }

        if (agent.getTypeAgent() == null) {
            return null;
        }

        if (agent.getTypeAgent() != TypeAgent.OUVRIER && 
            agent.getTypeAgent() != TypeAgent.STAGIAIRE) {
            return null;
        }

        Agent responsable = agentDao.lireParId(responsableId);
        if (responsable == null) {
            return null;
        }

        if (responsable.getDepartement() == null) {
            return null;
        }

        agent.setDepartement(responsable.getDepartement());

        List<Agent> existingAgents = agentDao.lireTous();
        for (Agent existingAgent : existingAgents) {
            if (existingAgent.getEmail().equals(agent.getEmail())) {
                return null;
            }
        }

        agentDao.creer(agent);
        return agent;
    }

    //menu responsable choix 3
    @Override
    public Agent modifierAgent(Agent agent, int responsableId) {
        if (!verifierPermissionsResponsable(responsableId)) {
            return null;
        }
        if (agent == null || agent.getId() <= 0) {
            return null;
        }
        Agent existingAgent = agentDao.lireParId(agent.getId());
        if (existingAgent == null) {
            return null;
        }
        if (agent.getNom() == null || agent.getNom().trim().isEmpty()) {
            return null;
        }

        if (agent.getPrenom() == null || agent.getPrenom().trim().isEmpty()) {
            return null;
        }

        if (agent.getEmail() == null || agent.getEmail().trim().isEmpty()) {
            return null;
        }

        if (agent.getTypeAgent() == null) {
            return null;
        }
        if (agent.getTypeAgent() != TypeAgent.OUVRIER && 
            agent.getTypeAgent() != TypeAgent.STAGIAIRE) {
            return null;
        }
        List<Agent> existingAgents = agentDao.lireTous();
        for (Agent otherAgent : existingAgents) {
            if (otherAgent.getId() != agent.getId() && otherAgent.getEmail().equals(agent.getEmail())) {
                return null;
            }
        }
        if (agent.getDepartement() != null && agent.getDepartement().getId() > 0) {
            Departement dept = departementDao.lireParId(agent.getDepartement().getId());
            if (dept == null) {
                return null;
            }
            agent.setDepartement(dept);
        }

        agentDao.mettreAJour(agent);
        return agent;
    }

    //menu responsable choix 4
    @Override
    public boolean supprimerAgent(int agentId, int responsableId) {
        if (!verifierPermissionsResponsable(responsableId)) {
            return false;
        }

        if (agentId <= 0) {
            return false;
        }

        Agent agent = agentDao.lireParId(agentId);
        if (agent == null) {
            return false;
        }

        List<Paiement> paiements = paiementDao.findPaiementsByAgentId(agentId);
        // do not delete associated payments here; caller should handle
        agentDao.supprimer(agentId);
        return true;
    }

    public boolean affecterAgentDepartement(int agentId, int departementId, int responsableId) {
        if (!verifierPermissionsResponsable(responsableId)) {
            return false;
        }
        Agent agent = agentDao.lireParId(agentId);
        if (agent == null) {
            return false;
        }

        Departement departement = departementDao.lireParId(departementId);
        if (departement == null) {
            return false;
        }
        agent.setDepartement(departement);
        agentDao.mettreAJour(agent);
        return true;
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
        if (!verifierPermissionsResponsable(responsableId)) {
            return new ArrayList<>();
        }
        if (!verifierAgentDansDepartementResponsable(agentId, responsableId)) {
            return new ArrayList<>();
        }
        List<Paiement> paiements = paiementService.obtenirPaiementsParAgent(agentId);
        return paiements;
    }

    @Override
    public List<Paiement> consulterTousPaiementsDepartement(int departementId, int responsableId) {
        if (!verifierPermissionsResponsable(responsableId)) {
            return new ArrayList<>();
        }
        if (!verifierResponsableDepartement(responsableId, departementId)) {
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
        return tousLesPaiements;
    }

    @Override
    public Map<String, Object> calculerStatistiquesDepartement(int departementId, int responsableId) {
        try {
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

            // statistics computed
            return statistiques;

        } catch (Exception e) {
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

            // ranking generated

            return classement;

        } catch (Exception e) {
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
                
            return agentsDepartement;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}