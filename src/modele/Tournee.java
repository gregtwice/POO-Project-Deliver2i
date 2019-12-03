package modele;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Tournee {

    @ManyToOne
    private Instance appartient;

    @ManyToOne
    private Shift shift;
    @Basic
    private int debut;
    private int fin;
    @Id
    private Long id;

    public Tournee(int debut, int fin) {
        this.debut = debut;
        this.fin = fin;
    }

    public Tournee() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
