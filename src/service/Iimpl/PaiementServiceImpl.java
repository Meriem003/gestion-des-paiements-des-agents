package service.Iimpl;

import service.IPaiementService;
import dao.IPaiementDao;
import dao.IAgentDao;
import dao.Iimpl.PaiementDao;
import dao.Iimpl.AgentDao;
import utils.DBConnection;
import model.Paiement;
import model.Agent;
import model.TypePaiement;
import model.TypeAgent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class PaiementServiceImpl implements IPaiementService {
    
    private IPaiementDao paiementDao;
    private IAgentDao agentDao;
    
    public PaiementServiceImpl() {
        DBConnection dbConnection = DBConnection.getInstance();
        this.paiementDao = new PaiementDao(dbConnection.getConnection());
        this.agentDao = new AgentDao(dbConnection.getConnection());
    }
    
    public PaiementServiceImpl(IPaiementDao paiementDao, IAgentDao agentDao) {
        this.paiementDao = paiementDao;
        this.agentDao = agentDao;
    }
    
    @Override
    public Paiement creerPaiement(Paiement paiement) {
        if (paiement == null) {
            throw new IllegalArgumentException("Le paiement ne peut pas être null");
        }
        
        if (!validerDonneesPaiement(paiement)) {
            throw new IllegalArgumentException("Les données du paiement ne sont pas valides");
        }
        
        if (paiement.getDatePaiement() == null) {
            paiement.setDatePaiement(LocalDate.now());
        }
        paiement.setConditionValidee(validerConditionsPaiement(paiement));
        
        paiementDao.creer(paiement);
        return paiement;
    }
    
    @Override
    public Paiement obtenirPaiementParId(int id) {
        return paiementDao.lireParId(id);
    }
    
    @Override
    public List<Paiement> obtenirTousLesPaiements() {
        return paiementDao.lireTous();
    }
    
    @Override
    public List<Paiement> obtenirPaiementsParAgent(int agentId) {
        return paiementDao.findPaiementsByAgentId(agentId);
    }
    
    @Override
    public Paiement mettreAJourPaiement(Paiement paiement) {
        if (paiement == null) {
            throw new IllegalArgumentException("Le paiement ne peut pas être null");
        }
        Paiement paiementExistant = paiementDao.lireParId(paiement.getId());
        if (paiementExistant == null) {
            throw new IllegalArgumentException("Le paiement avec l'ID " + paiement.getId() + " n'existe pas");
        }
        
        if (!validerDonneesPaiement(paiement)) {
            throw new IllegalArgumentException("Les données du paiement ne sont pas valides");
        }
        
        paiement.setConditionValidee(validerConditionsPaiement(paiement));
        
        paiementDao.mettreAJour(paiement);
        return paiement;
    }
    
    @Override
    public boolean supprimerPaiement(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'ID doit être positif");
        }
        
        Paiement paiement = paiementDao.lireParId(id);
        if (paiement == null) {
            return false;
        }
        
        try {
            paiementDao.supprimer(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public BigDecimal calculerSalaireTotalPeriode(int agentId, LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates ne peuvent pas être nulles");
        }
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit être antérieure à la date de fin");
        }
        
        List<Paiement> paiements = obtenirPaiementsParAgent(agentId);
        
        return paiements.stream()
                .filter(p -> p.getDatePaiement() != null && 
                           !p.getDatePaiement().isBefore(dateDebut) && 
                           !p.getDatePaiement().isAfter(dateFin) &&
                           p.isConditionValidee())
                .map(Paiement::getMontant)
                .filter(montant -> montant != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Override
    public Paiement traiterPaiement(int agentId, TypePaiement typePaiement, BigDecimal montant, String motif) {
        if (typePaiement == null) {
            throw new IllegalArgumentException("Le type de paiement ne peut pas être null");
        }
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        
        Agent agent = agentDao.lireParId(agentId);
        if (agent == null) {
            throw new IllegalArgumentException("L'agent avec l'ID " + agentId + " n'existe pas");
        }
        
        if (!verifierDroitPaiement(agentId, typePaiement)) {
            throw new IllegalArgumentException("L'agent de type " + agent.getTypeAgent() + " n'a pas droit au paiement de type " + typePaiement);
        }
        
        Paiement paiement = new Paiement();
        paiement.setAgent(agent);
        paiement.setTypePaiement(typePaiement);
        paiement.setMontant(montant);
        paiement.setMotif(motif);
        paiement.setDatePaiement(LocalDate.now());
        
        return creerPaiement(paiement);
    }
    
    @Override
    public boolean validerConditionsPaiement(Paiement paiement) {
        if (paiement == null) {
            return false;
        }
        
        if (paiement.getAgent() == null || paiement.getTypePaiement() == null || 
            paiement.getMontant() == null || paiement.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        switch (paiement.getTypePaiement()) {
            case SALAIRE:
                return validerSalaire(paiement);
            case PRIME:
                return validerPrime(paiement);
            case BONUS:
                return validerBonus(paiement);
            case INDEMNITE:
                return validerIndemnite(paiement);
            default:
                return false;
        }
    }
    
    @Override
    public List<Paiement> obtenirHistoriquePaiementsParType(int agentId, TypePaiement typePaiement) {
        if (typePaiement == null) {
            throw new IllegalArgumentException("Le type de paiement ne peut pas être null");
        }
        
        List<Paiement> paiements = obtenirPaiementsParAgent(agentId);
        
        return paiements.stream()
                .filter(p -> p.getTypePaiement() == typePaiement)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Paiement> trierPaiements(int agentId, String critere, boolean ascendant) {
        if (critere == null || critere.trim().isEmpty()) {
            throw new IllegalArgumentException("Le critère de tri ne peut pas être vide");
        }
        
        List<Paiement> paiements = obtenirPaiementsParAgent(agentId);
        
        Comparator<Paiement> comparator;
        
        switch (critere.toLowerCase()) {
            case "date":
                comparator = Comparator.comparing(Paiement::getDatePaiement, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "montant":
                comparator = Comparator.comparing(Paiement::getMontant, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "type":
                comparator = Comparator.comparing(p -> p.getTypePaiement().name());
                break;
            default:
                throw new IllegalArgumentException("Critère de tri non supporté: " + critere);
        }
        
        if (!ascendant) {
            comparator = comparator.reversed();
        }
        
        return paiements.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    
    @Override
    public Paiement creerBonusDirectParDirecteur(int directeurId, int responsableId, BigDecimal montant, String motif) {
        if (directeurId <= 0) {
            throw new IllegalArgumentException("L'ID du directeur doit être positif");
        }
        if (responsableId <= 0) {
            throw new IllegalArgumentException("L'ID du responsable doit être positif");
        }
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        if (motif == null || motif.trim().isEmpty()) {
            throw new IllegalArgumentException("Le motif ne peut pas être vide");
        }
        
        Agent directeur = agentDao.lireParId(directeurId);
        if (directeur == null) {
            throw new IllegalArgumentException("Le directeur avec l'ID " + directeurId + " n'existe pas");
        }
        if (directeur.getTypeAgent() != TypeAgent.DIRECTEUR) {
            throw new IllegalArgumentException("Seul un directeur peut créer des bonus directs");
        }
        
        Agent responsable = agentDao.lireParId(responsableId);
        if (responsable == null) {
            throw new IllegalArgumentException("Le responsable avec l'ID " + responsableId + " n'existe pas");
        }
        if (responsable.getTypeAgent() != TypeAgent.RESPONSABLE_DEPARTEMENT) {
            throw new IllegalArgumentException("Le bonus ne peut être accordé qu'à un responsable de département");
        }
        
        Paiement bonus = new Paiement();
        bonus.setAgent(responsable);
        bonus.setTypePaiement(TypePaiement.BONUS);
        bonus.setMontant(montant);
        bonus.setMotif(motif + " (Accordé directement par le directeur)");
        bonus.setDatePaiement(LocalDate.now());
        bonus.setConditionValidee(true);
        paiementDao.creer(bonus);
        return bonus;
    }
    
    
    private boolean verifierDroitPaiement(int agentId, TypePaiement typePaiement) {
        Agent agent = agentDao.lireParId(agentId);
        if (agent == null) {
            return false;
        }
        
        TypeAgent typeAgent = agent.getTypeAgent();
        
        switch (typePaiement) {
            case SALAIRE:
                return true;
                
            case PRIME:
                return true;
                
            case BONUS:
                return typeAgent == TypeAgent.RESPONSABLE_DEPARTEMENT;
                
            case INDEMNITE:
                return typeAgent == TypeAgent.RESPONSABLE_DEPARTEMENT || 
                       typeAgent == TypeAgent.DIRECTEUR;
                
            default:
                return false;
        }
    }
    
    private boolean validerDonneesPaiement(Paiement paiement) {
        if (paiement.getAgent() == null) {
            return false;
        }
        if (paiement.getTypePaiement() == null) {
            return false;
        }
        if (paiement.getMontant() == null || paiement.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        return true;
    }
    
    private boolean validerSalaire(Paiement paiement) {
        LocalDate datePaiement = paiement.getDatePaiement();
        if (datePaiement == null) {
            return false;
        }
        
        LocalDate debutMois = datePaiement.withDayOfMonth(1);
        LocalDate finMois = datePaiement.withDayOfMonth(datePaiement.lengthOfMonth());
        
        List<Paiement> paiementsMois = obtenirPaiementsParAgent(paiement.getAgent().getId())
                .stream()
                .filter(p -> p.getTypePaiement() == TypePaiement.SALAIRE &&
                           p.getDatePaiement() != null &&
                           !p.getDatePaiement().isBefore(debutMois) &&
                           !p.getDatePaiement().isAfter(finMois) &&
                           p.getId() != paiement.getId())
                .collect(Collectors.toList());
        
        return paiementsMois.isEmpty();
    }
    
    private boolean validerPrime(Paiement paiement) {
        return paiement.getMotif() != null && !paiement.getMotif().trim().isEmpty();
    }
    
    private boolean validerBonus(Paiement paiement) {
        Agent agent = paiement.getAgent();
        if (agent == null) {
            return false;
        }        
        if (agent.getTypeAgent() != TypeAgent.RESPONSABLE_DEPARTEMENT) {
            return false;
        }        
        if (paiement.getMotif() == null || paiement.getMotif().trim().isEmpty()) {
            return false;
        }
        BigDecimal montantMax = new BigDecimal("10000.00");
        return paiement.getMontant().compareTo(montantMax) <= 0;
    }
    
    private boolean validerIndemnite(Paiement paiement) {
        Agent agent = paiement.getAgent();
        if (agent == null) {
            return false;
        }        
        if (agent.getTypeAgent() != TypeAgent.RESPONSABLE_DEPARTEMENT && 
            agent.getTypeAgent() != TypeAgent.DIRECTEUR) {
            return false;
        }        
        return paiement.getMotif() != null && !paiement.getMotif().trim().isEmpty();
    }
}
