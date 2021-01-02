package src.main.java;

import java.io.Serializable;
import java.util.LinkedList;

// Graph vertex class
public class GraphVertex implements Serializable {

    private static final long serialVersionUID = 1L;
    private Object label = null;
    private Object value = null;
    private LinkedList<GraphVertex> links = null;
    private boolean visited = false;

    // Constructor
    public GraphVertex(Object label, Object value) {
        this.label = label;
        this.value = value;
        links = new LinkedList<GraphVertex>();
    }

    /**********************************************************************/
    // Accessors
    public Object getLabel() {
        return this.label;
    }

    public Object getValue() {
        return this.value;
    }

    public LinkedList<GraphVertex> getAdjacent() {
        return this.links;
    }

    public boolean getVisited() {
        return this.visited;
    }

    public String toString() {
        return "Asset: " + label;
    }

    /**********************************************************************/
    // Mutators
    public void addEdge(GraphVertex vertex) {
        links.add(vertex);
    }

    public void setVisited() {
        visited = true;
    }

    public void clearVisited() {
        visited = false;
    }
}