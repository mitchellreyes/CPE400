import java.util.*;

public class node {
	private static final int MAX_PACKETS = 10;
	private static final int ID_INCREMENT = 10;
	//nodeCount will keep track of how many nodes are made each time the constructor is called
	static int nodeCount = 0;
	static int idCount = 100;
	
	private int id;
	private String status;
	private List<Integer> packets = new ArrayList<Integer>();
	private List<node> connections = new ArrayList<node>();

	public node()
	{
		nodeCount += 1;
		
		status = "INNER";
		id = idCount;
		idCount += ID_INCREMENT;
	}
	
	public int getID(){return id;}
	
	public void setStatus(String input){status = input;}
	
	public String getStatus(){return status;}
	
	public boolean isFull(){return (packets.size() == MAX_PACKETS);}
	
	public void addConnection(node Node){connections.add(Node);}
	
	public int getNodeCount(){return nodeCount;}
	
	public boolean addPacket(Integer packet)
	{
		if(packets.size() < MAX_PACKETS)
		{
			packets.add(packet);
			return true;
		}
		return false;
	}
	

}
