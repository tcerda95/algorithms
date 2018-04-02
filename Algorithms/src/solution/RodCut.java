package solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// From Cormen book, Introduction to Algorithms, Dynamic Programming Seciton.

public class RodCut {
	
	private static int maxRevenueBottomUp(List<Integer> prices, int rodSize) {
		List<Integer> optimalPrices = new ArrayList<Integer>(rodSize);
		List<String> wayToBeCut = new ArrayList<String>(rodSize);
		
		optimalPrices.add(prices.get(0));
		wayToBeCut.add("1");
		
		// i+1 represents current rod size
		for (int i = 1; i < rodSize; i++) {
			int max = prices.get(i);
			String wayToCut = Integer.toString(i+1);
			
			// evaluate optimal prices from rods size j+1 and i-1
			for (int j = 0; j <= i-j-1; j++) {
				int revenue = optimalPrices.get(j) + optimalPrices.get(i-j-1);
				if (revenue > max) {
					max = revenue;
					wayToCut = wayToBeCut.get(j) + " + " + wayToBeCut.get(i-j-1);
				}
			}
			
			optimalPrices.add(max);
			wayToBeCut.add(wayToCut);
		}
		
		System.out.println(wayToBeCut.get(rodSize-1));
		return optimalPrices.get(rodSize-1);
	}
	
	private static int maxRevenueTopDown(List<Integer> prices, int rodSize) {
		List<Integer> optimalPrices = new ArrayList<Integer>(rodSize);
		return maxRevenueTopDown(prices, rodSize, optimalPrices);
	}
	
	private static int maxRevenueTopDown(List<Integer> prices, int rodSize, List<Integer> optimalPrices) {
		if (optimalPrices.size() >= rodSize)
			return optimalPrices.get(rodSize-1);
				
		int max = prices.get(rodSize-1);
		for (int i = 1; i <= rodSize-i; i++) {
			int revenue = maxRevenueTopDown(prices, i, optimalPrices) + maxRevenueTopDown(prices, rodSize-i, optimalPrices);
			if (revenue > max)
				max = revenue;
		}
		
		optimalPrices.add(max);
		return max;
	}

	public static void main(String[] args) {
		Integer[] prices = {1, 5, 8, 9, 10, 17, 17, 20, 24, 30};
		List<Integer> p = Arrays.asList(prices);
		for (int i = 1; i <= 10; i++) {
			System.out.println(maxRevenueBottomUp(p, i) + " " + maxRevenueTopDown(p, i));
		}
	}

}
