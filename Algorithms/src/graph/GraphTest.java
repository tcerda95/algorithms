package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import graph.list.AbstractGraphList;
import graph.list.DiGraphList;
import graph.list.SimpleGraphList;

public class GraphTest {

	private final static int N = 8;
	
	public static void main(String[] args) {
		DiGraphList<String> graph = new DiGraphList<>();
		
		graph.addVertex("t");
		graph.addVertex("r");
		graph.addVertex("x");
		graph.addVertex("s");
		graph.addVertex("y");
		graph.addVertex("z");

		graph.addEdge("r", "s", 5);
		graph.addEdge("r", "t", 3);
		graph.addEdge("s", "x", 6);
		graph.addEdge("s", "t", 2);
		graph.addEdge("t", "x", 7);
		graph.addEdge("t", "y", 4);
		graph.addEdge("t", "z", 2);
		graph.addEdge("x", "y", -1);
		graph.addEdge("x", "z", 1);
		graph.addEdge("y", "z", -2);

		Map<String, TracedVertex<String>> map = graph.minPathDAG("s");
		
		for (TracedVertex<String> tv : map.values())
			System.out.println(tv.getPath() + " " + tv.getDistance());

		map = graph.maxPathDAG("s");
		System.out.println();
		
		for (TracedVertex<String> tv : map.values())
			System.out.println(tv.getPath() + " " + tv.getDistance());
		
		/*
		graph.addVertex("Cálculo I");
		graph.addVertex("Cálculo II");
		graph.addVertex("Estadística I");
		graph.addVertex("Estadística II");
		graph.addVertex("Estadística III");
		graph.addVertex("Programación lineal");
		graph.addVertex("Programación no lineal");
		graph.addVertex("Programación estocástica");

		graph.addEdge("Cálculo I", "Cálculo II");
		graph.addEdge("Cálculo I", "Estadística I");
		graph.addEdge("Estadística I", "Estadística II");
		graph.addEdge("Estadística II", "Estadística III");
		graph.addEdge("Cálculo II", "Estadística III");
		graph.addEdge("Cálculo II", "Programación no lineal");
		graph.addEdge("Cálculo II", "Programación estocástica");
		graph.addEdge("Programación lineal", "Programación no lineal");
		graph.addEdge("Programación lineal", "Programación estocástica");
		graph.addEdge("Estadística III", "Programación estocástica");


		graph.addEdge("Programación estocástica", "Cálculo I");
		System.out.println(graph.hasCycle());
		*/
	}
}
