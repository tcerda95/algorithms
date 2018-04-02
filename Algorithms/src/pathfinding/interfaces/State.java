package pathfinding.interfaces;

import java.util.List;

public interface State extends Cloneable {
	@Override
	public boolean equals(Object obj);
	
	@Override
	public int hashCode();
	
	public State clone() throws CloneNotSupportedException;
	
	public <T extends State> List<Move<T>> validMoves();
}
