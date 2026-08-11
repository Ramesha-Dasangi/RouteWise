package com.nibm.routewise.ds;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Custom Min-Heap Priority Queue implementation.
// Core Data Structure for Dijkstra Algorithm & Priority Routing.

// @param <T> Element type stored in heap

public class MinHeap<T> {

    private final List<T> heap;
    private final Comparator<T> comparator;

    public MinHeap(Comparator<T> comparator) {
        this.heap = new ArrayList<>();
        this.comparator = comparator;
    }

    public void insert(T element) {
        heap.add(element);
        siftUp(heap.size() - 1);
    }

    public T poll() {
        if (isEmpty()) {
            return null;
        }
        T result = heap.get(0);
        T lastElement = heap.remove(heap.size() - 1);
        if (!isEmpty()) {
            heap.set(0, lastElement);
            siftDown(0);
        }
        return result;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return heap.get(0);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public void clear() {
        heap.clear();
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (comparator.compare(heap.get(index), heap.get(parentIndex)) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private void siftDown(int index) {
        int size = heap.size();
        while (index < size) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            if (leftChild < size && comparator.compare(heap.get(leftChild), heap.get(smallest)) < 0) {
                smallest = leftChild;
            }
            if (rightChild < size && comparator.compare(heap.get(rightChild), heap.get(smallest)) < 0) {
                smallest = rightChild;
            }

            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
