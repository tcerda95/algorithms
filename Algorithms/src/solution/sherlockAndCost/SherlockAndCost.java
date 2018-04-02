package solution.sherlockAndCost;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// https://www.hackerrank.com/challenges/sherlock-and-cost

public class SherlockAndCost {

	private static int solve(int[] array) {
		int len = array.length;
		int[] cachedVal = new int[len];
		int[] cachedOne = new int[len];
		
		cachedVal[0] = 0; // Not necessary to cache whole array; last values would suffice.
		cachedOne[0] = 0;
		
		for (int i = 1; i < len; i++) {
			int absDif = Math.abs(array[i] - array[i-1]);
			
			cachedOne[i] = Integer.max(cachedVal[i-1] + (array[i-1] - 1), cachedOne[i-1]);
			cachedVal[i] = Integer.max(cachedOne[i-1] + (array[i] - 1), cachedVal[i-1] + absDif);
		}
		
		return Integer.max(cachedVal[len-1], cachedOne[len-1]);
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("src/solution/sherlockAndCost/TestCase.txt"));
        
        int t = in.nextInt();
        while (t-- > 0) {
            int len = in.nextInt();
            int[] array = new int[len];
            for (int i = 0; i < array.length; i++)
                array[i] = in.nextInt();
            System.out.println(solve(array));
        }
        
        in.close();
	}

}
