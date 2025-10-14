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
    protected AgentDao agentDao;
    protected PaiementDao paiementDao;
    protected DepartementDao departementDao;

    public AgentServiceImpl(AgentDao agentDao, PaiementDao paiementDao, DepartementDao departementDao) {
        this.agentDao = agentDao;
        this.paiementDao = paiementDao;
        this.departementDao = departementDao;
    }

    //menu agent choix 1
    @Override
    public Agent obtenirInformationsAgent(int agentId) {
        Agent agent = agentDao.lireParId(agentId);
        return agent;
    }

    //menu agent choix 6
    @Override
    public Map<TypePaiement, Integer> compterPrimesBonus(int agentId) {
        if (agentId <= 0) {
            return new HashMap<>();
        }
        Agent agent = agentDao.lireParId(agentId);
        if (agent == null) {
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
        return compteurs;
    }

    @Override
    public Agent authentifier(String email, String motDePasse) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        if (motDePasse == null || motDePasse.trim().isEmpty()) {
            return null;
        }
        Agent agent = agentDao.authentifier(email.trim(), motDePasse);
        return agent;
    }
}