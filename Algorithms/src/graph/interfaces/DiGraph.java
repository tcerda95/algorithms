package graph.interfaces;

public interface DiGraph<V> extends Graph<V> {
	/**
	 * Returns the in degree of the given vertex.
	 * @param v value of the vertex
	 * @return in degree of vertex
	 */
	public int inDegree(V v);
	
	/**
	 * Returns the out degree of the given vertex.
	 * @param v value of the vertex
	 * @return out degree of vertex
	 */
	public int outDegree(V v);
}
