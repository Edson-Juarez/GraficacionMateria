/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graficación_1;

/**
 *
 * @author edson
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import javax.swing.*;

public class PoligonosRegulares extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

    private JButton[] botones;
    private JButton botonArrastrar;
    private JColorChooser colorChooser;
    private Color[] colores;
    private JPanel panelPoligonos;
    private boolean arrastrar = false;
    private int figuraArrastrada = -1;
    private int dx, dy;

    public PoligonosRegulares() {
        super("Poligonos Regulares");
        setLayout(new BorderLayout());

        panelPoligonos = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Triángulo
                drawRegularPolygon(g2d, colores[0], 3, 150, 150, 120);
                // Cuadrado
                drawRegularPolygon(g2d, colores[1], 4, 450, 150, 120);

                // Pentágono
                drawRegularPolygon(g2d, colores[2], 5, 750, 150, 120);

                // Hexágono
                drawRegularPolygon(g2d, colores[3], 6, 1050, 150, 120);

                // Heptágono
                drawRegularPolygon(g2d, colores[4], 7, 1350, 150, 120);
            }
        };
        panelPoligonos.setPreferredSize(new Dimension(1650, 450));
        panelPoligonos.addMouseListener(this);
        panelPoligonos.addMouseMotionListener(this);
        add(panelPoligonos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(6, 1));
        botones = new JButton[5];
        colores = new Color[5];
        for (int i = 0; i < botones.length; i++) {
            botones[i] = new JButton("Color " + (i + 1));
            botones[i].addActionListener(this);
            panelBotones.add(botones[i]);
            colores[i] = Color.WHITE;
        }
        botonArrastrar = new JButton("Arrastrar figuras");
        botonArrastrar.addActionListener(this);
        panelBotones.add(botonArrastrar);
        add(panelBotones, BorderLayout.EAST);

        colorChooser = new JColorChooser();

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void drawRegularPolygon(Graphics2D g2d, Color color, int sides, int xCenter, int yCenter, int radius) {
        double angle = Math.PI * 2 / sides;
        Path2D path = new Path2D.Double();
        path.moveTo(xCenter + radius * Math.cos(0), yCenter + radius * Math.sin(0));
        for (int i = 1; i < sides; i++) {
            path.lineTo(xCenter + radius * Math.cos(angle * i), yCenter + radius * Math.sin(angle * i));
        }
        path.closePath();
        g2d.setColor(color);
        g2d.fill(path);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < botones.length; i++) {
            if (e.getSource() == botones[i]) {
                colores[i] = JColorChooser.showDialog(this, "Elige un color", colores[i]);
            }
        }

        if (e.getSource() == botonArrastrar) {
            arrastrar = !arrastrar;
            if (arrastrar) {
                botonArrastrar.setText("Detener arrastre");
            } else {
                botonArrastrar.setText("Arrastrar figuras");
            }
            figuraArrastrada = -1;
        }

        panelPoligonos.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (arrastrar) {
            for (int i = 0; i < 5; i++) {
                int xCenter = (i + 1) * 300 - 150;
                int yCenter = 150;
                int radius = 120;
                if (Math.hypot(e.getX() - xCenter, e.getY() - yCenter) <= radius) {
                    figuraArrastrada = i;
                    dx = e.getX() - xCenter;
                    dy = e.getY() - yCenter;
                    break;
                }
            }

            if (figuraArrastrada == -1) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (arrastrar && figuraArrastrada != -1) {
            int xCenter = e.getX() - dx;
            int yCenter = e.getY() - dy;

            if (xCenter < 120) {
                xCenter = 120;
            } else if (xCenter > 1530) {
                xCenter = 1530;
            }

            if (yCenter < 120) {
                yCenter = 120;
            } else if (yCenter > 330) {
                yCenter = 330;
            }
            panelPoligonos.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        figuraArrastrada = -1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public static void main(String[] args) {
        new PoligonosRegulares();
    }
}
