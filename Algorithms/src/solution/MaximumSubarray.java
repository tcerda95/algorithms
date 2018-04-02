package solution;

import java.util.Random;

public class MaximumSubarray {

	private static int bruteForce(int[] array) {
		int max = Integer.MIN_VALUE;
		
		for (int i = 0; i < array.length; i++) {
			int current = 0;
			for (int j = i; j < array.length; j++) {
				current += array[j];
				if (current > max)
					max = current;
			}
		}
		
		return max;
	}
		
	private static int divideAndConquer(int[] array) {
		return divideAndConquer(array, 0, array.length-1);
	}
	
	private static int divideAndConquer(int[] array, int low, int high) {
		if (low == high)
			return array[low];
		
		int mid = (low + high) / 2;
		
		int leftMaxSub = divideAndConquer(array, low, mid);
		int crossMaxSub = maxCrossSub(array, low, mid, high);
		int rightMaxSub = divideAndConquer(array, mid+1, high);
		
		int max = leftMaxSub > crossMaxSub ? leftMaxSub : crossMaxSub;
		
		return max > rightMaxSub ? max : rightMaxSub;
	}

	private static int maxCrossSub(int[] array, int low, int mid, int high) {
		int maxLeft = Integer.MIN_VALUE;
		int currentLeft = 0;
		
		for (int i = mid; i >= low; i--) {
			currentLeft += array[i];
			if (maxLeft < currentLeft)
				maxLeft = currentLeft;
		}
		
		int maxRight = Integer.MIN_VALUE;
		int currentRight = 0;
		
		for (int i = mid+1; i <= high; i++) {
			currentRight += array[i];
			if (maxRight < currentRight)
				maxRight = currentRight;
		}
		
		return maxLeft + maxRight;
	}

	private static int dynamicProgrammingMaxSubArray(int[] array) {
		int maxSub = array[0];
		int[] maxSubIncluding = new int[array.length];
		
		maxSubIncluding[0] = array[0];
		
		for (int i = 1; i < array.length; i++) {
			int currentSub = array[i];
			
			if (maxSubIncluding[i-1] > 0)
				currentSub += maxSubIncluding[i-1];
						
			if (currentSub > maxSub)
				maxSub = currentSub;
			
			maxSubIncluding[i] = currentSub;
		}
		
		return maxSub;
	}
	
	public static void main(String[] args) {
		int size = 200000;
		int bound = 100000;
		Random rand = new Random();
		
		int[] array = new int[size];
		
		for (int i = 0; i < array.length; i++)
			array[i] = rand.nextInt(bound) - bound/2;
		
		System.out.println(dynamicProgrammingMaxSubArray(array));
		System.out.println(divideAndConquer(array));
		//System.out.println(bruteForce(array));
	}

}
