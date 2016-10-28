/*
 * NetVisualizer ViewPort
*/
package netvisualizer.Frontend;

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
    
    private long window;
    
    private int WIDTH = 800;
    private int HEIGHT = 600;
    
    public NetView( NetGui newNetGui )
    {
        netGui = newNetGui;
    }
    
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
            errorCallback.free();
            keyCallback.free();
            glfwTerminate();
        }
    }
    
    private void init()
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
        glfwWindowHint( GLFW_RESIZABLE, GLFW_TRUE );
        
        // Create window
        window = glfwCreateWindow( WIDTH, HEIGHT, "NetView", NULL, NULL );
        if( window == NULL )
        {
            glfwTerminate();
            throw new RuntimeException( "Failed to create GLFW window" );
        }
        
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        keyCallback = new GLFWKeyCallback()
        {
            @Override
            public void invoke( long window, int key, int scancode, int action, int mods )
            {
                /*
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                {
                    glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
                }
                */
            }
        };

        glfwSetKeyCallback( window, keyCallback );
        
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
        
        // Set the clear color
        glClearColor(0.3f, 0.3f, 0.5f, 1.0f);
        
        while ( !glfwWindowShouldClose(window) )
        {
            time = glfwGetTime();
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(window);
            
            glfwPollEvents();
        }
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
}
