package graph.intArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class GraphArrayTest {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("src/graph/intArray/JeaneTest.txt"));

		int N = in.nextInt();
        int nCities = in.nextInt();
        
        Set<Integer> cities = new HashSet<>();
        
        for (int i = 0; i < nCities; i++)
            cities.add(in.nextInt()-1);
        
        IntGraphArray graph = new IntGraphArray(N);
        
        for (int i = 0; i < N-1; i++)
            graph.addEdge(in.nextInt()-1, in.nextInt()-1, in.nextInt());
        
        in.close();
        System.out.println(graph.jeanieRoute(cities));
	}

}
