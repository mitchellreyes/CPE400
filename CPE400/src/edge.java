import java.awt.Color;


public class edge{
	private vertex one, two;
	private int prevWeight = 0;
	private int weight;
	private boolean isBroken = false;
	
	public GraphicsArea.EdgeGraphic graphic;
	
	public edge(vertex one, vertex two){
		this(one, two, 1);
	}
	
	public edge(vertex one, vertex two, int weight){
		if(one.getLabel().compareTo(two.getLabel()) <= 0){
			this.one = one;
		}
		else{
			this.one = two;
		}
		
		if(this.one == one){
			this.two = two;
		}
		else{
			this.two = one;
		}
		
		this.weight = weight;
	}
	
	public vertex getNeighbor(vertex selected)
	{
		if(!(selected.equals(one)) || selected.equals(two)){
			return null;
		}
		else{
			if(selected.equals(one)){
				return two;
			}
			else{
				return one;
			}
		}
	}
	
	public vertex getVertexOne(){
		return this.one;
	}
	
	public vertex getVertexTwo(){
		return this.two;
	}
	
	public int getWeight(){
		return this.weight;
	}
	
	public boolean isBroken()
	{
		return isBroken;
	}
	
	public void setWeight(int someWeight){
		this.weight = someWeight;
	}
	
	public void setBroken(boolean broke)
	{
		if(isBroken == false && broke == true)
		{
			prevWeight = weight;
			weight = 99;
			if(graphic != null){
				graphic.setColor(Color.red);
			}
			isBroken = true;
		}
		else if(isBroken == true && broke == false)
		{
			System.out.println("Unbreaking");
			weight = prevWeight;
			graphic.setColor(Color.black);
			isBroken = false;
		}
	}
	
	public String toString(){
		return "({" + one.getLabel() + ", " + two.getLabel() + "}, " + weight + ")";
	}
	
	public int hashCode(){
		return (one.getLabel() + two.getLabel()).hashCode();
	}
	
	public boolean equals(Object otherEdge){
		if(!(otherEdge instanceof edge)){
			return false;
		}
		edge e = (edge)otherEdge;
		return e.one.equals(this.one) && e.two.equals(this.two);
	}
}
