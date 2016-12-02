import java.sql.Timestamp;
import java.util.*;

public class vertex implements Runnable {
	private ArrayList<edge> neighborhood;
	private String label;
	private int desiredDegree = -1;
	private int degree = 0;
	private int nodeID = 0;
	private int numNodes = 0;
	private DVTable dvTable;
	
	private boolean linkCostsChanged;
	
	public Point position;
	
	public vertex(String label, int numNodes, int nodeID)
	{
		this.label = label;
		this.neighborhood = new ArrayList<edge>();
		this.nodeID = nodeID;
		this.numNodes = numNodes;
		position = new Point();
		dvTable = new DVTable(numNodes);
		
		linkCostsChanged = false;
	}
	
	@Override
	public void run()
	{
		initializeDV();
		loopDV();
		
	}
	
	public void initializeDV()
	{
		int neighborCount = this.getNeighborCount();
		int neighborIndex = 0;

		for(int[] row: dvTable.costs)
		{
			Arrays.fill(row, 99);
		}
		
		dvTable.costs[nodeID][nodeID] = 0;
		while(neighborCount != 0)
		{
			dvTable.costs[nodeID][this.getNeighborID(neighborIndex)] 
					= neighborhood.get(neighborIndex).getWeight();
			neighborCount--;
			neighborIndex++;
		}
	}
	
	public void loopDV()
	{
		while(true)
		{
			// Wait until link cost changes
			// while(!linkCostsChanged);
			
			for(int neighborIndex = 0; neighborIndex < this.getNeighborCount(); neighborIndex++)
			{
				sendDV(this.getNeighborVertex(neighborIndex));
				//linkCostsChanged = false;
			}
		}
	}
	
	public void sendDV(vertex neighbor)
	{
		neighbor.receiveDV(this.nodeID, dvTable.costs[this.nodeID]);
	}
	
	public void receiveDV(int senderID, int[] dvRow)
	{
		
		
		// Copy sender DV Row 
		this.dvTable.costs[senderID] = Arrays.copyOf(dvRow, numNodes);
		// Copy timestamps
		
		// For each destination in dvRow
		// if distance vector cost + edge weight < destination cost in dvTable row
		// then dvTable.cost(dst) = destination vector cost + edge cost
		// 		dvTable.timestamp(dst) = time.now
		
		// Update this DV Row
		for(int index = 0; index < numNodes; index++)
		{
			// if index is neighbor
			// {
				if((dvRow[index] + this.getNeighbor(index).getWeight()) < dvTable.costs[this.nodeID][index])
				{
					dvTable.costs[this.nodeID][index] = dvRow[index] + this.getNeighbor(index).getWeight();
					// timestamp
				}
			// }
			// else
			//  Non neighbor case
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
	
	public int getDesiredDegree()
	{
		return desiredDegree;
	}
	
	public void setDesiredDegree(int newDegree)
	{
		desiredDegree = newDegree;
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
	
	public vertex getNeighborVertex(int index)
	{
		edge nEdge = this.neighborhood.get(index);
		
		if(nEdge.getVertexOne().equals(this) == false )
		{
			return nEdge.getVertexOne();
		}
		else
		{
			return nEdge.getVertexTwo();
		}
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
	
	
	public class DVTable
	{
		public int[][] costs;
		public Timestamp[][] timestamps;
		
		public DVTable(int numNodes)
		{
			costs = new int[numNodes][numNodes];
			timestamps = new Timestamp[numNodes][numNodes];
		}
	}
}
