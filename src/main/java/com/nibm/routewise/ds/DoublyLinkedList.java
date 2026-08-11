package com.nibm.routewise.ds;

import java.util.ArrayList;
import java.util.List;

// Custom Doubly Linked List implementation.
// Member 3 Core Data Structure for delivery route step management & undo/redo reordering.

// @param <T> Element type stored in list

public class DoublyLinkedList<T> {

    public static class Node<T> {
        private T data;
        private Node<T> prev;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public Node<T> getPrev() {
            return prev;
        }

        public Node<T> getNext() {
            return next;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size = 0;

    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public boolean remove(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    head = current.next;
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    tail = current.prev;
                }

                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void swap(int i, int j) {
        if (i < 0 || j < 0 || i >= size || j >= size || i == j) return;
        List<T> list = toArrayList();
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);

        clear();
        for (T item : list) {
            addLast(item);
        }
    }

    public List<T> toArrayList() {
        List<T> list = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Node<T> getHead() {
        return head;
    }

    public Node<T> getTail() {
        return tail;
    }
}
