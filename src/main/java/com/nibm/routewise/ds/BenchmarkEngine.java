package com.nibm.routewise.ds;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.TreeSet;

// Compares custom Data Structures vs Standard Java Collections.

public class BenchmarkEngine {

    public static class BenchmarkResult {
        public final String dsName;
        public final int sampleSize;
        public final long customTimeNs;
        public final long javaStandardTimeNs;
        public final String complexityLabel;

        public BenchmarkResult(String dsName, int sampleSize, long customTimeNs, long javaStandardTimeNs, String complexityLabel) {
            this.dsName = dsName;
            this.sampleSize = sampleSize;
            this.customTimeNs = customTimeNs;
            this.javaStandardTimeNs = javaStandardTimeNs;
            this.complexityLabel = complexityLabel;
        }
    }

    public static BenchmarkResult runHeapBenchmark(int elementsCount) {
        MinHeap<Integer> customHeap = new MinHeap<>(Integer::compareTo);
        PriorityQueue<Integer> javaPQ = new PriorityQueue<>();

        // Test Custom MinHeap
        long startCustom = System.nanoTime();
        for (int i = elementsCount; i > 0; i--) {
            customHeap.insert(i);
        }
        while (!customHeap.isEmpty()) {
            customHeap.poll();
        }
        long customTime = System.nanoTime() - startCustom;

        // Test Java PriorityQueue
        long startJava = System.nanoTime();
        for (int i = elementsCount; i > 0; i--) {
            javaPQ.add(i);
        }
        while (!javaPQ.isEmpty()) {
            javaPQ.poll();
        }
        long javaTime = System.nanoTime() - startJava;

        return new BenchmarkResult("Min-Heap (Priority Queue)", elementsCount, customTime, javaTime, "Insert: O(log n) | Poll: O(log n)");
    }

    public static BenchmarkResult runBSTBenchmark(int elementsCount) {
        BinarySearchTree<Integer, String> customBST = new BinarySearchTree<>();
        TreeSet<Integer> javaTree = new TreeSet<>();

        long startCustom = System.nanoTime();
        for (int i = 0; i < elementsCount; i++) {
            customBST.insert(i, "Package-" + i);
        }
        for (int i = 0; i < elementsCount; i++) {
            customBST.search(i);
        }
        long customTime = System.nanoTime() - startCustom;

        long startJava = System.nanoTime();
        for (int i = 0; i < elementsCount; i++) {
            javaTree.add(i);
        }
        for (int i = 0; i < elementsCount; i++) {
            javaTree.contains(i);
        }
        long javaTime = System.nanoTime() - startJava;

        return new BenchmarkResult("Binary Search Tree (BST)", elementsCount, customTime, javaTime, "Search: O(log n) | Insert: O(log n)");
    }

    public static BenchmarkResult runDoublyLinkedListBenchmark(int elementsCount) {
        DoublyLinkedList<Integer> customDLL = new DoublyLinkedList<>();
        LinkedList<Integer> javaLL = new LinkedList<>();

        long startCustom = System.nanoTime();
        for (int i = 0; i < elementsCount; i++) {
            customDLL.addLast(i);
        }
        long customTime = System.nanoTime() - startCustom;

        long startJava = System.nanoTime();
        for (int i = 0; i < elementsCount; i++) {
            javaLL.addLast(i);
        }
        long javaTime = System.nanoTime() - startJava;

        return new BenchmarkResult("Doubly Linked List", elementsCount, customTime, javaTime, "Insertion: O(1) | Navigation: O(1)");
    }
}
