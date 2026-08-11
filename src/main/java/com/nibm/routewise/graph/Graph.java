package com.nibm.routewise.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Graph data structure using an adjacency list.
// Represents the delivery location network (nodes) and routes (edges).

public class Graph {

    private final Map<String, Node> nodes = new LinkedHashMap<>();
    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();
    private final boolean directed;

    public Graph() {
        this(false); // undirected by default (roads work both ways)
    }

    public Graph(boolean directed) {
        this.directed = directed;
    }

    public void addNode(Node node) {
        nodes.putIfAbsent(node.getId(), node);
        adjacencyList.putIfAbsent(node.getId(), new ArrayList<>());
    }

    public void addEdge(Node source, Node destination, double weight) {
        addNode(source);
        addNode(destination);
        adjacencyList.get(source.getId()).add(new Edge(source, destination, weight));

        if (!directed) {
            adjacencyList.get(destination.getId()).add(new Edge(destination, source, weight));
        }
    }

    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node.getId(), Collections.emptyList());
    }

    public Collection<Node> getAllNodes() {
        return nodes.values();
    }

    public Node getNode(String id) {
        return nodes.get(id);
    }

    public boolean hasNode(String id) {
        return nodes.containsKey(id);
    }

    public int nodeCount() {
        return nodes.size();
    }
}
