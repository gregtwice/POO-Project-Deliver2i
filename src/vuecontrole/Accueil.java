package vuecontrole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class Accueil extends JFrame {
    private JList listeInstance;
    private JButton voirInstanceButton;
    private JPanel PanelAccueil;



    private void initComponents(){
        voirInstanceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                InstanceClicked();
            }
        });
    }

    private void InstanceClicked() {
        new Instance();
        dispose();
    }

    public Accueil() {
        super("Assurance");
        initComponents();
        initialisationFenetre();
       // initConnexion();                                  /!\
    }

//    private void initConnexion() {
//        try {
//            assurance = RequeteAssurance.getInstance();
//        } catch (SQLException | ClassNotFoundException e) {
//            showException(e);
//        }
//    }

    private void initialisationFenetre() {
        setPreferredSize(new Dimension(400, 400));
        setContentPane(PanelAccueil);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        Accueil a = new Accueil();

    }

}
