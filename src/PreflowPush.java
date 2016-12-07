import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PrimitiveIterator.OfDouble;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import sun.net.www.content.image.gif;
import sun.nio.cs.ext.ISCII91;

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

		HashMap<Vertex, Integer> ef = new HashMap<>();
		HashMap<Vertex, Integer> h = new HashMap<>();

		Vertex s = null;
		Vertex t = null;

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

		Hashtable<Object, Vertex> visited = new Hashtable();
		visited.put(s.getName(), s);

		LinkedList<Vertex> q = new LinkedList();

		// initiate
		for (Object o : s.incidentEdgeList) {
			Edge e = (Edge) o;
			Vertex w = e.getSecondEndpoint();
			q.add(w);
			int d = (Integer) e.getData();
			// ef[s] -= d
			ef.put(s, ef.get(s) - d);

			// ef[w] += d
			ef.put(w, ef.get(w) + d);
			e.setData((Integer) e.getData() - d);
			sg.insertEdge(w, s, d, null);
		}

//		while (!q.isEmpty()) {
//
//			boolean find = false;
//			int minh = Integer.MAX_VALUE;
//
//			Vertex v = q.poll();
//			visited.put(v.getName(), v);
//			while (!find && !v.getName().equals("t")) {
//				if (ef.get(v) > 0) {
//					System.out.println(v.getName());
//					for (Object o : v.incidentEdgeList) {
//						Edge e = (Edge) o;
//						Vertex w = e.getSecondEndpoint();
//						if ((Integer) e.getData() > 0) {
//							minh = Integer.min(minh, h.get(w));
//						}
//						if ((Integer) e.getData() > 0 && h.get(v) > h.get(w)) {
//							int d = Integer.min((Integer) e.getData(), ef.get(v));
//							if (d > 0) {
//								ef.put(v, ef.get(v) - d);
//								ef.put(w, ef.get(w) + d);
//								e.setData((Integer) e.getData() - d);
//								sg.insertEdge(w, v, d, null);
//							}
//							find = true;
//							if (ef.get(w) > 0 && visited.get(w.getName()) == null) {
//								q.add(w);
//								System.out.println("add " + w.getName());
//							}
//
//							// q.add(w);
//							System.out.println("push " + w.getName() + " with " + d);
//						}
//
//					}
//				}
//				if (ef.get(v) > 0 && !find) {
//					h.put(v, minh + 1);
//					// h.put(v, h.get(v) + 1);
//					System.out.println("didn't find, set h[" + v.getName() + "] to " + h.get(v));
//				} else
//					break;
//			}
//			visited.remove(v.getName());
//		}

		while (true) {
			boolean exist = false;
			for (Object o : sg.vertexList) {
				Vertex u = (Vertex) o;
				// ef[u] > 0
				if (ef.get(u) > 0 && u != s && u != t) {
					exist = true;

					while (true) {
						boolean find = false;
						int minh = Integer.MAX_VALUE;

						for (Object oo : u.incidentEdgeList) {
							Edge e = (Edge) oo;
							Vertex v = e.getSecondEndpoint();
							if ((Integer) e.getData() > 0) {
								minh = Integer.min(minh, h.get(v));
							}

							// push
							// e(u, v) > 0 and h[u] > h[v]
							if ((Integer) e.getData() > 0 && h.get(u) > h.get(v)) {
								int d = Integer.min((Integer) e.getData(), ef.get(u));
								if (d > 0) {
									ef.put(u, ef.get(u) - d);
									ef.put(v, ef.get(v) + d);
									e.setData((Integer) e.getData() - d);
									sg.insertEdge(v, u, d, null);
								}
								find = true;
							}

						}
						if (!find) {
							// relabel
							h.put(u, minh + 1);
						}
						if (ef.get(u) <= 0)
							break;
					}
				}

			}
			if (!exist) {
				break;
			}
		}
		// System.out.println(ef.get(s));
		maxflow = ef.get(t);

		return maxflow;
	}
}
