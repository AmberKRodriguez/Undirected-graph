package hellofx;
import java.util.*;


 /* Amber Rodriguez
    * CMSC 315- Data Structures and Analysis
    * Project 4 
    * 12.9.25
    * 
    * 
second class 
defines the graph. 
that allows an unlimited number of vertices and edges. It should have the following public 
methods

A method to add an edge
A method that checks whether the graph has cycles
A method to check whether the graph is connected
A method that returns a list of vertices resulting from a depth-first graph search
A method that returns a list of vertices resulting from a breadth-first graph search
*/


public interface Graph<V> {

    /** Return the number of vertices in the graph */
    int getSize();

    /** Return the vertices in the graph */
    List<V> getVertices();

    /** Return the object for the specified vertex index */
    V getVertex(int index);

    /** Return the index for the specified vertex object */
    int getIndex(V v);

    /** Return the neighbors of vertex with the specified index */
    List<Integer> getNeighbors(int index);

    /** Return the degree for a specified vertex */
    int getDegree(int v);

    /** Print the edges */
    void printEdges();

    /** Clear the graph */
    void clear();

    /** Add a vertex to the graph */
    boolean addVertex(V vertex);

    /** Add an edge (u, v) to the graph */
    boolean addEdge(int u, int v);

    /** Add an edge to the graph */
    boolean addEdge(Edge e);

    /** Remove a vertex v from the graph */
    boolean remove(V v);

    /** Remove an edge (u, v) from the graph */
    boolean remove(int u, int v);

    /** Obtain a depth-first search tree */
    SearchTree dfs(int v);

    /** Obtain a breadth-first search tree */
    SearchTree bfs(int v);


   boolean isConnected();
    boolean hasCycle();

    
    interface SearchTree {
        List<Integer> getSearchOrder();
        int getRoot();
        int getParent(int v);
  
    }
}

// Edge class
class Edge {
    public int u;
    public int v;

    public Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Edge)) return false;
        Edge other = (Edge) o;
        return this.u == other.u && this.v == other.v;
    }

    @Override
    public int hashCode() {
        return Objects.hash(u, v);  
    }
}


  class Graphs<V> implements Graph<V> {
    protected final List<V> vertices = new ArrayList<>();
    protected final List<List<Edge>> neighbors = new ArrayList<>();

    public Graphs() {}

    public Graphs(V[] vertices, int[][] edges) {
        for (V v : vertices) addVertex(v);
        createAdjacencyLists(edges);
    }

    public Graphs(List<V> vertices, List<Edge> edges) {
        for (V v : vertices) addVertex(v);
        createAdjacencyLists(edges);
    }

    private void createAdjacencyLists(int[][] edges) {
        for (int[] e : edges) addEdge(e[0], e[1]);
    }

    private void createAdjacencyLists(List<Edge> edges) {
        for (Edge e : edges) addEdge(e);
    }

    @Override
    public int getSize() {
        return vertices.size();
    }

    @Override
    public List<V> getVertices() {
        return vertices;
    }

    @Override
    public V getVertex(int index) {
        return vertices.get(index);
    }

    @Override
    public int getIndex(V v) {
        return vertices.indexOf(v);
    }

    @Override
    public List<Integer> getNeighbors(int index) {
        List<Integer> result = new ArrayList<>();
        for (Edge e : neighbors.get(index)) result.add(e.v);
        return result;
    }

    @Override
    public int getDegree(int v) {
        return neighbors.get(v).size();
    }

    @Override
    public void printEdges() {
        for (int u = 0; u < neighbors.size(); u++) {
            System.out.print(getVertex(u) + " (" + u + "): ");
            for (Edge e : neighbors.get(u)) {
                System.out.print("(" + u + ", " + e.v + ") ");
            }
            System.out.println();
        }
    }

    @Override
    public void clear() {
        vertices.clear();
        neighbors.clear();
    }

    @Override
    public boolean addVertex(V vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            neighbors.add(new ArrayList<>());
            return true;
        }
        return false;
    }

    @Override
    public boolean addEdge(Edge e) {
        int u = e.u, v = e.v;
        if (u < 0 || u >= getSize() || v < 0 || v >= getSize())
            throw new IllegalArgumentException("Invalid vertex index");

        Edge forward = new Edge(u, v);
        Edge backward = new Edge(v, u);

        boolean added = false;
          if (!neighbors.get(e.u).contains(new Edge(e.u, e.v))) {
            neighbors.get(e.u).add(new Edge(e.u, e.v));
        }
        if (!neighbors.get(e.v).contains(new Edge(e.v, e.u))) {
            neighbors.get(e.v).add(new Edge(e.v, e.u)); // add reverse
        }
        return added;
      }

    @Override
    public boolean addEdge(int u, int v) {
    if (u < 0 || u >= getSize() || v < 0 || v >= getSize())
        throw new IllegalArgumentException("Invalid vertex index");

    Edge forward = new Edge(u, v);
    Edge backward = new Edge(v, u);

    boolean added = false;
    if (!neighbors.get(u).contains(forward)) {
        neighbors.get(u).add(forward);
        added = true;
    }
    if (!neighbors.get(v).contains(backward)) {
        neighbors.get(v).add(backward);
        added = true;
    }
    return added;
}
    @Override
    public boolean remove(V v) {
        int index = getIndex(v);
        if (index == -1) return false;

     
        vertices.remove(index);
        neighbors.remove(index);

        
        for (List<Edge> list : neighbors) {
            list.removeIf(e -> e.v == index);
            for (int i = 0; i < list.size(); i++) {
                Edge e = list.get(i);
                if (e.v > index) {
                    list.set(i, new Edge(e.u, e.v - 1));
                }
                if (e.u > index) {
                    list.set(i, new Edge(e.u - 1, e.v));
                }
            }
        }
        return true;
    }

    @Override
    public boolean remove(int u, int v) {
        boolean r1 = neighbors.get(u).remove(new Edge(u, v));
        boolean r2 = neighbors.get(v).remove(new Edge(v, u));
        return r1 || r2;
    }

    @Override
    public SearchTree dfs(int v) {
        List<Integer> searchOrder = new ArrayList<>();
        int[] parent = new int[getSize()];
        Arrays.fill(parent, -1);
        boolean[] isVisited = new boolean[getSize()];
        dfsUtil(v, parent, searchOrder, isVisited);
        return new SearchTreeImpl(v, parent, searchOrder);
    }

    private void dfsUtil(int v, int[] parent, List<Integer> searchOrder, boolean[] isVisited) {
        isVisited[v] = true;
        searchOrder.add(v);
        for (Edge e : neighbors.get(v)) {
            int w = e.v;
            if (!isVisited[w]) {
                parent[w] = v;
                dfsUtil(w, parent, searchOrder, isVisited);
            }
        }
    }

    @Override
    public SearchTree bfs(int v) {
        List<Integer> searchOrder = new ArrayList<>();
        int[] parent = new int[getSize()];
        Arrays.fill(parent, -1);
        boolean[] isVisited = new boolean[getSize()];
        Queue<Integer> queue = new LinkedList<>();

        isVisited[v] = true;
        queue.offer(v);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            searchOrder.add(u);
            for (Edge e : neighbors.get(u)) {
                int w = e.v;
                if (!isVisited[w]) {
                    isVisited[w] = true;
                    parent[w] = u;
                    queue.offer(w);
                }
            }
        }
        return new SearchTreeImpl(v, parent, searchOrder);
    }

    @Override
    public boolean isConnected() {
        if (getSize() == 0) return true;
        List<Integer> order = dfs(0).getSearchOrder();
        return order.size() == getSize();
    }

    @Override
    public boolean hasCycle() {
    int n = getSize();
    if (n == 0) return false;

    boolean[] visited = new boolean[n];

    // Check all components (graph can be disconnected)
    for (int start = 0; start < n; start++) {
        if (!visited[start]) {
            if (hasCycleFrom(start, -1, visited)) {
                return true;
            }
        }
    }
        return false;
    }

    
    private boolean hasCycleFrom(int u, int parent, boolean[] visited) {
        visited[u] = true;
        for (Edge e : neighbors.get(u)) {
            int v = e.v;
            if (!visited[v]) {
                if (hasCycleFrom(v, u, visited)) return true;
            } else if (v != parent) {
                return true;
            }
        }
        return false;
    }


    public static class SearchTreeImpl implements SearchTree {
        private final int root;
        private final int[] parent;
        private final List<Integer> searchOrder;

        public SearchTreeImpl(int root, int[] parent, List<Integer> searchOrder) {
            this.root = root;
            this.parent = parent;
            this.searchOrder = searchOrder;
        }

        @Override
        public List<Integer> getSearchOrder() {
            return searchOrder;
        }

        @Override
        public int getRoot() {
            return root;
        }

        @Override
        public int getParent(int v) {
            return parent[v];
        }
    }
}