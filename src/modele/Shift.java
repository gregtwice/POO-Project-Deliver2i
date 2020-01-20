package modele;

import utils.DateMath;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "Shift.Sol", query = "select e from Shift e where e.appartient.id = :id")
})

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

    @ManyToMany(fetch = FetchType.EAGER)
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
        int duree = 0;
        for (Tournee tourne : tournees) {
            duree += tourne.getTempsTournee();
        }
        return tempsTotal - duree;
    }

    public List<Tournee> getTournees() {
        return tournees;
    }

    /**
     * Ajoute une tournée au shift
     *
     * @param tournee La tournée à ajouter, la tournée ne doit pas se superposer avec les tournées déja présentes dans
     *                le shift
     *
     * @return true si la tournée a été ajoutée, false sinon
     */
    public boolean addTournee(Tournee tournee) {
        // Si le shift est vide, alors on ajoute
        if (tournees.isEmpty()) {
            tournees.add(tournee);
            tempsTotal += tournee.getTempsTournee();
        } else {
            // sinon
            // est ce que les tournées se superposent

            for (Tournee t : tournees) {
                if (tournee.getDebut().getTime() <= t.getFin().getTime() && t.getDebut().getTime() <= t.getFin().getTime()) {
                    return false;
                }
            }

            // est ce que la tournée est dans le temps maximal ?
            int minutesDebut = (int) (this.tournees.get(0).getDebut().getTime() / 1000 / 60);
            int minutesFin = (int) (tournee.getFin().getTime() / 1000 / 60);
            if (minutesDebut + this.DUREE_MAX < minutesFin) {
                return false;
            }
            this.tournees.add(tournee);
//            tempsTotal += (int) (Last.getFin().getTime() - tournee.getDebut().getTime()) / 1000 / 60;
//            tempsTotal += tournee.getTempsTournee();
        }
        this.sortTournees();
        this.calculerTempTotal();
        return true;
    }

    private void sortTournees() {
        this.tournees.sort(Comparator.comparing(Tournee::getDebut));
    }

    private void calculerTempTotal() {
        this.tempsTotal = 0;
        this.tempsTotal = (int) (tournees.get(tournees.size() - 1).getFin().getTime() / 1000 / 60) - (int) (tournees.get(0).getDebut().getTime() / 1000 / 60);
        if (tempsTotal < DUREE_MIN) {
            this.tempsTotal += DUREE_MIN - tempsTotal;
        }
    }

    /**
     * Vérifie que le temps total de la tournée est supérieur au temps minimal défini par l'instance
     *
     * @return true si le temps total de la tournée est supérieur, false sinon
     */
    public boolean hasTempsMinimum() {
        int temps = 0;
        for (int i = 0; i < tournees.size(); i++) {
            Tournee tournee = tournees.get(i);
            temps += tournee.getTempsTournee();
            if (i != 0) {
                temps += (tournee.getDebut().getTime() - tournees.get(i - 1).getFin().getTime()) / 1000 / 60;
            }
        }
        return temps > this.DUREE_MIN;
    }

    public  Tournee lastTournee () {
       return this.getTournees().get(this.getTournees().size()-1);
    }

    /**
     * Retire la dernière tournée du shift
     *
     * @return la tournée qui a été retirée
     */
    public Tournee popTournee() {

        Tournee tournee = this.tournees.remove(this.tournees.size() - 1);
        this.calculerTempTotal();
        return tournee;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "id=" + id +
                ", DUREE_MIN=" + DUREE_MIN +
                ", DUREE_MAX=" + DUREE_MAX +
                ", tournees=" + tournees +
                ", tempsTotal=" + tempsTotal +
                '}';
    }
}
