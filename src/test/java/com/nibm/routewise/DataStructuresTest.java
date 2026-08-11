package com.nibm.routewise;

import com.nibm.routewise.ds.BinarySearchTree;
import com.nibm.routewise.ds.CircularLinkedList;
import com.nibm.routewise.ds.DoublyLinkedList;
import com.nibm.routewise.ds.MinHeap;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataStructuresTest {

    @Test
    public void testMinHeapOperations() {
        MinHeap<Integer> heap = new MinHeap<>(Comparator.<Integer>naturalOrder());
        heap.insert(40);
        heap.insert(10);
        heap.insert(30);
        heap.insert(5);

        assertEquals(4, heap.size());
        assertEquals(5, heap.peek());

        assertEquals(5, heap.poll());
        assertEquals(10, heap.poll());
        assertEquals(30, heap.poll());
        assertEquals(40, heap.poll());
        assertTrue(heap.isEmpty());
    }

    @Test
    public void testBinarySearchTree() {
        BinarySearchTree<String, String> bst = new BinarySearchTree<>();
        bst.insert("TRK-102", "Package 102");
        bst.insert("TRK-101", "Package 101");
        bst.insert("TRK-103", "Package 103");

        assertEquals(3, bst.size());
        assertEquals("Package 101", bst.search("TRK-101"));
        assertEquals("Package 103", bst.search("TRK-103"));
        assertNull(bst.search("TRK-999"));

        List<String> inOrder = bst.getInOrderTraversal();
        assertEquals("Package 101", inOrder.get(0));
        assertEquals("Package 102", inOrder.get(1));
        assertEquals("Package 103", inOrder.get(2));
    }

    @Test
    public void testDoublyLinkedList() {
        DoublyLinkedList<String> dll = new DoublyLinkedList<>();
        dll.addLast("Stop 1");
        dll.addLast("Stop 2");
        dll.addLast("Stop 3");

        assertEquals(3, dll.size());
        assertEquals("Stop 1", dll.getHead().getData());
        assertEquals("Stop 3", dll.getTail().getData());

        dll.swap(0, 1);
        assertEquals("Stop 2", dll.getHead().getData());
    }

    @Test
    public void testCircularLinkedList() {
        CircularLinkedList<String> cll = new CircularLinkedList<>();
        cll.add("Node A");
        cll.add("Node B");

        List<String> loop = cll.getLoopSequence(5);
        assertEquals(5, loop.size());
        assertEquals("Node A", loop.get(0));
        assertEquals("Node B", loop.get(1));
        assertEquals("Node A", loop.get(2));
        assertEquals("Node B", loop.get(3));
        assertEquals("Node A", loop.get(4));
    }
}
