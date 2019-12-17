package modele;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


@Entity
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "DUREE_MIN")
    private int DUREE_MIN;
    @Basic
    @Column(name = "DUREE_MAX")
    private int DUREE_MAX;

    @OneToMany( cascade = CascadeType.ALL)
    private Set<Tournee> tournees;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SOLUTION_ID")
    private Solution appartient;

    @Column(name = "TEMPS_TOTAL")
    private int tempsTotal;

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

    public Solution getAppartient() {
        return appartient;
    }

    public void setAppartient(Solution appartient) {
        this.appartient = appartient;
    }

    public int getTempsTotal() {
        return tempsTotal;
    }

    /**
     * Calcule la valeur du temps mort
     * @return la valeur du temps mort du shift
     */
    public int tempsMort() {
        int tm = 0;
        if (tournees.size() > 1) {
            ArrayList<Tournee> tourneesArrayList = new ArrayList<>(this.tournees);
            for (int i = 0; i < tourneesArrayList.size() - 1; i++) {
            }
        } else {
            Iterator<Tournee> iter = tournees.iterator();
            Tournee t = iter.next();
            if (t.temps() > this.DUREE_MIN) {
                tm = 0;
            } else {
                tm = this.DUREE_MIN - t.temps();
            }
        }

        return tm;
    }

    public boolean addTournee(Tournee tournee) {
        if (tournee.temps() > this.DUREE_MAX)
            return false;
        tournees.add(tournee);
        tempsTotal += tournee.temps();
        return true;
    }


}
