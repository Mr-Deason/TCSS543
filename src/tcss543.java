import java.io.BufferedReader;
import java.util.Hashtable;
import java.util.LinkedList;


public class tcss543 {

	public static void main(String[] args) {
		
		String[] inputs = null;
		if (args.length == 0) {
			inputs = new String[]{"-"};
		}else if (args.length == 1) {
			if (args[0].equals("-all")) {
				BufferedReader in = InputLib.fopen("inputs.txt");
				String line = InputLib.getLine(in);
				StringBuilder all = new StringBuilder();
				while (line != null) {
					all.append(line);
					System.out.println(line);
					line = InputLib.getLine(in);
					if (line != null) {
						all.append(';');
					}
				}
				inputs = all.toString().split(";");
			}
			else {
				inputs = new String[]{args[0]};
			}
		}else {
			System.out.println("Invalid arguments");
			return;
		}
		
		for (String input: inputs) {
			System.out.println(input);
			SimpleGraph sg = new SimpleGraph();
			GraphInput.LoadSimpleGraph(sg, input);
			
			if (input.equals("-")) {
				System.out.println("<The graph from user input>");
			}else{
				System.out.println("<The graph in " + input + ">");
			}
			int ans;
			
			SimpleGraph rg1 = cloneGraph(sg);
			long start = System.currentTimeMillis();
			ans = FordFulkerson.MaxFlow(rg1);
			long stop = System.currentTimeMillis();
			System.out.println("---Ford Fulkerson Algorithm---");
			System.out.println("Max Flow: " + ans + "    Time: " + (stop-start) + " ms");
			
			SimpleGraph rg2 = cloneGraph(sg);;
			start = System.currentTimeMillis();
			ans = ScalingMaxFlow.MaxFlow(rg2);
			stop = System.currentTimeMillis();
			System.out.println("---Scaling Max-Flow Algorithm---");
			System.out.println("Max Flow: " + ans + "    Time: " + (stop-start) + " ms");

			SimpleGraph rg3 = cloneGraph(sg);
			start = System.currentTimeMillis();
			ans = PreflowPush.MaxFlow(rg3);
			stop = System.currentTimeMillis();
			System.out.println("---Preflow Push Algorithm---");
			System.out.println("Max Flow: " + ans + "    Time: " + (stop-start) + " ms");
		}
	}

	public static SimpleGraph cloneGraph(SimpleGraph src) {
		if (src == null) {
			return null;
		}
		SimpleGraph des = new SimpleGraph();
		LinkedList<Vertex> V = src.vertexList;
		LinkedList<Edge> E = src.edgeList;
		Hashtable<String, Vertex> hash = new Hashtable();

		for (Vertex v : V) {
			Vertex vv = des.insertVertex(v.getData(), v.getName());
			hash.put((String) vv.getName(), vv);
		}
		for (Edge e : E) {
			des.insertEdge(hash.get((String) e.getFirstEndpoint().getName()),
					hash.get((String) e.getSecondEndpoint().getName()), new Integer((Integer) e.getData()), null);
		}
		return des;
	}
}
