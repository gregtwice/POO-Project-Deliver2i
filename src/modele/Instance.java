package modele;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    private String date;

    @OneToMany(mappedBy = "appartient")
    private Set<Tournee> tournees;

    @Id
    private Long id;

    public Instance(String nom, int dureeMin, int dureeMax, String date) {
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
}
