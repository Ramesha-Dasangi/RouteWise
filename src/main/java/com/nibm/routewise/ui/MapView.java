package com.nibm.routewise.ui;

import com.nibm.routewise.graph.Edge;
import com.nibm.routewise.graph.Graph;
import com.nibm.routewise.graph.Node;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Modern visual canvas map rendering nodes, road networks with traffic colors,
// highlighted optimal path, and smooth animated vehicle simulation.

public class MapView {

    private final Canvas canvas;
    private final Map<String, double[]> positions = new HashMap<>();
    private AnimationTimer animationTimer;
    private double animationProgress = 0.0;
    private List<Node> currentPath = null;
    private Graph currentGraph = null;
    private boolean isDarkMode = true;

    public MapView(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setPosition(String nodeId, double x, double y) {
        positions.put(nodeId, new double[]{x, y});
    }

    public void setDarkMode(boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
        if (currentGraph != null) {
            draw(currentGraph, currentPath);
        }
    }

    public void draw(Graph graph, List<Node> highlightPath) {
        this.currentGraph = graph;
        this.currentPath = highlightPath;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        renderBaseMap(gc, graph, highlightPath);

        // Start vehicle animation if path is found
        if (highlightPath != null && highlightPath.size() > 1) {
            startVehicleAnimation();
        } else {
            stopVehicleAnimation();
        }
    }

    private void renderBaseMap(GraphicsContext gc, Graph graph, List<Node> highlightPath) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        // 1. Draw Map Background with Grid
        if (isDarkMode) {
            gc.setFill(Color.web("#0f172a")); // Dark Slate
        } else {
            gc.setFill(Color.web("#f8fafc")); // Soft Light Slate
        }
        gc.fillRect(0, 0, w, h);

        // Subtle Map Grid lines
        gc.setStroke(isDarkMode ? Color.web("#1e293b") : Color.web("#e2e8f0"));
        gc.setLineWidth(1);
        for (double x = 0; x < w; x += 40) {
            gc.strokeLine(x, 0, x, h);
        }
        for (double y = 0; y < h; y += 40) {
            gc.strokeLine(0, y, w, y);
        }

        // 2. Draw Edges (Roads)
        for (Node node : graph.getAllNodes()) {
            double[] p1 = positions.get(node.getId());
            if (p1 == null) continue;

            for (Edge edge : graph.getEdges(node)) {
                double[] p2 = positions.get(edge.getDestination().getId());
                if (p2 == null) continue;

                // Color code by traffic
                double mult = edge.getTrafficMultiplier();
                Color roadColor;
                if (mult >= 2.0) {
                    roadColor = Color.web("#ef4444"); // Heavy Traffic - Red
                } else if (mult > 1.2) {
                    roadColor = Color.web("#f59e0b"); // Moderate Traffic - Amber
                } else {
                    roadColor = isDarkMode ? Color.web("#334155") : Color.web("#cbd5e1"); // Normal
                }

                gc.setStroke(roadColor);
                gc.setLineWidth(3);
                gc.strokeLine(p1[0], p1[1], p2[0], p2[1]);

                // Distance text label
                double midX = (p1[0] + p2[0]) / 2;
                double midY = (p1[1] + p2[1]) / 2;
                gc.setFill(isDarkMode ? Color.web("#94a3b8") : Color.web("#64748b"));
                gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
                gc.fillText(String.format("%.0fkm", edge.getDistanceKm()), midX - 10, midY - 6);
            }
        }

        // 3. Draw Highlighted Route Path
        if (highlightPath != null && highlightPath.size() > 1) {
            gc.setStroke(Color.web("#6366f1")); // Vibrant Indigo Accent
            gc.setLineWidth(6);
            for (int i = 0; i < highlightPath.size() - 1; i++) {
                double[] p1 = positions.get(highlightPath.get(i).getId());
                double[] p2 = positions.get(highlightPath.get(i + 1).getId());
                if (p1 != null && p2 != null) {
                    gc.strokeLine(p1[0], p1[1], p2[0], p2[1]);
                }
            }
        }

        // 4. Draw Location Nodes
        for (Node node : graph.getAllNodes()) {
            double[] p = positions.get(node.getId());
            if (p == null) continue;

            boolean isOnPath = highlightPath != null && highlightPath.contains(node);
            boolean isStart = highlightPath != null && highlightPath.size() > 0 && highlightPath.get(0).equals(node);
            boolean isEnd = highlightPath != null && highlightPath.size() > 1 && highlightPath.get(highlightPath.size() - 1).equals(node);

            // Node Circle
            double radius = isOnPath ? 16 : 13;
            Color circleColor;
            if (isStart) {
                circleColor = Color.web("#10b981"); // Green Start
            } else if (isEnd) {
                circleColor = Color.web("#ec4899"); // Pink/Red End
            } else if (isOnPath) {
                circleColor = Color.web("#6366f1"); // Indigo Path
            } else {
                circleColor = isDarkMode ? Color.web("#1e293b") : Color.web("#64748b");
            }

            gc.setFill(circleColor);
            gc.fillOval(p[0] - radius, p[1] - radius, radius * 2, radius * 2);

            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeOval(p[0] - radius, p[1] - radius, radius * 2, radius * 2);

            // Location Name Label Badge
            gc.setFill(isDarkMode ? Color.web("#f8fafc") : Color.web("#0f172a"));
            gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
            gc.fillText(node.getName(), p[0] - 22, p[1] + radius + 15);
        }
    }

    private void startVehicleAnimation() {
        stopVehicleAnimation();
        animationProgress = 0.0;

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (currentPath == null || currentPath.size() < 2) return;

                animationProgress += 0.005; // speed
                if (animationProgress > 1.0) {
                    animationProgress = 0.0; // Loop vehicle animation
                }

                GraphicsContext gc = canvas.getGraphicsContext2D();
                renderBaseMap(gc, currentGraph, currentPath);

                // Draw moving delivery truck
                double totalSegments = currentPath.size() - 1;
                double scaledProgress = animationProgress * totalSegments;
                int currentSegment = (int) scaledProgress;
                if (currentSegment >= totalSegments) currentSegment = (int) totalSegments - 1;
                double segmentFraction = scaledProgress - currentSegment;

                Node startNode = currentPath.get(currentSegment);
                Node endNode = currentPath.get(currentSegment + 1);

                double[] p1 = positions.get(startNode.getId());
                double[] p2 = positions.get(endNode.getId());

                if (p1 != null && p2 != null) {
                    double truckX = p1[0] + (p2[0] - p1[0]) * segmentFraction;
                    double truckY = p1[1] + (p2[1] - p1[1]) * segmentFraction;

                    // Glowing Delivery Vehicle Marker
                    gc.setFill(Color.web("#fbbf24")); // Amber Yellow Glow
                    gc.fillOval(truckX - 10, truckY - 10, 20, 20);

                    gc.setFill(Color.BLACK);
                    gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
                    gc.fillText("🚚", truckX - 7, truckY + 4);
                }
            }
        };
        animationTimer.start();
    }

    public void stopVehicleAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
            animationTimer = null;
        }
    }
}
