package com.nibm.routewise.ds;

import java.util.ArrayList;
import java.util.List;

// Custom Circular Linked List implementation.
// Core Data Structure for recurring delivery patrol loops & driver shift cycles.

// @param <T> Element type stored in list

public class CircularLinkedList<T> {

    public static class Node<T> {
        private final T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public Node<T> getNext() {
            return next;
        }
    }

    private Node<T> head = null;
    private Node<T> tail = null;
    private int size = 0;

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
            newNode.next = head;
        } else {
            tail.next = newNode;
            tail = newNode;
            tail.next = head; // Maintains circular connection
        }
        size++;
    }

    public List<T> getLoopSequence(int count) {
        List<T> result = new ArrayList<>();
        if (head == null || count <= 0) return result;

        Node<T> current = head;
        for (int i = 0; i < count; i++) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
