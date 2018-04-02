package pathfinding.search.eightpuzzle;

import pathfinding.interfaces.Solution;
import pathfinding.search.Searcher;

public class Test {

	public static void main(String[] args) throws CloneNotSupportedException {
		int[] initialArray = {5,1,2,4,14,9,3,7,13,10,12,6,15,11,8,0};
		NPuzzleState initial = new NPuzzleState(initialArray, 4);
		NPuzzleState goal = initial.getGoalState();
				
		Searcher<NPuzzleState> searcher = new Searcher<NPuzzleState>(initial, goal);
		
	//	System.out.println(searcher.DFSearch(26).moves().size());
	//	System.out.println(searcher.IDDFSearch());

		Solution<NPuzzleState> solution = searcher.AStarSearch(new NPuzzleScorer(initial));
		System.out.println(solution);
		System.out.println(solution.moves().size());
		

	}
}
