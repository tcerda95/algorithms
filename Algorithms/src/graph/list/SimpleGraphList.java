package graph.list;

import java.util.ArrayList;
import java.util.List;

import graph.interfaces.UndirectedGraph;
import heap.BinaryMinHeap;
import heap.PriorityQueue;

public class SimpleGraphList<V> extends AbstractGraphList<V> implements UndirectedGraph<V> {
	
	@Override
	public boolean addEdge(V v, V u, int weight) {
		return super.addEdge(v, u, weight) && super.addEdge(u, v, weight);
	}
	
	@Override
	public boolean removeEdge(V v, V u) {
		return super.removeEdge(v, u) && super.removeEdge(u, v);
	}
	
	@Override
	public int edgeCount() {
		return super.edgeCount()/2;
	}
	
	@Override
	public boolean containsEdge(V v, V u) {
		return super.containsEdge(v, u) || super.containsEdge(u, v);
	}

	@Override
	public int degree(V v) {
		if (!containsVertex(v))
			return -1;
		return getVertex(v).outDegree();
	}
	
	public UndirectedGraph<V> minimumSpanTreePrim() {
		UndirectedGraph<V> graph = new SimpleGraphList<>();
		PriorityQueue<Vertex<V>> pq = new BinaryMinHeap<>(vertexCount());
		
		for (Vertex<V> v : getVertexes()) {
			v.unvisit();
			v.clearPredecessor();
			pq.enqueue(v, Integer.MAX_VALUE);
		}
		
		pq.decreasePriority(getVertex(), 0);
		
		while (!pq.isEmpty()) {
			int minWeight = pq.minPriority();
			Vertex<V> dequed = pq.dequeue();
			dequed.visit();
			graph.addVertex(dequed.getValue());
			graph.addEdge(dequed.getValue(), dequed.getPredecessor().getValue(), minWeight);
			for (Edge<V> edge : dequed.getEdges()) {
				Vertex<V> neighbor = edge.getNeighbor();
				if (!neighbor.isVisited()) {
					int weight = edge.getWeight();
					if (weight < pq.getPriority(neighbor)) {
						pq.decreasePriority(neighbor, weight);
						neighbor.setPredecessor(dequed);
					}
				}
			}
		}
		
		return graph;
	}
	
	/**
	 * Computes the number of vertexes of each connected component.
	 * @return list with number of vertexes of each connected component.
	 */
	public List<Integer> connectedComponents() {
		List<Integer> list = new ArrayList<>();
		unvisitVertexes();
		
		for (Vertex<V> v : getVertexes())
			if (!v.isVisited())
				list.add(DFSPath(v.getValue()).size());
		
		return list;
	}
}
