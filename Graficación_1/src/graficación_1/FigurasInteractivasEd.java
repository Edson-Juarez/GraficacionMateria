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
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class FigurasInteractivasEd extends JFrame {

    private Figura seleccionada;
    private List<Figura> figuras;
    private double escala = 1.0;
    private double angulo = 0.0;
    private double sesgoX = 0.0;
    private double sesgoY = 0.0;
    private BufferedImage buffer;

    public FigurasInteractivasEd() {
        setTitle("Figuras Interactivas");
        setSize(800, 600);
        setBackground(Color.DARK_GRAY);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        figuras = new ArrayList<>();
        figuras.add(new Cuadrado(200, 100, 100));
        figuras.add(new Elipse(370, 100, 60, 40));
        figuras.add(new Heptagono(50, 50, 30));
        figuras.add(new Cruz(400, 300, 100));
        figuras.add(new Corazon(100, 270, 80));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Figura figuraAnterior = seleccionada;

                for (Figura figura : figuras) {
                    if (figura.contiene(e.getX(), e.getY())) {
                        seleccionada = figura;
                        break;
                    }
                }

                if (figuraAnterior != seleccionada) {
                    for (Figura figura : figuras) {
                        figura.setColor(Color.pink);
                    }
                    if (seleccionada != null) {
                        seleccionada.setColor(Color.BLUE);
                    }
                    repaint();
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (seleccionada == null) {
                    return;
                }

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        seleccionada.mover(0, -5);
                        repaint();
                        break;
                    case KeyEvent.VK_DOWN:
                        seleccionada.mover(0, 5);
                        repaint();
                        break;
                    case KeyEvent.VK_LEFT:
                        seleccionada.mover(-5, 0);
                        repaint();
                        break;
                    case KeyEvent.VK_RIGHT:
                        seleccionada.mover(5, 0);
                        repaint();
                        break;
                    case KeyEvent.VK_PLUS:
                    case KeyEvent.VK_ADD:
                        seleccionada.redimensionar(1.1);
                        repaint();
                        break;
                    case KeyEvent.VK_MINUS:
                    case KeyEvent.VK_SUBTRACT:
                        seleccionada.redimensionar(0.9);
                        repaint();
                        break;
                    case KeyEvent.VK_L:
                        angulo += Math.toRadians(5);
                        seleccionada.rotar(angulo);
                        repaint();
                        break;
                    case KeyEvent.VK_J:
                        angulo -= Math.toRadians(5);
                        seleccionada.rotar(angulo);
                        repaint();
                        break;
                    case KeyEvent.VK_S:
                        sesgoX += 0.1;
                        seleccionada.setSesgoX(sesgoX);
                        repaint();
                        break;
                    case KeyEvent.VK_D:
                        sesgoX -= 0.1;
                        seleccionada.setSesgoX(sesgoX);
                        repaint();
                        break;
                    case KeyEvent.VK_W:
                        sesgoY += 0.1;
                        seleccionada.setSesgoY(sesgoY);
                        repaint();
                        break;
                    case KeyEvent.VK_A:
                        sesgoY -= 0.1;
                        seleccionada.setSesgoY(sesgoY);
                        repaint();
                        break;
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2dBuffer = (Graphics2D) buffer.getGraphics();
        g2dBuffer.setColor(getBackground());
        g2dBuffer.fillRect(0, 0, getWidth(), getHeight());

        for (Figura figura : figuras) {
            figura.dibujar(g2dBuffer);
        }

        g.drawImage(buffer, 0, 0, this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FigurasInteractivasEd app = new FigurasInteractivasEd();
            app.setVisible(true);
        });
    }

    abstract class Figura {

        protected int x;
        protected int y;
        protected Color color;

        public Figura(int x, int y) {
            this.x = x;
            this.y = y;
            this.color = Color.white;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public abstract void dibujar(Graphics2D g2d);

        public abstract boolean contiene(int x, int y);

        public abstract void redimensionar(double factor);

        public abstract void mover(int dx, int dy);

        public abstract void rotar(double angulo);

        public abstract void setSesgoX(double sesgoX);

        public abstract void setSesgoY(double sesgoY);
    }

    class Cuadrado extends Figura {

        private int lado;
        private double escala = 1.0;
        private double angulo = 0.0;
        private double sesgoX = 0.0;
        private double sesgoY = 0.0;

        public Cuadrado(int x, int y, int lado) {
            super(x, y);
            this.lado = lado;
        }

        public void dibujar(Graphics2D g2d) {
    g2d.setColor(color);
    int centroX = x + lado / 2;
    int centroY = y + lado / 2;

    AffineTransform oldTransform = g2d.getTransform();

    // Aplicar transformaciones (escala, rotación)
    g2d.translate(centroX, centroY);
    g2d.scale(escala, escala);
    g2d.rotate(angulo);

    // Crear una copia de la transformación actual
    AffineTransform shearTransform = new AffineTransform(g2d.getTransform());

    // Aplicar cizallamiento solo al lado superior de la figura en el eje X
    shearTransform.shear(sesgoX, 0);
    g2d.setTransform(shearTransform);
    g2d.fillRect(-lado / 2, -lado / 2, lado, lado / 2);  // Parte superior

    // Restaurar transformación original
    g2d.setTransform(oldTransform);
}


        public boolean contiene(int x, int y) {
            return x >= this.x && x <= this.x + lado && y >= this.y && y <= this.y + lado;
        }

        public void redimensionar(double factor) {
            escala *= factor;
        }

        public void mover(int dx, int dy) {
            x += dx;
            y += dy;
        }

        public void rotar(double angulo) {
            this.angulo = angulo;
        }

        public void setSesgoX(double sesgoX) {
            this.sesgoX = sesgoX;
        }

        public void setSesgoY(double sesgoY) {
            this.sesgoY = sesgoY;
        }
    }

    class Elipse extends Figura {

        private int semiejeMayor;
        private int semiejeMenor;
        private double escala = 1.0;
        private double angulo = 0.0;
        private double sesgoX = 0.0;
        private double sesgoY = 0.0;

        public Elipse(int x, int y, int semiejeMayor, int semiejeMenor) {
            super(x, y);
            this.semiejeMayor = semiejeMayor;
            this.semiejeMenor = semiejeMenor;
        }

        public void dibujar(Graphics2D g2d) {
            g2d.setColor(color);
            int centroX = x + semiejeMayor;
            int centroY = y + semiejeMenor;

            AffineTransform oldTransform = g2d.getTransform();

            // Aplicar transformaciones (escala, rotación, sesgo)
            g2d.translate(centroX, centroY);
            g2d.rotate(angulo);
            g2d.scale(escala, escala);
            g2d.shear(sesgoX, sesgoY);
            g2d.fill(new Ellipse2D.Double(-semiejeMayor, -semiejeMenor, 2 * semiejeMayor, 2 * semiejeMenor));

            // Restaurar transformación original
            g2d.setTransform(oldTransform);
        }

        public boolean contiene(int x, int y) {
            double dx = x - (this.x + semiejeMayor);
            double dy = y - (this.y + semiejeMenor);
            return (dx * dx) / (semiejeMayor * semiejeMayor) + (dy * dy) / (semiejeMenor * semiejeMenor) <= 1.0;
        }

        public void redimensionar(double factor) {
            escala *= factor;
        }

        public void mover(int dx, int dy) {
            x += dx;
            y += dy;
        }

        public void rotar(double angulo) {
            this.angulo = angulo;
        }

        public void setSesgoX(double sesgoX) {
            this.sesgoX = sesgoX;
        }

        public void setSesgoY(double sesgoY) {
            this.sesgoY = sesgoY;
        }
    }

    class Heptagono extends Figura {

        private int radio;
        private int lados = 6;
        private double escala = 1.0;
        private double angulo = 0.0;
        private double sesgoX = 0.0;
        private double sesgoY = 0.0;

        public Heptagono(int x, int y, int radio) {
            super(x, y);
            this.radio = radio;
        }

        public void dibujar(Graphics2D g2d) {
            g2d.setColor(color);
            int centroX = x + radio;
            int centroY = y + radio;

            AffineTransform oldTransform = g2d.getTransform();

            // Aplicar transformaciones (escala, rotación, sesgo)
            g2d.translate(centroX, centroY);
            g2d.rotate(angulo);
            g2d.scale(escala, escala);
            g2d.shear(sesgoX, sesgoY);

            Path2D path = new Path2D.Double();
            double angle = 2 * Math.PI / lados;
            path.moveTo(radio, 0);
            for (int i = 1; i < lados; i++) {
                path.lineTo(radio * Math.cos(angle * i), radio * Math.sin(angle * i));
            }
            path.closePath();
            g2d.fill(path);

            // Restaurar transformación original
            g2d.setTransform(oldTransform);
        }

        public boolean contiene(int x, int y) {
            double dx = x - (this.x + radio);
            double dy = y - (this.y + radio);
            double distancia = Math.sqrt(dx * dx + dy * dy);
            return distancia <= radio;
        }

        public void redimensionar(double factor) {
            escala *= factor;
        }

        public void mover(int dx, int dy) {
            x += dx;
            y += dy;
        }

        public void rotar(double angulo) {
            this.angulo = angulo;
        }

        public void setSesgoX(double sesgoX) {
            this.sesgoX = sesgoX;
        }

        public void setSesgoY(double sesgoY) {
            this.sesgoY = sesgoY;
        }
    }

    class Cruz extends Figura {

        private int tamano;
        private double escala = 1.0;
        private double angulo = 0.0;
        private double sesgoX = 0.0;
        private double sesgoY = 0.0;

        public Cruz(int x, int y, int tamano) {
            super(x, y);
            this.tamano = tamano;
        }

        public void dibujar(Graphics2D g2d) {
        g2d.setColor(color);
        int centroX = x + tamano / 2;
        int centroY = y + tamano / 2;

        AffineTransform oldTransform = g2d.getTransform();

        // Aplicar transformaciones (escala, rotación)
        g2d.translate(centroX, centroY);
        g2d.scale(escala, escala);
        g2d.rotate(angulo);

        // Crear una copia de la transformación actual
        AffineTransform shearTransform = new AffineTransform(g2d.getTransform());

        // Aplicar cizallamiento solo al lado izquierdo de la figura en el eje Y
        shearTransform.shear(0, sesgoY);
        g2d.setTransform(shearTransform);
        g2d.fillRect(-tamano / 2, -2, tamano, 4);  // Parte horizontal de la cruz

        // Restaurar transformación original
        g2d.setTransform(oldTransform);

        // Aplicar cizallamiento solo al lado superior de la figura en el eje X
        shearTransform.shear(sesgoX, 0);
        g2d.setTransform(shearTransform);
        g2d.fillRect(-2, -tamano / 2, 4, tamano);  // Parte vertical de la cruz

        // Restaurar transformación original
        g2d.setTransform(oldTransform);
    }

        public boolean contiene(int x, int y) {
            int mitadAncho = tamano / 2;
            int mitadAlto = tamano / 2;

            // Verificar si el punto está dentro del área rectangular de la cruz
            boolean dentroDeRectangulo = (x >= this.x - mitadAncho && x <= this.x + mitadAncho
                    && y >= this.y - mitadAlto && y <= this.y + mitadAlto);

            // Verificar si el punto está dentro de una de las líneas de la cruz
            boolean dentroDeLineaHorizontal = (y >= this.y - 3 && y <= this.y + 3 && x >= this.x - mitadAncho && x <= this.x + mitadAncho);
            boolean dentroDeLineaVertical = (x >= this.x - 3 && x <= this.x + 3 && y >= this.y - mitadAlto && y <= this.y + mitadAlto);

            // El punto está dentro de la cruz si está dentro del área rectangular o de una de las líneas
            return dentroDeRectangulo || dentroDeLineaHorizontal || dentroDeLineaVertical;
        }

        public void redimensionar(double factor) {
            escala *= factor;
        }

        public void mover(int dx, int dy) {
            x += dx;
            y += dy;
        }

        public void rotar(double angulo) {
            this.angulo = angulo;
        }

        public void setSesgoX(double sesgoX) {
            this.sesgoX = sesgoX;
        }

        public void setSesgoY(double sesgoY) {
            this.sesgoY = sesgoY;
        }
    }

    class Corazon extends Figura {

        private int tamano;
        private double escala = 1.0;
        private double angulo = 0.0;
        private double sesgoX = 0.0;
        private double sesgoY = 0.0;

        public Corazon(int x, int y, int tamano) {
            super(x, y);
            this.tamano = tamano;
        }

        public void dibujar(Graphics2D g2d) {
            g2d.setColor(color);
            int centroX = x + tamano / 2;
            int centroY = y + tamano / 2;

            AffineTransform oldTransform = g2d.getTransform();

            // Aplicar transformaciones (escala, rotación, sesgo)
            g2d.translate(centroX, centroY);
            g2d.rotate(angulo);
            g2d.scale(escala, escala);
            g2d.shear(sesgoX, 0);

            Path2D path = new Path2D.Double();
            path.moveTo(0, 0);
            path.quadTo(-tamano / 4, -tamano / 2, -tamano / 2, -tamano / 4);
            path.quadTo(-tamano, tamano / 4, 0, tamano);
            path.quadTo(tamano, tamano / 4, tamano / 2, -tamano / 4);
            path.quadTo(tamano / 4, -tamano / 2, 0, 0);
            g2d.fillRect(-tamano / 2, -tamano / 2, tamano, tamano);
            path.closePath();
            g2d.fill(path);

            // Restaurar transformación original
            g2d.setTransform(oldTransform);
        }

        public boolean contiene(int x, int y) {
            // Crear un área con el corazón para verificar la contención
            int centroX = this.x + tamano / 2;
            int centroY = this.y + tamano / 2;

            Area areaCorazon = new Area();
            AffineTransform transform = new AffineTransform();
            transform.translate(centroX, centroY);
            transform.rotate(angulo);
            transform.scale(escala, escala);
            transform.shear(sesgoX, sesgoY);

            Path2D path = new Path2D.Double();
            path.moveTo(0, 0);
            path.quadTo(-tamano / 4, -tamano / 2, -tamano / 2, -tamano / 4);
            path.quadTo(-tamano, tamano / 4, 0, tamano);
            path.quadTo(tamano, tamano / 4, tamano / 2, -tamano / 4);
            path.quadTo(tamano / 4, -tamano / 2, 0, 0);
            path.closePath();

            areaCorazon.add(new Area(transform.createTransformedShape(path)));

            // Verificar si el punto (x, y) está dentro del área del corazón
            return areaCorazon.contains(x, y);
        }

        public void redimensionar(double factor) {
            escala *= factor;
        }

        public void mover(int dx, int dy) {
            x += dx;
            y += dy;
        }

        public void rotar(double angulo) {
            this.angulo = angulo;
        }

        public void setSesgoX(double sesgoX) {
            this.sesgoX = sesgoX;
        }

        public void setSesgoY(double sesgoY) {
            this.sesgoY = sesgoY;
        }
    }

}
