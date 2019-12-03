package test;

import io.InstanceReader;
import io.exception.ReaderException;
import modele.Instance;
import modele.Solution;

public class Test1 {

    public static void main(String[] args) {
        try {

            for (int i = 1; i <= 10; i++) {
                InstanceReader reader = new InstanceReader("instances/instance_" + i + ".csv");
                Instance instance = reader.readInstance();
                Solution s = new Solution();
                s.setInstance(instance);
                s.algoBasique();
            }

            System.out.println("Instance lue avec success !");
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
