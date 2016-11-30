import java.util.ArrayList;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.time.*;
import java.util.Set;

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
            int weight = findWeight(source, dest);
            packets.add(new PacketGraphic(source, dest, weight));
        }
        
        /*
            Find Weight
        
            Finds the weight of edge between two vertices
        */
        private int findWeight(vertex one, vertex two)
        {
            for(edge e : one.getNeighbors())
            {
                if(e.getNeighbor(one) == two || e.getNeighbor(two) == one)
                {
                    return e.getWeight();
                }
            }
            
            // TODO: Fix this hack
            // If edge isn't found return 1 so nothing breaks
            return 1;
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
            private final int DEFAULT_SIZE = 45;
            
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
                java.awt.FontMetrics fm = graphics.getFontMetrics();
                int strWidth = fm.stringWidth(label);
                
                graphics.drawString
                    (
                        label,
                        position.x - strWidth / 2 + 1,
                        position.y + fm.getAscent() / 2
                    );
                
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
                java.awt.FontMetrics fm = graphics.getFontMetrics();
                int wide = fm.charWidth(weight);
                int tall = fm.getAscent();
                Point wPos = new Point
                        (
                            (start.x + end.x) / 2,
                            (start.y + end.y) / 2
                        );
                
                graphics.clearRect(wPos.x-wide/2, wPos.y-tall/2, wide, tall);
                
                graphics.drawString(""+weight, wPos.x-wide/3, wPos.y+tall/3);
            }
        }
        
        private class PacketGraphic extends Drawable
        {
            // Color color is outline color
            private final Color DEFAULT_COLOR = Color.BLACK;
            private final Color DEFAULT_FILL_COLOR = Color.GRAY;
            private final int DEFAULT_SIZE = 20;
            private final int DEFAULT_WEIGHT_TIME = 250;
            
            private Color fillColor;
            
            private Point position;
            private int size;
            
            private Point source;
            private Point dest;
            private int weight;
            
            private boolean pointsValid = false;
            
            private vertex sourceVertex;
            private vertex destVertex;
            
            private int timeToTravel;
            private int timeElapsed;
            
            /*
                Parameterized Constructor with vertices
            */
            public PacketGraphic(vertex newSourceVert, vertex newDestVert, int newWeight)
            {
                color = DEFAULT_COLOR;
                fillColor = DEFAULT_FILL_COLOR;
                size = DEFAULT_SIZE;
                weight = newWeight;
                
                timeToTravel = weight * DEFAULT_WEIGHT_TIME;
                
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

                position = new Point(source);
                timeElapsed = 0;

                System.out.println
                    (
                        "Sending packet from "
                        + sourceVertex.getLabel() + " to "
                        + destVertex.getLabel()
                        + "; Weight = " + weight
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
                Move
            
                Moves the packet along the edge at the correct speed
                Updates distanceTraveled
                When distanceTraveled >= hyp, informs backend that packet reached
                its destination and removes graphic from packet array
            */
            private void move()
            {
                double percent = (double) timeElapsed / (double) timeToTravel;
                Point newPos = lerp(source, dest, percent);
                
                position = newPos;
                timeElapsed += (deltaT * 1000);
                
                if(timeElapsed >= timeToTravel)
                {
                    // Inform backend that packet reached destination
                    parent.packetDelivered(sourceVertex, destVertex);
                    
                    packets.remove(this);
                }
            }
            
            /*
                Lerp
            
                Linearly interpolate between two points
            */
            private Point lerp(Point start, Point end, double percent)
            {
                Point result = new Point();
                result.x = (int) (start.x + percent * (end.x - start.x));
                result.y = (int) (start.y + percent * (end.y - start.y));

                return result;
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