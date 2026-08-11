package com.nibm.routewise;

import com.nibm.routewise.graph.DijkstraSolver;
import com.nibm.routewise.graph.Graph;
import com.nibm.routewise.graph.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphTest {

    @Test
    public void testShortestPath() {
        Graph graph = new Graph();
        Node a = new Node("A", "A");
        Node b = new Node("B", "B");
        Node c = new Node("C", "C");

        graph.addEdge(a, b, 5);
        graph.addEdge(b, c, 3);
        graph.addEdge(a, c, 10);

        DijkstraSolver solver = new DijkstraSolver();
        DijkstraSolver.Result result = solver.findShortestPath(graph, a, c);

        assertEquals(8, result.totalDistance);
        assertEquals(3, result.path.size());
    }

    @Test
    public void testNoPathExists() {
        Graph graph = new Graph();
        Node a = new Node("A", "A");
        Node b = new Node("B", "B");
        graph.addNode(a);
        graph.addNode(b);

        DijkstraSolver solver = new DijkstraSolver();
        DijkstraSolver.Result result = solver.findShortestPath(graph, a, b);

        assertEquals(-1, result.totalDistance);
    }
}
