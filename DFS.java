/** Starter code for SP5
 *  @author Chetan Siddappareddy  (CXS210033)
 *  @author Hari Alekya  (AxH210042)
 */

package LP2;

import LP2.Graph.Edge;
import LP2.Graph.Factory;
import LP2.Graph.Vertex;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
public class DFS extends Graph.GraphAlgorithm<DFS.DFSVertex> {
    private int component_num; // Component no of the DFS graph
    private int V_Num; // V_num = No of Vertices = |V|

    private LinkedList<Graph.Vertex> finishList; // to store topological ordering
    private boolean isCyclic;

    public static class DFSVertex implements Factory {
        private int component_num; // Component no of this node
        private boolean mark; // flag to check if the node is seen or not

        private int orderNum; // the order number in which we visit this node
        private Vertex parent; // parent of this node
        // default constructor
        public DFSVertex(Vertex u) {
            mark = false; // unvisted vertex
            setParent(null);
            orderNum = 0;
            component_num = 0;
        }

        public DFSVertex make(Vertex u) {
            return new DFSVertex(u);
        }

        // parent getter
        public Vertex getParent() {
            return parent;
        }

        // parent setter
        public void setParent(Vertex parent) {
            this.parent = parent;
        }

    }

    // default constructor
    // NOTE: setting it private because we don't need to make a public handle
    // for some methods (like connectedComponents()) which need to run DFS before
    // they are called.
    private DFS(Graph g) {
        super(g, new DFSVertex(null));
        V_Num = g.size();
        finishList = new LinkedList<>();
        component_num = 0;
        setCyclic(false);
    }

    /**
     * Find strongly connected components of a DIRECTED graph.
     * @param g the input graph
     * @return the DFS object containing with updated relevant attributes
     */
    public static DFS stronglyConnectedComponents(Graph g) {
        DFS SCC = new DFS(g);
        SCC.dfs(g);
        List<Vertex> opOrder = SCC.finishList;
        g.reverseGraph();
        SCC.dfs(opOrder);
        g.reverseGraph();

        return SCC;
    }

    /**
     * Run Depth-First-Search algorithm on Graph g using method 1
     * @param g the graph as input
     * @return DFS object
     */
    public static DFS depthFirstSearch(Graph g) {
        DFS d = new DFS(g);
        d.dfs(g);

        return d;
    }

    /**
     * Runs Depth-First-Search on g as well as finishList, in a Generic way.
     * Keeping all other valid usages from SP08.
     *
     * @param iterable the Iterable collection of type Vertex
     */
    private void dfs(Iterable<Vertex> iterable) {
        // No of Vertices in g
        V_Num = g.size();

        for (Vertex u: g) {
            get(u).mark = false; // SETTING EVERY NODE TO BE UNVISITED.
            get(u).setParent(null);// IF NONE IS VISITED NO ONE IS PARENT.
        }

        finishList = new LinkedList<>();
        component_num = 0; // NOTE: class variable

        for (Vertex u: iterable) {

            //if (get(u).vColor == DFSVertex.Color.WHITE) {
            if (!get(u).mark) { // IF THE NODE IS VISITED ALREADY
                component_num++;
                dfsVisit(u);
            }
        }
    }

    /**
     * Helper method which recursively visits the nodes in DFS manner
     * @param u giving a visit to node 'u'
     */
    private void dfsVisit(Vertex u) {
        get(u).mark = true;
        get(u).component_num = component_num; // updating using class variable


        for (Edge e: g.incident(u)) {
            Vertex v = e.otherEnd(u); //if the vertex u is the head of the arc, then return the tail else return the head


            if (!get(v).mark) {
                // Visited the node which is 'UnVisited'
                get(v).setParent(u);
                dfsVisit(v);
            }

            else if (get(u).orderNum > get(v).orderNum) {
                // Visited the node which is in 'Visiting'

                setCyclic(true); // SHOULD NEVER THROW AN EXCEPTION. WORKING SILENTLY.
            }


            else {
                // Visited the node which was 'Visited'

            }
        }
        // top: the number of VISITED node.
        // E.g. if u.top = |V| u is the first node which was done in DFS algorithm
        get(u).orderNum = V_Num--;
        finishList.addFirst(u); // same as finishList.addFirst(u)
        // DEFINE finishList as LinkedList (not a List)

    }


    /**
     * Method to update finishList which stores the nodes visited
     * in increasing order of inDegrees (using method 2) FROM CLASS NOTES
     * @param g the input graph
     */
    private void topologicalOrdering2(Graph g) {

    }

    /**
     * Find the number of connected components of a DAG by running DFS.
     *
     * Enter the component number of each vertex u in u.cno.
     * Note that the graph g is available as a class field via GraphAlgorithm.
     * @return total number of components
     */
    public int connectedComponents() {
        return component_num;
    }

    /**
     * Precondition: Run strongly connected components, or
     * connected components algorithm.
     *
     * Returns Component no of vertex u.
     * @param u the vertex
     * @return component number of vertex u
     */
    public int cno(Vertex u) {
        return get(u).component_num;
    }

    /**
     * Find topological oder of a DAG using DFS method 1.
     * @param g the input graph
     * @return the List of vertices in order of DFS, if not DAG return null
     */
    public static List<Vertex> topologicalOrder1(Graph g) {
        DFS d = depthFirstSearch(g);
        // Ran DFS and then giving the order
        return d.topologicalOrder1();
    }

    // Member function to find topological order using method 1
    public List<Vertex> topologicalOrder1() {
        // When graph is not a DAG
        if (isCyclic())
            return null;

        return this.finishList;
    }

    /*
     * Find topological order of a DAG using the second algorithm.
     * @param g the input graph
     * @return the List of vertices in order of DFS, if not DAG return null
     */



    // Can be queried for a graph which was supposed to be DAG
    public boolean isCyclic() {
        return isCyclic;
    }

    // setter method
    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
    }

    // --------------------------- MAIN METHOD -------------------------------
    public static void main(String[] args) throws Exception {
        //String string = "7 6   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   6 7 1 0";

        // Graph from Class Notes
        String string = "11 17   1 11 1   2 3 1   2 7 1   3 10 1   4 1 1   4 9 1   5 4 1   "+
                "5 7 1   5 8 1   6 3 1   7 8 1   8 2 1   9 11 1   10 6 1   11 3 1   11 4 1   11 6 1 0";

        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

        // Read graph from input
        Graph g = Graph.readDirectedGraph(in);

        // ------------------- Strongly Connected Components (SP10: Task 1) -----------------
        DFS dSCC = stronglyConnectedComponents(g);
        int numSCC = dSCC.connectedComponents();

        System.out.println("# SP10 - STRONGLY CONNECTED COMPONENTS: ");
        g.printGraph(false);
        System.out.println("Number of components: " + numSCC + "\nu\tcno");
        for (Vertex u: g) {
            System.out.println(u + "\t" + dSCC.cno(u));
        }

        // --------------------------- NEW GRAPH (DAG) --------------------------------------
//		string = "10 12   1 3 1   1 8 1   6 8 1   6 10 1   3 2 1   8 2 1   8 5 1   5 10 1   "+
//				"2 4 1   5 4 1   4 7 1   10 9 1 0";
        string = "7 8   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   5 1 7   6 7 1   7 6 1 0";
//		string="4 3   1 2 2   2 3 2   3 4 1  4 1 0";
//		string = "5 5  2 1 1  1 3 3  3 2 2  1 4 4  4 5 5";

        in = new Scanner(string);

        // Read graph from input
        g = Graph.readDirectedGraph(in);

        System.out.println("\n# SP08 - CONNECTED COMPONENTS AND TOPOLOGICAL ORDERINGS: ");

        // ----------------------- Connected Components (SP08: Task 3) ----------------------
        DFS d = depthFirstSearch(g);
        int numcc = d.connectedComponents();


        g.printGraph(false);

        System.out.println("Number of components: " + numcc + "\nu\tcno");
        for (Vertex u: g) {
            System.out.println(u + "\t" + d.cno(u));
        }

        // ----------------------- Topological Order 1 (SP08: Task 1) -----------------------
        List<Vertex> tOrder = topologicalOrder1(g);

        System.out.println("\nTopological Ordering 1: ");

        for (Vertex u: tOrder) {
            System.out.print(u + " ");
        }
        System.out.println();

    }
}