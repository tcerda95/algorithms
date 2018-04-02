package graph.intArray;

import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import graph.weighter.Weighter;
import heap.BinaryMinHeap;

public class IntGraphArray {
	private static class Vertex {
		private boolean visited;
		private int value;
		private int inDegree;
		private double tag;
		private List<Edge> adyacents;
		
		public Vertex(int value) {
			this.value = value;
			adyacents = new LinkedList<>();
		}
		
		public int outDegree() {
			return adyacents.size();
		}
		
		public int inDegree() {
			return inDegree;
		}
		
		@Override
		public int hashCode() {
			return value;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			
			if (obj == null || !(obj instanceof Vertex))
				return false;
			
			Vertex v = (Vertex) obj;
			
			return value == v.value;
		}
		
		@Override
		public String toString() {
			return "v[" + value + "]";
		}
	}
	
	private static class Edge {
		private Vertex from;
		private Vertex neighbor;
		private double weight;
		
		public Edge(Vertex from, Vertex neighbor, double weight) {
			this.from = from;
			this.neighbor = neighbor;
			this.weight = weight;
		}
	}
	
	private Vertex[] vertexes;
	private List<Edge> edges;
	private int edgeCount;
	private int totalWeight;
	private boolean directed;
	
	private Vertex[] getVertexes() {
		return vertexes;
	}
	
	private void clearMarks() {
		for (Vertex v : getVertexes())
			v.visited = false;
	}
	
	private void clearMarks(int tag) {
		for (Vertex v : getVertexes()) {
			v.visited = false;
			v.tag = tag;
		}
	}
	
	public IntGraphArray(int n) {
		this(n, false);
	}
	
	public IntGraphArray(int n, boolean directed) {
		vertexes = new Vertex[n];
		for (int i = 0; i < n; i++)
			vertexes[i] = new Vertex(i);
		this.directed = directed;
		
		this.edges = new LinkedList<>();
	}
	
	public void addEdge(int from, int to) {
		addEdge(from, to, 0);
	}
	
	public void addEdge(int from, int to, double weight) {
		if (to >= vertexes.length || to < 0)
			throw new IndexOutOfBoundsException("Invalid to index");
		
		Vertex f = vertexes[from];
		Vertex neighbor = vertexes[to];
		Edge e = new Edge(f, neighbor, weight);
		totalWeight += weight;
		
		edges.add(e);
		f.adyacents.add(e);
		neighbor.inDegree++;
		
		if (!directed) {
			neighbor.adyacents.add(new Edge(neighbor, f, weight));
			f.inDegree++;
		}

		edgeCount += 1;
	}
	
	public int edgeCount() {
		return edgeCount;
	}
	
	public int vertexCount() {
		return vertexes.length;
	}
	
	public boolean hasCycle() {
		clearMarks();
		Vertex v = vertexes[0];
		return hasCycle(v, null);
	}
	
	private boolean hasCycle(Vertex v, Vertex prev) {
		if (v.visited)
			return true;
		
		v.visited = true;
		for(Edge e : v.adyacents) {
			Vertex ady = e.neighbor;
			if (ady != prev && hasCycle(ady, v))
				return true;
		}
		
		return false;
	}

	public double[] BFSPath(int source) {
		clearMarks();
		Vertex v = vertexes[source];
		double[] ans = new double[vertexCount()];
		
		for (int i = 0; i < ans.length; i++)
			ans[i] = -1;
		
		Queue<Vertex> q = new LinkedList<>();
		q.add(v);
		v.visited = true;
		
		while (!q.isEmpty()) {
			v = q.remove();
			ans[v.value] = v.tag;
			for (Edge e : v.adyacents) {
				Vertex ady = e.neighbor;
				if (!ady.visited) {
					ady.visited = true;
					ady.tag = v.tag + e.weight;
					q.add(ady);
				}
			}
		}
		
		return ans;
	}
	
	public int evenTree(int root) {
		Vertex v = vertexes[root];
		return evenTree(v);
	}
	
	private int evenTree(Vertex v) {		
		int count = 1;
		int evenCount = 0;
		
		for (Edge e : v.adyacents) {
			Vertex neighbor = e.neighbor;
			evenCount += evenTree(neighbor);
			count += neighbor.tag;
			evenCount += neighbor.tag % 2 == 1 ? 0 : 1;
		}
		
		v.tag = count;
		
		return evenCount;
	}

	private static class PQNode implements Comparable<PQNode> {
		private Vertex v;
		private double cost;
		
		public PQNode(Vertex v, double cost) {
			this.v = v;
			this.cost = cost;
		}
		
		public int compareTo(PQNode o) {
			return Double.compare(cost, o.cost);
		}
	}
	
	private long minPath(int from, int to, Weighter weighter) {
		clearMarks();
		
		Vertex current = vertexes[from];
		Vertex destination = vertexes[to];
				
		PriorityQueue<PQNode> pq = new PriorityQueue<>();

		pq.add(new PQNode(current, 0));
		
		while (!pq.isEmpty()) {
			PQNode dequed = pq.remove();
			current = dequed.v;
			
			if (current == destination)
				return (long) dequed.cost;
			
			if (!current.visited) {
				current.visited = true;
				for (Edge e : current.adyacents) {
					Vertex neighbor = e.neighbor;
					if (!neighbor.visited)
						pq.add(new PQNode(neighbor, weighter.weight(dequed.cost, e.weight)));
				}
			}
		}
		
		return -1;
	}
	
	public long minMaxWeight(int from, int to) {
		return minPath(from, to, (acum, edgeWeight) -> (Double.max(acum, edgeWeight)));
	}
	
	public long dijkstraMinPath(int from, int to) {
		return minPath(from, to, (acum, edgeWeight) -> (acum + edgeWeight));
	}
	
	public long primTotalWeight() {
		return primTotalWeight(0);
	}
	
	public long primTotalWeight(int initial) {
		clearMarks();
		BinaryMinHeap<Vertex> heap = new BinaryMinHeap<>(vertexCount());
		
		for (Vertex v : vertexes)
			heap.enqueue(v, Integer.MAX_VALUE);
		
		heap.decreasePriority(vertexes[initial], 0);
		
		long totalWeight = 0;
		
		while (!heap.isEmpty()) {
			totalWeight += heap.minPriority();
			Vertex dequed = heap.dequeue();
			dequed.visited = true;
			
			for (Edge e : dequed.adyacents) {
				Vertex neighbor = e.neighbor;
				if (!neighbor.visited && e.weight < heap.getPriority(neighbor))
					heap.decreasePriority(neighbor, (int) e.weight);
			}
		}
		
		return totalWeight;
	}
	
	public double kruskalTotalWeight() {
		clearMarks(0);
		PriorityQueue<Edge> pq = new PriorityQueue<Edge>(new Comparator<Edge>() {

			@Override
			public int compare(Edge e1, Edge e2) {
				int cmp = Double.compare(e1.weight, e2.weight);		
				if (cmp == 0)
					return (e1.from.value + e1.neighbor.value) - (e2.from.value + e2.neighbor.value);
				return cmp;
			}
		});
		
		for (Edge e : edges)
			pq.add(e);
		
		double weight = 0;
		int component = 1;
		
		while (!pq.isEmpty()) {
			Edge dequed = pq.remove();
			
			Vertex from = dequed.from;
			Vertex neighbor = dequed.neighbor;
			
			if (!from.visited && !neighbor.visited) {				
				from.tag = component;
				neighbor.tag = component;
				from.visited = true;
				neighbor.visited = true;
				weight += dequed.weight;
				component++;
			}
			else if (!from.visited || !neighbor.visited) {				
				if (!from.visited) {
					from.visited = true;
					from.tag = neighbor.tag;
				}
				else {
					neighbor.visited = true;
					neighbor.tag = from.tag;
				}
				
				weight += dequed.weight;
			}
			else if (from.tag != neighbor.tag) {
				kruskalUpdateTags(neighbor, from.tag);
				weight += dequed.weight;
			}
		}
		
		return weight;
	}

	private void kruskalUpdateTags(Vertex current, double tag) {
		double prevTag = current.tag;
		current.tag = tag;
		
		for (Edge e : current.adyacents)
			if (e.neighbor.tag == prevTag)
				kruskalUpdateTags(e.neighbor, tag);
	}
	
	public List<Vertex> topologicalSort() {
		if (!directed)
			throw new IllegalStateException("Graph must be directed for topological sort");
		
		clearMarks();
		LinkedList<Vertex> sorted = new LinkedList<>();
		for (Vertex v : vertexes)
			if (v.inDegree == 0)
				topologicalSort(v, sorted);
		
		return sorted;
	}

	private void topologicalSort(Vertex v, LinkedList<Vertex> sorted) {
		for (Edge e : v.adyacents)
			if (!e.neighbor.visited)
				topologicalSort(e.neighbor, sorted);
		
		sorted.addFirst(v);
		v.visited = true;
	}
	
	public long jeanieRoute(Set<Integer> deliveryCities) {
		int firstCity = -1;
		
		for (int i = 0; i < vertexes.length && firstCity == -1; i++)
			if (deliveryCities.contains(i))
				firstCity = i;
		
		if (firstCity == -1)
			throw new IllegalStateException("No cities to deliver to");
		
		IntGraphArray subgraph = new IntGraphArray(vertexCount());
		jeanieSubgraph(firstCity, -1, deliveryCities, subgraph);
		
		long maxPath = -1;
		int visitedTag = 0;
		for (Integer city : deliveryCities) {
			Vertex v = subgraph.vertexes[city];
			if (v.outDegree() == 1) {
				long path = subgraph.maxJeaniePath(v, visitedTag++);
				if (path > maxPath)
					maxPath = path;
			}
		}
		
		return (subgraph.totalWeight - maxPath) * 2 + maxPath;
	}

	private boolean jeanieSubgraph(int city, int prevCity, Set<Integer> deliveryCities, IntGraphArray subgraph) {
		boolean prevInSubgraph = false;
		
		if (deliveryCities.contains(city))
			prevInSubgraph = true;

		for (Edge e : vertexes[city].adyacents) {
			Vertex ady = e.neighbor;
			if (ady.value != prevCity && jeanieSubgraph(ady.value, city, deliveryCities, subgraph)) {
				prevInSubgraph = true;
				subgraph.addEdge(city, ady.value, e.weight);
			}
		}
		
		return prevInSubgraph;
	}

	private long maxJeaniePath(Vertex current, Vertex prev, long currentPath, boolean retrace) {
		long maxPath = -1;
				
		if (prev != null && current.inDegree == 1)
			return currentPath;
		
		if (current.inDegree == 2 && !retrace)
			current.visited = true;
		else if (current.inDegree != 2)
			retrace = true;
		
		for (Edge e : current.adyacents) {
			Vertex ady = e.neighbor;
			if (!ady.visited && ady != prev) {
				long path = maxJeaniePath(ady, current, (long) (currentPath+e.weight), retrace);
				if (path > maxPath)
					maxPath = path;
			}
		}
		
		return maxPath;
	}
	
	private long maxJeaniePath(Vertex source, int visitedTag) {
		long[] pathsDist = new long[vertexCount()];
		long maxPath = 0;
		boolean retrace = false;
		Deque<Vertex> stack = new LinkedList<>();
		
		pathsDist[source.value] = 0;
		stack.push(source);
		
		while (!stack.isEmpty()) {
			Vertex current = stack.pop();
			long currentDist = pathsDist[current.value];
			
			if (current.outDegree() == 1 && current != source) {
				if (currentDist > maxPath)
					maxPath = currentDist;
			}
			else {
				if (current.outDegree() == 2 && !retrace)
					current.visited = true;
				else
					retrace = true;
				
				for (Edge e : current.adyacents) {
					Vertex ady = e.neighbor;
					if (!ady.visited && ady.tag != visitedTag) {
						stack.push(ady);
						pathsDist[ady.value] = currentDist + (long) e.weight;
					}
				}
			}
			
			current.tag = visitedTag;
		}
		
		return maxPath;
	}
}
