package pathfinding.interfaces;

public interface Move<T extends State> {
	public boolean isValid(T n);
	public boolean execute(T n);
	public boolean undo(T n);
	public int cost(); // Used for calculating g(n)
}
