package tree;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import function.Function;

public class BinaryTree<T> {

	T value;
	BinaryTree<T> left;
	BinaryTree<T> right;
	
	public static <T> boolean isPostOrderSorted(BinaryTree<T> tree, Comparator<T> cmp) {
		return tree == null || isPostOrderSorted(tree, cmp, null) != null;
	}
	
	private static <T> T isPostOrderSorted(BinaryTree<T> tree, Comparator<T> cmp, T prev) {
		if (tree.hasLeftChild()) {
			prev = isPostOrderSorted(tree.left, cmp, prev);
			if (prev == null)
				return null;
		}
		
		if (tree.hasRightChild()) {
			prev = isPostOrderSorted(tree.right, cmp, prev);
			if (prev == null)
				return null;
		}
		
		return (prev == null || cmp.compare(tree.value, prev) >= 0) ? tree.value : null;
	}
	
	public static <T> BinaryTree<T> spanning(BinaryTree<T> tree, T value) {
		if (tree == null)
			return null;
		
		BinaryTree<T> left = spanning(tree.left, value);
		BinaryTree<T> right = spanning(tree.right, value);
		
		if (left != null || right != null || tree.value.equals(value)) {
			BinaryTree<T> span = new BinaryTree<T>(tree.value);
			span.left = left;
			span.right = right;
			return span;
		}
		
		return null;
	}

	public static <T> BinaryTree<T> buildHeapFromList(List<T> values) {
		if (values.size() < 1)
			return null;
		
		Queue<BinaryTree<T>> queue = new LinkedList<>();
		Iterator<T> iter = values.iterator();
		BinaryTree<T> root = new BinaryTree<T>(iter.next());
		
		queue.add(root);
		
		while(iter.hasNext()) {
			BinaryTree<T> dequed = queue.remove();
			
			dequed.left = new BinaryTree<T>(iter.next());
			queue.add(dequed.left);
			
			if (iter.hasNext()) {
				dequed.right = new BinaryTree<T>(iter.next());
				queue.add(dequed.right);
			}
		}
		
		return root;
	}
	
	public static <T> boolean isMinHeap(BinaryTree<T> tree, Comparator<T> cmp) {
		return isMaxHeap(tree, new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				return cmp.compare(o1, o2) * (-1);
			}
			
		});
	}
	
	public static <T> boolean isMaxHeap(BinaryTree<T> tree, Comparator<T> cmp) {
		if (tree == null)
			return true;
		
		Queue<BinaryTree<T>> queue = new LinkedList<>();
		
		boolean searchChildren = true;
		queue.add(tree);
		
		while (!queue.isEmpty()) {
			BinaryTree<T> dequed = queue.remove();
			
			if (dequed.hasLeftChild()) {
				if (!searchChildren || cmp.compare(dequed.value, dequed.left.value) < 0)
					return false;
				queue.add(dequed.left);
			}
			else
				searchChildren = false;
			
			if (dequed.hasRightChild()) {
				if (!searchChildren || cmp.compare(dequed.value, dequed.right.value) < 0)
					return false;
				queue.add(dequed.right);
			}
			else
				searchChildren = false;
		}
		
		return true;
	}
	
	public static <T> boolean checkPostorder(BinaryTree<T> tree, List<T> values) {
		Iterator<T> iter = values.iterator();
		return checkPostorder(tree, iter) && !iter.hasNext();
	}
	
	private static <T> boolean checkPostorder(BinaryTree<T> tree, Iterator<T> iterator) {
		if (tree == null)
			return true;
		
		boolean isPostOrder = false;
		
		isPostOrder = checkPostorder(tree.left, iterator) && checkPostorder(tree.right, iterator);
		
		if (isPostOrder && iterator.hasNext())
			return iterator.next().equals(tree.value);
		
		return false;
	}

	public static <T> BinaryTree<T> build(T[] sortedArray) {
		return build(sortedArray, 0, sortedArray.length-1);
	}
	
	private static <T> BinaryTree<T> build(T[] sortedArray, int left, int right) {
		if (right < left)
			return null;
		
		int mid = (left + right) / 2;
		
		BinaryTree<T> tree = new BinaryTree<T>(sortedArray[mid]);
		tree.left = build(sortedArray, left, mid-1);
		tree.right = build(sortedArray, mid+1, right);
		
		return tree;
	}
	
	public static int maximumPathSum(BinaryTree<Integer> tree) {
		PathAcumulator acum = maximumPathSumRec(tree);
		return Integer.max(acum.mutableAcum, acum.unmutableAcum);
	}
	
	private static PathAcumulator maximumPathSumRec(BinaryTree<Integer> tree) {
		if (tree == null)
			return new PathAcumulator();
		
		PathAcumulator leftAcum = maximumPathSumRec(tree.left);
		PathAcumulator rightAcum = maximumPathSumRec(tree.right);
		
		PathAcumulator newAcum =  new PathAcumulator();
		
		int unmutableSum = leftAcum.mutableAcum + rightAcum.mutableAcum + tree.value;
		
		newAcum.unmutableAcum = Integer.max(Integer.max(leftAcum.unmutableAcum, rightAcum.unmutableAcum), unmutableSum);		
		newAcum.mutableAcum = Integer.max(Integer.max(leftAcum.mutableAcum, rightAcum.mutableAcum) + tree.value, 0);
		
		return newAcum;
	}

	private static class PathAcumulator {
		private int mutableAcum;
		private int unmutableAcum;
		
		public PathAcumulator(int m, int u) {
			mutableAcum = m;
			unmutableAcum = u;
		}
		
		public PathAcumulator() {}
	}
	
	public int maxLengthPath() {
		return maxLengthPath(this).unmutableAcum;
	}

	private PathAcumulator maxLengthPath(BinaryTree<T> tree) {
		if (tree == null)
			return new PathAcumulator(-1, -1);
		
		PathAcumulator leftAcum = maxLengthPath(tree.left);
		PathAcumulator rightAcum = maxLengthPath(tree.right);
		
		PathAcumulator newAcum = new PathAcumulator(Integer.max(leftAcum.mutableAcum, rightAcum.mutableAcum) + 1, 
				Integer.max(leftAcum.unmutableAcum, rightAcum.unmutableAcum));
				
		int unmutableAcum = leftAcum.mutableAcum + rightAcum.mutableAcum + 2;
		if (unmutableAcum > newAcum.unmutableAcum)
			newAcum.unmutableAcum = unmutableAcum;
		
		return newAcum;
	}

	public BinaryTree(T value) {
		this.value = value;
	}
	
	public boolean isBST(Comparator<T> cmp) {
		return isBST(this, cmp, null, null);
	}
	
	private boolean isBST(BinaryTree<T> tree, Comparator<T> cmp, T lessThan, T higherThan) {
		if (tree == null)
			return true;
		
		if (lessThan != null && cmp.compare(tree.value, lessThan) > 0)
			return false;
		
		if (higherThan != null && cmp.compare(tree.value, higherThan) < 0)
			return false;
		
		return isBST(tree.left, cmp, tree.value, higherThan) && isBST(tree.right, cmp, lessThan, tree.value);
	}
	
	public boolean isAVL(Comparator<T> cmp) {
		return isAVL(this, cmp, null, null) != -2;
	}
	
	private int isAVL(BinaryTree<T> tree, Comparator<T> cmp, T lessThan, T higherThan) {
		if (tree == null)
			return -1;
		
		if (lessThan != null && cmp.compare(tree.value, lessThan) > 0)
			return -2;
		
		if (higherThan != null && cmp.compare(tree.value, higherThan) < 0)
			return -2;
		
		int h1 = isAVL(tree.left, cmp, tree.value, higherThan);
		int h2 = isAVL(tree.right, cmp, lessThan, higherThan);
		int diff = h2 - h1;
		
		if (h1 != -2 && h2 != -2 && diff <= 1 && diff >= -1)
			return Integer.max(h1, h2) + 1;
		
		return -2;
	}

	public <S> BinaryTree<S> map(Function<T, S> f) {
		return map(this, f);
	}
	
	private <S> BinaryTree<S> map(BinaryTree<T> tree, Function<T, S> f) {
		if (tree == null)
			return null;
		
		BinaryTree<S> mapped = new BinaryTree<>(f.apply(tree.value));
		
		mapped.left = map(tree.left, f);
		mapped.right = map(tree.right, f);
		
		return mapped;
	}
	
	public boolean hasChildren() {
		return hasLeftChild() || hasRightChild();
	}

	public boolean hasLeftChild() {
		return left != null;
	}

	public boolean hasRightChild() {
		return right != null;
	}

	public boolean hasBothChildren() {
		return hasLeftChild() && hasRightChild();
	}
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("[");
		Queue<BinaryTree<T>> queue = new LinkedList<>();
		queue.add(this);
		while (!queue.isEmpty()) {
			BinaryTree<T> dequed = queue.remove();
			str.append(dequed.value.toString() + ", ");
			if (dequed.hasLeftChild())
				queue.add(dequed.left);
			if (dequed.hasRightChild())
				queue.add(dequed.right);
		}
		str.delete(str.length()-2, str.length());
		str.append(']');
		return str.toString();
	}
}
