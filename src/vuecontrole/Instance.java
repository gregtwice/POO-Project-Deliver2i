package vuecontrole;

import modele.Shift;
import modele.Solution;
import modele.Tournee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Instance extends JFrame {

    private JPanel PanelInstance;
    private JList listInstances;
    private JList listShift;
    private JButton visuNavigateurButton;
    private JLabel nbTourneesLabel;
    private JLabel bestSolLabel;
    private JLabel dateLabel;
    private JList listeSolutions;
    private JLabel solDateDebutLabel;
    private JLabel solDateFinLabel;
    private JLabel solTempsMortLabel;
    private JLabel solPercentTMLabel;
    private JLabel solNbMoyTSol;
    private JLabel solDureeTotaleLabel;
    private JPanel image;
    private JTable tableInfos;
    private JPanel PanelGraphique;

    //private Accueil accueil;
    private modele.Instance instance;
    private Solution selectedSolution;


    public Instance() {
        super("Instance");
//        this.accueil = accueil;
        this.instance = new modele.Instance();

        initialisationFenetre();
        remplirListInstances();
        selectedSolution = null;
        // initConnexion();                                  /!\
        initComponent();

    }

    private void initComponent() {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        image = new JPanel();
        image.add(new Image());
        PanelInstance.add(image);
        listInstances.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                instance = (modele.Instance) listInstances.getSelectedValue();
                super.mouseClicked(e);
                remplirInfosTable();
                remplirListShift();
                listeSolutions.setSelectedIndex(0);
            }
        });

        listeSolutions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                selectedSolution = (Solution) listeSolutions.getSelectedValue();
                actualiserListeShift();
                majInfosSolution();
            }
        });

        visuNavigateurButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop()
                                .browse(new URI("http://localhost/Poo/visu%20Poo/?instance=" + instance.getNom()));
                    } catch (IOException | URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }


    private void majInfosSolution() {
        instance.getTournees().sort(Comparator.comparing(Tournee::getDebut));
        final Date debut = instance.getTournees().get(0).getDebut();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        solDateDebutLabel.setText(sdf.format(debut));

        Date lastDate = instance.getTournees().get(instance.getTournees().size() - 1).getFin();
//        final Date fin = selectedSolution.getShifts().get(0).getTournees().get(0).getDebut();
        solDateFinLabel.setText(sdf.format(lastDate));


        int tempsTotal = 0;
        int nbMoy = 0;
        for (Shift shift : selectedSolution.getShifts()) {
            tempsTotal += shift.getTempsTotal();
            nbMoy += shift.getTournees().size();
        }
        solDureeTotaleLabel.setText(Integer.toString(tempsTotal));


        final float f = (float) nbMoy / selectedSolution.getShifts().size();
        solNbMoyTSol.setText(String.format(Locale.FRANCE, "%.2f", f));

        solTempsMortLabel.setText(Integer.toString(selectedSolution.getTempsMort()));

        final float v = (float) 100 * selectedSolution.getTempsMort() / tempsTotal;

        solPercentTMLabel.setText(String.format(Locale.FRANCE, "%.2f", v));
    }

    private void remplirInfosTable() {
        int maxTempsMort = Integer.MAX_VALUE;
        Solution bestSol = null;
        for (Solution solution : instance.getSolutions()) {
            int tm = solution.getTempsMort();
            if (tm < maxTempsMort) {
                bestSol = solution;
                maxTempsMort = tm;
            }
        }


        bestSolLabel.setText(bestSol.getNomAlgo());
        nbTourneesLabel.setText(Integer.toString(instance.getTournees().size()));
        dateLabel.setText(instance.getDate().toString());

    }


    private void initialisationFenetre() {
        setPreferredSize(new Dimension(1000, 700));
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
        em.close();
        emf.close();
    }

    private void remplirListShift() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Deliver2iPU");
        final EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("Instance.Solutions").setParameter("id", instance.getId());
        List<Solution> solutions = query.getResultList();
        query = em.createNamedQuery("Shift.Sol");
        query.setParameter("id", solutions.get(0).getId());
        selectedSolution = solutions.get(0);
        List<modele.Shift> shifts = query.getResultList();

        this.selectedSolution.setShifts(shifts);

//        panelGraphique.repaint();


        selectedSolution.setShifts(shifts);
        DefaultListModel<modele.Shift> defaultListModel = new DefaultListModel<>();
        for (modele.Shift shift : shifts) {
            defaultListModel.addElement(shift);
        }
        listShift.setModel(defaultListModel);
        listShift.setVisible(true);
        // Combo Box

        DefaultListModel<Solution> dlm = new DefaultListModel<>();
        for (Solution solution : solutions) {
            dlm.addElement(solution);
        }
        listeSolutions.setModel(dlm);
        em.close();
        emf.close();
    }

    private void actualiserListeShift() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Deliver2iPU");
        final EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("Shift.Sol");
        query.setParameter("id", selectedSolution.getId());
        List<modele.Shift> shifts = query.getResultList();
        DefaultListModel<modele.Shift> defaultListModel = new DefaultListModel<>();
        for (modele.Shift shift : shifts) {
            defaultListModel.addElement(shift);
        }
        listShift.setModel(defaultListModel);
        listShift.setVisible(true);
    }

    public static void main(String[] args) {
        Instance I = new Instance();
    }

    private void calculShift() {
        Set<Solution> solutions = new HashSet<>();
        //        for (Solution solution:solutions) {
//            solution.algoBasique();
//        }
        remplirListShift();
    }

}
