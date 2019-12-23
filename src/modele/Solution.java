package modele;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "INSTANCE_ID")
    private Instance instance;

    @OneToMany(mappedBy = "appartient", cascade = CascadeType.ALL)
    private List<Shift> shifts;

    public Solution() {this.shifts = new ArrayList<>();}

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

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
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

    public void algoBourrage() {
        Shift currshift = new Shift(instance.getDureeMin(), instance.getDureeMax());
        this.shifts.add(currshift);
        for (Tournee tournee : instance.getTournees()) {
            boolean flaginsert = false;
            for (Shift shift : shifts) {
                if (shift.addTournee(tournee)) {
                    flaginsert = true;
                    break;
                }
            }
            if (!flaginsert){
                currshift = new Shift(instance.getDureeMin(), instance.getDureeMax());
                currshift.addTournee(tournee);
                this.shifts.add(currshift);
            }
        }
        int tempsMort = 0;
        for (Shift shift : shifts) {
            tempsMort += shift.tempsMort();
        }
        System.out.println(tempsMort);
    }
}
