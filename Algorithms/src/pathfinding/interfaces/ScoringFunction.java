package pathfinding.interfaces;

import pathfinding.search.Node;

public interface ScoringFunction<T extends State> {
	public int score(Node<T> node);
}
