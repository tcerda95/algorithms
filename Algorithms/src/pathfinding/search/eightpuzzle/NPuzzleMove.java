package pathfinding.search.eightpuzzle;

import pathfinding.interfaces.Move;

public class NPuzzleMove implements Move<NPuzzleState> {

	private int from;
	private int to;
	
	public NPuzzleMove(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	public boolean isValid(NPuzzleState n) {
		return true;
	}

	@Override
	public boolean execute(NPuzzleState state) {
		state.move(from);
		return true;
	}

	@Override
	public boolean undo(NPuzzleState state) {
		state.move(to);
		return true;
	}
	
	@Override
	public int cost() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !obj.getClass().equals(this.getClass()))
			return false;
		
		NPuzzleMove other = (NPuzzleMove) obj;
		
		return from == other.from && to == other.to;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + from;
		result = prime * result + to;
		return result;
	}

	@Override
	public String toString() {
		return from + " -> " + to;
	}
}
