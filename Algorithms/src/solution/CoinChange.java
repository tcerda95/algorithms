package solution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CoinChange {

	private static class ChangeCalculator {
		private List<Integer> coins;
		private List<Integer> skipCoins;
		private int[] dealings;
		private int value;
		
		public ChangeCalculator(int value, List<Integer> coins) {
			this.value = value;
			this.dealings = new int[value+1]; // [0,value]
			this.coins = coins;
			this.skipCoins = new ArrayList<>(coins.size());
		}
		
		public int solve() {
			dealings[0] = 1;
			
			for(int i = 1; i <= value; i++)
				dealings[i] = calculate(i);
			
			for (int i : dealings)
				System.out.println(i);
			
			return dealings[value];
		}
		
		private int calculate(int num) {
			int count = 0;
			
			for(int coin : coins) {
				if (num >= coin && !skipCoins.contains(coin)) {
					int dif = num-coin;
					
					if (coins.contains(dif)) {
						skipCoins.add(dif);
						count += Integer.max(dealings[dif], dealings[coin]);
					}
					else {
						count += dealings[dif];
					}
				}
			}
			
			skipCoins.clear();
			return count;
		}
	}
	
	public static void main(String[] args) {
		List<Integer> coins = new LinkedList<>();
		coins.add(1);
		coins.add(2);
		coins.add(3);
		
		ChangeCalculator calcu = new ChangeCalculator(4, coins);
		
		System.out.println(calcu.solve());
	}
}
