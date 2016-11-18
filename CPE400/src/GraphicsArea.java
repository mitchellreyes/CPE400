import java.util.ArrayList;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.time.*;

public class GraphicsArea extends JPanel
{
        private static final long serialVersionUID = 1L;
        private Graphics graphics;
        
        private ArrayList<VertexGraphic> vertices;
        private ArrayList<EdgeGraphic> edges;
        private ArrayList<PacketGraphic> packets;
        
        private netVisMain parent;
        
        private boolean visualizationBegan = false;
        
        private static final int INTERVAL = 16; // 60 times/sec
        private Timer timer;
        private double deltaT; // delta T between frames, in sec
        
        // Constructor
        public GraphicsArea(netVisMain newParent)
        {
            parent = newParent;
            vertices = new ArrayList<>();
            edges = new ArrayList<>();
            packets = new ArrayList<>();
        }
        
        /*
            Begin Visualization
        
            Places all of the netGraph's components and begins the drawing
            interval
        */
        public void beginVisualization()
        {
            placeVertices();
            timer = new Timer(INTERVAL, new ActionHandler());
            timer.start();
            deltaT = 0.0d;
            visualizationBegan = true;
        }
        
        /*
            Stop Visualization
        
            Stops visualization
            Empties vertex and edge graphics lists
        */
        public void stopVisualization()
        {
            visualizationBegan = false;
            
            timer.stop();
            timer = null;
            
            vertices.clear();
            edges.clear();
            
            repaint();
        }
        
        /*
            Place Vertices
        
            Places all of the vertex graphics in a circle
            Creates edge graphics between connected vertices
        */
        private void placeVertices()
        {
            // Get vertex and edge collections
            java.util.HashMap<String, vertex> vertMap = parent.getGraph().getVertices();
            java.util.Set<edge> edgeSet = parent.getGraph().getEdges();
            
            //Set up circle
            Point center = new Point(getWidth() / 2, getHeight() / 2);
            int limiter = Math.min(getWidth(), getHeight());
            int radius = (int) (limiter * 0.9) / 2;
            
            double curRadian = -0.5 * Math.PI;
            double radStep = (2.0f * Math.PI) / ((double) vertMap.size());
            
            // Place vertex graphics
            for(vertex v : vertMap.values())
            {
                Point pos = new Point
                    (
                        (int) center.x + (int) (Math.cos(curRadian) * radius),
                        (int) center.y + (int) (Math.sin(curRadian) * radius)
                    );
                
                vertices.add(new VertexGraphic(pos, v.getLabel()));
                v.position = pos; // update position in vertex map
                
                curRadian += radStep;
            }
            
            // Place edge graphics
            for(edge e : edgeSet)
            {
                edges.add(new EdgeGraphic
                    (
                        e.getVertexOne().position,
                        e.getVertexTwo().position,
                        e.getWeight()
                    ));
            }
        }
        
        /*
            Send Packet
            
            Adds a packet to the visualization
        */
        public void sendPacket(vertex source, vertex dest)
        {
            packets.add(new PacketGraphic(source, dest));
        }
        
        /*
            Paint
        
            Redraws all of the graphics
        */
        @Override
        public void paint(Graphics newG)
        {
            graphics = newG;
            super.paintComponent(graphics);
            
            if(visualizationBegan)
            {
                for(EdgeGraphic eg: edges)
                {
                    eg.draw();
                }
                
                // Removing a packet from packets arraylist will cause for loop
                // to throw exception, arraylist fixes itself on next call
                // though so no action needed to correct issue
                try
                {
                    for(PacketGraphic pg : packets)
                    {
                        pg.draw();
                    }
                }
                catch(Exception e)
                {
                    // Do nothing
                }
                
                for(VertexGraphic vg : vertices)
                {
                    vg.draw();
                }
            }
        }
        
        /*
            Drawable Classes
        */
        
        private abstract class Drawable
        {
            Color color;
            
            public abstract void draw();
        }
        
        private class VertexGraphic extends Drawable
        {
            private final Color DEFAULT_COLOR = Color.RED;
            private final int DEFAULT_SIZE = 40;
            
            private String label;
            
            private Point position; // position at center
            private int size;
            
            // Parameterized constructor
            public VertexGraphic(Point newPosition, String newLabel)
            {
                color = DEFAULT_COLOR;
                size = DEFAULT_SIZE;

                position = new Point(newPosition);
                label = newLabel;
                
                System.out.println
                    ("Creating vertex " + label + " at: " + position.x + "," + position.y);
            }
            
            /*
                Get/Set
            */
            
            public void setColor(Color newColor)
            {
                color = newColor;
            }
            
            public void setSize(int newSize)
            {
                size = newSize;
            }
            
            public Point getPosition()
            {
                return new Point(position.x, position.y);
            }
            
            /*
                Draw Override
            */
            @Override
            public void draw()
            {
                int offsetX = position.x - (size / 2);
                int offsetY = position.y - (size / 2);
                
                graphics.clearRect(offsetX, offsetY, size, size);
                
                // Draw outline
                graphics.setColor(color);
                graphics.drawRect(offsetX, offsetY, size, size);
                
                // Draw label
                graphics.setColor(Color.BLACK);
                graphics.drawString(label, position.x-5, position.y+5);
            }
        }
        
        private class EdgeGraphic extends Drawable
        {
            private final Color DEFAULT_COLOR = Color.BLACK;
            
            private Point start;
            private Point end;
            
            private int weight;
            
            /*
                Parameterized Constructor
            */
            public EdgeGraphic(Point newStart, Point newEnd, int newWeight)
            {
                color = DEFAULT_COLOR;
                
                start = new Point(newStart);
                end = new Point(newEnd);
                
                weight = newWeight;
            }
            
            /*
                Get/Set
            */
            
            private void setColor(Color newColor)
            {
                color = newColor;
            }
            
            /*
                Draw Override
            */
            @Override
            public void draw()
            {
                // Draw Line
                graphics.setColor(color);
                graphics.drawLine(start.x, start.y, end.x, end.y);
                
                // Draw weight
                graphics.drawString
                        (
                            "" + weight,
                            ((start.x + end.x) / 2) + 5,
                            ((start.y + end.y) / 2) - 5
                        );
            }
        }
        
        private class PacketGraphic extends Drawable
        {
            // Color color is outline color
            private final Color DEFAULT_COLOR = Color.BLACK;
            private final Color DEFAULT_FILL_COLOR = Color.GRAY;
            private final int DEFAULT_SIZE = 20;
            private final int DEFAULT_SPEED = 10;
            
            private Color fillColor;
            
            private Point position;
            private int size;
            
            private Point source;
            private Point dest;
            
            private boolean pointsValid = false;
            
            private vertex sourceVertex;
            private vertex destVertex;
            
            private double theta; // angle between source/dest, in radians
            private double hyp; // hypotenuse - required distance to travel
            private int speed; // in pixels/sec
            private int distanceTraveled = 0;
            
            /*
                Parameterized Constructor with vertices
            */
            public PacketGraphic(vertex newSourceVert, vertex newDestVert)
            {
                color = DEFAULT_COLOR;
                fillColor = DEFAULT_FILL_COLOR;
                size = DEFAULT_SIZE;
                speed = DEFAULT_SPEED;
                
                sourceVertex = newSourceVert;
                destVertex = newDestVert;
                
                if(visualizationBegan)
                {
                    validatePoints();
                }
            }
            
            /*
                Initialize Graphic
            
                Called if packet was created before vizualization began
            */
            private void validatePoints()
            {
                updateVertexPositions();
                calculateTheta();

                position = new Point(source);

                System.out.println
                    (
                        "Sending packet from "
                        + sourceVertex.getLabel() + " to "
                        + destVertex.getLabel()
                        + "; Angle = " + Math.toDegrees(theta)
                    );
            }
            
            /*
                Update Positions
            
                Gets the current positions of source and dest vertices and
                recalculates theta
            */
            private void updateVertexPositions()
            {
                source = new Point(sourceVertex.position);
                dest = new Point(destVertex.position);
                
                pointsValid = true;
            }
            
            /*
                Calculate Theta
            
                Calculates the angle between source and dest vertices
            */
            private void calculateTheta()
            {
                hyp = Math.sqrt
                    (
                        Math.pow(dest.x - source.x, 2) + 
                        Math.pow(dest.y - source.y, 2)
                    );
                
                theta = Math.atan2(dest.y - source.y, dest.x - source.x);
            }
            
            /*
                Move
            
                Moves the packet along the edge at the correct speed
                Updates distanceTraveled
                When distanceTraveled >= hyp, informs backend that packet reached
                its destination and removes graphic from packet array
            */
            private void move()
            {
                Point newPos = new Point();
                
                newPos.x = (int) (position.x + (Math.cos(theta) * speed));
                newPos.y = (int) (position.y + (Math.sin(theta) * speed));
                
                distanceTraveled += position.distanceTo(newPos);
                position = newPos;
                
                if(distanceTraveled >= hyp)
                {
                    // Inform backend that packet reached destination
                    parent.packetDelivered(sourceVertex, destVertex);
                    
                    packets.remove(this);
                }
            }
            
            /*
                Get/Set
            */
            
            
            /*
                Draw override
            */
            @Override
            public void draw()
            {
                if(!pointsValid) validatePoints();
                
                int offsetX = position.x - (size / 2);
                int offsetY = position.y - (size / 2);
                
                // Draw Fill
                graphics.setColor(fillColor);
                graphics.fillRect(offsetX, offsetY, size, size);
                
                // Draw Outline
                graphics.setColor(color);
                graphics.drawRect(offsetX, offsetY, size, size);
                
                // Move packet along edge
                move();
            }
        }
        
        /*
            ActionHandler for interval/repaint timer
        */
        private class ActionHandler implements ActionListener
        {
            private Clock clock;
            private Instant prevTime;
            
            public ActionHandler()
            {
                clock = Clock.systemUTC();
                prevTime = clock.instant();
            }
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Instant curTime = clock.instant();
                deltaT = (curTime.toEpochMilli() - prevTime.toEpochMilli()) / 1000.0d;
                prevTime = curTime;
                
                repaint();
            }
        }

    }