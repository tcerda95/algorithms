package solution;


public class StringReduction {

	// http://stackoverflow.com/questions/8033553/stumped-on-a-java-interview-need-some-hints/8715230#8715230
	
	private static char convert(char c1, char c2) {
		if ((c1 == 'a' && c2 == 'b') || (c1 == 'b' && c2 == 'a'))
			return 'c';
		if ((c1 == 'a' && c2 == 'c') || (c1 == 'c' && c2 == 'a'))
			return 'b';
		if ((c1 == 'b' && c2 == 'c') || (c1 == 'c' && c2 == 'b'))
			return 'a';	
		
		throw new IllegalArgumentException("c1 and c2 must be different");
	}
	
	private static int stringReduction3(String str) {
		int[] frequencies = new int[3];
		
		for (int i = 0; i < str.length(); i++)
			frequencies[str.charAt(i) - 'a']++;
		
		boolean swapped = true;
		StringBuffer strB = new StringBuffer(str);
		
		while (swapped && strB.length() > 1) {
			swapped = trySwap(strB, frequencies);
			System.out.println(strB);
		}
		
		return strB.length();
	}
	
	private static boolean trySwap(StringBuffer strB, int[] frequencies) {
		char freqChar = maxFreqChar(frequencies);
		
		if (strB.charAt(0) == freqChar && strB.charAt(1) != freqChar) {
			swap(strB, 0, 1, frequencies);
			return true;
		}
		
		if (strB.charAt(strB.length()-1) == freqChar && strB.charAt(strB.length()-2) != freqChar) {
			swap(strB, strB.length()-1, strB.length()-2, frequencies);
			return true;
		}
		
		for (int i = 1; i < strB.length()-1; i++) {
			if (strB.charAt(i) == freqChar) {
				if (strB.charAt(i-1) != freqChar) {
					swap(strB, i-1, i, frequencies);
					return true;
				}
				
				if (strB.charAt(i+1) != freqChar) {
					swap(strB, i, i+1, frequencies);
					return true;
				}
			}
		}
		
		return false;
	}
	
	private static void swap(StringBuffer strB, int i, int j, int[] frequencies) {
		if (j < i) {
			int tmp = j;
			j = i;
			i = tmp;
		}
		
		char first = strB.charAt(i);
		char second = strB.charAt(j);
		char converted = convert(strB.charAt(i), strB.charAt(j));
		
		frequencies[converted-'a']++;
		frequencies[first-'a']--;
		frequencies[second-'a']--;

		strB.replace(i, j+1, Character.toString(converted));
	}	
	
	private static char maxFreqChar(int[] frequencies) {
		int max = -1;
		int index = -1;
		
		for (int i = 0; i < frequencies.length; i++) {
			if (frequencies[i] > max) {
				max = frequencies[i];
				index = i;
			}
		}
		
		return (char) (index + 'a');
	}

	
	private static int stringReductionBest(String str) {
		int[] frequencies = new int[3];
		
		for (int i = 0; i < str.length(); i++)
			frequencies[str.charAt(i) - 'a']++;
		
		for (int freq : frequencies)
			if (freq == str.length())
				return freq;
		
		int parity = 0;
		
		for (int i = 0; i < frequencies.length; i++)
			parity += frequencies[i] % 2;
		
		if (parity == 0 || parity == 3)
			return 2;
		
		return 1;
	}
	
	public static void main(String[] args) {
		String str = "abcccabcacacbacac";
		System.out.println(stringReduction3(str));
		System.out.println(stringReductionBest(str));
	}

}
