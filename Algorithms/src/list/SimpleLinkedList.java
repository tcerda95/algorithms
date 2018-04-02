package list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SimpleLinkedList<T> implements Iterable<T>{
	protected Node<T> first;
	protected int size;
	
	public void add(T value) {		
		first = new Node<T>(value, first);
		size += 1;
	}
	
	public boolean contains(T value) {
		for (T elem : this)
			if (value.equals(elem))
				return true;
		return false;
	}
	
	public void clear() {
		size = 0;
		first = null;
	}
	
	public void invert() {
		first = invert(first, null);
	}
	
	private Node<T> invert(Node<T> current, Node<T> prev) {
		if (current == null)
			return prev;
		Node<T> next = current.next;
		current.next = prev;
		return invert(next, current);
	}
	
	public boolean remove(T value) {
		int prevSize = size;
		first = remove(value, first);
		return prevSize != size;
	}
	
	public int size() {
		return size;
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
	
	public boolean hasLoop() {
		Node<T> slow = first;
		Node<T> fast = first;
		
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			if (fast == slow)
				return true;
		}
		
		return false;
	}
	
	public boolean isCircular() {
		boolean checkedFirst = false;
		Node<T> slow = first;
		Node<T> fast = first;
		
		while (fast != null && fast.next != null) {
			slow = slow.next;
			checkedFirst = fast.next == first || fast.next.next == first;
			fast = fast.next.next;
			if (fast == slow)
				return checkedFirst;
		}
		
		return false;
	}
	
	public T getMiddle() {
		Node<T> fast, slow;
		fast = slow = first;
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		return slow.value;
	}
	
	public SimpleLinkedList<T> removeRepeated() {
		Node<T> current = first;
		
		while (current != null) {
			current.next = removeAll(current.value, current.next);
			current = current.next;
		}
		
		return this;
	}
	
	private Node<T> removeAll(T value, Node<T> n) {
		if (n == null)
			return n;
		
		n.next = removeAll(value, n.next);
		
		if (n.value.equals(value))
			return n.next;
		
		return n;
	}

	private Node<T> remove(T value, Node<T> n) {
		if (n == null)
			return n;
		
		if (n.value.equals(value)) {
			size -= 1;
			return n.next;
		}
		
		n.next = remove(value, n.next);
		
		return n;
	}
	
	protected static class Node<E> {
		protected E value;
		protected Node<E> next;
		
		public Node(E value, Node<E> next) {
			this.value = value;
			this.next = next;
		}
		
		public Node(E value) {
			this(value, null);
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new SimpleLinkedListIterator<T>(first);
	}
	
	private static class SimpleLinkedListIterator<E> implements Iterator<E>{
		private Node<E> current;
		
		public SimpleLinkedListIterator(Node<E> first) {
			current = first;
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public E next() {
			E value = current.value;
			current = current.next;
			return value;
		}
	}
	
	/**
	 * Union of sorted lists.
	 * @param lists - lists which the union will be made. Must be sorted.
	 */
	public static <E> SimpleLinkedList<E> sortedUnion(Collection<SimpleLinkedList<E>> lists, Comparator<E> comp) {
		
		List<Node<E>> arrayNode = new ArrayList<>(lists.size());
		SimpleLinkedList<E> unionList = new SimpleLinkedList<E>();
		int minIndex;
		
		for (SimpleLinkedList<E> l : lists)
			arrayNode.add(l.first);
		
		while (arrayNode.size() > 0) {
			minIndex = 0;
			E minValue = arrayNode.get(0).value;
			
			for (int i = 1; i < arrayNode.size(); i++) {
				E value = arrayNode.get(i).value;
				int cmp = comp.compare(value, minValue) * -1;  // List inserts at front. Must reverse comp.
				
				if (cmp < 0) {
					minIndex = i;
					minValue = value;
				}
				else if (cmp == 0 && advanceNode(arrayNode, i))
					i--;
			}
						
			unionList.add(minValue);
			advanceNode(arrayNode, minIndex);
		}
		
		return unionList;
	}
	
	private static <E> boolean advanceNode(List<Node<E>> arrayNode, int index) {
		Node<E> next = arrayNode.get(index).next;
		if (next != null) {
			arrayNode.set(index, next);
			return false;
		}
		else {
			arrayNode.remove(index);
			return true;
		}
	}
	
	public static SimpleLinkedList<Integer> sumLists(SimpleLinkedList<Integer> l1, SimpleLinkedList<Integer> l2) {
		SimpleLinkedList<Integer> ans = new SimpleLinkedList<>();
		
		if (l1.size() < l2.size()) {
			ans.first = sumLists(skipList(l2.first, l2.size() - l1.size()), l1.first);
			ans.first = prependSum(ans.first, l2.first, l2.size() - l1.size());
		}
		else if (l2.size() < l1.size()) {
			ans.first = sumLists(skipList(l1.first, l1.size() - l2.size()), l2.first);
			ans.first = prependSum(ans.first, l1.first, l1.size() - l2.size());
		}
		else
			ans.first = sumLists(l1.first, l2.first);
		
		if (ans.first.value == 0 && ans.first.next != null)
			ans.first = ans.first.next;
		
		return ans;
	}

	private static Node<Integer> prependSum(Node<Integer> dest, Node<Integer> src, int len) {
		if (len == 0)
			return dest;
		
		dest = prependSum(dest, src.next, len-1);
		
		int sum = dest.value + src.value;
		dest.value = sum % 10;
			
		return new Node<Integer>(sum / 10, dest);
	}

	private static Node<Integer> skipList(Node<Integer> n, int i) {
		while (i-- > 0)
			n = n.next;
		return n;
	}

	private static Node<Integer> sumLists(Node<Integer> n1, Node<Integer> n2) {
		if (n1 == null && n2 == null)
			return new Node<Integer>(0);
		
		Node<Integer> sumNode;
		int sum;
		
		sumNode = sumLists(n1.next, n2.next);
		sum = n1.value + n2.value;
		
		sum += sumNode.value;
		sumNode.value = sum % 10;

		return new Node<Integer>(sum / 10, sumNode);
	}
	
	public static SimpleLinkedList<Integer> substractLists(SimpleLinkedList<Integer> l1, SimpleLinkedList<Integer> l2) {
		SimpleLinkedList<Integer> ans = new SimpleLinkedList<>();
		Node<Integer> smaller = findSmaller(l1, l2);
		if (smaller == l1.first)
			ans.first = substractLists(l2.first, smaller, l2.size() - l1.size());
		else
			ans.first = substractLists(l1.first, smaller, l1.size() - l2.size());
		return ans;
	}

	private static Node<Integer> substractLists(Node<Integer> bigger, Node<Integer> smaller, int lenDif) {
		Node<Integer> ansFirst;
		if (lenDif > 0) {
			ansFirst = substractLists(skipList(bigger, lenDif), smaller);
			ansFirst = prependSub(ansFirst, bigger, lenDif).next; // There's an extra leading zero so call next
		}
		else
			ansFirst = substractLists(bigger, smaller).next; // There's an extra leading zero so call next
		return ansFirst;
	}

	private static Node<Integer> prependSub(Node<Integer> dest, Node<Integer> src, int len) {
		if (len == 0)
			return dest;
		
		dest = prependSub(dest, src.next, len-1);
		dest.value += src.value;
		
		if (dest.value < 0)
			return new Node<Integer>(-1, dest);
		
		return new Node<Integer>(0, dest);
	}

	private static Node<Integer> substractLists(Node<Integer> bigger, Node<Integer> smaller) {
		if (bigger == null && smaller == null)
			return new Node<Integer>(0);
		
		Node<Integer> subNode = substractLists(bigger.next, smaller.next);
		
		int sub = bigger.value - smaller.value;
		
		subNode.value += sub;
		
		if (subNode.value < 0) {
			subNode.value += 10;
			return new Node<Integer>(-1, subNode);
		}
		
		return new Node<Integer>(0, subNode);
	}

	private static Node<Integer> findSmaller(SimpleLinkedList<Integer> n1, SimpleLinkedList<Integer> n2) {
		if (n1.size() < n2.size())
			return n1.first;
		else if (n2.size() < n1.size())
			return n2.first;
		return n1.first.value < n2.first.value ? n1.first : n2.first;
	}
}
