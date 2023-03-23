package LP2;

import DFS.java;
import LP2.Graph.Vertex;
import LP2.Graph.Edge;
import LP2.Graph.GraphAlgorithm;
import LP2.Graph.Factory;
import LP2.Graph.Timer;
import LP2.Graph.AdjList;

import java.util.Iterator;
import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class Euler extends GraphAlgorithm<Euler.EulerVertex> {
    static int VERBOSE = 1;
    Vertex start;
    List<Vertex> tour;

    Stack<Vertex> verticesStack;
    int INFINITY = Integer.MAX_VALUE;

    // You need this if you want to store something at each node
    static class EulerVertex implements Factory {

        EulerVertex(Vertex u) {

        }

        public EulerVertex make(Vertex u) {
            return new EulerVertex(u);
        }

    }

    // To do: function to find an Euler tour
    public Euler(Graph g, Vertex start) {
        super(g, new EulerVertex(null));
        this.start = start;

        tour = new LinkedList<>();
    }

    /*
     * To do: test if the graph is Eulerian. If the graph is not Eulerian, it prints
     * the message: "Graph is not Eulerian" and one reason why, such as
     * "inDegree = 5, outDegree = 3 at Vertex 37" or
     * "Graph is not strongly connected"
     */
    public boolean isEulerian() {

        for (int i = 0; i < g.n; i++) {
            Vertex u = g.getVertex(i + 1);
            if (u.inDegree() != u.outDegree()) {
                if (isStronglyConnected()) {
                    g.reverseGraph();
                    return true;
                }
                return false;
            }
        }
        return false;

    }

    public boolean isStronglyConnected() {
        DFS d = new DFS(g);

        if (d.stronglyConnectedComponents(g).result.size() == 1) {
            return true;
        }

        return false;
    }

    public List<Vertex> findEulerTour() {

        if (!isEulerian()) {
            return new LinkedList<Vertex>(); ;
        }

        Vertex u = g.getVertex(1);
        verticesStack = new Stack<Vertex>();
        
        verticesStack.push(u);

        while(!verticesStack.isEmpty()) {
            Vertex top = verticesStack.peek();
            int flag = 0;
            AdjList adjList = g.adj(top.getIndex());

            for(Edge e:adj.outEdges){
    
                if(e.weight != INFINITY){
                    e.weight=INFINITY;
                    verticesStack.push(e.to);
                    flag=1;
                    break;
                }
            }

            if(flag){
                continue;
            }
            else{
                tour.add(verticesStack.pop());
            }
        }
        return tour;
    }


    public static void main(String[] args) throws Exception {
        Scanner in;
        System.out.println(args.length+"test");
        if (args.length > 0) {
            in = new Scanner(new File(args[0]));
        }
         else {
            String input = "9 13 1 2 1 2 3 1 3 1 1 3 4 1 4 5 1 5 6 1 6 3 1 4 7 1 7 8 1 8 4 1 5 7 1 7 9 1 9 5 1";
            //String input = "4 4  1 2 1  2 3 1  3 4 1  4 1 1";
            in = new Scanner(input);
            System.out.println(input);
        }
        int start = 1;
        if (args.length > 1) {
            start = Integer.parseInt(args[1]);
        }

        // output can be suppressed by passing 0 as third argument
        if (args.length > 2) {
            VERBOSE = Integer.parseInt(args[2]);
        }

        Graph g = Graph.readDirectedGraph(in);
        Vertex startVertex = g.getVertex(start);
        Timer timer = new Timer();
        

        Euler euler = new Euler(g, startVertex);
        List<Vertex> tour = euler.findEulerTour();

        timer.end();
        if (VERBOSE > 0) {
            System.out.println("Output:");
            System.out.println(tour);
            System.out.println();
        }
        System.out.println(timer);

    }

    public void setVerbose(int ver) {
        VERBOSE = ver;
    }
}
