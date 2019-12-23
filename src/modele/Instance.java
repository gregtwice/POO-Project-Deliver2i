package modele;

import javax.persistence.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@NamedQueries({

        @NamedQuery(name = "Instance.All", query = "select e from Instance e")
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
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public void addSolution(Solution solution) {
        this.solutions.add(solution);
        solution.setInstance(this);
    }

    @Override
    public String toString() {
        return "Instance{" +
                "nom='" + nom + '\'' +
//                ", dureeMin=" + dureeMin +
//                ", dureeMax=" + dureeMax +
//                ", date=" + date +
//                ", id=" + id +
                '}';
    }

    public void writeJson() {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.nom + ".js"));
            bw.write("let solutions = [\n");
            for (Solution solution : solutions) {
                bw.write("{\n");
                bw.write("\"shifts\":[");
                for (Shift shift : solution.getShifts()) {
                    bw.write("{id:" + shift.getId() + ", tournees:[");
                    for (Tournee tournee : shift.getTournees()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        bw.write("{id:" + tournee.getId() +
                                ", debut:'" + sdf.format(tournee.getDebut()) +
                                "', fin:'" + sdf.format(tournee.getFin()) + "'},\n");
                    }
                    bw.write("]},");
                }
                bw.write("]");
            }
            bw.write("}]");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
