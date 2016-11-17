import java.util.ArrayList;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GraphicsArea extends JPanel
{
        private static final long serialVersionUID = 1L;
        private Graphics graphics;
        
        private ArrayList<VertexGraphic> vertices;
        private ArrayList<EdgeGraphic> edges;
        
        private netVisMain parent;
        
        private boolean visualizationBegan = false;
        
        private static final int INTERVAL = 16; // 60 times/sec
        private Timer timer;
        
        // Constructor
        public GraphicsArea(netVisMain newParent)
        {
            parent = newParent;
            vertices = new ArrayList<>();
            edges = new ArrayList<>();
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
            visualizationBegan = true;
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
            
            private Point position;
            private int size;
            
            // Parameterized constructor
            public VertexGraphic(Point newPosition, String newLabel)
            {
                color = DEFAULT_COLOR;
                size = DEFAULT_SIZE;

                position = new Point(newPosition);
                label = newLabel;
                
                System.out.println
                    ("Creating new node at: " + position.x + "," + position.y);
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
                graphics.clearRect(position.x-size/2, position.y-size/2, size, size);
                
                graphics.setColor(color);
                graphics.drawRect(position.x-size/2, position.y-size/2, size, size);
                
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
                
                start = newStart;
                end = newEnd;
                
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
                graphics.setColor(color);
                graphics.drawLine(start.x, start.y, end.x, end.y);
                
                graphics.drawString
                        (
                            "" + weight,
                            ((start.x + end.x) / 2) + 5,
                            ((start.y + end.y) / 2) - 5
                        );
            }
        }
        
        /*
            ActionHandler for interval/repaint timer
        */
        private class ActionHandler implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                repaint();
            }
        }

    }