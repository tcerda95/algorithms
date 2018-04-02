package array;

public class GenArrayTest {

	public static void main(String[] args) {
		SortableGenArray<Integer> array = new SortableGenArray<>(Integer.class, 20);
		
		for (int i = 5; i > -1000; i--)
			array.add(i);
				
		array.remove(4);
		System.out.println(array);
		array.heapSort();
		System.out.println(array);
		System.out.println(array.isSorted());
		System.out.println(array.getMedian());
		System.out.println(array.binarySearch(0));
		System.out.println(array.indexOf(0));
	}

}
