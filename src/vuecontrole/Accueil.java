package vuecontrole;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Accueil extends JFrame {
    private JList<modele.Instance> listeInstances;
    private JButton voirInstanceButton;
    private JPanel PanelAccueil;
    private JButton actualiserButton;


    public Accueil() {
        super("Assurance");
        initComponents();
        remplirJList();
        initConnexion();                                 // /!\
        initialisationFenetre();

        actualiserButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                remplirJList();
            }
        });
    }

    private void initComponents() {
        voirInstanceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                InstanceClicked();
            }
        });
    }

    private void initConnexion() {


//        try {
//
//        } catch (SQLException | ClassNotFoundException e) {
//
//        }
    }

    private void initialisationFenetre() {
        setPreferredSize(new Dimension(400, 400));
        setContentPane(PanelAccueil);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void InstanceClicked() {
        new Instance(this);
        dispose();
    }

    public static void main(String[] args) {
        Accueil a = new Accueil();

    }


    private void remplirJList() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Deliver2iPU");
        final EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("Instance.All");
        List<modele.Instance> instances = query.getResultList();
        DefaultListModel<modele.Instance> defaultListModel = new DefaultListModel<>();
        for (modele.Instance instance : instances) {
            defaultListModel.addElement(instance);
        }
        listeInstances.setModel(defaultListModel);
        listeInstances.setVisible(true);
    }

}
