package matrix;

public class Pair<X, Y> {
	private X x;
	private Y y;
	
	public Pair(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	public X getX() {
		return x;
	}
	
	public Y getY() {
		return y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !obj.getClass().equals(this.getClass()))
			return false;
		Pair<?,?> other = (Pair<?,?>) obj;
		return this.getX().equals(other.getX()) && this.getY().equals(other.getY());
	}
	
	@Override
	public int hashCode() {
		return getX().hashCode() ^ getY().hashCode();
	}
}
