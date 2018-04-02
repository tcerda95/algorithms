package graph.list;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import graph.interfaces.Visitable;

public class Vertex<V> implements Visitable {
	
	private V value;
	private int color;
	private boolean visited;
	private Vertex<V> predecessor;
	private List<Edge<V>> edges;
	
	public Vertex(V value) {
		this.value = value;
		edges = new LinkedList<>();
	}
	
	public boolean addEdge(Edge<V> e) {
		if (edges.contains(e))
			return false;
		
		return edges.add(e);
	}
	
	public boolean removeEdge(V to) {
		Iterator<Edge<V>> iter = edges.iterator();
		
		while (iter.hasNext()) {
			if (iter.next().getNeighbor().getValue().equals(to)) {
				iter.remove();
				return true;
			}
		}
		
		return false;
	}
	
	public List<Edge<V>> getEdges() {
		return edges;
	}
	
	public int outDegree() {
		return edges.size();
	}
	
	public int inDegree() {
		int inDegree = 0;
		for (Edge<V> edge : edges)
			if (edge.getNeighbor().isAdjacent(this))
				inDegree += 1;
		return inDegree;
	}
	
	public boolean isAdjacent(Vertex<V> v) {
		return isAdjacent(v.value);
	}
	
	public boolean isAdjacent(V value) {
		for (Edge<V> edge : edges)
			if (edge.getNeighbor().getValue().equals(value))
				return true;
		return false;
	}

	public V getValue() {
		return value;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}
	
	public void unvisitEdges() {
		for (Edge<V> e : getEdges())
			e.unvisit();
	}
	
	public void setPredecessor(Vertex<V> predecessor) {
		this.predecessor = predecessor;
	}
	
	public Vertex<V> getPredecessor() {
		return predecessor;
	}
	
	public void clearPredecessor() {
		predecessor = null;
	}
	
	public boolean neighborsVisited() {
		for (Edge<V> e : getEdges())
			if (!e.getNeighbor().isVisited())
				return false;
		return true;
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
		Vertex<?> other = (Vertex<?>) obj;
		
		return getValue().equals(other.getValue());
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
