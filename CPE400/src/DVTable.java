import java.io.*;

/*This class models the functionality of what the distance vector table looks like.
 * Uses the Bellman-Ford Equation to figure out the best path and updates the table to the according values.
 * 
 * @author: Mitchell Reyes
 * @date 12/7/2016
 * 
 */

public class DVTable {
	public static final int BROKEN_WEIGHT = -1;
	public static final int UNREACHABLE = 99;
	private int numNodes;
	int weightToAdd;
	int myRow = -1;
	vertex v;
	DVTableEntry dvTable[][];
	vertex[] verticies;
	Thread myThread;
	
	/*constructor
	 * @param: all the nodes in the graph, the number of nodes in the graph, and the vertex this DV table is for
	 */
	public DVTable(vertex[] nodes, int num, vertex me){
		numNodes = num;
		dvTable = new DVTableEntry[numNodes][numNodes];
		v = me;
		verticies = nodes;
		for(int i = 0; i < numNodes; i++){
			for(int j = 0; j < numNodes; j++){
				//if the node row isn't me, the weight will be 
				if(!me.equals(nodes[i])){
					weightToAdd = UNREACHABLE;
				}
				else{
					myRow = i;
					weightToAdd = checkMyNeighbors(nodes[j]);
				}
				dvTable[i][j] = new DVTableEntry(nodes[i], nodes[j], weightToAdd);

			}
		}
	}
	
	//copy constructor
	public DVTable(DVTable other){
		numNodes = other.getNumNodes();
		dvTable = other.dvTable;
		myRow = other.myRow;
		verticies = other.verticies;
		v = other.v;
	}
	
	/*private boolean checkIfMyNeighborsAreBroken(){
		//for everyone in my neighborhood
		for(int i = 0; i < v.getNeighborCount(); i++){
			if(!v.getNeighbor(i).isBroken()){
				return false;
			}
		}
		return true;
	}
	
	private void setMyRowToUnreachable(){
		for(int i = 0; i < this.getMyRow().length; i++){
			this.getMyRow()[i].setWeight(UNREACHABLE);
		}
	}*/
	
	/*The main component of the DV Table: how the BF equation comes into play
	 * @param: the row the table received
	 */
	public void updateRow(DVTableEntry[] receivedRow){
		vertex dest;
		int srcToMe, me2Dest, me2Src, src2Dest;
		if(receivedRow[0].src != null){
			for(int myRowIndex = 0; myRowIndex < this.getMyRow().length; myRowIndex++){
				dest = this.getMyRow()[myRowIndex].dest;
				srcToMe = receivedRow[0].src.getMyDVTable().getEntry(receivedRow[0].src, v).getWeight();
				me2Dest = this.getEntry(v, dest).getWeight();
				me2Src = this.getEntry(v, receivedRow[0].src).getWeight();
				src2Dest = receivedRow[0].src.getMyDVTable().checkMyNeighbors(dest);
				//if the destination is NOT me
				if(!dest.equals(v)){
					//if the destination is the source of the received row
					//X->Y = BROKEN, but Y->X = GOOD
					if(dest.equals(receivedRow[0].src)){
						//check if me to the src is UNREACHABLE
						if(me2Dest == UNREACHABLE || me2Dest == BROKEN_WEIGHT){
							//if they can reach me
							if(srcToMe != UNREACHABLE && srcToMe != BROKEN_WEIGHT){
								this.getMyRow()[myRowIndex].setWeight(srcToMe);
							}
						}
						//REACHABLE
						else if(me2Dest != UNREACHABLE && me2Dest != BROKEN_WEIGHT){
							//check the source's entry on them to me
							if(srcToMe != UNREACHABLE && srcToMe != BROKEN_WEIGHT && (srcToMe < this.getMyRow()[myRowIndex].getWeight())){
								this.getMyRow()[myRowIndex].setWeight(srcToMe);
							}
						}
					}
					//if my destination is not the source
					else{
						//if me to my destination is REACHABLE
						if(me2Dest != UNREACHABLE && me2Dest != BROKEN_WEIGHT){
							//if they can reach my destination
							if(src2Dest != UNREACHABLE && src2Dest != BROKEN_WEIGHT){
								//if me to src + src to dest < me to dest
								if((this.getEntry(v, receivedRow[0].src).getWeight() + src2Dest) < me2Dest){
									//update
									this.getEntry(v, dest).setWeight(me2Src + src2Dest);
								}
							}
							//if they cant reach it
						}
						//if me to my destination is UNREACHABLE
						else if(me2Dest == UNREACHABLE && me2Dest == BROKEN_WEIGHT && me2Src != UNREACHABLE && me2Src != BROKEN_WEIGHT){
							//if my source can reach it, then add it to our table
							if(src2Dest != UNREACHABLE && src2Dest != BROKEN_WEIGHT){
									this.getEntry(v, dest).setWeight(me2Src + src2Dest);
							}
							//if they cant reach it
							/*else{
								int whatDidTheNeighborsSay = askMyNeighbors(receivedRow[0].src, dest);
								if(whatDidTheNeighborsSay < UNREACHABLE){
									this.getEntry(v, dest).setWeight(whatDidTheNeighborsSay);
								}
							}*/
						}
					}
				}
			}
			
				//update the row in my table for the received row
				for(int i = 0; i < receivedRow.length; i++){
					dvTable[receivedRow[0].src.getMyRowIndex()][i].setWeight(receivedRow[i].getWeight());
				}
		}
					
	}
		
/*private int askMyNeighbors(vertex src, vertex dest){
	int weight = 0;
	DVTableEntry current;
	int meToMyDest, myDestToDest;
	//for every node in my list
	for(int i = 0; i < numNodes; i++){
		current = src.getMyDVTable().getEntry(src.getMyRowIndex(), i);
		meToMyDest = current.getWeight();
		myDestToDest = current.dest.getMyDVTable().checkMyNeighbors(dest);
		//check if the dest is not me && if they can reach me
		if(!current.dest.equals(src) 
				&& (current.dest.getMyDVTable().checkMyNeighbors(src) == UNREACHABLE || current.dest.getMyDVTable().checkMyNeighbors(src) == BROKEN_WEIGHT)){
			weight = UNREACHABLE;
		}
		else if(meToMyDest == UNREACHABLE || meToMyDest == BROKEN_WEIGHT){
			weight = UNREACHABLE;
		}
		//if they can reach me and can reach the dest
		else if(current.dest.getMyDVTable().checkMyNeighbors(dest) != UNREACHABLE 
				&& current.dest.getMyDVTable().checkMyNeighbors(dest) != BROKEN_WEIGHT){
			weight = src.getMyDVTable().checkMyNeighbors(current.dest) + current.dest.getMyDVTable().checkMyNeighbors(dest);
		}
		//if they can reach me but cant reach the dest
		else{
			weight = src.getMyDVTable().checkMyNeighbors(current.dest) + askMyNeighbors(current.dest, dest);
		}
	}
	return weight;
}*/
	
	public void writeTableToFile(){
		File file = new File(System.getProperty("java.io.tmpdir") + "DVTABLE_" + v.getLabel() + ".txt");
		try(BufferedWriter br = new BufferedWriter(new FileWriter(file))){
			br.write("TABLE FOR: " + v.getLabel() + "\r\n");
			for(int i = 0; i < numNodes; i++){
				for(int j = 0; j < numNodes; j++){
					br.write(dvTable[i][j].printEntry_toString() + "\r\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	/*
	 * @return: returns my row index
	 */
	public int getRowIndex(){
		return myRow; 
	}
	
	/*
	 * @return: gets an entry from the dvTable based on two integers
	 */
	public DVTableEntry getEntry(int row, int col){
		return dvTable[row][col];
	}
	
	/*
	 * @return: gets an entry from the dvTable based on two verticies
	 */
	public DVTableEntry getEntry(vertex src, vertex dest){
		return dvTable[src.getMyRowIndex()][dest.getMyRowIndex()];
	}
	
	/*
	 * prints the whole dvTable using the DVTableEntry print function
	 */
	public void printDVTable(){
		for(int i = 0; i < numNodes; i++){
			for(int j = 0; j < numNodes; j++){
				dvTable[i][j].printEntry();
			}
		}
	}
	
	/*
	 * @return: returns my row of entries
	 * 
	 */
	public DVTableEntry[] getMyRow(){
		return dvTable[myRow];
	}
	
	/*
	 * @return: returns the number of nodes
	 */
	public int getNumNodes(){
		return numNodes;
	}
	
	/*
	 * @return: returns the row based on a vertex
	 */
	public DVTableEntry[] getDVTableRow(vertex me){
		for(int i = 0; i < numNodes; i++){
			if(dvTable[i][0].src.equals(me)){
				return dvTable[i];
			}
		}
		return null;
	}
	
	/*
	 * @returns: checks what my weight to a destination is in my table
	 */
	private int checkMyNeighbors(vertex dest){
		vertex vertexToCheck = dest;
		//if dest is me
		if(v.equals(dest)){
			return 0;
		}
		//if dest is in my neighborhood
		for(int i = 0; i < v.getNeighborCount(); i++){
			//checks if were both in the same neighborhood
			if(v.getNeighbor(i).getVertexOne().equals(v)){
				vertexToCheck = v.getNeighbor(i).getVertexTwo();
			}
			else{
				vertexToCheck = v.getNeighbor(i).getVertexOne();
			}
			if(vertexToCheck != null && vertexToCheck.equals(dest)){
				if(v.getNeighbor(i).getWeight() == BROKEN_WEIGHT){
					return UNREACHABLE;
				}
				return v.getNeighbor(i).getWeight();
			}
		}
	
		//anything else
		return UNREACHABLE;
	}
}
