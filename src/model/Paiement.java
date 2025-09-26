package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Paiement {
    private int id;
    private TypePaiement typePaiement;
    private BigDecimal montant;
    private LocalDate datePaiement;
    private String motif;
    private boolean conditionValidee;
    private Agent agent;

    public Paiement() {}

    public Paiement(int id, TypePaiement typePaiement, BigDecimal montant,
                    LocalDate datePaiement, String motif, boolean conditionValidee, Agent agent) {
        this.id = id;
        this.typePaiement = typePaiement;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.motif = motif;
        this.conditionValidee = conditionValidee;
        this.agent = agent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypePaiement getTypePaiement() {
        return typePaiement;
    }

    public void setTypePaiement(TypePaiement typePaiement) {
        this.typePaiement = typePaiement;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public boolean isConditionValidee() {
        return conditionValidee;
    }

    public void setConditionValidee(boolean conditionValidee) {
        this.conditionValidee = conditionValidee;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}