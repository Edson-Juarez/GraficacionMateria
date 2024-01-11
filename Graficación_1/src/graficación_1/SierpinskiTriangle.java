/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graficación_1;

/**
 *
 * @author edson
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SierpinskiTriangle extends JPanel {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int DEPTH = 6;

    public SierpinskiTriangle() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.lightGray);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSierpinski(g, DEPTH, WIDTH / 2, 0, 0, HEIGHT, WIDTH, HEIGHT);
    }

    private void drawSierpinski(Graphics g, int depth, double x1, double y1, double x2, double y2, double x3, double y3) {
    if (depth == 0) {
        int[] xPoints = {(int) x1, (int) x2, (int) x3};
        int[] yPoints = {(int) y1, (int) y2, (int) y3};
        
        Color azul = new Color(0, 0, 255);
        Color rosa = new Color(255, 0, 255);

        GradientPaint gradient = new GradientPaint((float) x1, (float) y1, azul, (float) x3, (float) y3, rosa);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(gradient);
        g2d.fillPolygon(xPoints, yPoints, 3);
    } else {
        double mid1x = (x1 + x2) / 2;
        double mid1y = (y1 + y2) / 2;
        double mid2x = (x2 + x3) / 2;
        double mid2y = (y2 + y3) / 2;
        double mid3x = (x1 + x3) / 2;
        double mid3y = (y1 + y3) / 2;

        drawSierpinski(g, depth - 1, x1, y1, mid1x, mid1y, mid3x, mid3y);
        drawSierpinski(g, depth - 1, mid1x, mid1y, x2, y2, mid2x, mid2y);
        drawSierpinski(g, depth - 1, mid3x, mid3y, mid2x, mid2y, x3, y3);
    }
}


    public static void main(String[] args) {
        JFrame frame = new JFrame("Triángulo de Sierpinski");
        SierpinskiTriangle sierpinski = new SierpinskiTriangle();

        Timer timer = new Timer(1000, new ActionListener() {
            int currentDepth = 1;

            @Override
            public void actionPerformed(ActionEvent e) {
                sierpinski.repaint();
                if (currentDepth < DEPTH) {
                    currentDepth++;
                }
            }
        });

        frame.add(sierpinski);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setName("Fractal  - Graficación de 7-8 pm");

        timer.start();
    }
}
