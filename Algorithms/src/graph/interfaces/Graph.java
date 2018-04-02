package graph.interfaces;


public interface Graph<V> {
	/**
	 * Adds a vertex to the graph.
	 * @param v value of the vertex
	 * @return true if the graph was modified, false otherwise.
	 */
	public boolean addVertex(V v);
	
	/**
	 * Adds an edge to the graph with weight 0. Depends on the implementation
	 * whether if it is directed or not.
	 * @param v value of the from vertex
	 * @param u value of the to vertex
	 * @return true if the graph was modified, false otherwise
	 */
	public boolean addEdge(V v, V u);
	
	/**
	 * Adds an edge to the graph with the given weight. Depends on the implementation
	 * whether if it is directed or not.
	 * @param v value of the from vertex
	 * @param u value of the to vertex
	 * @param weight weight of the vertex
	 * @return true if the graph was modified, false otherwise
	 */
	public boolean addEdge(V v, V u, int weight);
	
	/**
	 * Determines whether there is a vertex v.
	 * @param v value of the vertex
	 * @return true if such vertex exists, false otherwise
	 */
	public boolean containsVertex(V v);
	
	/**
	 * Determines whether there is an edge from vertex v to vertex u or not.
	 * @param v value of the from vertex
	 * @param u value of the to vertex
	 * @return true if such edge exists, false otherwise
	 */
	public boolean containsEdge(V v, V u);
	
	/**
	 * Removes a vertex and incident edges.
	 * @param v value of the vertex to be removed
	 * @return true if the graph was modified, false otherwise
	 */
	public boolean removeVertex(V v);
	
	/**
	 * Removes edge that goes from vertex v to vertex u.
	 * @param v value of the from vertex
	 * @param u value of the to vertex
	 * @return true if the graph was modified, false otherwise
	 */
	public boolean removeEdge(V v, V u);
	
	/**
	 * Returns the number of vertexes in this graph.
	 * @return the number of vertexes
	 */
	public int vertexCount();
	
	/**
	 * Returns the number of edges in this graph.
	 * @return the number of edges
	 */
	public int edgeCount();
}
