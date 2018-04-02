package pathfinding.interfaces;

import java.util.List;

import pathfinding.search.Node;

public interface Solution<T extends State> {
	public T getInitial();
	public Node<T> getGoal();
	public List<Move<T>> moves();
	public boolean succeded();
}
