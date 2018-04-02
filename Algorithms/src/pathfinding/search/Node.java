package pathfinding.search;

import pathfinding.interfaces.Move;
import pathfinding.interfaces.State;

public class Node<T extends State> {
	private T state;
	private int gScore; // g(n)
	private int score; // f(n)
	private int depth;
	private Node<T> parent;
	private Move<T> generatingMove;
	
	public Node(T initial) {
		this(initial, 0, null, null);
	}
	
	public Node(T state, int depth, Node<T> parent, Move<T> generatingMove) {
		this(state, depth, parent, generatingMove, 0);
	}
	
	public Node(T state, int depth, Node<T> parent, Move<T> generatingMove, int gScore) {
		this.state = state;
		this.depth = depth;
		this.parent = parent;
		this.generatingMove = generatingMove;
		this.gScore = gScore;
	}
	
	public T getState() {
		return state;
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public int getDepth() {
		return depth;
	}
	
	public boolean hasParent() {
		return parent != null;
	}

	public Node<T> getParent() {
		return parent;
	}

	public Move<T> getMove() {
		return generatingMove;
	}
	
	public int getGScore() {
		return gScore;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		if (obj == null || !obj.getClass().equals(this.getClass()))
			return false;
		
		Node<?> other = (Node<?>) obj;
		
		return getScore() == other.getScore() && getDepth() == other.getDepth() && getMove().equals(other.getMove()) && 
				getState().equals(other.getState()) && getParent().equals(other.getParent());
	}
	
	@Override
	public int hashCode() {
		return getState().hashCode() ^ getDepth();
	}
}
