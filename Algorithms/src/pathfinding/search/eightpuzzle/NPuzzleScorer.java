package pathfinding.search.eightpuzzle;

import java.util.HashMap;
import java.util.Map;

import pathfinding.interfaces.ScoringFunction;
import pathfinding.search.Node;

public class NPuzzleScorer implements ScoringFunction<NPuzzleState> {
	
	private Map<Integer, Integer> map;
	private int dim;
	
	public NPuzzleScorer(NPuzzleState state) {
		int[] goalArray = state.getGoalState().getArray();
		dim = state.getDim();
		map = new HashMap<>();

		for (int i = 0; i < goalArray.length; i++) 
			map.put(goalArray[i], i);
	}
	
	@Override
	public int score(Node<NPuzzleState> node) {
		int score = 0;
		NPuzzleState state = node.getState();
		int[] array = state.getArray();
		
		for (int i = 0; i < array.length; i++)
			score += distance(array[i], i);
		
		return score;
	}

	private int distance(int number, int index) {
		int goalIndex = map.get(number);
		int indexDif = Math.abs(goalIndex - index);
				
		return (indexDif / dim) + (indexDif % dim);
	}
}
