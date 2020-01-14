package vuecontrole;

import io.InstanceReader;
import io.exception.ReaderException;
import modele.Solution;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.ref.PhantomReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Instance extends JFrame {
    private JPanel PanelInstance;
    private JList listInstances;
    private JList listShift;
    private JComboBox solComboBox;
    //private Accueil accueil;
    private modele.Instance instance;


    public Instance() {
        super("Instance");
//        this.accueil = accueil;
        this.instance = new modele.Instance();

        initialisationFenetre();
        remplirListInstances();
        // initConnexion();                                  /!\
        listInstances.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                instance = (modele.Instance) listInstances.getSelectedValue();
                remplirListShift();
                super.mouseClicked(e);
            }
        });
        solComboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                changerSolution();
            }
        });
    }


    private void initialisationFenetre() {
        setPreferredSize(new Dimension(400, 400));
        setContentPane(PanelInstance);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void remplirListInstances() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Deliver2iPU");
        final EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("Instance.All");
        List<modele.Instance> instances = query.getResultList();
        DefaultListModel<modele.Instance> defaultListModel = new DefaultListModel<>();
        for (modele.Instance instance : instances) {
            defaultListModel.addElement(instance);
        }
        listInstances.setModel(defaultListModel);
        listInstances.setVisible(true);
    }


    private void calculShift() {
        Set<Solution> solutions = new HashSet<>();
        //        for (Solution solution:solutions) {
//            solution.algoBasique();
//        }
        remplirListShift();
    }

    private void remplirListShift() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Deliver2iPU");
        final EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("Instance.Solutions").setParameter("id", instance.getId());
        List<Solution> solutions = query.getResultList();
        query = em.createNamedQuery("Shift.Sol");
        query.setParameter("id", solutions.get(0).getId());
        List<modele.Shift> shifts = query.getResultList();
        DefaultListModel<modele.Shift> defaultListModel = new DefaultListModel<>();
        for (modele.Shift shift : shifts) {
            defaultListModel.addElement(shift);
        }
        listShift.setModel(defaultListModel);
        listShift.setVisible(true);
        // Combo Box

        DefaultComboBoxModel<Solution> dcbm = new DefaultComboBoxModel<>();
        for (Solution solution : solutions) {
            dcbm.addElement(solution);
        }
        solComboBox.setModel(dcbm);


        em.close();
        emf.close();

    }

    private void changerSolution(){
        listShift.removeAll();
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Deliver2iPU");
        final EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("Shift.Sol");
        System.out.println(solComboBox.getSelectedItem());
        query.setParameter("id", solComboBox.getSelectedItem());
        List<modele.Shift> shifts = query.getResultList();
        DefaultListModel<modele.Shift> defaultListModel = new DefaultListModel<>();
        for (modele.Shift shift : shifts) {
            defaultListModel.addElement(shift);
        }
        listShift.setModel(defaultListModel);
        listShift.setVisible(true);

        em.close();
        emf.close();

    }


    public static void main(String[] args) {
        Instance I = new Instance();
    }

}
