import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.xml.internal.fastinfoset.algorithm.BooleanEncodingAlgorithm;

import javafx.scene.shape.QuadCurve;
import sun.misc.Queue;

public class FordFulkerson {
	
	public static int MaxFlow(SimpleGraph sg) {
		
		int maxflow = 0;
		int flow = 0;
		int cnt = 0;
		while ((flow = AugmentPath(sg)) > 0) {
			maxflow += flow;
		}
		return maxflow;
	}
	
	public static int AugmentPath(SimpleGraph sg) {
		int flow = 0;
		Vertex s = null;
		for (Iterator ite = sg.vertices(); ite.hasNext();) {
			Vertex v = (Vertex) ite.next();
			if (v.getName().equals("s")) {
				s = v;
				break;
			}
		}
		ArrayList list = new ArrayList();
		ArrayList flist = new ArrayList();
		ArrayList last = new ArrayList();
		ArrayList edges = new ArrayList();
		Hashtable visited = new Hashtable();
		list.add(s);
		flist.add(Integer.MAX_VALUE);
		last.add(-1);
		edges.add(new Object());
		visited.put(s.getName(), s);
		for (int i=0;i<list.size(); ++i) {
			Vertex v = (Vertex) list.get(i);
			boolean find = false;
			for (Iterator ite = v.incidentEdgeList.iterator(); ite.hasNext();) {
				Edge e = (Edge) ite.next();
				Vertex u = e.getSecondEndpoint();
				Vertex next = (Vertex) visited.get(u.getName());
				if (next == null && (Integer) e.getData() > 0) {
					System.out.println("find path from " +v.getName()+ " to "+ u.getName() + " : "+(Integer)e.getData());
					list.add(u);
					flist.add(Integer.min((Integer) flist.get(i), (Integer) e.getData()));
					edges.add(e);
					last.add(i);
					visited.put(u.getName(), u);
					if (u.getName().equals("t")) {
						flow = Integer.min((Integer) flist.get(i), (Integer) e.getData());
						find = true;
						break;
					}
				}
			}
			System.out.println("queue: " + i);
			if (find) {
				break;
			}
			
		}
		
		for (int i=list.size()-1;i!=0;) {
			int j = (Integer) last.get(i);
			Vertex u = (Vertex) list.get(i);
			Vertex v = (Vertex) list.get(j);
			Edge e = (Edge) edges.get(i);
			sg.insertEdge(u, v, flow, null);
			e.setData((Integer) e.getData() - flow);
			i = j;
		}
		System.out.println("augment path flow " + flow);
		return flow;
	}
}
