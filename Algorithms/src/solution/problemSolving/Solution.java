package solution.problemSolving;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Solution {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("src/solution/problemSolving/MyTestCase.txt"));
		Scanner outputIn = new Scanner(new File("src/solution/problemSolving/output.txt"));
		
        int t = in.nextInt();
        
        for(int a0 = 0; a0 < t; a0++) {
            int n = in.nextInt();
            int k = in.nextInt();
            
            ProblemSolvingGraph graph = new ProblemSolvingGraph(n, k);
            
            for(int a1 = 0; a1 < n; a1++){
                int x = in.nextInt();
                graph.tagVertex(a1, x);
            }
            
            int days = graph.minSolvingDays();
            System.out.println(days);
            //int expectedOutput = outputIn.nextInt();
            
            //if (days != expectedOutput)
            	//System.out.println("Ans: " + days + " Expected: " + expectedOutput + " Testcase " + n + " " + k);
        }
        
        in.close();
        outputIn.close();
	}
}
