package modele;

import utils.DateMath;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private String nomAlgo;

    /**
     * Constructeur de la classe
     */
    public Solution() {
        this.shifts = new ArrayList<>();
    }

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

    public boolean tourneeSol(Tournee tournee){

        for (Shift shift : shifts) {
           for (Tournee tournee1 : shift.getTournees()){
               if (tournee.equals(tournee1)){
                   return true;
               }
           }
        }
        return false;

    }

    /**
     * Algorithme le moins optimisé, il crée un shift pour chaque tournée
     *
     * @return
     */
    public int algoBasique() {
        for (Tournee tournee : instance.getTournees()) {
            Shift curShift = new Shift(instance.getDureeMin(), instance.getDureeMax());
            curShift.setAppartient(this);
            curShift.addTournee(tournee);
            tournee.addShift(curShift);
            shifts.add(curShift);
        }

        int tempsMort = getTempsMort();
        System.out.println(tempsMort);
        return tempsMort;
    }

    private int getTempsMort() {
        int tempsMort = 0;
        int tournees = 0;
        for (Shift shift : shifts) {
            tempsMort += shift.tempsMort();
            tournees += shift.getTournees().size();
        }
        System.out.print("\tnb Tournées = " + tournees + " \t");
        return tempsMort;
    }

    /**
     * Algorithme qui ajoute un maximum de tournées au shift
     *
     * @return
     */
    public int algoBourrage() {
        Shift currshift = new Shift(instance.getDureeMin(), instance.getDureeMax());
        currshift.setAppartient(this);
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
                currshift.setAppartient(this);
                this.shifts.add(currshift);
            }
        }
        int tempsMort = getTempsMort();
        System.out.println(tempsMort);
        return tempsMort;
    }

    /**
     * Algorithme qui remplit les tournées jusqu'a la durée minimale
     *
     * @return
     */
    public int algoCollage() {
        Shift currShift = new Shift(this.instance.getDureeMin(), this.instance.getDureeMax());
        currShift.setAppartient(this);
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
                currShift.setAppartient(this);
                currShift.addTournee(tournee);
                this.shifts.add(currShift);
            }
        }
        int tempsMort = getTempsMort();
        System.out.println(tempsMort);
        return tempsMort;
    }

    /**
     * Algorithme qui optimise la solution de l'algo collage en prenant les shifts avec une seule tournée et en essyant
     * de les redistribuer sur des shifts valides
     *
     * @return le temps mort
     */
    public int algoCollageOpti() {
        Shift currShift = new Shift(this.instance.getDureeMin(), this.instance.getDureeMax());
        currShift.setAppartient(this);
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
                currShift.setAppartient(this);
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
        int tempsMort = getTempsMort();
        System.out.println(tempsMort);
        return tempsMort;
    }

    public int algoPlusProche() {
        Shift currShift = new Shift(this.instance.getDureeMin(), this.instance.getDureeMax());
        currShift.setAppartient(this);
        this.shifts.add(currShift);

        // Les tournées sont classés par début croissant
        for (Tournee tournee : this.instance.getTournees()) {

            Shift bestShift = null;
            int bestTempMort = Integer.MAX_VALUE;

            for (Shift shift : shifts) {
                if (shift.addTournee(tournee)) {
                    if (shift.tempsMort() < bestTempMort) {
                        if (bestShift != null) {
                            bestShift.popTournee();
                        }
                        bestShift = shift;
                        bestTempMort = shift.tempsMort();
                    } else {
                        shift.popTournee();
                    }
                }
            }
            if (bestShift == null) {
                currShift = new Shift(instance.getDureeMin(), instance.getDureeMax());
                currShift.setAppartient(this);
                currShift.addTournee(tournee);
                this.shifts.add(currShift);
            }
        }
        int tempsMort = getTempsMort();
        System.out.println(tempsMort);
        return tempsMort;
    }


    @Override
    public String toString() {
        return "Solution{" +
                "nomAlgo='" + nomAlgo + '\'' +
                '}';
    }
}
