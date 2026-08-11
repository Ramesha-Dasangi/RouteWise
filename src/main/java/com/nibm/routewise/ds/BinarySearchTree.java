package com.nibm.routewise.ds;

import java.util.ArrayList;
import java.util.List;


// Custom Binary Search Tree (BST) implementation.
// Core Data Structure for package tracking and O(log n) lookup.

// @param <K> Comparable key type (e.g. Tracking ID)
// @param <V> Value type stored in node

public class BinarySearchTree<K extends Comparable<K>, V> {

    public static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> left;
        private Node<K, V> right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getLeft() {
            return left;
        }

        public Node<K, V> getRight() {
            return right;
        }
    }

    private Node<K, V> root;
    private int size = 0;

    public void insert(K key, V value) {
        root = insertRecursive(root, key, value);
    }

    private Node<K, V> insertRecursive(Node<K, V> current, K key, V value) {
        if (current == null) {
            size++;
            return new Node<>(key, value);
        }

        int cmp = key.compareTo(current.key);
        if (cmp < 0) {
            current.left = insertRecursive(current.left, key, value);
        } else if (cmp > 0) {
            current.right = insertRecursive(current.right, key, value);
        } else {
            // Key already exists, update value
            current.value = value;
        }
        return current;
    }

    public V search(K key) {
        Node<K, V> node = searchRecursive(root, key);
        return node != null ? node.value : null;
    }

    private Node<K, V> searchRecursive(Node<K, V> current, K key) {
        if (current == null || key == null) {
            return null;
        }

        int cmp = key.compareTo(current.key);
        if (cmp == 0) {
            return current;
        } else if (cmp < 0) {
            return searchRecursive(current.left, key);
        } else {
            return searchRecursive(current.right, key);
        }
    }

    public List<V> getInOrderTraversal() {
        List<V> result = new ArrayList<>();
        inOrderRecursive(root, result);
        return result;
    }

    private void inOrderRecursive(Node<K, V> node, List<V> result) {
        if (node != null) {
            inOrderRecursive(node.left, result);
            result.add(node.value);
            inOrderRecursive(node.right, result);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Node<K, V> getRoot() {
        return root;
    }

    public int getHeight() {
        return calculateHeight(root);
    }

    private int calculateHeight(Node<K, V> node) {
        if (node == null) return 0;
        return 1 + Math.max(calculateHeight(node.left), calculateHeight(node.right));
    }
}
