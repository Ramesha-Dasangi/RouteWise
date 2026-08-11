package com.nibm.routewise.graph;

// Represents a weighted connection (road/route) between two locations.
// Includes base distance (km) and dynamic traffic multiplier.

public class Edge {

    private final Node source;
    private final Node destination;
    private final double distanceKm;
    private double trafficMultiplier; // 1.0 = Normal, 1.5 = Moderate, 2.0+ = Heavy Traffic

    public Edge(Node source, Node destination, double distanceKm) {
        this(source, destination, distanceKm, 1.0);
    }

    public Edge(Node source, Node destination, double distanceKm, double trafficMultiplier) {
        this.source = source;
        this.destination = destination;
        this.distanceKm = distanceKm;
        this.trafficMultiplier = trafficMultiplier;
    }

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double getTrafficMultiplier() {
        return trafficMultiplier;
    }

    public void setTrafficMultiplier(double trafficMultiplier) {
        this.trafficMultiplier = trafficMultiplier;
    }

//     Effective weight = Distance (km) * Traffic Multiplier

    public double getWeight() {
        return distanceKm * trafficMultiplier;
    }
}
