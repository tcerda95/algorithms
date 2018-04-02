package solution.problemSolving;

import java.util.HashSet;
import java.util.Set;

import heap.BinaryMinHeap;

public class ProblemSolvingGraph {
	private static class Vertex {
		private int value;
		private int tag;
		private Set<Vertex> outAdyacents;
		private Set<Vertex> inAdyacents;
		
		public Vertex(int value) {
			this.value = value;
			this.outAdyacents = new HashSet<>();
			this.inAdyacents = new HashSet<>();
		}
		
		public int outDegree() {
			return outAdyacents.size();
		}
		
		public int inDegree() {
			return inAdyacents.size();
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
	}
	
	private Vertex[] vertexes;
	private int ratingDif;
	
	public ProblemSolvingGraph(int n, int ratingDif) {
		vertexes = new Vertex[n];
		this.ratingDif = ratingDif;
		
		for (int i = 0; i < n; i++)
			vertexes[i] = new Vertex(i);
	}
	
	/**
	 * Tags a vertex and adds the necesary edges.
	 * Precondition: if vertex n+1 is being tagged, vertexes [0,n] must be tagged.
	 */
	public void tagVertex(int vertex, int tag) {
		Vertex v = vertexes[vertex];
		v.tag = tag;
		
		for (int i = 0; i < vertex; i++) {
			if (Math.abs(vertexes[i].tag - tag) >= ratingDif) {
				vertexes[i].outAdyacents.add(v);
				v.inAdyacents.add(vertexes[i]);
			}
		}
	}
		
	public int vertexCount() {
		return vertexes.length;
	}
	
	public int minSolvingDays() {
		BinaryMinHeap<Vertex> pq = new BinaryMinHeap<>(vertexCount());
		
		int days = 0;
		for (Vertex v : vertexes)
			pq.enqueue(v, v.outDegree());
		
		while (!pq.isEmpty()) {
			Vertex dequed = pq.dequeue();
			System.out.println(dequed.value + " " + dequed.tag + " " + dequed.outDegree());
						
			if (dequed.outDegree() == 0) {
				days += 1;
			}
			else {
				Vertex minInDegree = null;
				
				for (Vertex ady : dequed.outAdyacents) {
					if (minInDegree == null || minInDegree.inDegree() > ady.inDegree())
						minInDegree = ady;
					ady.inAdyacents.remove(dequed);
				}
								
				for (Vertex ady : minInDegree.inAdyacents) {
					ady.outAdyacents.remove(minInDegree);
					pq.decreasePriority(ady, ady.outDegree());
				}
				
				System.out.println(minInDegree.value + " " + minInDegree.tag);
			}
		}
		
		return days;
	}
}
