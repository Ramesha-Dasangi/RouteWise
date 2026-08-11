package com.nibm.routewise.graph;

import com.nibm.routewise.ds.MinHeap;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Implements Dijkstra's Shortest Path Algorithm using Member 1's custom MinHeap data structure.
// Separates physical road distance (km) from traffic-weighted travel time (mins).

public class DijkstraSolver {

    public static class Result {
        public final List<Node> path;
        public final double totalDistance;
        public final double estimatedTimeMins;
        public final List<String> stepInstructions;

        public Result(List<Node> path, double totalDistance, double estimatedTimeMins, List<String> stepInstructions) {
            this.path = path;
            this.totalDistance = totalDistance;
            this.estimatedTimeMins = estimatedTimeMins;
            this.stepInstructions = stepInstructions;
        }
    }

    public Result findShortestPath(Graph graph, Node start, Node end) {
        Map<String, Double> distances = new HashMap<>();
        Map<String, Node> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (Node node : graph.getAllNodes()) {
            distances.put(node.getId(), Double.MAX_VALUE);
        }
        distances.put(start.getId(), 0.0);

        // Uses our custom MinHeap Data Structure!
        MinHeap<Node> heap = new MinHeap<>(
                Comparator.comparingDouble(n -> distances.getOrDefault(n.getId(), Double.MAX_VALUE)));

        heap.insert(start);

        while (!heap.isEmpty()) {
            Node current = heap.poll();

            if (!visited.add(current.getId())) {
                continue; // already processed
            }
            if (current.getId().equals(end.getId())) {
                break; // reached destination
            }

            for (Edge edge : graph.getEdges(current)) {
                double newDist = distances.get(current.getId()) + edge.getWeight();
                Node neighbour = edge.getDestination();

                if (newDist < distances.getOrDefault(neighbour.getId(), Double.MAX_VALUE)) {
                    distances.put(neighbour.getId(), newDist);
                    previous.put(neighbour.getId(), current);
                    heap.insert(neighbour);
                }
            }
        }

        double distanceCost = distances.getOrDefault(end.getId(), Double.MAX_VALUE);
        List<Node> path = new LinkedList<>();

        if (distanceCost == Double.MAX_VALUE) {
            return new Result(path, -1, -1, List.of("No path exists between " + start.getName() + " and " + end.getName()));
        }

        Node step = end;
        while (step != null) {
            path.add(0, step);
            step = previous.get(step.getId());
        }

        // Calculate actual physical distance (km) and traffic-weighted travel time (mins)
        double realDistanceKm = 0.0;
        double weightedCostSum = 0.0;
        List<String> steps = new LinkedList<>();

        for (int i = 0; i < path.size() - 1; i++) {
            Node from = path.get(i);
            Node to = path.get(i + 1);
            double legDist = 0;
            for (Edge e : graph.getEdges(from)) {
                if (e.getDestination().equals(to)) {
                    legDist = e.getDistanceKm();
                    realDistanceKm += legDist;
                    weightedCostSum += e.getWeight();
                    break;
                }
            }
            steps.add("Depart " + from.getName() + " ➔ Head to " + to.getName() + " (" + String.format("%.1f", legDist) + " km)");
        }

        // Travel time based on weighted cost (35 km/h avg urban speed)
        double estimatedMins = (weightedCostSum / 35.0) * 60.0;

        return new Result(path, realDistanceKm, estimatedMins, steps);
    }
}
