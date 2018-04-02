package graph.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import graph.TracedVertex;
import graph.interfaces.Graph;
import heap.BinaryMinHeap;
import heap.PriorityQueue;
import matrix.Matrix;

public class AbstractGraphList<V> implements Graph<V> {
	
	private Map<V, Vertex<V>> vertexMap;
	private int edgeCount;

	public AbstractGraphList() {
		vertexMap = new HashMap<>();
	}
	
	@Override
	public boolean addVertex(V v) {
		if (containsVertex(v))
			return false;
		vertexMap.put(v, new Vertex<>(v));
		return true;
	}

	@Override
	public boolean addEdge(V v, V u) {
		return addEdge(v, u, 0);
	}

	@Override
	public boolean addEdge(V v, V u, int weight) {
		if (v == null || u == null || !containsVertex(v) || !containsVertex(u))
			throw new IllegalArgumentException("Vertexes not found");
		edgeCount += 1;
		return getVertex(v).addEdge(new Edge<>(getVertex(u), weight));
	}

	@Override
	public boolean containsVertex(V v) {
		return vertexMap.containsKey(v);
	}

	@Override
	public boolean containsEdge(V v, V u) {
		if (!containsVertex(v) || !containsVertex(u))
			return false;
		return getVertex(v).isAdjacent(u);
	}

	@Override
	public boolean removeVertex(V v) {
		if (!containsVertex(v))
			return false;

		vertexMap.remove(v);
		for (Vertex<V> vertex : getVertexes())
			if (vertex.isAdjacent(v))
				vertex.removeEdge(v);
		
		return true;
	}
	
	@Override
	public boolean removeEdge(V v, V u) {
		if (!containsVertex(v) || !containsVertex(u))
			return false;
		edgeCount -= 1;
		return getVertex(v).removeEdge(u);
	}

	@Override
	public int vertexCount() {
		return vertexMap.size();
	}

	@Override
	public int edgeCount() {
		return edgeCount;
	}

	protected Collection<Vertex<V>> getVertexes() {
		return vertexMap.values();
	}
	
	protected Vertex<V> getVertex(V v) {
		return vertexMap.get(v);
	}
	
	protected Vertex<V> getVertex() {
		for(Vertex<V> v : getVertexes())
			return v;
		return null;
	}
	
	protected void unvisitVertexes() {
		for (Vertex<V> vertex : getVertexes())
			vertex.unvisit();
	}
	
	protected void clearPredecessors() {
		for (Vertex<V> vertex : getVertexes())
			vertex.clearPredecessor();
	}
	
	protected void unvisitEdges() {
		for (Vertex<V> vertex : getVertexes())
			vertex.unvisitEdges();
	}
	
	protected void setVertexesColor(int color) {
		for (Vertex<V> vertex : getVertexes())
			vertex.setColor(color);
	}
	
	// Algorithms
	
	public List<TracedVertex<V>> DFSPath(V origin) {
		List<TracedVertex<V>> list = new ArrayList<>();
		
		if (containsVertex(origin)) {
			unvisitVertexes();
			DFSPath(getVertex(origin), list, null);
		}
		
		return list;
	}

	private void DFSPath(Vertex<V> vertex, List<TracedVertex<V>> list, TracedVertex<V> parent) {
		if (!vertex.isVisited()) {
			vertex.visit();
			TracedVertex<V> tv = new TracedVertex<>(vertex.getValue(), parent);
			list.add(tv);
			for (Edge<V> e : vertex.getEdges())
				DFSPath(e.getNeighbor(), list, tv);
		}
	}
	
	private static class VertexPair<V> {
		private Vertex<V> vertex;
		private TracedVertex<V> tracedVertex;
		
		public VertexPair(Vertex<V> vertex, TracedVertex<V> tracedVertex) {
			this.vertex = vertex;
			this.tracedVertex = tracedVertex;
		}
		
		@Override
		public boolean equals(Object obj) {
			return vertex.equals(obj);
		}
		
		@Override
		public int hashCode() {
			return vertex.hashCode();
		}
	}
	
	public List<TracedVertex<V>> BFSPath(V origin) {
		List<TracedVertex<V>> list = new LinkedList<>();
		if (!containsVertex(origin))
			return list;
		
		Queue<VertexPair<V>> queue = new LinkedList<>();
		Vertex<V> vertex = getVertex(origin);
		TracedVertex<V> tracedVertex = new TracedVertex<V>(vertex.getValue());
		
		unvisitVertexes();
		queue.add(new VertexPair<V>(vertex, tracedVertex));
		
		while(!queue.isEmpty()) {
			VertexPair<V> pair = queue.remove();
			if (!pair.vertex.isVisited()) {
				pair.vertex.visit();
				list.add(pair.tracedVertex);
				for (Edge<V> edge : pair.vertex.getEdges()) {
					vertex = edge.getNeighbor();
					if (!vertex.isVisited())
						queue.add(new VertexPair<V>(vertex, new TracedVertex<V>(vertex.getValue(), pair.tracedVertex)));
				}
			}
		}
		
		queue.clear();
		return list;
	}
	
	public Map<V, TracedVertex<V>> minPathDijkstra(V origin) {
		Map<V, TracedVertex<V>> map = new HashMap<>();
		if (!containsVertex(origin))
			return map;
		
		PriorityQueue<Vertex<V>> pq = new BinaryMinHeap<>(vertexCount());
		
		for (Vertex<V> v : getVertexes()) {
			v.unvisit();
			pq.enqueue(v, Integer.MAX_VALUE);
		}
		
		Vertex<V> vertex = getVertex(origin);
		pq.decreasePriority(vertex, 0);
		map.put(vertex.getValue(), new TracedVertex<V>(vertex.getValue(), 0));
		
		while (!pq.isEmpty() && pq.minPriority() != Integer.MAX_VALUE) { // graph not connected if min priority is MAX_VALUE
			int weight = pq.minPriority();
			vertex = pq.dequeue();
			vertex.visit();
			
			for (Edge<V> edge : vertex.getEdges()) {
				Vertex<V> frontier = edge.getNeighbor();
				if (!frontier.isVisited()) { 
					int frontierWeight = edge.getWeight() + weight;
					if (frontierWeight < pq.getPriority(frontier)) {
						pq.decreasePriority(frontier, frontierWeight);
						map.put(frontier.getValue(), new TracedVertex<V>(frontier.getValue(), map.get(vertex.getValue()), frontierWeight));
					}
				}
			}
		}
		
		return map;
	}
	
	// TODO: no funciona lo de los predecesores
	public Matrix<V, TracedVertex<V>> minPathFloyd() {
		Matrix<V, TracedVertex<V>> matrix = new Matrix<>();
		int n = vertexCount();
		
		if (n < 1)
			return matrix;
		
		for (Vertex<V> v1 : getVertexes())
			for (Vertex<V> v2 : getVertexes())
				matrix.put(v1.getValue(), v2.getValue(), new TracedVertex<V>(v2.getValue(), Integer.MAX_VALUE));
		
		for (Vertex<V> v : getVertexes()) {
			matrix.put(v.getValue(), v.getValue(), new TracedVertex<V>(v.getValue(), 0));
			for (Edge<V> e : v.getEdges()) {
				V neighbor = e.getNeighbor().getValue();
				matrix.put(v.getValue(), neighbor, 
						new TracedVertex<V>(neighbor, matrix.get(v.getValue(), v.getValue()), e.getWeight()));
			}
		}
		
		for (Vertex<V> k : getVertexes()) {
			for (Vertex<V> i : getVertexes()) {
				int ikWeight = matrix.get(i.getValue(), k.getValue()).getDistance();
				if (ikWeight != Integer.MAX_VALUE) {
					for (Vertex<V> j : getVertexes()) {
						TracedVertex<V> tv = matrix.get(k.getValue(), j.getValue());
						long newWeight = (long) ikWeight + (long) tv.getDistance();
						if (newWeight < matrix.get(i.getValue(), j.getValue()).getDistance())
							matrix.put(i.getValue(), j.getValue(), new TracedVertex<V>(j.getValue(), matrix.get(i.getValue(), tv.getParent().getValue()), (int) newWeight));
					}
				}
			}
		}
		
		return matrix;
	}
}
