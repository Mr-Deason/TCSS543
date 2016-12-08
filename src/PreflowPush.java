import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

public class PreflowPush {

	private static String source;
	private static String sink;

	public static int MaxFlow(SimpleGraph sg, String source, String sink) {
		PreflowPush.source = source;
		PreflowPush.sink = sink;
		return MaxFlow(sg);
	}

	static public int MaxFlow(SimpleGraph sg) {
		int maxflow = 0;

		//excess flow
		HashMap<Vertex, Integer> ef = new HashMap<>();
		//height
		HashMap<Vertex, Integer> h = new HashMap<>();

		LinkedList<Vertex> q = new LinkedList();
		
		Vertex s = null;
		Vertex t = null;
		//initialization
		//init(sg, ef, h, s, t, q);

		// initiate ef[] and h[]
		for (Iterator ite = sg.vertices(); ite.hasNext();) {
			Vertex v = (Vertex) ite.next();
			ef.put(v, 0);
			if (v.getName().equals("s")) {
				s = v;
				h.put(v, sg.numVertices());
			} else {
				h.put(v, 0);
			}
			if (v.getName().equals("t")) {
				t = v;
			}
		}

		for (Object o : s.incidentEdgeList) {
			Edge e = (Edge) o;
			Vertex w = e.getSecondEndpoint();
			q.add(w);
			int d = (Integer) e.getData();
			// ef[s] -= d
			ef.put(s, ef.get(s) - d);
			// ef[w] += d
			ef.put(w, ef.get(w) + d);
			
			//update residual graph
			e.setData((Integer) e.getData() - d);
			sg.insertEdge(w, s, d, null);
		}

		//using BFS
		while (!q.isEmpty()) {

			Vertex v = q.poll();
			while (ef.get(v) > 0 && !v.getName().equals("t")) {
				boolean find = false;
				int minh = Integer.MAX_VALUE;
				for (Object o : v.incidentEdgeList) {
					Edge e = (Edge) o;
					Vertex w = e.getSecondEndpoint();
					if ((Integer) e.getData() > 0) {
						minh = Integer.min(minh, h.get(w));
					}

					//PUSH
					//if push applicable
					if ((Integer) e.getData() > 0 && h.get(v) > h.get(w)) {
						push(sg, ef, h, v, w, e);
						find = true;
						if (ef.get(w) > 0) {
							q.add(w);
						}
					}
				}
				//RELABEL
				if (ef.get(v) > 0 && !find) {
					relabel(h, v, minh);
				}
			}
		}
		maxflow = ef.get(t);

		return maxflow;
	}
	
	private static void init(SimpleGraph sg, HashMap<Vertex, Integer> ef, HashMap<Vertex, Integer> h, Vertex s, Vertex t, LinkedList<Vertex> q) {

		// initiate ef[] and h[]
		for (Iterator ite = sg.vertices(); ite.hasNext();) {
			Vertex v = (Vertex) ite.next();
			ef.put(v, 0);
			if (v.getName().equals("s")) {
				s = v;
				h.put(v, sg.numVertices());
			} else {
				h.put(v, 0);
			}
			if (v.getName().equals("t")) {
				t = v;
			}
		}

		for (Object o : s.incidentEdgeList) {
			Edge e = (Edge) o;
			Vertex w = e.getSecondEndpoint();
			q.add(w);
			int d = (Integer) e.getData();
			// ef[s] -= d
			ef.put(s, ef.get(s) - d);
			// ef[w] += d
			ef.put(w, ef.get(w) + d);
			
			//update residual graph
			e.setData((Integer) e.getData() - d);
			sg.insertEdge(w, s, d, null);
		}
	}
	
	private static void push(SimpleGraph sg, HashMap<Vertex, Integer> ef, HashMap<Vertex, Integer> h, Vertex v, Vertex w, Edge e) {

		int d = Integer.min((Integer) e.getData(), ef.get(v));
		if (d > 0) {
			
			// ef[v] -= d
			ef.put(v, ef.get(v) - d);
			// ef[w] += d
			ef.put(w, ef.get(w) + d);
			
			//update residual graph
			e.setData((Integer) e.getData() - d);
			sg.insertEdge(w, v, d, null);
		}
	}
	
	private static void relabel(HashMap<Vertex, Integer> h, Vertex v, int minh) {
		h.put(v, minh + 1);
	}
}
