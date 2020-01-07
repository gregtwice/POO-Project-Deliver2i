package test;

import io.InstanceReader;
import io.exception.ReaderException;
import modele.Instance;
import modele.Solution;
import vuecontrole.Accueil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
                Solution s = new Solution();
                s.setInstance(instance);
                s.algoBasique();
                instance.addSolution(s);
                em.persist(instance);
                em.persist(s);
                et.commit();
            }
            em.close();
            emf.close();

            System.out.println("Instance lue avec success !");
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
        Accueil a = new Accueil();
    }
}
