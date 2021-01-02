package src.main.java;
/**
 * Adjacency list implementation of Graphs.
 */
import java.io.Serializable;
import java.util.*;
import java.io.*;

public class Graph implements Serializable {
    private static final long serialVersionUID = 1L;
    private LinkedList<GraphVertex> vertices = null;
    private LinkedList<GraphEdge> edges = null;
    
    //Constructor
    public Graph(){
        vertices = new LinkedList<>();
        edges = new LinkedList<>();
    }

    //addVertex
    public void addVertex(String label, Object value){
        GraphVertex vertex = new GraphVertex(label, value);
        if(hasVertex(label) == null){
            vertices.add(vertex);
        }
    }

    //addEdge
    public void addEdge(String source, String destination, String edgeLabel, Trades edgeValue){
        //find source & destination
        GraphVertex from = hasVertex(source), to = hasVertex(destination);

        //if source doesn't exist
        if (hasVertex(source) == null){
            from = new GraphVertex(source, "NA");
            vertices.add(from);
        }
        // if destination doesn't exist
        if (hasVertex(destination) == null){
            to = new GraphVertex(destination, "NA");
            vertices.add(to);
        }
        //add the edge
        if (hasEdge(source+""+destination) == null){
            from.addEdge(to);
            GraphEdge edge = new GraphEdge(edgeLabel, edgeValue);
            edges.add(edge);
        }
    }
    //hasVertex
    public GraphVertex hasVertex(String label){
        GraphVertex v = null;
        for (GraphVertex vertex : vertices) {
            if ((vertex.getLabel()).equals(label))
                v = vertex;
        }
        return v;
    }

    // hasEdge
    public GraphEdge hasEdge(String label) {
        GraphEdge e = null;
        for (GraphEdge edge : edges) {
            if ((edge.getLabel()).equals(label))
                e = edge;
        }
        return e;
    }

    //get number of vertices
    public int getVertexCount() {
        return vertices.size();
    }

    //get the number of edges
    public int getEdgeCount() {
        return edges.size();
    }
    
    //update the data of each edge 
    public void updateEdgeData(String symbol, Object value) {
        // find the edge
        GraphEdge edge = hasEdge(symbol);
        edge.setValue(value);
    }

    //isAdjacent
    public boolean isAdjacent(String source, String destination){
        return edges.contains(hasEdge(source + "" + destination));
    }

    // get tradeinfo
    public Trades getTrade(String label) {
        GraphEdge edge = hasEdge(label);
        return (Trades) (edge.getValue());
    }

    // get price
    public String getPrice(String label) {
        GraphEdge edge = hasEdge(label);
        HashMap<String, Double> priceTree = new HashMap<>();
        if(edge != null)
            return ((Trades) (edge.getValue())).getLastPrice(priceTree);
        else
            return "NA";
    }

    public void display(){
        LinkedList<GraphVertex> adjacentVertices = null;
        for (GraphVertex vertex : vertices) {
            UserInterface.displayMsg(vertex.toString());
            UserInterface.displayMsg("Adjacency List: ");
            UserInterface.displayMsg("----------------------");
            adjacentVertices = vertex.getAdjacent();
            for (GraphVertex adjacentVertex : adjacentVertices) {
                UserInterface.displayMsg(adjacentVertex.getLabel() + "");
            }
            UserInterface.displayMsg("----------------------");
        }
    }
    
    //Find the path b/w two vertices
    public void DFSPath(String origin, String goal, List<Path> pathList) {
        // list to hold price path
        List<String> priceList = new LinkedList<>();
        // list to hold asset path
        List<String> assetList = new LinkedList<>();
        // vertex to hold the current vertex
        GraphVertex currentVertex = hasVertex(origin);
        // vertex to hold destination vertex
        GraphVertex destination = hasVertex(goal);
        // Zero price tree
        HashMap<String, Double> zeroPrice = new HashMap<>();
        //lastprice of each trade pair
        String lastPrice = "";
        // check if graph is empty
        if (this.vertices.isEmpty())
            throw new IllegalArgumentException("Graph is empty!");
        else {
            // insert first vertex
            assetList.add((String) currentVertex.getLabel());
            currentVertex.setVisited();
            if (!currentVertex.equals(destination))
                dfsPathRecursive(currentVertex, destination, assetList, pathList, priceList, zeroPrice, lastPrice);
        }
        // clear visited - for future searches
        for (GraphVertex vertex : this.vertices) {
            if (vertex.getVisited()) {
                vertex.clearVisited();
            }
        }
    }
    
    private void dfsPathRecursive(GraphVertex currentVertex,GraphVertex destination,
            List<String> assetList, 
            List<Path> pathList, List<String> priceList, HashMap<String, Double> zeroPrice, 
            String lastPrice) {
                //current vertex is the goal
        if(currentVertex.equals(destination)){
            //new path instance
            Path currentPath = new Path(assetList, priceList);
            //add path instance to the path list
            pathList.add(currentPath);
            // delete vertex from path
            assetList.remove(currentVertex.getLabel());
            // delete edge from path
            priceList.remove(lastPrice);
            // clear goal from visited
            currentVertex.clearVisited();
        }
        else{
            for (GraphVertex adjVertex : currentVertex.getAdjacent() ) {
                //if not visited
                if(!adjVertex.getVisited()){
                    //set as visited
                    adjVertex.setVisited();
                    //add asset to path
                    assetList.add(""+adjVertex.getLabel());
                    //reetirve the edge
                    GraphEdge edge = hasEdge(currentVertex.getLabel() + "" + adjVertex.getLabel());
                    //retrieve the price
                    lastPrice = ((Trades) edge.getValue()).getLastPrice(zeroPrice);
                    // insert edge price data price path
                    priceList.add(lastPrice);
                    // recursive call to access the links of each asset
                    dfsPathRecursive(adjVertex, destination,assetList, pathList, priceList,
                            zeroPrice,lastPrice);
                    // delete vertex from path
                    assetList.remove(adjVertex.getLabel());
                    // delete edge from path
                    priceList.remove(lastPrice);
                }                    
            }
            // clear visited flag of currentVertex
            currentVertex.clearVisited();
        }
    }


    public void writeTradeOverview(String outfileName, Graph graph) {
        FileOutputStream outStream = null;
        PrintWriter writer;
        try {
            outStream = new FileOutputStream(outfileName);
            writer = new PrintWriter(outStream);
            // Object arrays to hold general over view data
            Object[] countData = new Object[2];
            Object[] volumeData = new Object[2];
            Object[] priceData = new Object[2];
            writer.println("General overview: ");
            writer.println("Largest count: " + countData[1] + ", Count: " + countData[0]);
            writer.println("Largest volume: " + volumeData[1] + ", Volume: " + volumeData[0]);
            writer.println("Largest priceChange: " + priceData[1] + ", priceChange: " + priceData[0]);
            writer.println("----------------------------------\n");
            writer.println("Individual trade pair overview: ");
            writer.println("----------------------------------");
            for (GraphEdge edge : this.edges) {
                writer.println("" + edge.getValue().toString());
                writer.println("----------------------------------");
            }
            writer.close();
            UserInterface.displayScs("The trade overview has been written to '" + outfileName + "'.");
        } catch (FileNotFoundException e) {
            UserInterface.displayError("File not found.");
        }
    }

    public void writeAssetOverview(String outfileName, Graph graph) {
        FileOutputStream outStream = null;
        PrintWriter writer;
        List<GraphVertex> adjacentVertices = null;
        GraphEdge edge = null;
        String baseAsset, quoteAsset;
        try {
            // open a stream
            outStream = new FileOutputStream(outfileName);
            writer = new PrintWriter(outStream);
            // loop through all vertices
            for (GraphVertex vertex : this.vertices) {
                writer.println("" + vertex);
                adjacentVertices = (vertex).getAdjacent();
                writer.println("Connections: ");
                for (GraphVertex adjacentVertex : adjacentVertices) {
                    // get the edge between the current vertex and the current adjacent vertex
                    baseAsset = (String) (vertex).getLabel();
                    quoteAsset = (String) (adjacentVertex).getLabel();
                    double price = 0;
                    try {
                        edge = hasEdge(baseAsset + "" + quoteAsset);
                        HashMap<String, Double> t = new HashMap<>();
                        price = Double.parseDouble(((Trades) edge.getValue()).getLastPrice(t));
                    } catch (Exception e) {}
                    writer.println(" " + adjacentVertex + ", lastPrice: " + price);
                }
                writer.println("----------------------------------");

            }
            writer.close();
            UserInterface.displayScs("The asset overview has been written to '" + outfileName + "'.");
        } catch (FileNotFoundException e) {
            UserInterface.displayError("File not found.");
        }
    }
}

