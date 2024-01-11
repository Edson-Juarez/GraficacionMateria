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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class FigurasGeometricasDobleBuffer extends JPanel implements ActionListener {
    private Timer timer;
    private int x = 100, y = 100; // Posición inicial
    private double scaleX = 1.0, scaleY = 1.0; // Escalado inicial
    private double shearX = 0.0, shearY = 0.0; // Sesgado inicial
    private double rotationAngle = 0.0; // Ángulo de rotación inicial
    private BufferedImage buffer;

    public FigurasGeometricasDobleBuffer() {
        timer = new Timer(100, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (buffer == null) {
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        }

        Graphics2D g2d = (Graphics2D) buffer.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Crear cuadrado
        Rectangle square = new Rectangle(x, y, 100, 100);

        // Crear rectángulo
        Rectangle rectangle = new Rectangle(x + 150, y, 150, 100);

        // Crear triángulo
        int[] xPoints = {x + 350, x + 450, x + 400};
        int[] yPoints = {y, y, y + 100};
        Polygon triangle = new Polygon(xPoints, yPoints, 3);

        // Aplicar transformaciones a las figuras
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y); // Mover
        transform.scale(scaleX, scaleY); // Escalar
        transform.shear(shearX, shearY); // Sesgar
        transform.rotate(rotationAngle); // Rotar

        // Aplicar transformaciones a las figuras
        g2d.setColor(Color.RED);
        g2d.fill(transform.createTransformedShape(square));

        g2d.setColor(Color.GREEN);
        g2d.fill(transform.createTransformedShape(rectangle));

        g2d.setColor(Color.BLUE);
        g2d.fill(transform.createTransformedShape(triangle));

        g.drawImage(buffer, 0, 0, this);
    }

    public void actionPerformed(ActionEvent e) {
        // Actualizar valores para las transformaciones
        x += 1;
        scaleX += 0.01;
        scaleY += 0.01;
        shearX += 0.01;
        shearY += 0.01;
        rotationAngle += 0.01;

        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Manipulación de Figuras (Doble Buffer)");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new FigurasGeometricasDobleBuffer());
            frame.setVisible(true);
        });
    }
}
