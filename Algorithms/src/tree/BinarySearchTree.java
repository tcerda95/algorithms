package tree;

import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class BinarySearchTree<T> implements Iterable<T>{
	private BinaryTree<T> root;
	private Comparator<T> cmp;
	private int size;

	public BinarySearchTree(Comparator<T> c) {
		cmp = c;
	}
	
	public BinarySearchTree() {
		cmp = new Comparator<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public int compare(T o1, T o2) {
				return ((Comparable<T>) o1).compareTo(o2);
			}
		};
	}

	public void insert(T value) {
		root = insert(value, root);
	}

	private BinaryTree<T> insert (T value, BinaryTree<T> t) {
		if (t == null) {
			size += 1;
			return new BinaryTree<T>(value);
		}
		int comp = cmp.compare(value, t.value);
		if (comp < 0)
			t.left = insert(value, t.left);
		else if (comp > 0)
			t.right = insert(value, t.right);
		return t;
	}

	public void remove(T key) {
		root = remove(key, root);
	}

	private BinaryTree<T> remove(T key, BinaryTree<T> t) {
		if (t == null)
			return t;

		int comp = cmp.compare(key, t.value);

		if (comp < 0) {
			t.left = remove(key, t.left);
			return t;
		}
		else if (comp > 0) {
			t.right = remove(key, t.right);
			return t;
		}

		size -= 1;

		if (!t.hasChildren())
			return null;

		if (!t.hasBothChildren())
			return t.hasRightChild() ? t.right : t.left;

		BinaryTree<T> prev = null;
		BinaryTree<T> current = t.right;

		while (current.hasLeftChild()) {
			prev = current;
			current = current.left;
		}

		if (prev != null) {
			prev.left = current.right;
			current.right = t.right;
		}

		current.left = t.left;

		return current;
	}

	public boolean contains(T value) {
		return contains(value, root);
	}

	public boolean contains(T value, BinaryTree<T> t) {
		if (t == null)
			return false;
		int comp = cmp.compare(value, t.value);
		if (comp > 0)
			return contains(value, t.right);
		else if (comp < 0)
			return contains(value, t.left);
		return true;
	}
	
	public int size() {
		return size;
	}
	
	public void printAntecessor(T value) {
		printAntecessor(root, value);
	}

	private void printAntecessor(BinaryTree<T> tree, T value) {
		if (tree == null)
			return;
		
		printAntecessor(tree.left, value);
		if (cmp.compare(tree.value, value) < 0) {
			System.out.println(tree.value);
			printAntecessor(tree.right, value);
		}
	}
	
	public void printDescendants(T value) {
		printDescendants(root, value);
	}
	
	public void printDescendants(BinaryTree<T> tree, T value) {
		if (tree == null)
			return;
		
		if (cmp.compare(tree.value, value) > 0) {
			printDescendants(tree.left, value);
			System.out.println(tree.value);
		}
		printDescendants(tree.right, value);
	}
	
	public List<T> getInRange(T min, T max) {
		List<T> list = new LinkedList<>();
		if (cmp.compare(min, max) > 0)
			return list;
		
		getInRange(root, min, max, list);
		return list;
	}
	
	private void getInRange(BinaryTree<T> tree, T min, T max, List<T> list) {
		if (tree == null)
			return;

		if (cmp.compare(min, tree.value) > 0) {
			getInRange(tree.right, min, max, list);
		}
		else {
			getInRange(tree.left, min, max, list);
			if (cmp.compare(max, tree.value) >= 0) {
				list.add(tree.value);
				getInRange(tree.right, min, max, list);
			}
		}
	}

	public List<T> getInOrder(int inf, int sup) {
		List<T> list = new LinkedList<>();
		if (inf <= 0 || sup <= 0 || sup < inf || size() < inf)
			return list;
		
		getInOrder(root, inf, sup, 1, list);
		return list;
	}
	
	private int getInOrder(BinaryTree<T> tree, int inf, int sup, int order, List<T> list)  {
		if (tree == null)
			return order;
		
		order = getInOrder(tree.left, inf, sup, order, list);
		
		if (order == -1)
			return -1;
		
		if (order >= inf && order <= sup)
			list.add(tree.value);
		
		if (order+1 <= sup)
			return getInOrder(tree.right, inf, sup, order+1, list);
		
		return -1;	
	}

	public Iterator<T> iterator() {
		return inorderIterator();
	}

	public Iterator<T> inorderIterator() {
		return new InorderIterator<>(root);
	}
	
	public Iterator<T> preorderIterator() {
		return new PreorderIterator<>(root);
	}
	
	
	public Iterator<T> postorderIterator() {
		return new PostorderIterator<T>(root);
	}
	
	private static class InorderIterator<T> implements Iterator<T> {
		private Deque<BinaryTree<T>> stack;
		
		public InorderIterator(BinaryTree<T> root) {
			stack = new LinkedList<>();
			fillStack(root);
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			
			BinaryTree<T> popped = stack.pop();
			if (popped.hasRightChild())
				fillStack(popped.right);
			
			return popped.value;
		}
		
		private void fillStack(BinaryTree<T> tree) {
			stack.push(tree);
			while (tree.hasLeftChild()) {
				stack.push(tree.left);
				tree = tree.left;
			}
		}
	}
	
	private static class PreorderIterator<T> implements Iterator<T> {
		private Deque<BinaryTree<T>> stack;
		
		public PreorderIterator(BinaryTree<T> root) {
			stack = new LinkedList<>();
			stack.push(root);
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			
			BinaryTree<T> popped = stack.pop();
			if (popped.hasRightChild())
				stack.push(popped.right);
			if (popped.hasLeftChild())
				stack.push(popped.left);
			return popped.value;
		}
	}
	
	private static class PostorderIterator<T> implements Iterator<T> {
		private Deque<MarkableTree<T>> stack;
		
		private static class MarkableTree<T> {
			private BinaryTree<T> tree;
			private boolean marked;
			
			public MarkableTree(BinaryTree<T> tree) {
				this.tree = tree;
			}
		}
		
		public PostorderIterator(BinaryTree<T> root) {
			stack = new LinkedList<>();
			fillStack(root);
		}

		private void fillStack(BinaryTree<T> tree) {
			stack.push(new MarkableTree<>(tree));
			while (tree.hasLeftChild()) {
				stack.push(new MarkableTree<>(tree.left));
				tree = tree.left;
			}
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			
			MarkableTree<T> popped = stack.pop();
			if (popped.marked || !popped.tree.hasRightChild())
				return popped.tree.value;
			
			popped.marked = true;
			stack.push(popped);
			fillStack(popped.tree.right);
			
			return stack.pop().tree.value;
		}
	}
}