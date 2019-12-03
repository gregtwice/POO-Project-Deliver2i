package modele;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Shift {

    @Id
    private Long id;
    @Basic
    private int DUREE_MIN;
    @Basic
    private int DUREE_MAX;

    @OneToMany(mappedBy = "shift")
    private Set<Tournee> tournees;
    @ManyToOne
    private Solution appartient;


    public Shift(int DUREE_MIN, int DUREE_MAX) {
        this.DUREE_MIN = DUREE_MIN;
        this.DUREE_MAX = DUREE_MAX;
        this.tournees = new HashSet<>();
    }

    public Shift() {
        this.tournees = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
