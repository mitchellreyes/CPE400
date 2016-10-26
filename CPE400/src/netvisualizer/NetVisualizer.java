/*
    NetVisualizer Main Class
*/
package netvisualizer;

import netvisualizer.Frontend.NetGui;

public class NetVisualizer
{
    public int numNodes = 0;
    public int numSourceNodes = 0;
    public int numDestNodes = 0;
    public int minDegree = 0;
    public int numPacketsToSend = 0;
    
    public static void main( String[] args )
    {
        NetVisualizer netVis = new NetVisualizer();
        NetGui netGui = new NetGui( netVis );
        
        if( netGui != null ) System.out.println("GUI Loaded Successfully");
    }
}
