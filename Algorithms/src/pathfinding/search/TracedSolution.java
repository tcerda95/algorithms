package pathfinding.search;

import java.util.LinkedList;
import java.util.List;

import pathfinding.interfaces.Move;
import pathfinding.interfaces.Solution;
import pathfinding.interfaces.State;

public class TracedSolution<T extends State> implements Solution<T> {

	private T initial;
	private Node<T> goal;
	private boolean succeded;
	
	public TracedSolution(T initial) {
		this(initial, null, false);
	}
	
	public TracedSolution(T initial, Node<T> goal) {
		this(initial, goal, true);
	}
	
	public TracedSolution(T initial, Node<T> goal, boolean succeded) {
		this.initial = initial;
		this.goal = goal;
		this.succeded = succeded;
	}
	
	@Override
	public T getInitial() {
		return initial;
	}

	@Override
	public Node<T> getGoal() {
		return goal;
	}

	@Override
	public List<Move<T>> moves() {
		if(!succeded())
			throw new IllegalStateException("No moves to extract. Solution has not succeeded.");
		
		LinkedList<Move<T>> list = new LinkedList<>();
		
		Node<T> n = goal;
		
		while (n.hasParent()) {
			list.addFirst(n.getMove());
			n = n.getParent();
		}
		
		return list;
	}

	@Override
	public boolean succeded() {
		return this.succeded;
	}

	@Override
	public String toString() {
		if(!succeded())
			throw new IllegalStateException("No moves to extract. Solution has not succeeded.");
		
		StringBuffer str = new StringBuffer("");
		
		Node<T> n = goal;
		
		while (n.hasParent()) {
			str.insert(0, n.getState().toString());
			n = n.getParent();
			str.insert(0, "\n\n");
		}
		
		str.insert(0, n.getState().toString());
		
		return str.toString();
	}
}
