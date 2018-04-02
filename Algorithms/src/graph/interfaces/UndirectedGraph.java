package graph.interfaces;

public interface UndirectedGraph<V> extends Graph<V> {
	/**
	 * Returns the degree of vertex v.
	 * @param v value of vertex
	 * @return degree of vertex
	 */
	public int degree(V v);
}
