package sak170006;

/** Starter code for SP8
 *  @author 
 */

// change to your netid

import rbk.Graph.Vertex;
import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;
import rbk.Graph.Timer;

import java.io.File;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
	List<Vertex> finish_list = new ArrayList<Vertex>();;

	public static class DFSVertex implements Factory {
		int cno;
		boolean seen;
		Vertex parent;
		static boolean cycle;
		boolean black;

		public DFSVertex(Vertex u) {
			seen = false;
			parent = null;
			cno = 0;
			cycle = false;
			black = false;
		}

		public DFSVertex make(Vertex u) {
			return new DFSVertex(u);
		}
	}

	public DFS(Graph g) {
		super(g, new DFSVertex(null));
	}

	// check later
	public void dfs(Graph g) {
		int old_no = 0;
		for (Vertex u : g) {
			if (!get(u).seen) {
				dfsVisit(u, (get(u).cno) + old_no + 1);
				old_no = get(u).cno;

			}
		}
	}

	private void dfsVisit(Vertex u, int cno) {
		get(u).seen = true;
		get(u).cno = cno;
		for (Edge e : g.incident(u)) {
			if (get(e.otherEnd(u)).seen && !get(e.otherEnd(u)).black) {
				get(u).cycle = true;
			} else {
				get(e.otherEnd(u)).parent = u;
				dfsVisit(e.otherEnd(u), cno);
			}

		}
		get(u).black = true;
		finish_list.add(u);

	}

	public static DFS depthFirstSearch(Graph g) {

		DFS d = new DFS(g);
		d.dfs(g);

		return d;
	}

	// Member function to find topological order
	public List<Vertex> topologicalOrder1() {
		return finish_list;
	}

	// Find the number of connected components of the graph g by running dfs.
	// Enter the component number of each vertex u in u.cno.
	// Note that the graph g is available as a class field via GraphAlgorithm.
	public int connectedComponents() {
		int max = 0;
		for (Vertex u : g) {
			if (cno(u) > max) {
				max = cno(u);
			}
		}
		return max;
	}

	// After running the onnected components algorithm, the component no of each
	// vertex can be queried.
	public int cno(Vertex u) {
		return get(u).cno;
	}

	// Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
	public static List<Vertex> topologicalOrder1(Graph g) {
		DFS d = new DFS(g);
		d.dfs(g);
		if (DFS.DFSVertex.cycle) {
			return null;
		} else {
			return d.topologicalOrder1();
		}

	}

	// Find topological oder of a DAG using the second algorithm. Returns null if g
	// is not a DAG.
	public static List<Vertex> topologicalOrder2(Graph g) {
		return null;
	}

	public static void main(String[] args) throws Exception {
		String string = "5 4   1 2 2     2 4 5   3 4 4   4 5 1    0";
		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

		// Read graph from input
		Graph g = Graph.readGraph(in);
		g.printGraph(false);

		// DFS d = new DFS(g);
		DFS d = depthFirstSearch(g);
		int numcc = d.connectedComponents();
		System.out.println("Number of components: " + numcc + "\nu\tcno");
		for (Vertex u : g) {
			System.out.println(u + "\t" + d.cno(u));
		}
		String string1 = "5 4   1 2 2     2 4 5   3 4 4   4 5 1    0";
		in = new Scanner(string1);
		Graph g1 = Graph.readDirectedGraph(in);
		List topological_order = DFS.topologicalOrder1(g1);
		g1.printGraph(false);
		if (topological_order == null) {
			System.out.println("Cycle detected");
		} else {
			// Generate an iterator. Start just after the last element.
			ListIterator li = topological_order.listIterator(topological_order.size());

			// Iterate in reverse.
			System.out.println("Topological Order:");
			while (li.hasPrevious()) {

				System.out.print(li.previous());
			}
		}
	}

}
