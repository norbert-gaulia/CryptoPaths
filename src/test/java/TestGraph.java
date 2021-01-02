package src.test.java;
import src.main.java.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import org.junit.Test;

public class TestGraph {
    Graph g = new Graph();
    Trades t = new Trades();
    HashMap<String, Double> priceTree = new HashMap<>();

    //addVertex
    @Test
    public void testAddVertex(){
        g.addVertex("AUD", "value");
        assertEquals("AUD", g.hasVertex("AUD").getLabel());
    }

    //addEdge
    @Test
    public void testAddEdge() {
        g.addEdge("B", "C", "BC", t);
        assertEquals("BC", g.hasEdge("BC").getLabel());
    }

    //hasVertex
    @Test
    public void testHasVertex() {
        assertEquals(null, g.hasVertex("L"));
    }

    //hasEdge
    @Test
    public void testHasEdge() {
        assertEquals(null, g.hasEdge("QR"));
    }

    //edgeCount
    @Test
    public void testEdgeCount() {
        assertEquals(0, g.getEdgeCount());
    }

    //vertexCount
    @Test
    public void testVertexCount() {
        assertEquals(0, g.getVertexCount());
    }

    //updateEdgeData
    @Test
    public void testupdateEdgeData() {
        g.addEdge("A", "B", "AB", t);
        t = new Trades("AB", "10", "90", "80", "14", 100);
        g.updateEdgeData("AB", t);
        assertEquals(t, g.hasEdge("AB").getValue());
    }

    //isAdjacent
    @Test
    public void testIsAdjacent(){
        g.addEdge("A", "B", "AB", t);
        assertEquals(true, g.isAdjacent("A", "B"));
    }


    // getTrade
    @Test
    public void testGetTrade() {
        g.addEdge("A", "B", "AB", t);
        t = new Trades("AB", "10", "90", "80", "14", 100);
        g.updateEdgeData("AB", t);
        assertEquals(t, g.getTrade("AB"));
    }

    // getPrice
    @Test
    public void testGetPriceInvalid() {
        assertEquals("NA", g.getPrice("AB"));
    }

    // getPrice
    @Test
    public void testGetPriceValid() {
        g.addEdge("A", "B", "AB", t);
        t = new Trades("AB", "10", "90", "80", "14", 100);
        g.updateEdgeData("AB", t);
        assertEquals(t.getLastPrice(priceTree), g.getPrice("AB"));
    }
}
