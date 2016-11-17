import java.util.*;

public class vertex {
	private ArrayList<edge> neighborhood;
	private String label;
	private int degree = 0;
	
	public vertex(String label)
	{
		this.label = label;
		this.neighborhood = new ArrayList<edge>();
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public int getDegree()
	{
		return degree;
	}
	
	public void addNeighbor(edge Edge)
	{
		if(this.neighborhood.contains(Edge))
			return;
		this.neighborhood.add(Edge);
		degree++;
	}
	
	public boolean containsNeighbor(edge other)
	{
		return this.neighborhood.contains(other);
	}
	
	public edge getNeighbor(int index)
	{
		return this.neighborhood.get(index);
	}
	
	edge removeNeighbor(int index)
	{
		degree--;
		return this.neighborhood.remove(index);
	}
	
	public void removeNeighbor(edge e)
	{
		this.neighborhood.remove(e);
		degree--;
	}
	
	public int getNeighborCount()
	{
		return this.neighborhood.size();
	}
	
	public ArrayList<edge> getNeighbors()
	{
		return new ArrayList<edge>(this.neighborhood);
	}
	
	public boolean equals(Object otherVertex){
		if(!(otherVertex instanceof vertex)){
			return false;
		}
		vertex v = (vertex)otherVertex;
		return this.label.equals(v.label);
	}
	
}
