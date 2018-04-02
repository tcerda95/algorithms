package graph.list;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import graph.TracedVertex;
import graph.interfaces.DiGraph;

public class DiGraphList<V> extends AbstractGraphList<V> implements DiGraph<V> {
	
	@Override
	public int inDegree(V v) {
		if (!containsVertex(v))
			return -1;
		return getVertex(v).inDegree();
	}

	@Override
	public int outDegree(V v) {
		if (!containsVertex(v))
			return -1;
		return getVertex(v).outDegree();
	}
	
	public Map<V, TracedVertex<V>> minPathBellmanFord(V origin) {
		Map<V, TracedVertex<V>> map = new HashMap<>();
		if (!containsVertex(origin))
			return map;
				
		for (Vertex<V> v : getVertexes())
			map.put(v.getValue(), new TracedVertex<V>(v.getValue(), Integer.MAX_VALUE));
		
		Vertex<V> vertex = getVertex(origin);
		map.put(vertex.getValue(), new TracedVertex<V>(vertex.getValue(), 0));
		boolean updated = true;
		int n = vertexCount();
		
		for (int i = 1; i <= n && updated; i++) {
			updated = false;
			updated = minPathBellmanFord(map);
			if (updated && i == n)
				throw new IllegalStateException("Negative cycle in graph. Impossible to find minimal paths.");
		}
		
		return map;
	}

	private boolean minPathBellmanFord(Map<V, TracedVertex<V>> map) {
		boolean updated = false;
		
		for (Vertex<V> v : getVertexes()) {
			TracedVertex<V> tracedV = map.get(v.getValue());
			for (Edge<V> edge : v.getEdges()) {
				Vertex<V> neighbor = edge.getNeighbor();
				long newWeight = (long) edge.getWeight() + (long) tracedV.getDistance();
				int oldWeight = map.get(neighbor.getValue()).getDistance();
				if (newWeight < oldWeight) {
					updated = true;
					map.put(neighbor.getValue(), new TracedVertex<V>(neighbor.getValue(), tracedV, (int) newWeight));
				}
			}
		}
		
		return updated;
	}
	
	public boolean hasCycle() {
		unvisitVertexes();
		setVertexesColor(0);  // Both iterations could be done in one for efficiency
		for (Vertex<V> v : getVertexes())
			if (v.getColor() == 0 && hasCycle(v))
				return true;
		return false;
	}
	
	private boolean hasCycle(Vertex<V> v) {
		if (v.isVisited())
			return true;
		
		if (v.getColor() != 0)
			return false;
		
		v.visit();
		
		for (Edge<V> e : v.getEdges())
			if (hasCycle(e.getNeighbor()))
				return true;
		
		v.setColor(1); // already checked for cycles
		v.unvisit();
		return false;
	}

	public List<List<V>> prerrequisites() {
		LinkedList<List<V>> ans = new LinkedList<>();
		unvisitVertexes();
		
		int n = 0;
		
		List<Vertex<V>> mustVisit = new LinkedList<>();
		
		while (n < vertexCount()) {
			List<V> timeSlot = new LinkedList<>();
			
			for (Vertex<V> v : getVertexes()) {
				if (!v.isVisited() && v.neighborsVisited()) {
					n++;
					timeSlot.add(v.getValue());
					mustVisit.add(v);
				}
			}
			
			for (Vertex<V> v : mustVisit)
				v.visit();
			
			mustVisit.clear();
			ans.addFirst(timeSlot);
		}
		
		return ans;
	}
	
	public List<V> topologicalSort() {
		unvisitVertexes();
		LinkedList<V> sorted = new LinkedList<V>();
		
		for (Vertex<V> v : getVertexes())
			if (v.inDegree() == 0)
				topologicalSort(v, sorted);
		
		return sorted;
	}
	
	private void topologicalSort(Vertex<V> v, LinkedList<V> sorted) {
		for (Edge<V> e : v.getEdges())
			if (!e.getNeighbor().isVisited())
				topologicalSort(e.getNeighbor(), sorted);
		
		sorted.addFirst(v.getValue());
		v.visit();
	}
	
	public Map<V, TracedVertex<V>> maxPathDAG(V source) {
		return pathDAG(source, true);
	}
	
	public Map<V, TracedVertex<V>> minPathDAG(V source) {
		return pathDAG(source, false);
	}
	
	private Map<V, TracedVertex<V>> pathDAG(V source, boolean isMax) {
		Map<V, TracedVertex<V>> map = new HashMap<>();
		
		if (hasCycle())
			throw new IllegalStateException("Graph must not have cycles");
		
		int infinity = Integer.MAX_VALUE * (isMax ? -1 : 1);
		
		for (Vertex<V> v : getVertexes())
			v.setColor(infinity);
			
		Vertex<V> v = getVertex(source);
		v.setColor(0);
		map.put(source, new TracedVertex<V>(source));
		
		for (V reached : topologicalSort()) {
			v = getVertex(reached);
			if (v.getColor() != infinity)
				updateAdyacents(v, map, isMax);
		}
		
		return map;
	}
	
	private void updateAdyacents(Vertex<V> v, Map<V, TracedVertex<V>> map, boolean isMax) {
		for (Edge<V> e : v.getEdges()) {
			Vertex<V> neighbor = e.getNeighbor();
			int distance = v.getColor() + e.getWeight();
			if ((distance < neighbor.getColor() && !isMax) || (distance > neighbor.getColor() && isMax)) {
				neighbor.setColor(distance);
				map.put(neighbor.getValue(), new TracedVertex<V>(neighbor.getValue(), map.get(v.getValue()), distance));
			}
		}
	}
}
