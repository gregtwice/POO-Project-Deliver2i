package modele;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Tournee {

    @ManyToOne(cascade = CascadeType.ALL)
    private Instance appartient;

    @ManyToMany(mappedBy = "tournees")
    private Set<Shift> shifts;

    @Basic
    private Date debut;
    private Date fin;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Tournee(Date debut, Date fin) {
        this();
        this.debut = debut;
        this.fin = fin;
        this.shifts = new HashSet<>();
    }

    public Tournee() {

    }

    public Set<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(Set<Shift> shifts) {
        this.shifts = shifts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Tournee{" +
                "debut=" + debut +
                ", fin=" + fin +
                '}';
    }

    public Date getDebut() {
        return debut;
    }

    public Date getFin() {
        return fin;
    }

    public int getTempsTournee() {
        return (int) ((fin.getTime() - debut.getTime()) / 1000 / 60);
    }

    public Instance getAppartient() {
        return appartient;
    }

    public void setAppartient(Instance appartient) {
        this.appartient = appartient;
    }

    /**
     * Ajoute le shift à la tournée
     *
     * @param shift le shift à ajouter
     */
    public void addShift(Shift shift) {
        this.shifts.add(shift);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tournee tournee = (Tournee) o;
        return this.getDebut().equals(tournee.getDebut()) &&
                this.getFin().equals(tournee.getFin()) &&
                this.getId().equals(tournee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDebut(), getFin(), getId());
    }
}
