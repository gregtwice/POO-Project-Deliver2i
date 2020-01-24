package modele;

import javax.persistence.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

@NamedQueries({
        @NamedQuery(name = "Instance.All", query = "select e from Instance e"),
        @NamedQuery(name = "Instance.Solutions", query = "select s from Solution s where s.instance.id= :id")
})
@Entity
public class Instance {
    @Basic
    private String nom;
    @Basic
    private int dureeMin;
    @Basic
    private int dureeMax;
    @Basic
    private Date date;

    @OneToMany(mappedBy = "appartient", cascade = CascadeType.ALL)
    private List<Tournee> tournees;

    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL)
    private Set<Solution> solutions;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Constructeur de la classe Instance
     *
     * @param nom      Le nom de l'instance, contenu en général dans un fichier instance.csv
     * @param dureeMin La durée minimale d'un shift
     * @param dureeMax La durée maximale d'un shift
     * @param date     La date du jour contenue dans la fichier
     */
    public Instance(String nom, int dureeMin, int dureeMax, Date date) {
        this.nom = nom;
        this.dureeMin = dureeMin;
        this.dureeMax = dureeMax;
        this.date = date;
        this.tournees = new ArrayList<>();
        this.solutions = new HashSet<>();
    }

    public Instance() {
        this.tournees = new ArrayList<>();
        this.solutions = new HashSet<>();
    }

    /**
     * @return L'id de l'instance
     */
    public Long getId() {
        return id;
    }

    /**
     * Met a jour l'id de l'instance
     *
     * @param id un id supérieur à 0
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Ajoute une tournée à l'instance
     *
     * @param tournee
     */
    public void addTournee(Tournee tournee) {
        this.tournees.add(tournee);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getDureeMin() {
        return dureeMin;
    }

    public void setDureeMin(int dureeMin) {
        this.dureeMin = dureeMin;
    }

    public int getDureeMax() {
        return dureeMax;
    }

    public void setDureeMax(int dureeMax) {
        this.dureeMax = dureeMax;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Tournee> getTournees() {
        return tournees;
    }

    public void setTournees(List<Tournee> tournees) {
        this.tournees = tournees;
    }

    public Set<Solution> getSolutions() {
        return solutions;
    }

    /**
     * Appelle toutes les fonctions contenant le mot "algo" de la classe solution
     */
    public void allAlgos() {
        int bestTM= Integer.MAX_VALUE;
        String bestMethode = "";
        for (Method m : Solution.class.getMethods()) {
            if (m.getName().contains("algo")) {
                Solution s = new Solution();
                s.setInstance(this);
                try {
                    System.out.print("\t" + m.getName() + " : \t");
                    int tempsMort = (int) m.invoke(s);
                    if (bestTM > tempsMort){
                        bestMethode = m.getName();
                        bestTM = tempsMort;
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                s.setNomAlgo(m.getName());
                addSolution(s);
            }
        }
        System.out.println("La meilleure méthode est : " + bestMethode +", avec " + bestTM + " minutes de temps Mort");

    }

    public void addSolution(Solution solution) {
        this.solutions.add(solution);
        solution.setInstance(this);
    }

    @Override
    public String toString() {
        return nom ;
    }

    /**
     * Crée un fichier Json représentant l'instance pour l'utiliser avec un outil de visualisation
     */
    public void writeJson() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("javascript/" + this.nom + ".json"));
            bw.write("[\n");
            String ssprefix = "";
            for (Solution solution : solutions) {
                bw.write(ssprefix);
                ssprefix = ",";
                bw.write("{\n");
                bw.write("\"nom\":\"" + solution.getNomAlgo() + "\",");
                bw.write("\"shifts\":[");
                String sprefix = "";
                for (Shift shift : solution.getShifts()) {
                    bw.write(sprefix);
                    sprefix = ",";
                    bw.write("{\"id\":" + shift.getId() + ", \"tempsMort\":" + shift.tempsMort() +", \"tempsTotal\":"+shift.getTempsTotal()+ ", \"tournees\":[");
                    StringBuilder str = new StringBuilder();
                    String tprefix = "";
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    for (Tournee tournee : shift.getTournees()) {
                        str.append(tprefix);
                        tprefix = ",";
                        str.append("{\"id\":").append(tournee.getId()).append(", \"debut\":\"")
                                .append(sdf.format(tournee.getDebut())).append("\", \"fin\":\"")
                                .append(sdf.format(tournee.getFin())).append("\"}");
                    }
                    bw.write(str.toString());
                    bw.write("\n]}");
                }
                bw.write("]}");
            }
            bw.write("]");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}