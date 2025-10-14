package service.Iimpl;

import model.Agent;
import model.Departement;
import model.Paiement;
import model.TypeAgent;
import model.TypePaiement;
import dao.Iimpl.AgentDao;
import dao.Iimpl.PaiementDao;
import dao.Iimpl.DepartementDao;
import service.IDirecteurService;
import service.IPaiementService;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DirecteurServiceImpl extends AgentServiceImpl implements IDirecteurService {
    private AgentDao agentDao;
    private DepartementDao departementDao;

    public DirecteurServiceImpl(AgentDao agentDao, PaiementDao paiementDao, DepartementDao departementDao) {
        super(agentDao, paiementDao, departementDao);
        this.agentDao = agentDao;
        this.departementDao = departementDao;
    }
    @Override
    public Departement creerDepartement(Departement departement, int directeurId) {
        try {
            Departement nouveauDept = new Departement();
            nouveauDept.setNom(departement.getNom());
            departementDao.creer(nouveauDept);
            System.out.println("Département '" + departement.getNom() + "' créé avec succès (ID: " + nouveauDept.getId() + ")");
            return nouveauDept;
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du département: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Departement modifierDepartement(Departement departement, int directeurId) {
        try {
            Departement deptExistant = departementDao.lireParId(departement.getId());
            if (deptExistant == null) {
                System.err.println("Département introuvable (ID: " + departement.getId() + ")");
                return null;
            }
            departementDao.mettreAJour(departement);

            System.out.println("Département '" + departement.getNom() + "' modifié avec succès");
            return departement;

        } catch (Exception e) {
            System.err.println("Erreur lors de la modification du département: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean supprimerDepartement(int departementId, int directeurId) {
        try {
            Departement dept = departementDao.lireParId(departementId);
            if (dept == null) {
                System.err.println("Département introuvable (ID: " + departementId + ")");
                return false;
            }
            departementDao.supprimer(departementId);
            System.out.println("Département '" + dept.getNom() + "' supprimé avec succès");
            return true;

        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du département: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Departement> listerTousDepartements(int directeurId) {
        try {
            List<Departement> departements = departementDao.lireTous();
            System.out.println("Liste des départements récupérée: " + departements.size() + " département(s)");
            for (Departement dept : departements) {
                System.out.println("- [" + dept.getId() + "] " + dept.getNom());
            }
            return departements;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des départements: " + e.getMessage());
            return new ArrayList<>();
        }
    }



    @Override
    public Agent creerUtilisateurAvecDepartement(Agent agent, int departementId, int directeurId) {
        try {
            Agent directeur = agentDao.lireParId(directeurId);
            if (directeur == null || directeur.getTypeAgent() != TypeAgent.DIRECTEUR) {
                throw new IllegalArgumentException("Seul un directeur peut créer des responsables");
            }
            
            Departement departement = departementDao.lireParId(departementId);
            if (departement == null) {
                throw new IllegalArgumentException("Département introuvable (ID: " + departementId + ")");
            }
            
            agent.setTypeAgent(TypeAgent.RESPONSABLE_DEPARTEMENT);
            
            agent.setDepartement(departement);
            
            agentDao.creer(agent);
            
            System.out.println(" Responsable de département créé avec succès :");
            System.out.println("   - Nom: " + agent.getNom() + " " + agent.getPrenom());
            System.out.println("   - Type: " + agent.getTypeAgent());
            System.out.println("   - Département: " + departement.getNom());
            
            return agent;
            
        } catch (Exception e) {
            System.err.println(" Erreur lors de la création du responsable: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Agent> genererTopAgentsMieuxPayes(int nombreAgents, int directeurId) {
        try {
            Agent directeur = agentDao.lireParId(directeurId);
            if (directeur == null || directeur.getTypeAgent() != TypeAgent.DIRECTEUR) {
                throw new IllegalArgumentException("Seul un directeur peut consulter ce rapport");
            }            
            List<Agent> tousLesAgents = agentDao.lireTous();
            IPaiementService paiementService = new service.Iimpl.PaiementServiceImpl();            
            List<Map.Entry<Agent, BigDecimal>> agentsAvecTotaux = new ArrayList<>();
            
            for (Agent agent : tousLesAgents) {
                try {
                    List<Paiement> paiements = paiementService.obtenirPaiementsParAgent(agent.getId());
                    BigDecimal totalPaiements = paiements.stream()
                            .filter(Paiement::isConditionValidee)
                            .map(Paiement::getMontant)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    agentsAvecTotaux.add(Map.entry(agent, totalPaiements));
                } catch (Exception e) {
                    System.err.println("Erreur pour l'agent " + agent.getId() + ": " + e.getMessage());
                }
            }
            
            List<Agent> topAgents = agentsAvecTotaux.stream()
                    .sorted(Map.Entry.<Agent, BigDecimal>comparingByValue().reversed())
                    .limit(nombreAgents)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            
            System.out.println(" TOP " + nombreAgents + " des agents les mieux payés généré");
            return topAgents;
            
        } catch (Exception e) {
            System.err.println(" Erreur lors de la génération du top des agents: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> genererRapportGlobalEntreprise(int directeurId) {
        try {
            Map<String, Object> rapport = new HashMap<>();
            
            List<Agent> tousLesAgents = agentDao.lireTous();
            List<Departement> tousLesDepartements = departementDao.lireTous();
            List<Paiement> tousLesPaiements = paiementDao.lireTous();            
            rapport.put("totalAgents", tousLesAgents.size());
            rapport.put("totalDepartements", tousLesDepartements.size());            
            BigDecimal totalPaiements = tousLesPaiements.stream()
                    .map(Paiement::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            rapport.put("totalPaiements", totalPaiements);
            
            if (!tousLesAgents.isEmpty()) {
                BigDecimal moyenneParAgent = totalPaiements.divide(
                    BigDecimal.valueOf(tousLesAgents.size()), 2, RoundingMode.HALF_UP);
                rapport.put("moyenneParAgent", moyenneParAgent);
            } else {
                rapport.put("moyenneParAgent", BigDecimal.ZERO);
            }
            
            if (!tousLesPaiements.isEmpty()) {
                BigDecimal max = tousLesPaiements.stream()
                        .map(Paiement::getMontant)
                        .max(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO);
                        
                BigDecimal min = tousLesPaiements.stream()
                        .map(Paiement::getMontant)
                        .min(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO);
                        
                rapport.put("paiementMax", max);
                rapport.put("paiementMin", min);
            }
            
            Map<String, Long> repartitionAgents = new HashMap<>();
            for (TypeAgent type : TypeAgent.values()) {
                long count = tousLesAgents.stream()
                        .filter(agent -> agent.getTypeAgent() == type)
                        .count();
                repartitionAgents.put(type.toString(), count);
            }
            rapport.put("repartitionAgents", repartitionAgents);
            
            return rapport;
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du rapport global: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    @Override
    public int obtenirNombreTotalDepartements() {
        try {
            List<Departement> departements = departementDao.lireTous();
            return departements.size();
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul du nombre total de départements: " + e.getMessage());
            return 0;
        }
    }
    
    @Override
    public int obtenirNombreTotalAgents() {
        try {
            List<Agent> agents = agentDao.lireTous();
            return agents.size();
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul du nombre total d'agents: " + e.getMessage());
            return 0;
        }
    }
    
    @Override
    public List<Agent> listerTousResponsables() {
        try {
            List<Agent> tousLesAgents = agentDao.lireTous();
            List<Agent> responsables = tousLesAgents.stream()
                .filter(agent -> agent.getTypeAgent() == TypeAgent.RESPONSABLE_DEPARTEMENT || 
                               agent.isEstResponsableDepartement())
                .collect(Collectors.toList());
            
            System.out.println("Responsables trouvés: " + responsables.size());
            return responsables;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des responsables: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public boolean supprimerResponsable(int idResponsable) {
        try {
            Agent responsable = agentDao.lireParId(idResponsable);
            if (responsable == null) {
                System.err.println("Responsable introuvable avec l'ID: " + idResponsable);
                return false;
            }
            
            if (!responsable.isEstResponsableDepartement() && 
                responsable.getTypeAgent() != TypeAgent.RESPONSABLE_DEPARTEMENT) {
                System.err.println("L'agent spécifié n'est pas un responsable");
                return false;
            }
            
            agentDao.supprimer(idResponsable);
            System.out.println("Responsable supprimé avec succès: " + responsable.getPrenom() + " " + responsable.getNom());
            return true;
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du responsable: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean modifierResponsable(int idResponsable, String nom, String prenom, String email) {
        try {
            Agent responsable = agentDao.lireParId(idResponsable);
            if (responsable == null) {
                System.err.println("Responsable introuvable avec l'ID: " + idResponsable);
                return false;
            }
            
            if (!responsable.isEstResponsableDepartement() && 
                responsable.getTypeAgent() != TypeAgent.RESPONSABLE_DEPARTEMENT) {
                System.err.println("L'agent spécifié n'est pas un responsable");
                return false;
            }
            
            responsable.setNom(nom);
            responsable.setPrenom(prenom);
            responsable.setEmail(email);
            
            agentDao.mettreAJour(responsable);
            System.out.println("Responsable modifié avec succès: " + prenom + " " + nom);
            return true;
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la modification du responsable: " + e.getMessage());
            return false;
        }
    }
}
