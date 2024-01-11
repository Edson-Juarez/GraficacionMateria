/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package graficación_1;

/**
 *
 * @author edson
 */
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class QuadraticKochIsland2 extends JPanel {

    private String axiom = "F+F+F+F";
    private String[] rules = {"F", "F+FFF++FF+F"};
    private int iterations = 6;
    private double angle = 90.0;
    private String result;
    public int x = 400;
    public int y = 150;
    private double direction = 0.0;
    private int length = 2;

    public QuadraticKochIsland2() {
        result = axiom;
        for (int i = 0; i < iterations; i++) {
            StringBuilder builder = new StringBuilder();
            for (char c : result.toCharArray()) {
                String replacement = String.valueOf(c);
                for (int j = 0; j < rules.length; j += 2) {
                    String pattern = rules[j];
                    if (pattern.equals(String.valueOf(c))) {
                        replacement = rules[j + 1];
                        break;
                    }
                }
                builder.append(replacement);
            }
            result = builder.toString();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x1 = getWidth() / 2;
        int y1 = getHeight() / 2;
        int x2, y2;

        for (int i = 0; i < iterations; i++) {
            // Calcular el color en función del número de iteraciones
            float hue = (float) i / iterations; // Variar el tono de 0 a 1
            Color fractalColor = Color.getHSBColor(hue, 1, 1);

            for (char c : result.toCharArray()) {
                g.setColor(fractalColor);
                if (c == 'F') {
                    x2 = x1 + (int) Math.round(length * Math.cos(Math.toRadians(direction)));
                    y2 = y1 + (int) Math.round(length * Math.sin(Math.toRadians(direction)));
                    g.drawLine(x1, y1, x2, y2);
                    x1 = x2;
                    y1 = y2;
                } else if (c == '+') {
                    direction += angle;
                } else if (c == '-') {
                    direction -= angle;
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fractal Chingon");
        QuadraticKochIsland2 panel = new QuadraticKochIsland2();
        panel.setBackground(Color.darkGray);
        frame.add(panel);
        frame.setBackground(Color.black);
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
