package model;

import java.util.List;

public class Departement {
    private int id;
    private String nom;
    private Agent responsable;
    private List<Agent> agents;

    public Departement() {}

    public Departement(int id, String nom, Agent responsable, List<Agent> agents) {
        this.id = id;
        this.nom = nom;
        this.responsable = responsable;
        this.agents = agents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Agent getResponsable() {
        return responsable;
    }

    public void setResponsable(Agent responsable) {
        this.responsable = responsable;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    @Override
    public String toString() {
        return "Departement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", responsable=" + (responsable != null ? responsable.getPrenom() + " " + responsable.getNom() : "Aucun") +
                ", nombreAgents=" + (agents != null ? agents.size() : 0) +
                '}';
    }
}