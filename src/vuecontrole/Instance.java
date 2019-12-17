package vuecontrole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Instance extends JFrame{
    private JPanel PanelInstance;
    private JList listeTache;
    private JList listShift;
    private JButton calculButton;




    private void initComponents(){

    }


    public Instance() {
        super("Instance");
        initComponents();
        initialisationFenetre();
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

    public static void main(String[] args) {
        Accueil a = new Accueil();
    }


}
