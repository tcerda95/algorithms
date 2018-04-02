package solution.problemSolving;

public class MySolution {

	public static void main(String[] args) {
        ProblemSolvingGraph graph = new ProblemSolvingGraph(5, 10);
		graph.tagVertex(0, 10);
		graph.tagVertex(1, 1);
		graph.tagVertex(2, 20);
		graph.tagVertex(3, 11);
		graph.tagVertex(4, 40);
		System.out.println(graph.minSolvingDays());
	}

}
