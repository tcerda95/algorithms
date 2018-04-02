package array;

import java.util.Comparator;
import java.util.Random;

public class SortableGenArray<T> extends GenArray<T> {
	
	private Comparator<T> cmp;
	private Random rand = new Random();
	
	private final static int Q_SORT_MIN_LENGTH = 8;

	public SortableGenArray(Class<T> clazz, int capacity) {
		super(clazz, capacity);
		cmp = new Comparator<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public int compare(T o1, T o2) {
				return ((Comparable<T>) o1).compareTo(o2);
			}
			
		};
	}
	
	public SortableGenArray(Class<T> clazz, int capacity, Comparator<T> cmp) {
		super(clazz, capacity);
		this.cmp = cmp;
	}
	
	public void setComparator(Comparator<T> cmp) {
		this.cmp = cmp;
	}

	public void insertionSort() {
		insertionSort(0, size()-1);
	}
	
	/**
	 * Retrieves the Kth positioned element when the array is sorted.
	 * @param k - position of element if array were sorted.
	 * @return Element [k-1] if array were sorted.
	 */
	public T getKth(int k) {
		if (k < 1 || k > size())
			throw new IllegalArgumentException("Position must be between 1 and size(). "
					+ "Note k is the position, not the index.");
		return getKth(k, 0, size()-1);
	}
	
	public T getMedian() {
		if (size() == 0)
			throw new IllegalStateException("There must be at leat 1 element.");
		@SuppressWarnings("unchecked")
		T[] tmp = (T[]) this.toArray();
		T med = getKth(size() / 2 + 1);
		array = tmp;
		return med;
	}
	
	public void quicksort() {
		quicksort(0, size()-1);
	}
	
	public boolean isSorted() {
		for (int i = 0; i < size() - 1; i++)
			if (cmp.compare(array[i], array[i+1]) > 0)
				return false;
		return true;
	}
	
	public void heapSort() {
		buildHeap();
		
		for(int i = size()-1; i > 0; i--) {
			swap(i, 0);
			heapify(0, i);
		}
	}
	
	public int binarySearch(T elem) {
		return binarySearch(elem, 0, size()-1);
	}
	
	private int binarySearch(T elem, int left, int right) {
		if (right < left)
			return -1;
		
		int mid = (left + right) / 2;
		int comp = cmp.compare(elem, array[mid]);
		
		if (comp < 0)
			return binarySearch(elem, left, mid-1);
		
		if (comp > 0)
			return binarySearch(elem, mid+1, right);
		
		return mid;
	}

	private void quicksort(int left, int right) {
		int length = right - left + 1;
		
		if (left >= right)
			return;
		
		if (length <= Q_SORT_MIN_LENGTH)
			insertionSort(left, right);
		else {
			int randIndex = rand.nextInt(right - left + 1) + left;
			int pivotIndex = partition(left, right, randIndex);
			
			quicksort(left, pivotIndex-1);
			quicksort(pivotIndex+1, right);
		}
	}
	
	private T getKth(int k, int left, int right) {
		int randIndex = rand.nextInt(right - left + 1) + left;
		int pivotIndex = partition(left, right, randIndex);
		int kIndex = left + k - 1;
		
		if (pivotIndex == kIndex)
			return get(pivotIndex);
		
		if (kIndex < pivotIndex)
			return getKth(k, left, pivotIndex-1);
		
		return getKth(kIndex - pivotIndex, pivotIndex+1, right);
	}

	private void insertionSort(int left, int right) {
		for (int i = left + 1; i <= right; i++) {
			int j;
			T elem = array[i];
			for (j = i-1; j >= 0 && cmp.compare(array[j], elem) > 0; j--)
				array[j+1] = array[j];
			array[j+1] = elem;
		}
	}
	
	private int partition(int left, int right, int pivot) {
		if (pivot < left || pivot > right)
			throw new IllegalArgumentException("Pivot must be between left and right");
		
		int low = left+1;
		int high = right;
		
		swap(left, pivot);
		
		while (low <= high) {
			if (cmp.compare(array[low], array[left]) > 0)
				swap(low, high--);
			else
				low++;
		}
		
		swap(left, high);
		return high;
	}
	
	private void swap(int i, int j) {
		if (i != j) {
			T tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}
	
	/**
	 * It begins heapify from the parent of the last node: array[size/2-1]
	 */
	private void buildHeap() {
		for (int i = size()/2-1; i >= 0; i--)
			heapify(i, size());
	}
	
	private void heapify(int i, int size) {
		int left = i*2+1;
		int right = i*2+2;
		int max;
		
		if (left < size && cmp.compare(array[left], array[i]) > 0)
			max = left;
		else
			max = i;
		
		if (right < size && cmp.compare(array[right], array[max]) > 0)
			max = right;
		
		if (max != i) {
			swap(max, i);
			heapify(max, size);
		}
	}
}