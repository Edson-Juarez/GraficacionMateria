package joglmaze;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.awt.GLJPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


/**
 * @author Joe Kilner
 *         <p>
 *         The application frame that handles initialisation and user interaction
 */
public class ViewerFrame extends JFrame implements KeyListener, ActionListener {
    // version ID for serialisation
    private static final long serialVersionUID = 1L;
    // The JOGL object that handles OpenGL context
    private GLJPanel jpanel;
    // The object that renders the scene
    private ViewRenderer renderer;
    // The Animator object used to control animation frame rates
    private FPSAnimator animator;

    // Menu items used in the program
    private JMenuItem exitItem;
    private JMenuItem resetItem;
    private JMenuItem vistaa;
    private JMenuItem volver;
    

    /**
     * The main method which runs the application
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        // This line initialises some OpenGL features and must come before any other
        // openGL calls
        GLProfile.initSingleton();
        // Get the default profile
        GLProfile glp = GLProfile.getDefault();

        // Create a new frame and make it visible
        new ViewerFrame("laberinto 3d", glp);
    }

    /**
     * Default constructor.
     *
     * @param title   The title of the Frame
     * @param profile The GLProfile object used to initialise the OpenGL context
     */
    public ViewerFrame(String title, GLProfile profile) {
        // Call the parent constructor
        super(title);

        // Setup the frame with some default intial parameters
        setSize(800, 800);
        setLocationRelativeTo(null);

        // exit when the close button is pressed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the OpenGL Capabilities object
        GLCapabilities caps = new GLCapabilities(profile);

        // Create a double buffered GLJPanel
        jpanel = new GLJPanel(caps);
        jpanel.setDoubleBuffered(true);

        // The jpanel will update 60 times a second, so we don't need to redraw
        // on other UI events
        jpanel.setIgnoreRepaint(true);

        // Create a new scene rendering object
        renderer = new ViewRenderer(60);
        jpanel.addGLEventListener(renderer);

        // Setup 60fps animator
        animator = new FPSAnimator(jpanel, 60);

        // Add the panel to the canvas
        getContentPane().add(jpanel);

        // Add the event listeners
        this.addKeyListener(this);

        // Build the menus
        buildMenu();

        // Display window
        setVisible(true);
        requestFocusInWindow();

        // Start the animation
        animator.start();

    }

    /**
     * Construct the main menu we are going to use in the program
     */
    private void buildMenu() {
        // Add the menu bar
        JMenuBar menuBar = new JMenuBar();

        // Build the file menu
        JMenu fileMenu = new JMenu("File");
        // Add an item to exit the program
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        //Build the Animation menu
        JMenu resetMenu = new JMenu("Paseme profe porfa");
        // Add an item to reset the user's position in the maze
        resetItem = new JMenuItem("Reestablecer posición");
        resetItem.addActionListener(this);
        resetMenu.add(resetItem);
        menuBar.add(resetMenu);

        
        JMenu vistaMenu = new JMenu("vista aerea");
        vistaa = new JMenuItem("ver laberinto");
        vistaa.addActionListener(this);
        vistaMenu.add(vistaa);
        menuBar.add(vistaMenu);
        setJMenuBar(menuBar);
        
        volver = new JMenuItem("volver al laberinto");
        volver.addActionListener(this);
        vistaMenu.add(volver);
        menuBar.add(vistaMenu);
        // Add the menu bar to this Frame
        setJMenuBar(menuBar);

    }

    /**
     * @param key The KeyEvent object which lets us know what key was pressed
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     * <p>
     * This function is called when a key is pressed so we can get the keypresses and act on them
     */
    @Override
    public void keyPressed(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_UP:
            renderer.moveUp(true);
            break;
        case KeyEvent.VK_DOWN:
            renderer.moveDown(true);
            break;
            // If the user presses up/down, change the Y camera variable pos.
            case KeyEvent.VK_W:
                renderer.moveForward(true);
                break;
            case KeyEvent.VK_S:
                renderer.moveBackward(true);
                break;
            // If the user presses left/right, change the X camera variable pos.
            case KeyEvent.VK_A:
                renderer.turnLeft(true);
                break;
            case KeyEvent.VK_D:
                renderer.turnRight(true);
                break;
            // If the user presses escape, exit the program
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            
                case KeyEvent.VK_I:
                renderer.w(rootPaneCheckingEnabled);
                break;
                case KeyEvent.VK_L:
                renderer.d(rootPaneCheckingEnabled);
                break;
                case KeyEvent.VK_J:
                renderer.a(rootPaneCheckingEnabled);
                break;
                case KeyEvent.VK_K:
                renderer.s(rootPaneCheckingEnabled);
                break;
                
                
            // If it wasn't one of those keys, do nothing
            default:
                break;
                
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            
        case KeyEvent.VK_UP:
            renderer.moveUp(false);
            break;
        case KeyEvent.VK_DOWN:
            renderer.moveDown(false);
            break;
            // If the user unpresses up/down, change the Y camera speed back.
            case KeyEvent.VK_Q:
                renderer.moveForward(false);
                break;
            case KeyEvent.VK_S:
                renderer.moveBackward(false);
                break;
            // If the user presses left/right, change the X camera speed back
            case KeyEvent.VK_A:
                renderer.turnLeft(false);
                break;
            case KeyEvent.VK_D:
                renderer.turnRight(false);
                break;
            // If it wasn't one of those keys, do nothing
            default:
                break;
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     * <p>
     * Called when a letter key is typed. Not used in this programme.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * (non-Javadoc)
     *
     * @param ActionEvent the object which triggered this action event. In this case
     *                    the object lets us know which menu item was selected
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     * <p>
     * Override of the actionPerformed method defined in the ActionListener interface.
     * Called every time a menu item is activated.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Determine which menu item is selected and act appropriately
        if (e.getSource().equals(exitItem)) {
            // Exit was selected - exit the program
            System.exit(0);
        }
        if (e.getSource().equals(resetItem)) {
            // Reset was selected - restart the position
            renderer.reset();
        }
        if(e.getSource().equals(vistaa)){
            renderer.vistaaerea();
        }
        if (e.getSource().equals(volver)) {
            // Reset was selected - restart the position
            renderer.volver();
        }
    }

}
