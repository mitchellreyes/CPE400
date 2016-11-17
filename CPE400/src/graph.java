import java.util.*;

public class graph {
	private HashMap<String, vertex> vertices;
	private HashMap<Integer, edge> edges;
	
	public graph(){
		this.vertices = new HashMap<String, vertex>();
		this.edges = new HashMap<Integer, edge>();
	}
	
	public graph(ArrayList<vertex> vertices)
	{
		this.vertices = new HashMap<String, vertex>();
		this.edges = new HashMap<Integer, edge>();
		for(vertex v : vertices){
			this.vertices.put(v.getLabel(), v);
		}
	}
	
	public boolean addEdge(vertex left, vertex right)
	{
		return addEdge(left, right, 1);
	}
	
	public boolean addEdge(vertex left, vertex right, int weight)
	{
		if(left.equals(right)){
			return false;
		}
		edge edge1 = new edge(left, right, weight);
		if(edges.containsKey(edge1.hashCode())){
			return false;
		}
		else if(left.containsNeighbor(edge1) || right.containsNeighbor(edge1)){
			return false;
		}

		edges.put(edge1.hashCode(), edge1);
		left.addNeighbor(edge1);
		right.addNeighbor(edge1);
		return true;
	}
	
	public boolean containsEdge(edge otherEdge)
	{
		if(otherEdge.getVertexOne() == null || otherEdge.getVertexTwo() == null)
		{
			return false;
		}
		return this.edges.containsKey(otherEdge.hashCode());
	}
	
	public edge removeEdge(edge otherEdge){
		otherEdge.getVertexOne().removeNeighbor(otherEdge);
		otherEdge.getVertexTwo().removeNeighbor(otherEdge);
		return this.edges.remove(otherEdge.hashCode());
	}
	
	public boolean containsVertex(vertex Vertex)
	{
		return this.vertices.get(Vertex.getLabel()) != null;
	}
	
	public vertex getVertex(String label)
	{
		return vertices.get(label);
	}
	
	public boolean addVertex(vertex Vertex, boolean overWrite)
	{
		vertex selected = this.vertices.get(Vertex.getLabel());
		if(selected != null){
			if(!overWrite)
			{
				return false;
			}
			while(selected.getNeighborCount() > 0)
			{
				this.removeEdge(selected.getNeighbor(0));
			}
		}
		
		vertices.put(Vertex.getLabel(), Vertex);
		return true;
	}
	
	public vertex removeVertex(String label){
		vertex Vertex = vertices.remove(label);
		while(Vertex.getNeighborCount() > 0)
		{
			this.removeEdge(Vertex.getNeighbor(0));
		}
		
		return Vertex;
	}
	
	public Set<String> vertexKeys()
	{
		return this.vertices.keySet();
	}
	
	public Set<edge> getEdges()
	{
		return new HashSet<edge>(this.edges.values());
	}
}
