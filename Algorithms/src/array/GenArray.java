package array;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class GenArray<T> implements List<T>{
	final private static int CAPACITY_FACTOR = 2;
	
	protected T[] array;
	private int size;
	
	@SuppressWarnings("unchecked")
	public GenArray(Class<T> clazz, int capacity) {
		array = (T[]) Array.newInstance(clazz, capacity);
		this.size = 0;
	}
	
	@Override
	public boolean add(T e) {
		add(size, e);
		return true;
	}
	
	@Override
	public void add(int index, T element) {
		if (index > size || index < 0)
			throw new IndexOutOfBoundsException();
		
		tryExpand();
		
		shiftRight(index);
		array[index] = element;
		size += 1;
	}
	
	@Override
	public boolean addAll(Collection<? extends T> c) {
		return addAll(size, c);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		if (index > size || index < 0)
			throw new IndexOutOfBoundsException();
		
		int cSize = c.size();
		if (cSize + size > array.length)
			expand(cSize + size);
		
		shiftRight(index, cSize);
		
		for (T elem : c)
			array[index++] = elem;
		
		size += cSize;
		return cSize > 0;
	}
	
	@Override
	public void clear() {
		size = 0;
	}
	
	@Override
	public boolean contains(Object o) {
		for (T elem : this)
			if (elem.equals(o))
				return true;
		return false;
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object elem : c)
			if (!contains(elem))
				return false;
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (other == null || other.getClass().equals(this.getClass()))
			return false;
		GenArray<?> otherArr = (GenArray<?>) other;
		
		if (size() != otherArr.size())
			return false;
		
		for (int i = 0; i < size(); i++)
			if (!get(i).equals(otherArr.get(i)))
				return false;
		
		return true;
	}
	
	@Override
	public T get(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException();
		return array[index];
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(array);
	}
	
	@Override
	public int indexOf(Object o) {
		for (int i = 0; i < size; i++)
			if (array[i].equals(o))
				return i;
		return -1;
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public Iterator<T> iterator() {
		return listIterator();
	}
	
	@Override
	public int lastIndexOf(Object o) {
		for (int i = size-1; i >= 0; i--)
			if (array[i].equals(o))
				return i;
		return -1;
	}
	
	@Override
	public ListIterator<T> listIterator() {
		return listIterator(0);
	}
	
	@Override
	public ListIterator<T> listIterator(int index) {
		return new ArrayIterator<T>(this, index);
	}
	
	@Override
	public T remove(int index) {
		T element = get(index);
		shiftLeft(index);
		size -= 1;
		return element;
	}
	
	@Override
	public boolean remove(Object o) {
		int index = indexOf(o);
		if (index != -1)
			remove(index);
		return index != -1;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		int prevSize = size();
		for (Object o : c)
			remove(o);
		return prevSize != size();
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		int prevSize = size();
		Iterator<T> iter = iterator();
		while (iter.hasNext())
			if (!c.contains(iter.next()))
				iter.remove();
		return prevSize != size();
	}
		
	@Override
	public T set(int index, T element) {
		T prevElem = get(index);
		array[index] = element;
		return prevElem;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return new ArrayList<T>(this).subList(fromIndex, toIndex);
	}
	
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(array, size());
	}
	
	@Override
	public <E> E[] toArray(E[] a) {
		return new ArrayList<T>(this).toArray(a);
	}
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("[");
		for (T elem : this)
			str.append(elem.toString() + ", ");
		str.delete(str.length()-2, str.length());
		str.append(']');
		return new String(str);
	}
	
	/**
	 * Shifts array elements the given slots to the right to the right
	 * @param index - index of the element to begin shifting right
	 * @param slots - number of slots to be shifted
	 */
	private void shiftRight(int index, int slots) {
		if (slots < 1)
			throw new IllegalArgumentException("Number of slots must be at least 1");
		for (int i = size-1; i >= index; i--)
			array[i+slots] = array[i];
	}
	
	private void shiftRight(int index) {
		shiftRight(index, 1);
	}
	
	/**
	 * Shifts array elements one slot to the left
	 * @param index - index of the slot which will be replaced by the shifting
	 */
	private void shiftLeft(int index) {
		for (int i = index+1; i < size; i++)
			array[i-1] = array[i];
	}
	
	/**
	 * Checks if expand of array is necessary and performs the expansion
	 */
	private void tryExpand() {
		if (array.length == size)
			expand(array.length * CAPACITY_FACTOR);
	}
	
	private void expand(int newCapacity) {
		array = Arrays.copyOf(array, newCapacity);
	}
	
	private static class ArrayIterator<E> implements ListIterator<E> {
		private GenArray<E> arr;
		private int current;
		private int lastReturned;
		
		public ArrayIterator(GenArray<E> arr, int index) {
			if (index < 0 || index >= arr.size())
				throw new IndexOutOfBoundsException();
			
			this.arr = arr;
			this.current = index;
			this.lastReturned = -1;
		}

		@Override
		public boolean hasNext() {
			return current < arr.size();
		}

		@Override
		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			lastReturned = current;
			return arr.get(current++);
		}
		
		@Override
		public void remove() {
			if (lastReturned == -1)
				throw new IllegalStateException();
			arr.remove(lastReturned);
			current = lastReturned;
			lastReturned = -1;
		}

		@Override
		public boolean hasPrevious() {
			return current > 0;
		}

		@Override
		public E previous() {
			if (!hasPrevious())
				throw new NoSuchElementException();
			lastReturned = current - 1;
			return arr.get(--current);
		}

		@Override
		public int nextIndex() {
			return current;
		}

		@Override
		public int previousIndex() {
			return current-1;
		}

		@Override
		public void set(E e) {
			arr.set(lastReturned, e);
			lastReturned = -1;
		}

		@Override
		public void add(E e) {
			lastReturned = -1;
			arr.add(current++, e);
		}
	}

}
