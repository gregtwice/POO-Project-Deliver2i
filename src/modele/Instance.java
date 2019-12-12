package modele;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Instance {
    @Basic
    private String nom;
    @Basic
    private int dureeMin;
    @Basic
    private int dureeMax;
    @Basic
    private Date date;

    @OneToMany(mappedBy = "appartient", cascade = CascadeType.ALL)
    private Set<Tournee> tournees;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Instance(String nom, int dureeMin, int dureeMax, Date date) {
        this.nom = nom;
        this.dureeMin = dureeMin;
        this.dureeMax = dureeMax;
        this.date = date;
        this.tournees = new HashSet<>();
    }

    public Instance() {
        this.tournees = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addTournee(Tournee tournee) {
        this.tournees.add(tournee);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getDureeMin() {
        return dureeMin;
    }

    public void setDureeMin(int dureeMin) {
        this.dureeMin = dureeMin;
    }

    public int getDureeMax() {
        return dureeMax;
    }

    public void setDureeMax(int dureeMax) {
        this.dureeMax = dureeMax;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Tournee> getTournees() {
        return tournees;
    }

    public void setTournees(Set<Tournee> tournees) {
        this.tournees = tournees;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "nom='" + nom + '\'' +
                ", dureeMin=" + dureeMin +
                ", dureeMax=" + dureeMax +
                ", date=" + date +
                ", tournees=" + tournees +
                ", id=" + id +
                '}';
    }
}
