# RouteWise 

> **Multi-Criteria Delivery Route & Package Logistics Optimization Platform**  
> Programming Data Structures and Algorithms (PDSA) Coursework — Higher National Diploma in Software Engineering (HNDSE), NIBM (Batch 25.2F)

---

## Project Overview

**RouteWise** is an enterprise-grade desktop software application designed to solve real-world urban logistics and package delivery routing challenges in Sri Lanka. The application models multi-modal delivery networks as a **Weighted Graph**, optimizes delivery paths using **Dijkstra's Algorithm** with custom **Min-Heap Priority Queues**, manages package inventories using an O(log n) **Binary Search Tree (BST)**, and tracks driver navigation history using **Doubly & Circular Linked Lists**.

---

## Key Features & Novel Features

- **Traffic-Aware Multi-Criteria Shortest Path Routing**: Calculates optimal delivery paths accounting for road distance (km), dynamic traffic congestion multipliers (1.0x Normal, 1.5x Moderate, 2.2x Heavy Congestion), turn-by-turn steps, and travel time estimates.
- **Visual Canvas Map & Animated Delivery Truck**: Renders Sri Lanka logistics hubs (Colombo 01-15, Kandy, Galle, Negombo, Gampaha) with color-coded traffic road links and frame-by-frame animated vehicle simulation.
- **O(log n) BST Package Inventory & QuickSort**: Instant package lookup by Tracking ID code using a custom Binary Search Tree, paired with QuickSort for urgency prioritization.
- **Driver Navigation & Continuous Patrol Loops**: Step forward/backward through route stops using a Doubly Linked List with instant stop re-ordering, plus 24/7 continuous shift patrol loops powered by a Circular Linked List.
- **Real-Time Big-O Performance Benchmarker**: Benchmarks custom Data Structures (`MinHeap`, `BST`, `DoublyLinkedList`) against Java Standard Collections in nanoseconds, paired with a complete theoretical Big-O complexity matrix.
- **Modern Glassmorphism UI**: High-contrast, dynamic Dark Mode and Light Mode themes.

---

## Group Member Module Allocation Matrix

| Index Number | Member Name |
|---|---|
| **GAHDSE252F-014** | **Tharushi Nethmini** | 
| **GAHDSE252F-024** | **Ramesha Dasangi** | 
| **GAHDSE252F-025** | **Himal Hansaka** |
| **GAHDSE252F-031** | **Harindu Adeesha** | 

---

## Tech Stack

| Component | Technology |
|---|---|
| **Programming Language** | Java 17 / OpenJDK 21+ |
| **GUI Framework** | JavaFX 21 (FXML & Canvas) |
| **Build Tool** | Apache Maven |
| **IDE** | IntelliJ IDEA |
