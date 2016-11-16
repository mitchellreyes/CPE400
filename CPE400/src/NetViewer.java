/*
 * NetVisualizer ViewPort
*/


import java.nio.*;
import java.util.ArrayList;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.lwjgl.BufferUtils;

public class NetViewer implements Runnable
{
    // Parent variable
    public netVisMain netVisMain;
    
    // GLFW Variables
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    
    private ArrayList<Renderable> renderObjs;
    
    private long window;
    
    private final int WIDTH = 960;
    private final int HEIGHT = 680;
    
    // Viewer Variables
    private int numNodes;
    private int numDegree;
    
    
    public NetViewer( netVisMain newNetVis )
    {
        netVisMain = newNetVis;
    }
    
    @Override
    public void run()
    {
        
        
        System.out.println("GFLW Window Opening");
        System.out.println("LWJGL Version: " + Version.getVersion());
        System.out.println("GLFW Version: " + glfwGetVersionString() );
        
        try
        {
            init();
            loop();
        }
        finally
        {
            System.out.println( "GLFW Window Closing" );
            
            netVisMain.netViewClosing();
            glfwDestroyWindow( window );
            Callbacks.glfwFreeCallbacks( window );
            glfwTerminate();
        }
    }
    
    private void init()
    {
        initializeGLFW();
        
        // Fetch variables from NetVisMain
        updateVariables();
        
        // Create renderables
        renderObjs = new ArrayList<Renderable>() {};
        populateRenderables();
        
        // Make window visible
        showWindow();
    }
    
    private void initializeGLFW()
    {
        GLFWVidMode vidmode;
        
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        errorCallback = GLFWErrorCallback.createPrint(System.err).set();
        glfwSetErrorCallback( errorCallback );
        
        // Initialize GLFW
        if( !glfwInit() )
        {
            throw new IllegalStateException( "Unable to initialize GLFW" );
        }
        
        // Configure window
        glfwWindowHint( GLFW_VISIBLE, GLFW_FALSE );
        glfwWindowHint( GLFW_RESIZABLE, GLFW_FALSE );
        
        // Create window
        window = glfwCreateWindow( WIDTH, HEIGHT, "NetView", NULL, NULL );
        if( window == NULL )
        {
            glfwTerminate();
            System.out.println("Failed to create GLFW window");
            throw new RuntimeException( "Failed to create GLFW window" );
        }
        
        // Create input callbacks
        createCallbacks();
        
        // Center window below NetVisMain
        int netVisLower = netVisMain.mainWindow.getLocation().y + netVisMain.mainWindow.getHeight();
        vidmode = glfwGetVideoMode( glfwGetPrimaryMonitor() );
        glfwSetWindowPos
            (
                window,
                (vidmode.width() - WIDTH) / 2,
                netVisLower + 50
            );
        
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();
    }
    
    private void loop()
    {
        double time;
        float ratio;
        IntBuffer bufWidth = BufferUtils.createIntBuffer(1);
        IntBuffer bufHeight = BufferUtils.createIntBuffer(1);
        
        // Set the clear color
        glClearColor(0.25f, 0.25f, 0.50f, 1.0f);
        
        while ( !glfwWindowShouldClose(window) )
        {
            time = glfwGetTime();
            
            // Calculate ratio
            glfwGetFramebufferSize( window, bufWidth, bufHeight );
            ratio = bufWidth.get() / (float) bufHeight.get();
            bufWidth.rewind();
            bufHeight.rewind();
            
            // Set Viewport and clear screen
            glViewport( 0, 0, bufWidth.get(), bufHeight.get() );
            glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
            
            // Render objects
            for( Renderable renderObj : renderObjs )
            {
                renderObj.render();
            }
            
            // Swap buffers and poll events
            glfwSwapBuffers( window );
            glfwPollEvents();
            
            // Flip buffers for next loop
            bufWidth.flip();
            bufHeight.flip();
        }    
    }
    
    private void createCallbacks()
    {
        keyCallback = new GLFWKeyCallback()
        {
            @Override
            public void invoke( long window, int key, int scancode, int action, int mods )
            {
                // Placeholder
            }
        };
        glfwSetKeyCallback( window, keyCallback );
        
        glfwSetInputMode( window, GLFW_CURSOR, GLFW_CURSOR_NORMAL );
        cursorPosCallback = new GLFWCursorPosCallback()
        {
            @Override
            public void invoke( long window, double xpos, double ypos )
            {
                // Placeholder
            }
        };
        glfwSetCursorPosCallback( window, cursorPosCallback );
        
        mouseButtonCallback = new GLFWMouseButtonCallback()
        {
          @Override
          public void invoke( long window, int button, int action, int mods )
          {
              if( button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS )
              {
                  // Placeholder
              }
          }
        };
        glfwSetMouseButtonCallback( window, mouseButtonCallback );
    }
    
    public void showWindow()
    {
        glfwShowWindow( window );
    }
    
    public void hideWindow()
    {
        glfwHideWindow( window );
    }
    
    public void closeWindow()
    {
        glfwSetWindowShouldClose(window, true);
    }
    
    public boolean isWindowVisible()
    {
        boolean visible;
        int visibleInt = glfwGetWindowAttrib( window, GLFW_VISIBLE );
        
        if( visibleInt == GLFW_TRUE ) visible = true;
        else visible = false;
        
        return visible;
    }
    
    public void updateVariables()
    {
        numNodes = netVisMain.numNodes;
        numDegree = netVisMain.numDegree;
        
        System.out.println(
                "Variables updated: Nodes = " + numNodes + ", Degree = " + numDegree
                );
    }
    
    private void populateRenderables()
    {
        // Create nodes in a circle and draw connections
        float radius = 15.0f;
        
        float curRadian = 0;
        float radStep = (float) (2*Math.PI) / numNodes;

        for(int i = 0; i < numNodes; i++)
        {
            Point nodePos = new Point
                (
                    ((float) Math.cos(curRadian) * radius),
                    ((float) Math.sin(curRadian) * radius)
                );
            
            createNode(nodePos);
            
            curRadian += radStep;
        }
    }
    
    private void createNode(Point newPos)
    {
        Node newNode = new Node(newPos);
        renderObjs.add(newNode);
    }
    
    private class Point
    {
        public float x;
        public float y;
        
        public Point(float newX, float newY)
        {
            x = newX;
            y = newY;
        }
    };
    
    private interface Renderable
    {
        void render();
    }
    
    private class Node implements Renderable
    {
        public Point location;
        
        public Node(float newX, float newY)
        {
            location.x = newX;
            location.y = newY;
            
            //System.out.println( "Creating node at: " + location.x + "," + location.y );
        }
        
        public Node( Point newLocation )
        {
            location = newLocation;
            //System.out.println( "Creating node at: " + location.x + "," + location.y );
        }
        
        @Override
        public void render()
        {
            float scale = 0.05f;
            
            // Set scale and position
            glLoadIdentity();
            glScalef(scale, scale, 1.0f);
            glTranslatef(location.x, location.y, 0.0f);
            
            glColor3f(0.8f, 0.8f, 0.8f);
            
            glBegin(GL_POLYGON);
            glVertex3f(-0.75f, 1.0f, 0.0f);
            glVertex3f(0.75f, 1.0f, 0.0f);
            glVertex3f(0.75f, -1.0f, 0.0f);
            glVertex3f(-0.75f, -1.0f, 0.0f);
            glEnd();

        }
    }
    
    private class Connection implements Renderable
    {
        public Point start;
        public Point end;
        
        public Connection(Point newStart, Point newEnd)
        {
            start = newStart;
            end = newEnd;
        }

        @Override
        public void render() {
            // glBegin lines code goes here
        }
    }
}
