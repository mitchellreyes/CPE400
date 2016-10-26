/*
 * NetVisualizer ViewPort
*/
package netvisualizer.Frontend;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Zachary Waller
 */
public class NetView extends JPanel
{
    /* 
     * Adds a blank Node to the view at the current mouse position
    */
    public Node addNode(int x, int y)
    {
        Node newNode = new Node();
        Graphics g = this.getGraphics();
        
        g.setColor(Color.white);
        g.fillRect(x, y, 10, 10);
        
        return newNode;
    }
    
    class Node extends JComponent
    {
        private String name;
        
        public Node()
        {
            name = "Blank Node";
        }
        

        @Override
        public String toString()
        {
            return name;
        }
    }
}
