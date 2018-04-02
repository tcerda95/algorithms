package solution;

import java.util.HashMap;
import java.util.Map;

public class SamSubString {

    private static final Map<Long, Long> values = new HashMap<>();
    private static long mod = 1000000007;
    
    private static long solve(long n, long div, boolean isLeft) {
        if (div == 0)
            return 0;
                        
        if (values.containsKey(n) && isLeft) {
        	System.out.println("Found: " + n);
            return values.get(n);
        }
        
        System.out.println(n);        
        
        long left = solve(n / 10, div / 10, true);
        long right = solve(n % div, div / 10, false);
        long result = (n + left + right) % mod;
        
        values.put(n, result);
        
        if (!isLeft)
            result -= left;
                
        return result % mod;
    }
    
    private static int countDigits(long n) {
        int digits = 1;
        
        while (n / 10 != 0) {
            digits++;
            n = n / 10;
        }
        
        return digits;
    }
    
    public static void main(String args[] ) throws Exception {
//        final Scanner in = new Scanner(System.in);
        final long n = 222;
        
        int digits = countDigits(n);
        long div = 1;
        
        for (int i = 0; i < digits - 1; i++)
            div *= 10;
        
        System.out.println(solve(n, div, true) % mod);
    }
}
