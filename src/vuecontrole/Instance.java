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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Instance extends JFrame{
    private JPanel PanelInstance;
    private JList listInstances;
    private JList listShift;
    private JButton calculButton;
    private JButton retourBouton;
    private JComboBox comboBox1;
    private Accueil accueil;
    private modele.Instance instance;


    private void initComponents(){
        retourBouton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                retourBoutonClicked();
            }
        });

        calculButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                modele.Instance selectedValue = (modele.Instance) listInstances.getSelectedValue();
                if (selectedValue != null){
                    try {
                        InstanceReader reader = new InstanceReader("instances/"+selectedValue.getNom().toLowerCase()+".csv");
//                        instance = reader.readInstance();
                        instance = selectedValue;
                        Solution s = new Solution();
                        instance.addSolution(s);
                        calculShift();
                    } catch (ReaderException e1) {
                        e1.printStackTrace();
                    }

                }

            }
        });
    }


    private void calculShift() {
        Set<Solution> solutions = new HashSet<>();
        solutions = this.instance.getSolutions();
        for (Solution solution:solutions) {
            solution.algoBasique();
        }
        remplirListShift();
    }

    private void retourBoutonClicked() {
        this.accueil.setVisible(true);
        dispose();
    }

    public Instance(Accueil accueil) {
        super("Instance");
        this.accueil=accueil;
        this.instance = new modele.Instance();
        initComponents();
        initialisationFenetre();
        remplirListInstances();
        // initConnexion();                                  /!\
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



    private void remplirListShift() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Deliver2iPU");
        final EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("Shift.Sol");
        query.setParameter("id",instance.getId());
        List<modele.Shift> shifts = query.getResultList();
        System.out.println(shifts);
        DefaultListModel<modele.Shift> defaultListModel = new DefaultListModel<>();
        for (modele.Shift shift : shifts) {
            defaultListModel.addElement(shift);
        }
        listShift.setModel(defaultListModel);
        listShift.setVisible(true);
    }


    public static void main(String[] args) {
        Accueil a = new Accueil();
    }


}
