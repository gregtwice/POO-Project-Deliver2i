package vuecontrole;

import modele.Shift;
import modele.Solution;
import modele.Tournee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ZoneGraphique extends JPanel implements MouseListener, MouseMotionListener {

    private int relativeCenterX;
    private int relativeCenterY;

    private int dragBaseX;
    private int dragBaseY;

    private int decalageX;
    private int decalageY;

    private Solution solution;

    private boolean dragging;

    public ZoneGraphique() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.relativeCenterX = 0;
        this.relativeCenterY = 0;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        decalageX = dragBaseX - e.getX();
        decalageY = dragBaseY - e.getY();
        System.out.println(decalageX + " " + decalageY);
        System.out.println(dragBaseX + " " + dragBaseY);
        System.out.println("Moving !!");
        int tempX = relativeCenterX;
        int tempY = relativeCenterY;
        this.relativeCenterX += decalageX;
        this.relativeCenterY += decalageY;
        this.paintComponent(this.getGraphics());
        this.relativeCenterX = tempX;
        this.relativeCenterY = tempY;
    }


    @Override
    public void mouseMoved(MouseEvent e) {}


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        g.drawRect(relativeCenterX, relativeCenterY, 200, 100);
        g.translate(relativeCenterX, relativeCenterY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        this.dragging = true;
        this.dragBaseX = e.getX();
        this.dragBaseY = e.getY();
        System.out.println("Pressed");

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.relativeCenterX += decalageX;
        this.relativeCenterY += decalageY;
        this.dragging = false;
        System.out.println("released");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void paintTournee() {
        GregorianCalendar gc = new GregorianCalendar();
        int i = 0;
        int y = 20;
        for (Shift shift : this.solution.getShifts()) {
            for (Tournee tournee : shift.getTournees()) {
                gc.setTime(tournee.getDebut());
                int x = gc.get(Calendar.HOUR_OF_DAY) * 60;
                x += gc.get(Calendar.MINUTE);
                gc.setTime(tournee.getFin());
                int w = tournee.getTempsTournee();
                int h = 10;
                Rectangle r = new Rectangle(x - 600, y, w, h);
                System.out.println(r);
                r.seDessiner(getGraphics());
            }
            y += 15;
        }
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    class Rectangle {
        int x, y, w, h;

        public Rectangle(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        @Override
        public String toString() {
            return "Rectangle{" +
                    "x=" + x +
                    ", y=" + y +
                    ", w=" + w +
                    ", h=" + h +
                    '}';
        }

        public void seDessiner(Graphics g) {
            g.drawRect(x, y, w, h);
        }
    }
}
