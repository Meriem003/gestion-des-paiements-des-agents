package model;

import java.util.List;

public class Agent extends Personne {
    private TypeAgent typeAgent;
    private Departement departement;
    private List<Paiement> paiements;
    private boolean estResponsableDepartement;

    public Agent() {}

    public Agent(int id, String nom, String prenom, String email, String motDePasse,
                 TypeAgent typeAgent, Departement departement, List<Paiement> paiements,
                 boolean estResponsableDepartement) {
        super(id, nom, prenom, email, motDePasse);
        this.typeAgent = typeAgent;
        this.departement = departement;
        this.paiements = paiements;
        this.estResponsableDepartement = estResponsableDepartement;
    }

    public TypeAgent getTypeAgent() {
        return typeAgent;
    }

    public void setTypeAgent(TypeAgent typeAgent) {
        this.typeAgent = typeAgent;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }

    public boolean isEstResponsableDepartement() {
        return estResponsableDepartement;
    }

    public void setEstResponsableDepartement(boolean estResponsableDepartement) {
        this.estResponsableDepartement = estResponsableDepartement;
    }
}