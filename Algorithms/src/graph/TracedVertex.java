package graph;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class TracedVertex<V> {
	private TracedVertex<V> parent;
	private V value;
	private int distance;
	
	public TracedVertex(V value) {
		this(value, null);
	}
	
	public TracedVertex(V value, TracedVertex<V> parent) {
		this(value, parent, parent == null ? 0 : parent.getDistance() + 1);
	}
	
	public TracedVertex(V value, int distance) {
		this(value, null, distance);
	}
	
	public TracedVertex(V value, TracedVertex<V> parent, int distance) {
		this.value = value;
		this.parent = parent;
		this.distance = distance;
	}

	public boolean hasParent() {
		return parent != null;
	}
	
	public TracedVertex<V> getParent() {
		if (!hasParent())
			throw new NoSuchElementException("Vertex has no parent.");
		return parent;
	}
	
	public V getValue() {
		return value;
	}
	
	/**
	 * Returns distance from origin vertex to this vertex. If this vertex is
	 * the origin vertex the resulting distance is 0.
	 * @return distance from origin vertex
	 */
	public int getDistance() {
		return distance;
	}
	
	/**
	 * Returns a list of values from origin vertex to this vertex. 
	 * @return traced path from origin
	 */
	public List<V> getPath() {
		LinkedList<V> list = new LinkedList<>();
		TracedVertex<V> current = this;
		list.addFirst(current.getValue());

		while(current.hasParent()) {
			current = current.getParent();
			list.addFirst(current.getValue());
		}
		
		return list;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !obj.getClass().equals(this.getClass()))
			return false;
		
		TracedVertex<?> other = (TracedVertex<?>) obj;
		
		if (!getValue().equals(other.getValue()))
			return false;
		
		if (hasParent() && other.hasParent())
			return getParent().equals(other.getParent());
		
		return !hasParent() && !other.hasParent();
	}
	
	@Override
	public int hashCode() {
		return getValue().hashCode();
	}
	
	@Override
	public String toString() {
		return value.toString() + " " + distance;
	}
}
