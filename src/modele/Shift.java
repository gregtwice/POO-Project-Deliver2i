package modele;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Entity
public class Shift {

    /************************************************/
    /*                                              */
    /*                Attributs JPA                 */
    /*                                              */
    /************************************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "DUREE_MIN")
    private int DUREE_MIN;
    @Basic
    @Column(name = "DUREE_MAX")
    private int DUREE_MAX;

    @ManyToMany()
    @JoinTable(
            name = "tournee_in_shift",
            joinColumns = @JoinColumn(name = "tournee_id"),
            inverseJoinColumns = @JoinColumn(name = "shift_id")
    )
    private List<Tournee> tournees;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SOLUTION_ID")
    private Solution appartient;

    @Column(name = "TEMPS_TOTAL")
    private int tempsTotal;

    /**
     * Constructeur de la classe Shift
     *
     * @param DUREE_MIN la durée minimum d'un Shift
     * @param DUREE_MAX la durée maximale d'un Shift
     */

    public Shift(int DUREE_MIN, int DUREE_MAX) {
        this.DUREE_MIN = DUREE_MIN;
        this.DUREE_MAX = DUREE_MAX;
        this.tournees = new ArrayList<>();
    }

    public Shift() {

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
     *
     * @return la valeur du temps mort du shift
     */
    public int tempsMort() {
        int tempsMort = 0;
        if (tournees.size() > 1) {
            ArrayList<Tournee> tourneesArrayList = new ArrayList<>(this.tournees);
            for (int i = 0; i < tourneesArrayList.size() - 1; i++) {

            }
//            throw new UnsupportedOperationException("Not Implemented yet");
        } else {
            Iterator<Tournee> iter = tournees.iterator();
            Tournee tournee = iter.next();
            if (tournee.getTempsTournee() > this.DUREE_MIN) {
                tempsMort = 0;
            } else {
                tempsMort = this.DUREE_MIN - tournee.getTempsTournee();
            }
        }

        return tempsMort;
    }

    public List<Tournee> getTournees() {
        return tournees;
    }

    public boolean addTournee(Tournee tournee) {
        // Si le shift est vide, alors on ajoute
        if (tournees.isEmpty()) {
            tournees.add(tournee);
            tempsTotal += tournee.getTempsTournee();
        } else {
            // sinon
            // est ce que les tournées se superposent
            Tournee Last = tournees.get(tournees.size() - 1);
            if (Last.getFin().compareTo(tournee.getDebut()) > 0) {
                return false;
            }
            // est ce que la tournée est dans le temps maximal ?
            int minutesDebut = (int) (this.tournees.get(0).getDebut().getTime() / 1000 / 60);
            int minutesFin = (int) (tournee.getFin().getTime() / 1000 / 60);
            if (minutesDebut + this.DUREE_MAX < minutesFin) {
                return false;
            }

            this.tournees.add(tournee);
            tempsTotal += (int) (Last.getFin().getTime() - tournee.getDebut().getTime()) / 1000 / 60;
            tempsTotal += tournee.getTempsTournee();
        }
        return true;
    }


}
