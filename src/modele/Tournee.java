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

    @ManyToMany
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int result = debut != null ? debut.hashCode() : 0;
        result = 31 * result + (fin != null ? fin.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tournee tournee = (Tournee) o;
        if (!Objects.equals(debut, tournee.debut)) return false;
        return Objects.equals(fin, tournee.fin);
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

    public int temps() {
        return (int) ((fin.getTime() - debut.getTime()) / 1000 / 60);
    }


    public Instance getAppartient() {
        return appartient;
    }

    public void setAppartient(Instance appartient) {
        this.appartient = appartient;
    }

    public void addShift(Shift shift) {
        this.shifts.add(shift);
    }
}
