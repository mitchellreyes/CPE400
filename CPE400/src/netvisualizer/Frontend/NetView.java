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
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }
    
    private void init()
    {
        GLFWVidMode vidmode;

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();
        
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
            throw new RuntimeException( "Failed to create GLFW window" );
        }
        
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            /*
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
            {
                glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
            }
            */
        });

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
        
        // Make window visible
        showWindow();
    }
    
    private void loop()
    {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.3f, 0.3f, 0.5f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }
    
    public void showWindow()
    {
        glfwShowWindow(window);
    }
    
    public void hideWindow()
    {
        glfwHideWindow(window);
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
