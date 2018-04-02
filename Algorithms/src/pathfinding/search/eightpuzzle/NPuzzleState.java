package pathfinding.search.eightpuzzle;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import pathfinding.interfaces.Move;
import pathfinding.interfaces.State;

public class NPuzzleState implements State {
	
	private int[] array;
	private int zeroIndex;
	private int dim;
	private int size;
	private int[] moveOffset;
	
	public NPuzzleState(int[] initial, int dim) {
		size = dim*dim;
		if (initial.length != size)
			throw new IllegalArgumentException("N puzzle array size is " + size + ".");
		
		array = initial.clone();
		this.dim = dim;
		buildMoveOffset(dim);
		
		zeroIndex = -1;
		
		for (int i = 0; i < array.length && zeroIndex == -1; i++)
			if (array[i] == 0)
				zeroIndex = i;
		
		if (zeroIndex == -1)
			throw new IllegalArgumentException("There has to be an empty space represented by zero.");
	}
	
	protected NPuzzleState(int[] array, int dim, int zeroIndex) {
		this.array = array.clone();
		this.dim = dim;
		this.size = dim*dim;
		this.zeroIndex = zeroIndex;
		buildMoveOffset(dim);
	}
	
	protected int getZeroIndex() {
		return zeroIndex;
	}
	
	public int getDim() {
		return dim;
	}
	
	public NPuzzleState getGoalState() {
		int[] array = new int[size];
		
		for (int i = 0; i < size-1; i++)
			array[i] = i+1;
		
		return new NPuzzleState(array, dim, size-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Move<NPuzzleState>> validMoves() {
		List<Move<NPuzzleState>> moves = new LinkedList<>();
		
		for (int offset : moveOffset) {
			int from = zeroIndex + offset;
			if (from >= 0 && from < size && (from % dim != 0 || zeroIndex % dim != dim-1) && 
					(from % dim != dim-1 || zeroIndex % dim != 0))
				moves.add(new NPuzzleMove(from, zeroIndex));
		}
		
		return moves;
	}

	@Override
	public NPuzzleState clone() {
		return new NPuzzleState(array, dim, zeroIndex);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !obj.getClass().equals(this.getClass()))
			return false;
		
		NPuzzleState other = (NPuzzleState) obj;
		
		return Arrays.equals(other.array, this.array);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(array);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("");

		for(int i = 0; i < size; i++) {
			if (i % dim == 0 && i != 0)
				buffer.append("\n");
			buffer.append(array[i] + " ");
		}
		
		return buffer.toString();
	}
	
	void move(int from) {
		int tmp = array[from];
		array[from] = array[zeroIndex];
		array[zeroIndex] = tmp;
		
		zeroIndex = from;
	}
	
	int[] getArray() {
		return array;
	}
	
	private void buildMoveOffset(int dim) {
		moveOffset = new int[4];
		moveOffset[0] = -dim;
		moveOffset[1] = -1;
		moveOffset[2] = 1;
		moveOffset[3] = dim;		
	}
}
