package service.Iimpl;

import model.Agent;
import model.Departement;
import model.Paiement;
import model.TypeAgent;
import dao.Iimpl.AgentDao;
import dao.Iimpl.PaiementDao;
import dao.Iimpl.DepartementDao;
import service.IDirecteurService;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

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
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Permissions insuffisantes pour créer un département");
                return null;
            }
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
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Permissions insuffisantes pour modifier un département");
                return null;
            }
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
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Permissions insuffisantes pour supprimer un département");
                return false;
            }
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
    public boolean associerResponsableDepartement(int departementId, int responsableId, int directeurId) {
        try {
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Permissions insuffisantes pour associer un responsable");
                return false;
            }
            Departement dept = departementDao.lireParId(departementId);
            if (dept == null) {
                System.err.println("Département introuvable (ID: " + departementId + ")");
                return false;
            }
            Agent responsable = agentDao.lireParId(responsableId);
            if (responsable == null) {
                System.err.println("Agent responsable introuvable (ID: " + responsableId + ")");
                return false;
            }
            responsable.setEstResponsableDepartement(true);
            responsable.setDepartement(dept);
            agentDao.mettreAJour(responsable);
            System.out.println("Agent '" + responsable.getPrenom() + " " + responsable.getNom() +
                    "' associé comme responsable du département '" + dept.getNom() + "'");
            return true;

        } catch (Exception e) {
            System.err.println("Erreur lors de l'association du responsable: " + e.getMessage());
            return false;
        }
    }
    @Override
    public List<Departement> listerTousDepartements(int directeurId) {
        try {
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Permissions insuffisantes pour lister les départements");
                return new ArrayList<>();
            }
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
    public boolean verifierPermissionsDirecteur(int directeurId) {
        // TODO: Implémenter la vérification des permissions du directeur
        return true; // Implémentation temporaire
    }

    @Override
    public Agent creerUtilisateur(Agent agent, int directeurId) {
        try {
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Permissions insuffisantes pour créer un utilisateur");
                return null;
            }
            if (agent == null) {
                System.err.println("L'agent ne peut pas être null");
                return null;
            }
            if (agent.getNom() == null || agent.getNom().trim().isEmpty()) {
                System.err.println("Le nom de l'utilisateur est obligatoire");
                return null;
            }
            if (agent.getPrenom() == null || agent.getPrenom().trim().isEmpty()) {
                System.err.println("Le prénom de l'utilisateur est obligatoire");
                return null;
            }
            if (agent.getEmail() == null || agent.getEmail().trim().isEmpty()) {
                System.err.println("L'email de l'utilisateur est obligatoire");
                return null;
            }
            List<Agent> tousAgents = agentDao.lireTous();
            for (Agent agentExistant : tousAgents) {
                if (agentExistant.getEmail().equalsIgnoreCase(agent.getEmail().trim())) {
                    System.err.println("Un utilisateur avec cet email existe déjà: " + agent.getEmail());
                    return null;
                }
            }
            
            if (agent.getTypeAgent() == null) {
                agent.setTypeAgent(TypeAgent.RESPONSABLE_DEPARTEMENT);
                System.out.println("Type d'agent défini automatiquement: RESPONSABLE_DEPARTEMENT");
            } else if (agent.getTypeAgent() != TypeAgent.RESPONSABLE_DEPARTEMENT) {
                System.err.println("Erreur: Un directeur ne peut créer que des agents de type RESPONSABLE_DEPARTEMENT");
                System.err.println("Type demandé: " + agent.getTypeAgent());
                return null;
            }
            
            if (agent.getDepartement() == null) {
                System.out.println("⚠️  Attention: Agent créé sans département spécifique");
                System.out.println("   Utilisez creerUtilisateurAvecDepartement() pour spécifier un département");
            }
            if (agent.getMotDePasse() == null || agent.getMotDePasse().trim().isEmpty()) {
                String motDePasseTemporaire = genererMotDePasseTemporaire(agent.getNom(), agent.getPrenom());
                agent.setMotDePasse(motDePasseTemporaire);
                System.out.println("Mot de passe temporaire généré: " + motDePasseTemporaire);
                System.out.println("L'utilisateur devra le changer lors de sa première connexion.");
            }
            agentDao.creer(agent);
            System.out.println("Utilisateur '" + agent.getPrenom() + " " + agent.getNom() + "' créé avec succès");
            System.out.println("  ID: " + agent.getId());
            System.out.println("  Type: " + agent.getTypeAgent());
            System.out.println("  Email: " + agent.getEmail());

            return agent;

        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'utilisateur: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Agent creerUtilisateurAvecDepartement(Agent agent, int departementId, int directeurId) {
        try {
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Permissions insuffisantes pour créer un utilisateur");
                return null;
            }
            if (agent == null) {
                System.err.println("L'agent ne peut pas être null");
                return null;
            }
            Departement departement = departementDao.lireParId(departementId);
            if (departement == null) {
                System.err.println("Département avec l'ID " + departementId + " introuvable");
                return null;
            }
            if (agent.getNom() == null || agent.getNom().trim().isEmpty()) {
                System.err.println("Le nom de l'utilisateur est obligatoire");
                return null;
            }
            if (agent.getPrenom() == null || agent.getPrenom().trim().isEmpty()) {
                System.err.println("Le prénom de l'utilisateur est obligatoire");
                return null;
            }

            if (agent.getEmail() == null || agent.getEmail().trim().isEmpty()) {
                System.err.println("L'email de l'utilisateur est obligatoire");
                return null;
            }
            List<Agent> tousAgents = agentDao.lireTous();
            for (Agent agentExistant : tousAgents) {
                if (agentExistant.getEmail().equalsIgnoreCase(agent.getEmail().trim())) {
                    System.err.println("Un utilisateur avec cet email existe déjà: " + agent.getEmail());
                    return null;
                }
            }
            if (agent.getTypeAgent() == null) {
                agent.setTypeAgent(TypeAgent.OUVRIER);
            }
            agent.setDepartement(departement);
            if (agent.getTypeAgent() == TypeAgent.RESPONSABLE_DEPARTEMENT) {
                agent.setEstResponsableDepartement(true);
                System.out.println("✅ Agent configuré comme responsable du département: " + departement.getNom());
            } else {
                agent.setEstResponsableDepartement(false);
            }
            if (agent.getMotDePasse() == null || agent.getMotDePasse().trim().isEmpty()) {
                String motDePasseTemporaire = genererMotDePasseTemporaire(agent.getNom(), agent.getPrenom());
                agent.setMotDePasse(motDePasseTemporaire);
                System.out.println("Mot de passe temporaire généré: " + motDePasseTemporaire);
                System.out.println("L'utilisateur devra le changer lors de sa première connexion.");
            }
            agentDao.creer(agent);
            System.out.println("Utilisateur '" + agent.getPrenom() + " " + agent.getNom() + "' créé avec succès");
            System.out.println("  ID: " + agent.getId());
            System.out.println("  Type: " + agent.getTypeAgent());
            System.out.println("  Email: " + agent.getEmail());
            System.out.println("  Département: " + departement.getNom());
            System.out.println("  Responsable de département: " + agent.isEstResponsableDepartement());

            System.out.println("[AUDIT] Création utilisateur - Directeur ID: " + directeurId +
                    " a créé l'agent ID: " + agent.getId() + " dans le département " + departement.getNom());

            return agent;

        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'utilisateur avec département: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Agent modifierDroitsUtilisateur(int agentId, TypeAgent nouveauType, int directeurId) {
        try {
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Permissions insuffisantes pour modifier les droits d'un utilisateur");
                return null;
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Utilisateur introuvable avec l'ID: " + agentId);
                return null;
            }
            if (nouveauType == null) {
                System.err.println("Le nouveau type d'utilisateur ne peut pas être null");
                return null;
            }
            TypeAgent ancienType = agent.getTypeAgent();
            if (agent.getId() == directeurId && nouveauType != TypeAgent.DIRECTEUR) {
                System.err.println("Un directeur ne peut pas modifier ses propres droits pour retirer son rôle de directeur");
                return null;
            }
            if (nouveauType == TypeAgent.RESPONSABLE_DEPARTEMENT) {
                agent.setEstResponsableDepartement(true);
            } else if (ancienType == TypeAgent.RESPONSABLE_DEPARTEMENT && nouveauType != TypeAgent.RESPONSABLE_DEPARTEMENT) {
                agent.setEstResponsableDepartement(false);
            }
            agent.setTypeAgent(nouveauType);
            agentDao.mettreAJour(agent);

            System.out.println("Droits de l'utilisateur modifiés avec succès:");
            System.out.println("  Utilisateur: " + agent.getPrenom() + " " + agent.getNom());
            System.out.println("  Ancien type: " + ancienType);
            System.out.println("  Nouveau type: " + nouveauType);
            System.out.println("[AUDIT] Modification des droits - Directeur ID: " + directeurId +
                    " a modifié les droits de l'agent ID: " + agentId +
                    " de " + ancienType + " vers " + nouveauType);

            return agent;

        } catch (Exception e) {
            System.err.println("Erreur lors de la modification des droits: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean changerStatutUtilisateur(int agentId, boolean actif, int directeurId) {
        try {
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Permissions insuffisantes pour changer le statut d'un utilisateur");
                return false;
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Utilisateur introuvable avec l'ID: " + agentId);
                return false;
            }
            if (agent.getId() == directeurId && !actif) {
                System.err.println("Un directeur ne peut pas désactiver son propre compte");
                return false;
            }

            String action = actif ? "activé" : "désactivé";
            String actionEnCours = actif ? "activation" : "désactivation";
            System.out.println("Utilisateur " + action + " avec succès:");
            System.out.println("  Utilisateur: " + agent.getPrenom() + " " + agent.getNom() + " (ID: " + agent.getId() + ")");
            System.out.println("  Nouveau statut: " + (actif ? "ACTIF" : "INACTIF"));
            System.out.println("[AUDIT] " + actionEnCours + " utilisateur - Directeur ID: " + directeurId +
                    " a " + action + " l'agent ID: " + agentId);
            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors du changement de statut: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean reinitialiserMotDePasse(int agentId, String nouveauMotDePasse, int directeurId) {
        try {
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Permissions insuffisantes pour réinitialiser un mot de passe");
                return false;
            }
            Agent agent = agentDao.lireParId(agentId);
            if (agent == null) {
                System.err.println("Utilisateur introuvable avec l'ID: " + agentId);
                return false;
            }
            String motDePasse;
            if (nouveauMotDePasse == null || nouveauMotDePasse.trim().isEmpty()) {
                motDePasse = genererMotDePasseTemporaire(agent.getNom(), agent.getPrenom());
                System.out.println("Mot de passe temporaire généré automatiquement");
            } else {
                motDePasse = nouveauMotDePasse.trim();
                if (motDePasse.length() < 6) {
                    System.err.println("Le mot de passe doit contenir au moins 6 caractères");
                    return false;
                }
            }
            agent.setMotDePasse(motDePasse);
            agentDao.mettreAJour(agent);
            System.out.println("Mot de passe réinitialisé avec succès:");
            System.out.println("  Utilisateur: " + agent.getPrenom() + " " + agent.getNom() + " (ID: " + agent.getId() + ")");
            if (nouveauMotDePasse == null || nouveauMotDePasse.trim().isEmpty()) {
                System.out.println("  Nouveau mot de passe temporaire: " + motDePasse);
            } else {
                System.out.println("  Mot de passe personnalisé défini");
            }
            System.out.println("  L'utilisateur devra changer ce mot de passe lors de sa prochaine connexion.");
            System.out.println("[AUDIT] Réinitialisation mot de passe - Directeur ID: " + directeurId +
                    " a réinitialisé le mot de passe de l'agent ID: " + agentId);

            return true;

        } catch (Exception e) {
            System.err.println("Erreur lors de la réinitialisation du mot de passe: " + e.getMessage());
            return false;
        }
    }

    private String genererMotDePasseTemporaire(String nom, String prenom) {
        String base = nom.substring(0, Math.min(3, nom.length())).toLowerCase() +
                prenom.substring(0, Math.min(3, prenom.length())).toLowerCase();
        int numero = (int) (Math.random() * 900) + 100;

        return base + numero;
    }

    private Departement obtenirDepartementParDefaut(int directeurId) {
        try {
            Agent directeur = agentDao.lireParId(directeurId);
            if (directeur != null && directeur.getDepartement() != null) {
                return directeur.getDepartement();
            }

            List<Departement> departements = departementDao.lireTous();
            if (!departements.isEmpty()) {
                Departement premierDept = departements.get(0);
                System.out.println("Utilisation du département par défaut: " + premierDept.getNom());
                return premierDept;
            }

            System.err.println("Aucun département disponible dans le système");
            return null;

        } catch (Exception e) {
            System.err.println("Erreur lors de l'obtention du département par défaut: " + e.getMessage());
            return null;
        }
    }

    public void corrigerAgentsSansDepartement(int directeurId) {
        try {
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Permissions insuffisantes pour corriger les agents sans département");
                return;
            }

            List<Agent> tousAgents = agentDao.lireTous();
            Departement departementParDefaut = obtenirDepartementParDefaut(directeurId);

            if (departementParDefaut == null) {
                System.err.println("Impossible de corriger: aucun département disponible");
                return;
            }

            int agentsCorrigés = 0;
            for (Agent agent : tousAgents) {
                if (agent.getDepartement() == null) {
                    agent.setDepartement(departementParDefaut);
                    agentDao.mettreAJour(agent);
                    agentsCorrigés++;
                    System.out.println("Agent corrigé: " + agent.getPrenom() + " " + agent.getNom() +
                            " assigné au département " + departementParDefaut.getNom());
                }
            }

            if (agentsCorrigés > 0) {
                System.out.println("✅ " + agentsCorrigés + " agent(s) corrigé(s) avec succès");
                System.out.println("[AUDIT] Correction département - Directeur ID: " + directeurId +
                        " a corrigé " + agentsCorrigés + " agent(s) sans département");
            } else {
                System.out.println("✅ Tous les agents ont déjà un département assigné");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la correction des agents: " + e.getMessage());
        }
    }

    @Override
    public Agent obtenirInformationsAgent(int agentId) {
        return super.obtenirInformationsAgent(agentId);
    }

    @Override
    public Agent mettreAJourInformationsAgent(Agent agent) {
        return super.mettreAJourInformationsAgent(agent);
    }

    @Override
    public Departement obtenirDepartementAgent(int agentId) {
        return super.obtenirDepartementAgent(agentId);
    }

    @Override
    public List<Paiement> obtenirHistoriquePaiements(int agentId) {
        return super.obtenirHistoriquePaiements(agentId);
    }

    @Override
    public boolean validerDemandeBonus(int demandeId, boolean approuve, String motifRejet, int directeurId) {
        try {
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le directeur ID " + directeurId);
                return false;
            }
            if (demandeId <= 0) {
                System.err.println("Erreur: ID de demande invalide");
                return false;
            }
            Paiement demande = null;
            List<Agent> tousLesAgents = agentDao.lireTous();
            for (Agent agent : tousLesAgents) {
                List<Paiement> paiementsAgent = super.obtenirHistoriquePaiements(agent.getId());
                for (Paiement paiement : paiementsAgent) {
                    if (paiement.getId() == demandeId) {
                        demande = paiement;
                        break;
                    }
                }
                if (demande != null) break;
            }

            if (demande == null) {
                System.err.println("Erreur: Demande de bonus non trouvée avec l'ID " + demandeId);
                return false;
            }
            if (demande.getTypePaiement() != model.TypePaiement.BONUS) {
                System.err.println("Erreur: La demande ID " + demandeId + " n'est pas une demande de bonus");
                return false;
            }
            if (demande.isConditionValidee()) {
                System.err.println("Erreur: La demande de bonus ID " + demandeId + " est déjà validée");
                return false;
            }
            if (approuve) {
                demande.setConditionValidee(true);
                demande.setMotif(demande.getMotif() + " - Approuvé par directeur ID " + directeurId);
                System.out.println("Demande de bonus ID " + demandeId + " approuvée avec succès");
            } else {
                String nouveauMotif = demande.getMotif() + " - Rejetée par directeur ID " + directeurId;
                if (motifRejet != null && !motifRejet.trim().isEmpty()) {
                    nouveauMotif += " - Motif: " + motifRejet;
                }
                demande.setMotif(nouveauMotif);
                demande.setConditionValidee(false);
                System.out.println("Demande de bonus ID " + demandeId + " rejetée. Motif: " + motifRejet);
            }
            System.out.println("Validation de la demande de bonus terminée");
            return true;

        } catch (Exception e) {
            System.err.println("Erreur lors de la validation de la demande de bonus: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean validerDemandeIndemnite(int demandeId, boolean approuve, String motifRejet, int directeurId) {
        try {
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le directeur ID " + directeurId);
                return false;
            }
            if (demandeId <= 0) {
                System.err.println("Erreur: ID de demande invalide");
                return false;
            }
            Paiement demande = null;
            List<Agent> tousLesAgents = agentDao.lireTous();
            for (Agent agent : tousLesAgents) {
                List<Paiement> paiementsAgent = super.obtenirHistoriquePaiements(agent.getId());
                for (Paiement paiement : paiementsAgent) {
                    if (paiement.getId() == demandeId) {
                        demande = paiement;
                        break;
                    }
                }
                if (demande != null) break;
            }

            if (demande == null) {
                System.err.println("Erreur: Demande d'indemnité non trouvée avec l'ID " + demandeId);
                return false;
            }
            if (demande.getTypePaiement() != model.TypePaiement.INDEMNITE) {
                System.err.println("Erreur: La demande ID " + demandeId + " n'est pas une demande d'indemnité");
                return false;
            }
            if (demande.isConditionValidee()) {
                System.err.println("Erreur: La demande d'indemnité ID " + demandeId + " est déjà validée");
                return false;
            }
            if (approuve) {
                demande.setConditionValidee(true);
                demande.setMotif(demande.getMotif() + " - Approuvée par directeur ID " + directeurId);
                System.out.println("Demande d'indemnité ID " + demandeId + " approuvée avec succès");
            } else {
                String nouveauMotif = demande.getMotif() + " - Rejetée par directeur ID " + directeurId;
                if (motifRejet != null && !motifRejet.trim().isEmpty()) {
                    nouveauMotif += " - Motif: " + motifRejet;
                }
                demande.setMotif(nouveauMotif);
                demande.setConditionValidee(false);
                System.out.println("Demande d'indemnité ID " + demandeId + " rejetée. Motif: " + motifRejet);
            }
            System.out.println("Validation de la demande d'indemnité terminée");
            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors de la validation de la demande d'indemnité: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> consulterDemandesEnAttente(int directeurId) {
        try {
            if (!verifierPermissionsDirecteur(directeurId)) {
                System.err.println("Erreur: Permissions insuffisantes pour le directeur ID " + directeurId);
                return new ArrayList<>();
            }
            List<Map<String, Object>> demandesEnAttente = new ArrayList<>();
            List<Agent> tousLesAgents = agentDao.lireTous();
            for (Agent agent : tousLesAgents) {
                List<Paiement> paiementsAgent = super.obtenirHistoriquePaiements(agent.getId());
                for (Paiement paiement : paiementsAgent) {
                    if (!paiement.isConditionValidee() && 
                        (paiement.getTypePaiement() == model.TypePaiement.BONUS || 
                         paiement.getTypePaiement() == model.TypePaiement.INDEMNITE)) {
                        
                        Map<String, Object> demande = new HashMap<>();
                        demande.put("id", paiement.getId());
                        demande.put("type", paiement.getTypePaiement().toString());
                        demande.put("montant", paiement.getMontant());
                        demande.put("dateDemande", paiement.getDatePaiement());
                        demande.put("motif", paiement.getMotif());
                        demande.put("agentId", agent.getId());
                        demande.put("agentNom", agent.getNom());
                        demande.put("agentPrenom", agent.getPrenom());
                        
                        if (agent.getDepartement() != null) {
                            demande.put("departementId", agent.getDepartement().getId());
                            demande.put("departementNom", agent.getDepartement().getNom());
                        } else {
                            demande.put("departementId", null);
                            demande.put("departementNom", "Non assigné");
                        }
                        
                        demandesEnAttente.add(demande);
                    }
                }
            }
            demandesEnAttente.sort((d1, d2) -> {
                java.time.LocalDate date1 = (java.time.LocalDate) d1.get("dateDemande");
                java.time.LocalDate date2 = (java.time.LocalDate) d2.get("dateDemande");
                return date1.compareTo(date2);
            });

            System.out.println("Récupération de " + demandesEnAttente.size() + " demandes en attente de validation");
            return demandesEnAttente;

        } catch (Exception e) {
            System.err.println("Erreur lors de la consultation des demandes en attente: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
