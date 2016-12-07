
import java.util.*;

public class vertex implements Runnable {
	private ArrayList<edge> neighborhood;
	private String label;
	private int desiredDegree = -1;
	private int degree = 0;
	private int numNodes = 0;
	private DVTable dvTable;
	public boolean running = false;
	private vertex[] allVerticies;
	private Thread myThread;
	
	public Point position;
	
	private List<DVTableEntry[]> receivedRows = new ArrayList<DVTableEntry[]>();
	
	public vertex(String label, int numNodes, int nodeID)
	{
		this.label = label;
		this.neighborhood = new ArrayList<edge>();
		this.numNodes = numNodes;
		position = new Point();
	}
	
	public void setMyThread(Thread t){
		myThread = t;
	}
	
	public Thread getMyThread(){
		return myThread;
	}
	
	public void addAllVerticies(vertex[] verticies){
		allVerticies = verticies;
	}
	
	public void setRunning(boolean b){
		running = b;
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
	
	public int distanceToNeighbor(vertex v){
		vertex vertexToCheck = v;
		for(int i = 0; i < this.getNeighborCount(); i++){
			if(this.getNeighbor(i).getVertexOne().equals(this)){
				vertexToCheck = this.getNeighbor(i).getVertexTwo();
			}
			else{
				vertexToCheck = this.getNeighbor(i).getVertexOne();
			}
			if(this.getNeighbor(i).getNeighbor(this).equals(vertexToCheck)){
				return this.getNeighbor(i).getWeight();
			}
		}
		return 99;
	}
	
	public boolean isMyNeighbor(vertex v){
		vertex vertexToCheck = v;
		for(int i = 0; i < this.getNeighborCount(); i++){
			if(this.getNeighbor(i).getVertexOne().equals(this)){
				vertexToCheck = this.getNeighbor(i).getVertexTwo();
			}
			else{
				vertexToCheck = this.getNeighbor(i).getVertexOne();
			}
			if(this.getNeighbor(i).getNeighbor(vertexToCheck).equals(this)){
				return true;
			}
		}
		return false;
	}
	
	public void addDVTable(DVTable dV){
		dvTable = new DVTable(dV);
	}
	
	public void printDVTable(){
		dvTable.printDVTable();
	}

	public DVTable getMyDVTable(){
		return dvTable;
	}
	
	public int getMyRowIndex(){
		return dvTable.getRowIndex();
	}
	
	public void sendMyDVTableRow(){
		for(int i = 0; i < numNodes; i++){
			if(!allVerticies[i].equals(this)){
				allVerticies[i].reciveDVTableRow(this.dvTable.getMyRow());
			}
		}
	}

	public void reciveDVTableRow(DVTableEntry[] received){
		//if we haven't received this row yet
		if(receivedRows != null && !receivedRows.contains(received)){
			//add it to our queue
			receivedRows.add(received);
			//System.out.println(this.getLabel() + " RECEIVED " + received[0].getSrc() + "'s row!");
		}
		
		//while our queue is empty
		while(receivedRows.isEmpty()/*&& the visualization is still running*/);
		//while our queue isn't empty
		//while(!receivedRows.isEmpty()){
		if(!receivedRows.isEmpty()){
			for(int i = 0; i < receivedRows.size() - 1; i++){
				dvTable.updateRow(receivedRows.get(i));
				//System.out.println(this.getLabel() + "'s UPDATED TABLE:");
				//dvTable.printDVTable();
				if(receivedRows.size() != 0){
					receivedRows.remove(receivedRows.get(i));
				}
			}
		}
	}
	
	public void receiveBrokenLinkMessage(DVTableEntry message){
		if(message == null){
			return;
		}
		dvTable.getEntry(message.src, message.dest).setWeight(99);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
			sendMyDVTableRow();
	}
}
