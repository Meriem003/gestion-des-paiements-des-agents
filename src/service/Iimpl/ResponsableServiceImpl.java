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
import java.math.BigDecimal;
import java.time.LocalDate;

public class ResponsableServiceImpl extends AgentServiceImpl implements IResponsableService {
    private AgentDao agentDao;
    private PaiementDao paiementDao;
    private DepartementDao departementDao;

    public ResponsableServiceImpl(AgentDao agentDao, PaiementDao paiementDao, DepartementDao departementDao) {
        super(agentDao, paiementDao, departementDao);
        this.agentDao = agentDao;
        this.paiementDao = paiementDao;
        this.departementDao = departementDao;
    }

    @Override
    public Agent ajouterAgent(Agent agent, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return null;
            }
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

    @Override
    public boolean affecterAgentDepartement(int agentId, int departementId, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return false;
            }
            if (agentId <= 0) {
                System.err.println("Erreur: ID d'agent invalide");
                return false;
            }

            if (departementId <= 0) {
                System.err.println("Erreur: ID de département invalide");
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

    @Override
    public Paiement ajouterSalaire(int agentId, double montant, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return null;
            }
            if (agentId <= 0) {
                System.err.println("Erreur: ID d'agent invalide");
                return null;
            }

            if (montant <= 0) {
                System.err.println("Erreur: Le montant du salaire doit être positif");
                return null;
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Erreur: Agent non trouvé avec l'ID " + agentId);
                return null;
            }
            Agent responsable = agentDao.lireParId(responsableId);
            if (responsable.getDepartement() != null && agent.getDepartement() != null) {
                if (responsable.getDepartement().getId() != agent.getDepartement().getId()) {
                    System.err.println("Erreur: L'agent n'appartient pas au département du responsable");
                    return null;
                }
            }

            Paiement paiement = new Paiement();
            paiement.setTypePaiement(TypePaiement.SALAIRE);
            paiement.setMontant(new BigDecimal(montant));
            paiement.setDatePaiement(LocalDate.now());
            paiement.setMotif("Salaire ajouté par responsable ID " + responsableId);
            paiement.setConditionValidee(true);
            paiement.setAgent(agent);
            paiementDao.creer(paiement);
            System.out.println("Salaire de " + montant + " ajouté avec succès pour l'agent ID " + agentId);
            return paiement;

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout du salaire: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Paiement ajouterPrime(int agentId, double montant, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return null;
            }
            if (agentId <= 0) {
                System.err.println("Erreur: ID d'agent invalide");
                return null;
            }
            if (montant <= 0) {
                System.err.println("Erreur: Le montant de la prime doit être positif");
                return null;
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Erreur: Agent non trouvé avec l'ID " + agentId);
                return null;
            }
            Agent responsable = agentDao.lireParId(responsableId);
            if (responsable.getDepartement() != null && agent.getDepartement() != null) {
                if (responsable.getDepartement().getId() != agent.getDepartement().getId()) {
                    System.err.println("Erreur: L'agent n'appartient pas au département du responsable");
                    return null;
                }
            }
            Paiement paiement = new Paiement();
            paiement.setTypePaiement(TypePaiement.PRIME);
            paiement.setMontant(new BigDecimal(montant));
            paiement.setDatePaiement(LocalDate.now());
            paiement.setMotif("Prime ajoutée par responsable ID " + responsableId);
            paiement.setConditionValidee(true);
            paiement.setAgent(agent);
            paiementDao.creer(paiement);
            System.out.println("Prime de " + montant + " ajoutée avec succès pour l'agent ID " + agentId);
            return paiement;

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de la prime: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int demanderBonus(int agentId, double montant, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return -1;
            }
            if (agentId <= 0) {
                System.err.println("Erreur: ID d'agent invalide");
                return -1;
            }

            if (montant <= 0) {
                System.err.println("Erreur: Le montant du bonus doit être positif");
                return -1;
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Erreur: Agent non trouvé avec l'ID " + agentId);
                return -1;
            }
            Agent responsable = agentDao.lireParId(responsableId);
            if (responsable.getDepartement() != null && agent.getDepartement() != null) {
                if (responsable.getDepartement().getId() != agent.getDepartement().getId()) {
                    System.err.println("Erreur: L'agent n'appartient pas au département du responsable");
                    return -1;
                }
            }
            Paiement demande = new Paiement();
            demande.setTypePaiement(TypePaiement.BONUS);
            demande.setMontant(new BigDecimal(montant));
            demande.setDatePaiement(LocalDate.now());
            demande.setMotif("Demande de bonus par responsable ID " + responsableId + " pour agent ID " + agentId);
            demande.setConditionValidee(false); 
            demande.setAgent(agent);
            paiementDao.creer(demande);
            System.out.println("Demande de bonus de " + montant + " créée avec succès pour l'agent ID " + agentId);
            System.out.println("ID de la demande: " + demande.getId() + " (en attente de validation)");
            return demande.getId();

        } catch (Exception e) {
            System.err.println("Erreur lors de la création de la demande de bonus: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int demanderIndemnite(int agentId, double montant, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return -1;
            }
            if (agentId <= 0) {
                System.err.println("Erreur: ID d'agent invalide");
                return -1;
            }

            if (montant <= 0) {
                System.err.println("Erreur: Le montant de l'indemnité doit être positif");
                return -1;
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Erreur: Agent non trouvé avec l'ID " + agentId);
                return -1;
            }
            Agent responsable = agentDao.lireParId(responsableId);
            if (responsable.getDepartement() != null && agent.getDepartement() != null) {
                if (responsable.getDepartement().getId() != agent.getDepartement().getId()) {
                    System.err.println("Erreur: L'agent n'appartient pas au département du responsable");
                    return -1;
                }
            }
            Paiement demande = new Paiement();
            demande.setTypePaiement(TypePaiement.INDEMNITE);
            demande.setMontant(new BigDecimal(montant));
            demande.setDatePaiement(LocalDate.now());
            demande.setMotif("Demande d'indemnité par responsable ID " + responsableId + " pour agent ID " + agentId);
            demande.setConditionValidee(false); 
            demande.setAgent(agent);
            paiementDao.creer(demande);
            System.out.println("Demande d'indemnité de " + montant + " créée avec succès pour l'agent ID " + agentId);
            System.out.println("ID de la demande: " + demande.getId() + " (en attente de validation)");
            return demande.getId();

        } catch (Exception e) {
            System.err.println("Erreur lors de la création de la demande d'indemnité: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Paiement> consulterPaiementsAgent(int agentId, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return new ArrayList<>();
            }
            if (agentId <= 0) {
                System.err.println("Erreur: ID d'agent invalide");
                return new ArrayList<>();
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Erreur: Agent non trouvé avec l'ID " + agentId);
                return new ArrayList<>();
            }
            Agent responsable = agentDao.lireParId(responsableId);
            if (responsable.getDepartement() != null && agent.getDepartement() != null) {
                if (responsable.getDepartement().getId() != agent.getDepartement().getId()) {
                    System.err.println("Erreur: L'agent n'appartient pas au département du responsable");
                    return new ArrayList<>();
                }
            }
            List<Paiement> paiements = paiementDao.findPaiementsByAgentId(agentId);
            System.out.println("Récupération de " + paiements.size() + " paiements pour l'agent ID " + agentId);
            return paiements;
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des paiements de l'agent: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Paiement> consulterPaiementsDepartement(int departementId, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return new ArrayList<>();
            }
            if (departementId <= 0) {
                System.err.println("Erreur: ID de département invalide");
                return new ArrayList<>();
            }
            Departement departement = departementDao.lireParId(departementId);
            if (departement == null) {
                System.err.println("Erreur: Département non trouvé avec l'ID " + departementId);
                return new ArrayList<>();
            }
            Agent responsable = agentDao.lireParId(responsableId);
            if (responsable.getDepartement() == null || 
                responsable.getDepartement().getId() != departementId) {
                System.err.println("Erreur: Le responsable n'appartient pas à ce département");
                return new ArrayList<>();
            }
            List<Agent> agentsDuDepartement = agentDao.lireTous().stream()
                .filter(agent -> agent.getDepartement() != null && 
                               agent.getDepartement().getId() == departementId)
                .collect(Collectors.toList());
            List<Paiement> tousLesPaiements = new ArrayList<>();
            for (Agent agent : agentsDuDepartement) {
                List<Paiement> paiementsAgent = paiementDao.findPaiementsByAgentId(agent.getId());
                tousLesPaiements.addAll(paiementsAgent);
            }
            tousLesPaiements.sort(Comparator.comparing(Paiement::getDatePaiement).reversed());
            System.out.println("Récupération de " + tousLesPaiements.size() + 
                             " paiements pour le département ID " + departementId);
            return tousLesPaiements;
        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des paiements du département: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Paiement> filtrerTrierPaiementsDepartement(int departementId, TypePaiement typePaiement, 
                                                          boolean triParMontant, boolean triParDate, int responsableId) {
        try {
            if (!verifierPermissionsResponsable(responsableId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le responsable ID " + responsableId);
                return new ArrayList<>();
            }
            List<Paiement> paiementsDepartement = consulterPaiementsDepartement(departementId, responsableId);
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
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}