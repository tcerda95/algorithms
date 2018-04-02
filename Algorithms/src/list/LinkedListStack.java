package list;

import java.util.LinkedList;

public class LinkedListStack<E> extends LinkedList<E> {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(E e) {
		super.addFirst(e);
		return true;
	}
}
