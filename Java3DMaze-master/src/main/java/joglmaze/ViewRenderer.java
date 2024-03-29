package joglmaze;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.DebugGL2;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Joe Kilner
 * <p>
 * A View rendering class that handles all the OpenGL code.
 * <p>
 * This class creates a world with the following conventions: y is up the
 * default colour is white the default matrix is the modelview matrix
 */
public class ViewRenderer implements GLEventListener {

    // The current time for global animation
    private int time = 0;
    // The amount to update time
    private int deltaT = 1;
    // The maximum number of frames to skip
    private int maxFrameSkip = 10;
    // The delay between time updates
    private long delay;
    // The time last time we checked
    private long lastTime;
    // Camera Y position
    private static float yPos = 0, initYPos, yLookAt = 0;
// Current camera vertical-movement speed
    private float verticalSpeed = 0;
    private float verticalAngle = 0;
    //Camera X position, lookAt position
    private static float xPos = 1, initXPos, xLookAt = 0;
    //Camera Z position, lookAt position
    private static float zPos = 1, initZPos, zLookAt = 0;
    //Current camera forward-movement speed
    private float speed = 0;
    //Current camera angle
    private float angle = 0;
    //Current camera angular (turning) speed
    private float turn = 0;
    //Current camera angle
    private float camAngle;

    //The wall/ceiling/etc drawlist
    static int rectList = -1;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private float figureXPos = 2.0f;
    private float figureYPos = 0.0f;
    private float figureZPos = 1.3f;

    // The list of the maze cells
    private ArrayList<Cell> mazeCells;

    //Initialize the texture variable array
    private Texture[] textures = new Texture[3];

    GLU glu = new GLUgl2();
    GLUT glut = new GLUT();

    /**
     * Default constructor for the ViewRenderer class
     *
     * @param fps - the number of fps at which animation should occur.
     */
    public ViewRenderer(int fps) {
        // Delay between time updates in ms
        delay = 1000 / fps;
        // Look at the time now
        lastTime = System.currentTimeMillis();
    }

    /**
     * You have to implement dispose to avoid some warnings, but I don't need to
     * clean up
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    /**
     * (non-Javadoc)
     *
     * @see
     * javax.media.opengl.GLEventListener#display(javax.media.opengl.GLAutoDrawable)
     * <p>
     * This function sets up the camera and then tells the scene objects to draw
     * themselves
     */
    @Override
    public void display(GLAutoDrawable drawable) {

        // Get the OpenGL Context
        GL2 gl = drawable.getGL().getGL2();

        //Update the time variable for each frame
        time++;

        //Update the camera angle based on the angular speed
        angle += turn;

        //Update the camera position based on the current camera speeds
        xPos += speed * Math.sin(angle);
        zPos += speed * Math.cos(angle);
        yPos += verticalSpeed;

        //update the look-at position based on the current cam position
        xLookAt = (float) (xPos + Math.sin(angle));
        zLookAt = (float) (zPos + Math.cos(angle));

        yLookAt = (float) (yPos + Math.sin(verticalAngle));

        //Clear the colour and depth buffers
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        //Load the identity matrix and set up the camera based on its coords
        gl.glLoadIdentity();
        glu.gluLookAt(xPos, yPos, zPos, xLookAt, yLookAt, zLookAt, 0.0f, 1.0f, 0.0f);
//glu.gluLookAt(xPos, yPos, zPos, xLookAt, yPos, zLookAt, 0.0f, 1.0f, 0.0f);
        //glu.gluLookAt(8, 8, 8, 0, 0, 0, 0.0f, 1.0f, 0.0f);

        gl.glPushMatrix();

        //Iterate through the draw methods of each cell in the arraylist
        for (Cell cell : mazeCells) {
            cell.draw(textures, gl);
        }

        gl.glPopMatrix();

        // Flush the data.
        gl.glFlush();

        gl.glPushMatrix();
        // Añade un desplazamiento a la posición de la cámara
        gl.glTranslatef(figureXPos, figureYPos, figureZPos);
        drawFigure(gl);
        gl.glPopMatrix();
    }

    /**
     * (non-Javadoc)
     *
     * @see
     * javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
     * <p>
     * This function initialises the OpenGL environment
     */
    @Override
    public void init(GLAutoDrawable drawable) {

        drawable.setGL(new DebugGL2(drawable.getGL().getGL2()));
        GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);

        //Set up the default buffer clear parameters
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        gl.glClearDepth(1.0f);

        //Enable the depth testing
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);

        //Enable lighting
        gl.glEnable(GL2.GL_LIGHT6);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_LIGHT1);

        //Set up the minimum ambient lighting level of the whole scene
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{0.0f, 0.0f, 0.0f}, 0);

        //Set up the light's material properties
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.8f, 0.8f, 0.8f, 1.0f}, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.8f, 0.8f, 0.8f, 1.0f}, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.2f, 0.2f, 0.2f, 0.2f}, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, new float[]{50.0f}, 0);
        gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

        // using texture color as a base for material properties
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        //Set up a light source for light 0 and 1
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[]{0.5f, 0.5f, 0.2f, 1.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[]{0.4f, 0.4f, 0.4f, 1.0f}, 0);

        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, new float[]{0.5f, 0.5f, 0.2f, 1.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, new float[]{0.4f, 0.4f, 0.4f, 1.0f}, 0);

        //Set the attenuation parameter of the lights
        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_LINEAR_ATTENUATION, 0.5f);
        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_QUADRATIC_ATTENUATION, 0.5f);

        gl.glLightf(GL2.GL_LIGHT1, GL2.GL_LINEAR_ATTENUATION, 0.5f);
        gl.glLightf(GL2.GL_LIGHT1, GL2.GL_QUADRATIC_ATTENUATION, 0.5f);

        //Enable backface culling to save up more system resources
        gl.glCullFace(GL2.GL_BACK);
        gl.glEnable(GL2.GL_CULL_FACE);

        //Turn on texturing
        //gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        //gl.glActiveTexture(GL.GL_TEXTURE1);
        //gl.glEnable(GL2.GL_TEXTURE_2D);

        //Load the image files to be used as textures
        try {
            textures[0] = TextureIO.newTexture(new File("textures/brick.jpg"), true);
            textures[1] = TextureIO.newTexture(new File("textures/redbook.jpg"), true);
            textures[2] = TextureIO.newTexture(new File("textures/vine.png"), true);
        } catch (Exception e) {
            System.out.println("Error: cannot load textures");
            e.printStackTrace();
        }

        //Create a draw list of a generic wall/ceiling/floor rectangle
        //that consists of 100x100 smaller vertexes for smooth lignting
        rectList = gl.glGenLists(1);
        gl.glNewList(rectList, GL2.GL_COMPILE);
        gl.glPushMatrix();
        gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, -0.475f, -0.475f);
        //gl.glNormal3f(0,0,-1);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                gl.glBegin(GL2.GL_POLYGON);
                gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, (i / 20.0f) + (0.5f * 0.05f), (j / 20.0f) + (0.5f * 0.05f));
                gl.glVertex3f(-0.5f, (i / 20.0f) + (0.5f * 0.05f), (j / 20.0f) + (0.5f * 0.05f));
                gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, (i / 20.0f) - (0.5f * 0.05f), (j / 20.0f) + (0.5f * 0.05f));
                gl.glVertex3f(-0.5f, (i / 20.0f) - (0.5f * 0.05f), (j / 20.0f) + (0.5f * 0.05f));
                gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, (i / 20.0f) - (0.5f * 0.05f), (j / 20.0f) - (0.5f * 0.05f));
                gl.glVertex3f(-0.5f, (i / 20.0f) - (0.5f * 0.05f), (j / 20.0f) - (0.5f * 0.05f));
                gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, (i / 20.0f) + (0.5f * 0.05f), (j / 20.0f) - (0.5f * 0.05f));
                gl.glVertex3f(-0.5f, (i / 20.0f) + (0.5f * 0.05f), (j / 20.0f) - (0.5f * 0.05f));
                gl.glEnd();
            }
        }

        gl.glPopMatrix();
        gl.glEndList();

        //Load the maze from the text file
        mazeCells = MazeLoader.MakeMaze("maze_layout_1");

//final Runnable printer = new Runnable() {
//            public void run() {
//                System.out.println("Posición de la cámara: x=" + figureXPos + ", y=" + figureYPos + ", z=" + figureZPos);
//            }
//        };
//        final ScheduledFuture<?> printerHandle = scheduler.scheduleAtFixedRate(printer, 0, 5, TimeUnit.SECONDS);
//    }
    }

    /**
     * (non-Javadoc)
     *
     * @see
     * javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLAutoDrawable,
     * int, int, int, int)
     * <p>
     * Re-initialise the projection matrix and viewport after a reshape event
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
        GL2 gl = drawable.getGL().getGL2();
        if (height <= 0) {
            height = 1;
        }
        float aspectRatio = (float) x / (float) y;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(75, 1, 0.01, 100);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    /**
     * Update the time variable used in the world.
     */
    private void updateTime() {
        // Update the time for as much time as has passed
        long curTime = System.currentTimeMillis();
        long numFrames = ((curTime - lastTime) / delay);

        // Clamp numFrames to a minimum of 1 and a maximum of maxFrameSkip frames
        numFrames = Math.max(numFrames, 1l);
        numFrames = Math.min(numFrames, maxFrameSkip);

        // Increase time by the update amount. When deltaT is set
        // to zero this has no effect.
        time += deltaT * numFrames;

        // record the time so we can measure the time difference next frame
        // If we are paused or have skipped over maxFrameSkip frames, don't try to keep track of
        // partial frames
        if (delay == 0 || numFrames == maxFrameSkip) {
            lastTime = curTime;
        } else {
            lastTime += numFrames * delay;
        }
    }

    /**
     * Get the current time.
     *
     * @return The current time variable
     */
    public int getTime() {
        return time;
    }

    /**
     * Set the initial camera position
     *
     * @param initXPos the X position of the player
     * @param initZPos the Z position of the player
     */
    public static void setPos(float inXPos, float inYPos, float inZPos) {
        initXPos = inXPos;
        xPos = inXPos;
        initYPos = inYPos;
        yPos = inYPos;
        initZPos = inZPos;
        zPos = inZPos;
    }

    /**
     * Reset the camera position
     */
    public void reset() {
        xPos = initXPos;
        zPos = initZPos;
        yPos = initYPos;
        angle = 0;
        verticalAngle = 0;
//        verticalAngle=0;
    }

    public void volver() {
        xPos = (float) (figureXPos - 0.5f * Math.sin(angle));
        zPos = (float) (figureZPos - 0.5f * Math.cos(angle));
        yPos = figureYPos;
        angle = 0;
        verticalAngle = 0;
//        verticalAngle=0;
    }

    public void vistaaerea() {

        // Cambia las coordenadas de la cámara
        xPos = -9.7f;
        yPos = 24.1f;
        zPos = 19.4f;
        angle = 83.3f;
        verticalAngle = (float) Math.toRadians(220);
        // Ajusta el ángulo de la cámara para que mire hacia abajo
        // Aquí, 270 grados es solo un ejemplo, puedes ajustarlo según tus necesidades
        //verticalAngle = ;

    }

    /**
     * Move the camera forward
     *
     * @param move the boolean that decides to either move or stand still
     */
    public void w(Boolean move) {
        if (move) {
            figureZPos += 0.1f;
        }
    }

    public void s(Boolean move) {
        if (move) {
            figureZPos -= 0.1f;
        }
    }

    public void d(Boolean move) {
        if (move) {
            figureXPos -= 0.1f;
        }
    }

    public void a(Boolean move) {
        if (move) {
            figureXPos += 0.1f;
        }
    }

    public void moveForward(Boolean move) {
        if (move) {
            speed = 0.03f;
        } else {
            speed = 0f;
        }
    }

    /**
     * Move the camera backward
     */
    public void moveBackward(Boolean move) {
        if (move) {
            speed = -0.03f;
        } else {
            speed = 0f;
        }
    }

    /**
     * Turn the camera left
     */
    public void turnLeft(Boolean move) {
        if (move) {
            turn = 0.05f;
        } else {
            turn = 0f;
        }
    }

    /**
     * Turn the camera right
     */
    public void turnRight(Boolean move) {
        if (move) {
            turn = -0.05f;
        } else {
            turn = 0f;
        }
    }

    public void moveUp(Boolean move) {
        if (move) {
            verticalSpeed = 0.03f;
            yLookAt += 0.05f;  // Ajusta este valor según sea necesario
        } else {
            verticalSpeed = 0f;
        }
    }

    public void moveDown(Boolean move) {
        if (move) {
            verticalSpeed = -0.03f;
            yLookAt -= 0.05f;  // Ajusta este valor según sea necesario
        } else {
            verticalSpeed = 0f;
        }
    }

    public void drawFigure(GL2 gl) {
        // Habilita el color de material y la normalización

        // Configura las propiedades de material
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[]{1.0f, 1.0f, 0.0f, 1.0f}, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, new float[]{0.5f, 0.5f, 0.0f, 1.0f}, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, new float[]{60.0f}, 0);

        // Aquí dibujas tu figura 3D.
        // Por ejemplo, podrías dibujar una pirámide cuadrangular.
        gl.glPushMatrix();
        gl.glColor3f(0.0f, 0.5f, 1.0f);  // Color azul claro

        // Dibuja una pirámide cuadrangular
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glVertex3f(0.0f, 0.1f, 0.0f);
        gl.glVertex3f(-0.1f, -0.1f, 0.1f);
        gl.glVertex3f(0.1f, -0.1f, 0.1f);

        gl.glVertex3f(0.0f, 0.1f, 0.0f);
        gl.glVertex3f(0.1f, -0.1f, 0.1f);
        gl.glVertex3f(0.1f, -0.1f, -0.1f);

        gl.glVertex3f(0.0f, 0.1f, 0.0f);
        gl.glVertex3f(0.1f, -0.1f, -0.1f);
        gl.glVertex3f(-0.1f, -0.1f, -0.1f);

        gl.glVertex3f(0.0f, 0.1f, 0.0f);
        gl.glVertex3f(-0.1f, -0.1f, -0.1f);
        gl.glVertex3f(-0.1f, -0.1f, 0.1f);
        gl.glEnd();

        gl.glPopMatrix();
    }
}
