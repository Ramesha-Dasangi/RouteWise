package com.nibm.routewise.sorting;

import com.nibm.routewise.model.DeliveryPoint;

import java.util.List;


// Sorting utilities for ordering delivery points.
// Implemented using Insertion Sort for clarity and to demonstrate
// manual algorithm implementation (rather than relying on Collections.sort).

public class SortUtils {

    // Sorts delivery points by priority, highest priority first.
    public static void sortByPriority(List<DeliveryPoint> points) {
        for (int i = 1; i < points.size(); i++) {
            DeliveryPoint key = points.get(i);
            int j = i - 1;
            while (j >= 0 && points.get(j).getPriority() < key.getPriority()) {
                points.set(j + 1, points.get(j));
                j--;
            }
            points.set(j + 1, key);
        }
    }

    // Sorts delivery points by distance from origin, nearest first.
    public static void sortByDistance(List<DeliveryPoint> points) {
        for (int i = 1; i < points.size(); i++) {
            DeliveryPoint key = points.get(i);
            int j = i - 1;
            while (j >= 0 && points.get(j).getDistanceFromOrigin() > key.getDistanceFromOrigin()) {
                points.set(j + 1, points.get(j));
                j--;
            }
            points.set(j + 1, key);
        }
    }
}
