package pathfinding.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import list.LinkedListStack;
import pathfinding.interfaces.Move;
import pathfinding.interfaces.ScoringFunction;
import pathfinding.interfaces.Solution;
import pathfinding.interfaces.State;

public class Searcher<T extends State> {
	
	private final static int PQ_INIT_SIZE = 256;
	
	private T initial;
	private T goal;
	
	public Searcher(T initial, T goal) {
		this.initial = initial;
		this.goal = goal;
	}
	
	public void setInitialState(T initial) {
		this.initial = initial;
	}

	public void setGoalState(T goal) {
		this.goal = goal;
	}
	
	@SuppressWarnings("unchecked")
	private Solution<T> blindSearch(int maxDepth, LinkedList<Node<T>> open) throws CloneNotSupportedException {
		int steps = 0;
		int maxOpenSize = 0;
		Set<T> closed = new HashSet<>();

		open.add(new Node<T>(initial));
		
		while (!open.isEmpty()) {
			if (open.size() > maxOpenSize)
				maxOpenSize = open.size();
			
			steps += 1;
			Node<T> current = open.remove();
			T currentState = current.getState();
			
			if (!closed.contains(currentState)) {
				if (currentState.equals(goal)) {
					System.out.println("Blind search steps: " + steps);
					System.out.println("Blind search max open size: " + maxOpenSize);
					return new TracedSolution<T>(initial, current);
				}
				
				closed.add(currentState);
				
				if (current.getDepth()+1 <= maxDepth) {
					for (Move<State> move : currentState.validMoves()) {
						T copy = (T) currentState.clone();
						move.execute(copy);
						open.add(new Node<T>(copy, current.getDepth()+1, current, (Move<T>) move));
					}
				}
			}
		}
		
		System.out.println("Blind search steps: " + steps);
		System.out.println("Blind search max open size: " + maxOpenSize);
		return new TracedSolution<T>(initial);
	}
	
	@SuppressWarnings("unchecked")
	public Solution<T> DFSearch(int maxDepth) throws CloneNotSupportedException {
		int steps = 0;
		Map<T, Node<T>> closed = new HashMap<>();
		LinkedListStack<Node<T>> open = new LinkedListStack<>();
		
		Node<T> current = new Node<>(initial);		
		open.add(current);
		
		while (!open.isEmpty()) {
			steps += 1;
			current = open.remove();
			T currentState = current.getState();
			
			if (!closed.containsKey(currentState) || closed.get(currentState).getDepth() > current.getDepth()) {
				closed.put(currentState, current);
				if (current.getDepth() < maxDepth) {
					for (Move<State> move : currentState.validMoves()) {
						T copy = (T) currentState.clone();
						move.execute(copy);
						
						Node<T> copyNode = new Node<>(copy, current.getDepth()+1, current, (Move<T>) move);
						
						if (copy.equals(goal)) {
							System.out.println("DFS steps: " + steps);
							return new TracedSolution<T>(initial, copyNode);
						}
						
						boolean inClosed = closed.containsKey(copy);
						if (!inClosed || closed.get(copy).getDepth() > copyNode.getDepth()) {
							if (inClosed)
								closed.remove(copy);
							
							open.add(copyNode);
						}
					}
				}
			}
		}
		
		return new TracedSolution<T>(initial);	
	}
	
	public Solution<T> IDDFSearch() throws CloneNotSupportedException {
		int infinity = Integer.MAX_VALUE;
		Solution<T> solution = DFSearch(0);
		
		for (int i = 1; i < infinity && !solution.succeded(); i++)
			solution = DFSearch(i);
		
		return solution;
	}
	
	public Solution<T> BFSearch() throws CloneNotSupportedException {
		return blindSearch(Integer.MAX_VALUE, new LinkedList<Node<T>>());
	}
	
	@SuppressWarnings("unchecked")
	public Solution<T> AStarSearch(ScoringFunction<T> scorer) throws CloneNotSupportedException {
		int steps = 0;
		Map<T, Node<T>> closed = new HashMap<>();
		PriorityQueue<Node<T>> open = new PriorityQueue<>(PQ_INIT_SIZE, (o1, o2) -> (o1.getScore() - o2.getScore()));
		
		Node<T> current = new Node<>(initial);
		current.setScore(scorer.score(current));
		
		open.add(current);
		
		while (!open.isEmpty()) {
			steps += 1;
			current = open.remove();
			T currentState = current.getState();
			
			if (!closed.containsKey(currentState) || closed.get(currentState).getScore() > current.getScore()) {
				if (currentState.equals(goal)) {
					System.out.println("A* steps: " + steps);
					return new TracedSolution<T>(initial, current);
				}
				
				closed.put(currentState, current);
				
				for (Move<State> move : currentState.validMoves()) {
					T copy = (T) currentState.clone();
					move.execute(copy);
					
					Node<T> copyNode = new Node<>(copy, current.getDepth()+1, current, (Move<T>) move, current.getGScore() + move.cost());
					copyNode.setScore(scorer.score(copyNode) + copyNode.getGScore());
					
					boolean inClosed = closed.containsKey(copy);
					if (!inClosed || closed.get(copy).getScore() > copyNode.getScore()) {
						if (inClosed)
							closed.remove(copy);
						
						open.add(copyNode);
					}	
				}
			}
		}
		
		return new TracedSolution<T>(initial);
	}

}
