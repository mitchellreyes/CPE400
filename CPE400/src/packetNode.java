public class packetNode 
{
	//private static final int ID_INCREMENT = 10;
	static int packetCount = 0;
	static int idCount = 0;
	
	private int id;
	
	public packetNode()
	{
		idCount += 1;
		packetCount += 1;
		
		id = idCount;
	}
}
