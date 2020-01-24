package test;

import io.InstanceReader;
import io.exception.ReaderException;
import modele.Instance;
import modele.Solution;
import modele.Tournee;
import vuecontrole.Accueil;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;

public class Test1 {

    public static void main(String[] args) {
        try {

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("Deliver2iPU");
            EntityManager em = emf.createEntityManager();
            EntityTransaction et = em.getTransaction();
            for (int i = 1; i <= 10; i++) {
                et.begin();
                InstanceReader reader = new InstanceReader("instances/instance_" + i + ".csv");
                Instance instance = reader.readInstance();
//                System.out.println(instance.getNom());
                instance.getTournees().sort(Comparator.comparing(Tournee::getDebut));
//                instance.allAlgos();
//                instance.writeJson();
                em.persist(instance);
//                em.persist(s);
                et.commit();
            }
            Query query = em.createNamedQuery("Instance.All");
            List<Instance> instances = query.getResultList();
            System.out.println(instances);
            for (Instance instance : instances) {
                System.out.println(instance.getNom());

//                Solution solution=new Solution();
//                solution.setInstance(instance);
//                instance.addSolution(solution);
                instance.allAlgos();
//                em.persist(instance);
//                et.commit();

//                solution.setNomAlgo("algoTdebTfin");
                instance.writeJson();
            }
            em.close();
            emf.close();

            System.out.println("Instance lue avec success !");
        } catch (ReaderException ex) {
            System.err.println(ex.getMessage());
        }
//        Accueil a = new Accueil();
    }
}
