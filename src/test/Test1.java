package test;

import io.InstanceReader;
import io.exception.ReaderException;
import modele.Instance;
import modele.Tournee;
import org.eclipse.persistence.jpa.JpaHelper;

import javax.persistence.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test1 {

    public static void main(String[] args) {
        try {

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("Deliver2iPU");
            Map properties = new HashMap();
            properties.put("eclipselink.ddl-generation", "drop-and-create-tables");
            properties.put("eclipselink.ddl-generation.output-mode", "database");
            //this causes DDL generation to occur on refreshMetadata rather than wait until an em is obtained
            properties.put("eclipselink.deploy-on-startup", "true");
            JpaHelper.getEntityManagerFactory(emf).refreshMetadata(properties);
            EntityManager em = emf.createEntityManager();
            EntityTransaction et = em.getTransaction();
            for (int i = 1; i <= 10; i++) {
                et.begin();
                InstanceReader reader = new InstanceReader("instances/instance_" + i + ".csv");
                Instance instance = reader.readInstance();
                em.persist(instance);

//                em.persist(s);
                et.commit();
            }
            Query query = em.createNamedQuery("Instance.All");
            List <Instance> instances = query.getResultList();

            for (Instance instance : instances) {
                System.out.println(instance.getNom());
                instance.getTournees().sort(Comparator.comparing(Tournee::getDebut));
                instance.allAlgos();
                instance.writeJson();
                em.persist(instance);
            }

            System.out.println(instances);


            em.close();
            emf.close();

            System.out.println("Instance lue avec success !");
        } catch (ReaderException ex) {
            System.err.println(ex.getMessage());
        }
//        Accueil a = new Accueil();
    }
}
