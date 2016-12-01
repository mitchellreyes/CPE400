import java.util.*;

public class vertex {
	private ArrayList<edge> neighborhood;
	private String label;
	private int degree = 0;
	private int nodeID = 0;
	int dvTable[][];
	
	public Point position;
	
	public vertex(String label, int numNodes, int nodeID)
	{
		this.label = label;
		this.neighborhood = new ArrayList<edge>();
		this.nodeID = nodeID;
		position = new Point();
	}
	
	public void initializeDV(int numNodes)
	{
		int neighborCount = this.getNeighborCount();
		int neighborIndex = 0;
		
		dvTable = new int[numNodes][numNodes];
		
		for(int[] row: dvTable)
		{
			Arrays.fill(row, 99);
		}
		
		dvTable[nodeID][nodeID] = 0;
		while(neighborCount != 0)
		{
			dvTable[nodeID][this.getNeighborID(neighborIndex)] 
					= neighborhood.get(neighborIndex).getWeight();
			neighborCount--;
			neighborIndex++;
		}
	}
	
	public int getNeighborID(int index)
	{
		int nID;
		nID = neighborhood.get(index).getVertexOne().nodeID;
		if(this.nodeID == nID)
		{
			nID = neighborhood.get(index).getVertexTwo().nodeID;
		}
		return nID;
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
