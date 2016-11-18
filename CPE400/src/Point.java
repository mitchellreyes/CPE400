/*
    Simple class to hold a two-dimensional coordinate
*/

public class Point
{
    public int x;
    public int y;

    // Copy constructor
    public Point(Point rhs)
    {
        x = rhs.x;
        y = rhs.y;
    }

    // Parameterized constructor
    public Point(int newX, int newY)
    {
        x = newX;
        y = newY;
    }
    
    // Default constructor
    public Point()
    {
        x = 0;
        y = 0;
    }
    
    /*
        Distance To
    
        Calculates the distance to another Point
    */
    public double distanceTo(Point other)
    {
        double hyp = Math.sqrt
                    (
                        Math.pow(other.x - x, 2) + 
                        Math.pow(other.y - y, 2)
                    );
        
        return hyp;
    }
}
