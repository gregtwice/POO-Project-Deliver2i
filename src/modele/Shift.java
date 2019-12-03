package modele;

import java.util.HashSet;

public class Shift {
    private int DUREE_MIN;
    private int DUREE_MAX;

    private HashSet<Tournee> tournees;

    public Shift(int DUREE_MIN, int DUREE_MAX) {
        this.DUREE_MIN = DUREE_MIN;
        this.DUREE_MAX = DUREE_MAX;
        this.tournees = new HashSet<>();
    }
}
