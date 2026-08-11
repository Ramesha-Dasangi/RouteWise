package com.nibm.routewise.graph;

// Represents a location (vertex) in the delivery route graph.

public class Node {

    private final String id;
    private final String name;

    public Node(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        return id.equals(((Node) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
