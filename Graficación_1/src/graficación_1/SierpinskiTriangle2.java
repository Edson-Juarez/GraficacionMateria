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

public class SierpinskiTriangle2 extends JPanel {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int MAX_DEPTH = 10; // Número máximo de iteraciones
    private int currentDepth = 1; // Variable para controlar las iteraciones
    private Color startColor = new Color(0, 0, 255); // Azul
    private Color endColor = new Color(255, 0, 255); // Rosa

    public SierpinskiTriangle2() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        float fraction = (float) currentDepth / MAX_DEPTH;
        Color currentColor = calculateGradientColor(startColor, endColor, fraction);
        drawSierpinski(g, currentDepth, WIDTH / 2, 0, 0, HEIGHT, WIDTH, HEIGHT, currentColor);
    }

    private void drawSierpinski(Graphics g, int depth, double x1, double y1, double x2, double y2, double x3, double y3, Color color) {
        if (depth == 0) {
            int[] xPoints = {(int) x1, (int) x2, (int) x3};
            int[] yPoints = {(int) y1, (int) y2, (int) y3};

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            g2d.fillPolygon(xPoints, yPoints, 3);
        } else {
            double mid1x = (x1 + x2) / 2;
            double mid1y = (y1 + y2) / 2;
            double mid2x = (x2 + x3) / 2;
            double mid2y = (y2 + y3) / 2;
            double mid3x = (x1 + x3) / 2;
            double mid3y = (y1 + y3) / 2;

            drawSierpinski(g, depth - 1, x1, y1, mid1x, mid1y, mid3x, mid3y, color);
            drawSierpinski(g, depth - 1, mid1x, mid1y, x2, y2, mid2x, mid2y, color);
            drawSierpinski(g, depth - 1, mid3x, mid3y, mid2x, mid2y, x3, y3, color);
        }
    }

    private Color calculateGradientColor(Color startColor, Color endColor, float fraction) {
        int red = (int) (startColor.getRed() + fraction * (endColor.getRed() - startColor.getRed()));
        int green = (int) (startColor.getGreen() + fraction * (endColor.getGreen() - startColor.getGreen()));
        int blue = (int) (startColor.getBlue() + fraction * (endColor.getBlue() - startColor.getBlue()));
        return new Color(red, green, blue);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Triángulo de Sierpinski");
        SierpinskiTriangle2 sierpinski = new SierpinskiTriangle2();

        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sierpinski.currentDepth < MAX_DEPTH) {
                    sierpinski.currentDepth++;
                    sierpinski.repaint();
                }
            }
        });

        frame.add(sierpinski);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setName("Fractal - Graficación de 7-8 pm");

        timer.start();
    }
}
