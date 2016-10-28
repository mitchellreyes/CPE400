/*
 * NetVisualizer ViewPort
*/
package netvisualizer.Frontend;

import java.nio.*;
import java.util.ArrayList;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class NetView implements Runnable
{
    public NetGui netGui;
    
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    
    private ArrayList<Renderable> renderObjs;
    
    private long window;
    
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    
    public NetView( NetGui newNetGui )
    {
        netGui = newNetGui;
    }
    
    @Override
    public void run()
    {
        System.out.println( "GFLW Window Opening" );
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
            
            netGui.netViewClosing();
            glfwDestroyWindow( window );
            Callbacks.glfwFreeCallbacks( window );
            glfwTerminate();
        }
    }
    
    private void init()
    {
        GLFWVidMode vidmode;

        // Create renderables list
        renderObjs = new ArrayList<Renderable>() {};
        
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
        glfwWindowHint( GLFW_RESIZABLE, GLFW_TRUE );
        
        // Create window
        window = glfwCreateWindow( WIDTH, HEIGHT, "NetView", NULL, NULL );
        if( window == NULL )
        {
            glfwTerminate();
            throw new RuntimeException( "Failed to create GLFW window" );
        }
        
        createCallbacks();
        
        // Center Window
        vidmode = glfwGetVideoMode( glfwGetPrimaryMonitor() );
        glfwSetWindowPos
            (
                window,
                (vidmode.width() - WIDTH) / 2,
                (vidmode.height() - HEIGHT) / 2
            );
        
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();
        
        // Make window visible
        showWindow();
    }
    
    private void loop()
    {
        double time;
        float ratio;
        IntBuffer bufWidth = BufferUtils.createIntBuffer(1);
        IntBuffer bufHeight = BufferUtils.createIntBuffer(1);
        
        // Set the clear color
        glClearColor(0.3f, 0.3f, 0.5f, 1.0f);
        
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
                  if( NetGui.getTool() == NetGui.Tool.Node )
                  {
                      createNode();
                  }
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
    
    public boolean isWindowVisible()
    {
        boolean visible;
        int visibleInt = glfwGetWindowAttrib( window, GLFW_VISIBLE );
        
        if( visibleInt == GLFW_TRUE ) visible = true;
        else visible = false;
        
        return visible;
    }
    
    private void createNode()
    {
        DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);
        
        glfwGetCursorPos( window, xPos, yPos );
        
        
        
        Node newNode = new Node( xPos.get(0) , yPos.get(0) );
        renderObjs.add( newNode );
    }
    
    private interface Renderable
    {
        void render();
    }
    
    private class Node implements Renderable
    {
        float xPos, yPos;
        
        public Node( double newX, double newY )
        {
            xPos = (float) newX;
            yPos = (float) newY;
            System.out.println( "Creating node at: " + xPos + "," + yPos );
            // TODO integrate backend
        }
        
        @Override
        public void render()
        {
            float size = 0.6f;
            
            glLoadIdentity();
            glTranslatef( xPos, yPos, 0f );
            glColor3f(1f, 0f, 0f);
            
            glBegin( GL_TRIANGLES );
            glVertex3f( -size, -size, 0f );
            glVertex3f( +size, -size, 0f );
            glVertex3f( 0f, +size, 0f );
            glEnd();
        }
    }
}
