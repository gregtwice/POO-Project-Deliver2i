package modele;

import java.util.HashSet;

public class Instance {
    private String nom;
    private int DureeMin;
    private int DureeMax;
    private String date;

    private HashSet<Tournee> tournees;

    public Instance(String nom, int dureeMin, int dureeMax, String date) {
        this.nom = nom;
        DureeMin = dureeMin;
        DureeMax = dureeMax;
        this.date = date;
    }
}
