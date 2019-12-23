package test;

import io.InstanceReader;
import io.exception.ReaderException;
import modele.Instance;
import modele.Solution;

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
                instance.getTournees().sort((o1, o2) -> o1.getDebut().compareTo(o2.getDebut()));
                Solution s = new Solution();
//                s.setInstance(instance);
//                s.algoBasique();
//                instance.addSolution(s);
//                s = new Solution();
                s.setInstance(instance);
                s.algoBourrage();
                instance.addSolution(s);
//                ArrayList<Tournee> tournees = new ArrayList<>(instance.getTournees());

//                instance.setTournees(new HashSet<>(tournees));
                instance.writeJson();
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
    }
}
