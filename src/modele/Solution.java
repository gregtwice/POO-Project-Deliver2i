package modele;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "INSTANCE_ID")
    private Instance instance;

    @OneToMany(mappedBy = "appartient", cascade = CascadeType.ALL)
    private Set<Shift> shifts;

    public Solution() {this.shifts = new HashSet<>();}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public Set<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(Set<Shift> shifts) {
        this.shifts = shifts;
    }


    public void algoBasique() {
        for (Tournee tournee : instance.getTournees()) {
            Shift curShift = new Shift(instance.getDureeMin(), instance.getDureeMax());
            curShift.setAppartient(this);
            curShift.addTournee(tournee);
            tournee.addShift(curShift);
            shifts.add(curShift);
        }

        int tempsMort = 0;
        for (Shift shift : shifts) {
            tempsMort += shift.tempsMort();
        }
        System.out.println(tempsMort);

    }

}
