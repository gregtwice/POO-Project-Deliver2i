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

    @Transient
    private String nomAlgo;

    /**
     * Constructeur de la classe
     */
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

    public String getNomAlgo() {
        return nomAlgo;
    }

    public void setNomAlgo(String nomAlgo) {
        this.nomAlgo = nomAlgo;
    }

    /**
     * Algorithme le moins optimisé, il crée un shift pour chaque tournée
     */
    public void algoBasique() {
        for (Tournee tournee : instance.getTournees()) {
            Shift curShift = new Shift(instance.getDureeMin(), instance.getDureeMax());
            curShift.setAppartient(this);
            curShift.addTournee(tournee);
            tournee.addShift(curShift);
            shifts.add(curShift);
        }

        int tempsMort = 0;
        tempsMort = getTempsMort(tempsMort);
        System.out.println(tempsMort);

    }

    private int getTempsMort(int tempsMort) {
        for (Shift shift : shifts) {
            tempsMort += shift.tempsMort();
        }
        return tempsMort;
    }

    /**
     * Algorithme qui ajoute un maximum de tournées au shift
     */
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
            if (!flaginsert) {
                currshift = new Shift(instance.getDureeMin(), instance.getDureeMax());
                currshift.addTournee(tournee);
                this.shifts.add(currshift);
            }
        }
        int tempsMort = 0;
        tempsMort = getTempsMort(tempsMort);
        System.out.println(tempsMort);
    }

    /**
     * Algorithme qui remplit les tournées jusqu'a la durée minimale
     */
    public void algoCollage() {
        Shift currShift = new Shift(this.instance.getDureeMin(), this.instance.getDureeMax());
        // Pour chaque shift on cherche à coller les tournées le plus possible.
        // Si le shift
        this.shifts.add(currShift);
        for (Tournee tournee : instance.getTournees()) {
            boolean flaginsert = false;
            for (Shift shift : shifts) {
                if (shift.hasTempsMinimum())
                    continue;
                if (shift.addTournee(tournee)) {
                    flaginsert = true;
                    break;
                }
            }
            if (!flaginsert) {
                currShift = new Shift(instance.getDureeMin(), instance.getDureeMax());
                currShift.addTournee(tournee);
                this.shifts.add(currShift);
            }
        }
        int tempsMort = 0;
        tempsMort = getTempsMort(tempsMort);
        System.out.println(tempsMort);

    }

    /**
     * Algorithme qui optimise la solution de l'algo collage en prenant les shifts avec une seule tournée et en essyant
     * de les redistribuer sur des shifts valides
     */
    public void algoCollageOpti() {
        Shift currShift = new Shift(this.instance.getDureeMin(), this.instance.getDureeMax());
        // Pour chaque shift on cherche à coller les tournées le plus possible.
        // Si le shift
        this.shifts.add(currShift);
        for (Tournee tournee : instance.getTournees()) {
            boolean flaginsert = false;
            for (Shift shift : shifts) {
                if (shift.hasTempsMinimum())
                    continue;
                if (shift.addTournee(tournee)) {
                    flaginsert = true;
                    break;
                }
            }
            if (!flaginsert) {
                currShift = new Shift(instance.getDureeMin(), instance.getDureeMax());
                currShift.addTournee(tournee);
                this.shifts.add(currShift);
            }
        }
        ArrayList<Shift> badShifts = new ArrayList<>();
        // On prend les pire shifts
        for (Shift shift : shifts) {
            // Nombre arbitraire
            if (shift.getTournees().size() == 1) {
                badShifts.add(shift);
            }
        }
        for (Shift badShift : badShifts) {
            Shift bestShift = null;
            int minTempsMort = 9999;
            Tournee tournee = badShift.getTournees().get(0);
            for (Shift shift : shifts) {
                // si on peut ajouter au shift
                if (shift.getTournees().get(shift.getTournees().size() - 1).getFin().compareTo(tournee.getDebut()) <= 0) {
                    if (shift.addTournee(tournee)) {
                        if (minTempsMort > shift.tempsMort()) {
                            bestShift = shift;
                            minTempsMort = shift.tempsMort();
                        }
                        shift.popTournee();
                    }
                }
            }

            if (bestShift != null && bestShift.addTournee(tournee)) {
                this.shifts.remove(badShift);
            }
        }
        int tempsMort = 0;
        tempsMort = getTempsMort(tempsMort);
        System.out.println(tempsMort);

    }

}
