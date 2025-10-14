package service;

import model.Agent;
import model.Departement;
import model.Paiement;
import model.TypePaiement;
import java.util.List;
import java.util.Map;


public interface IAgentService {
    Agent obtenirInformationsAgent(int agentId);
    Map<TypePaiement, Integer> compterPrimesBonus(int agentId);
    Agent authentifier(String email, String motDePasse);
}