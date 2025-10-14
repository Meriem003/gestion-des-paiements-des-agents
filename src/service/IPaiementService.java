package service;

import model.Paiement;
import model.TypePaiement;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IPaiementService {
    Paiement creerPaiement(Paiement paiement);
    Paiement obtenirPaiementParId(int id);
    List<Paiement> obtenirTousLesPaiements();
    List<Paiement> obtenirPaiementsParAgent(int agentId);
    Paiement mettreAJourPaiement(Paiement paiement);
    boolean supprimerPaiement(int id);
    BigDecimal calculerSalaireTotalPeriode(int agentId, LocalDate dateDebut, LocalDate dateFin);
    Paiement traiterPaiement(int agentId, TypePaiement typePaiement, BigDecimal montant, String motif);
    boolean validerConditionsPaiement(Paiement paiement);
    List<Paiement> obtenirHistoriquePaiementsParType(int agentId, TypePaiement typePaiement);
    List<Paiement> trierPaiements(int agentId, String critere, boolean ascendant);
    Paiement creerBonusDirectParDirecteur(int directeurId, int responsableId, BigDecimal montant, String motif);

}