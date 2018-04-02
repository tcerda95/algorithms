package graph.list;

import graph.interfaces.Visitable;

public class Edge<V> implements Visitable {
	private Vertex<V> neighbor;
	private int weight;
	private boolean visited;
	
	public Edge(Vertex<V> neighbor, int weight) {
		this.neighbor = neighbor;
		this.weight = weight;
	}
	
	public Vertex<V> getNeighbor() {
		return neighbor;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
	public void visit() {
		visited = true;
	}

	@Override
	public void unvisit() {
		visited = false;
	}

	@Override
	public boolean isVisited() {
		return visited;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !obj.getClass().equals(this.getClass()))
			return false;
		Edge<?> other = (Edge<?>) obj;
		return neighbor.equals(other.getNeighbor());
	}
	
	@Override
	public int hashCode() {
		return neighbor.hashCode() + weight;
	}
}
