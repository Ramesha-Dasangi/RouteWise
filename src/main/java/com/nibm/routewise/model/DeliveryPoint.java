package com.nibm.routewise.model;

// Represents a delivery location with a priority level and
// distance from the origin/warehouse. Used for sorting deliveries.

public class DeliveryPoint {

    private final String name;
    private final int priority;             // 1 (low) - 5 (high)
    private final double distanceFromOrigin;

    public DeliveryPoint(String name, int priority, double distanceFromOrigin) {
        this.name = name;
        this.priority = priority;
        this.distanceFromOrigin = distanceFromOrigin;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public double getDistanceFromOrigin() {
        return distanceFromOrigin;
    }

    @Override
    public String toString() {
        return name + " (priority=" + priority + ", distance=" + distanceFromOrigin + "km)";
    }
}
