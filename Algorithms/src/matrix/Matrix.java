package matrix;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Matrix<V, S> {
	private Map<Pair<V, V>, S> matrix;
	
	public Matrix() {
		matrix = new HashMap<>();
	}
	
	public S put(V row, V col, S value) {
		return matrix.put(new Pair<>(row, col), value);
	}
	
	public S get(V row, V col) {
		return matrix.get(new Pair<>(row, col));
	}
	
	public Collection<S> values() {
		return matrix.values();
	}
	
	public boolean containsEntry(V row, V col) {
		return matrix.containsKey(new Pair<>(row, col));
	}
	
	public boolean containsValue(S value) {
		return matrix.containsValue(value);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || !o.getClass().equals(this.getClass()))
			return false;
		
		Matrix<?,?> other = (Matrix<?, ?>) o;
		for (Pair<V,V> pair : matrix.keySet())
			if (!matrix.get(pair).equals(other.matrix.get(pair)))
				return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return matrix.hashCode();
	}
}
