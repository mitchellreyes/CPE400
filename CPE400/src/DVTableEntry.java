
public class DVTableEntry {
	vertex src, dest;
	int weight;
	
	public DVTableEntry(vertex source, vertex desty, int num){
		src = source;
		dest = desty;
		weight = num;
	}
	
	public DVTableEntry(DVTableEntry other){
		src = other.src;
		dest = other.dest;
		weight = other.weight;
	}
	
	public void printEntry(){
		//System.out.println("[{SRC: " + src.getLabel() + ", DEST: " + dest.getLabel() + ", " + weight + "]");
		//System.out.println(src.getLabel() + "[" + dest.getLabel() + ", " + weight + "]");
		//return 
				System.out.println(src.getLabel() + " to " + dest.getLabel() + ", " + weight);
	}
	
	public String printEntry_toString(){
		return src.getLabel() + " to " + dest.getLabel() + " = " + weight;
	}
	
	public void setWeight(int other){
		weight = other;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public String getSrc(){
		return src.getLabel();
	}
}
