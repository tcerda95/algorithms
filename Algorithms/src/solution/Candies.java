package solution;

// https://www.hackerrank.com/challenges/candies

public class Candies {

	private static long solveFaster(int[] array) {
		int[] candies = new int[array.length];
		long count = 1;
		candies[0] = 1;
		
		for (int i = 1; i < candies.length; i++) {
			if (array[i] > array[i-1])
				count += (candies[i] = candies[i-1] + 1);
			else
				count += (candies[i] = 1);
		}
		
		for (int i = candies.length-2; i >= 0; i--) {
			if (array[i] > array[i+1] && candies[i] < candies[i+1]+1) {
				count += (candies[i+1]+1 - candies[i]);
				candies[i] = candies[i+1]+1;
			}
		}
		
		return count;
	}
	
	private static long solve(int[] array) {
		long[] candies = new long[array.length];
		long count = 1;
		
		candies[0] = 1;
		
		for(int i = 1; i < array.length; i++)
			count += handCandies(array, candies, i);
		
		return count;
	}
	
	private static long handCandies(int[] array, long[] candies, int i) {
		if (array[i] > array[i-1])
			return (candies[i] = candies[i-1] + 1);
		else {
			long count = 1;
			candies[i] = 1;
			
			for (int j = i-1; j >= 0 && candies[j] <= candies[j+1] && array[j] > array[j+1]; j--) {
				candies[j]++;
				count++;
			}
			
			return count;
		}
	}

	public static void main(String[] args) {		
		int[] array = new int[100000];
		
		for (int j = array.length-1; j >= 0; j--)
			array[j] = array.length-j;
		
		System.out.println(solveFaster(array));
		System.out.println(solve(array));
	}

}
