package pathfinding.search.eightpuzzle;

public class EightPuzzleState extends NPuzzleState {
	
	private final static int DIM = 3;
	
	@Override
	public EightPuzzleState getGoalState() {
		int goalArray[] = {1,2,3,8,0,4,7,6,5};
		return new EightPuzzleState(goalArray, 4);
	}
	
	public EightPuzzleState(int[] initial) {
		super(initial, DIM);
	}
	
	@Override
	public EightPuzzleState clone() {
		return new EightPuzzleState(getArray(), getZeroIndex());
	}
	
	private EightPuzzleState(int[] initial, int zeroIndex) {
		super(initial, DIM, zeroIndex);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
