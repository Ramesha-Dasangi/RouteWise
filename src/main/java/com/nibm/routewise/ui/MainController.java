package com.nibm.routewise.ui;

import com.nibm.routewise.ds.BenchmarkEngine;
import com.nibm.routewise.ds.BinarySearchTree;
import com.nibm.routewise.ds.CircularLinkedList;
import com.nibm.routewise.ds.DoublyLinkedList;
import com.nibm.routewise.graph.DijkstraSolver;
import com.nibm.routewise.graph.Edge;
import com.nibm.routewise.graph.Graph;
import com.nibm.routewise.graph.Node;
import com.nibm.routewise.model.PackageItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;


// Main Controller for RouteWise Enterprise JavaFX UI.
// Integrates all 4 group member modules and data structures.

public class MainController {

    // FXML Root & Header
    @FXML private BorderPane rootBorderPane;
    @FXML private Button themeToggleButton;

    // Tab 1: Route Finder
    @FXML private ComboBox<Node> startComboBox;
    @FXML private ComboBox<Node> endComboBox;
    @FXML private ComboBox<String> trafficComboBox;
    @FXML private Button findRouteButton;
    @FXML private Label distanceLabel;
    @FXML private Label timeLabel;
    @FXML private Label statusLabel;
    @FXML private ListView<String> navigationStepsListView;
    @FXML private Canvas mapCanvas;

    // Tab 2: Package BST Logistics
    @FXML private TextField searchTrackingField;
    @FXML private Label bstStatsLabel;
    @FXML private TextField newTrackingField;
    @FXML private TextField newRecipientField;
    @FXML private ComboBox<Node> newDestinationComboBox;
    @FXML private Slider urgencySlider;
    @FXML private TextField newWeightField;
    @FXML private ComboBox<String> newStatusComboBox;
    @FXML private ComboBox<String> updateStatusComboBox;
    @FXML private TableView<PackageItem> packageTableView;
    @FXML private TableColumn<PackageItem, String> colTracking;
    @FXML private TableColumn<PackageItem, String> colRecipient;
    @FXML private TableColumn<PackageItem, String> colDestination;
    @FXML private TableColumn<PackageItem, Integer> colUrgency;
    @FXML private TableColumn<PackageItem, Double> colWeight;
    @FXML private TableColumn<PackageItem, String> colStatus;

    // Tab 3: Route Steps Navigator (DLL & Circular)
    @FXML private Label currentStopLabel;
    @FXML private ListView<String> routeStopListView;
    @FXML private TextArea patrolLoopTextArea;

    // Tab 4: Benchmarks
    @FXML private Label heapCustomTimeLabel;
    @FXML private Label heapJavaTimeLabel;
    @FXML private Label bstCustomTimeLabel;
    @FXML private Label bstJavaTimeLabel;
    @FXML private Label dllCustomTimeLabel;
    @FXML private Label dllJavaTimeLabel;
    @FXML private TableView<BigOModel> complexityTableView;
    @FXML private TableColumn<BigOModel, String> colDsName;
    @FXML private TableColumn<BigOModel, String> colAccess;
    @FXML private TableColumn<BigOModel, String> colSearch;
    @FXML private TableColumn<BigOModel, String> colInsert;
    @FXML private TableColumn<BigOModel, String> colDelete;

    // Core Data & State
    private final Graph graph = new Graph();
    private final DijkstraSolver dijkstraSolver = new DijkstraSolver();
    private final BinarySearchTree<String, PackageItem> packageBST = new BinarySearchTree<>();
    private DoublyLinkedList<Node> activeRouteDLL = new DoublyLinkedList<>();
    private CircularLinkedList<Node> patrolCircularList = new CircularLinkedList<>();
    private MapView mapView;
    private boolean isDarkMode = true;
    private DoublyLinkedList.Node<Node> currentNavPointer = null;

    // Helper model for Big-O table
    public static class BigOModel {
        private final SimpleStringProperty dsName;
        private final SimpleStringProperty access;
        private final SimpleStringProperty search;
        private final SimpleStringProperty insert;
        private final SimpleStringProperty delete;

        public BigOModel(String dsName, String access, String search, String insert, String delete) {
            this.dsName = new SimpleStringProperty(dsName);
            this.access = new SimpleStringProperty(access);
            this.search = new SimpleStringProperty(search);
            this.insert = new SimpleStringProperty(insert);
            this.delete = new SimpleStringProperty(delete);
        }

        public String getDsName() { return dsName.get(); }
        public String getAccess() { return access.get(); }
        public String getSearch() { return search.get(); }
        public String getInsert() { return insert.get(); }
        public String getDelete() { return delete.get(); }
    }

    @FXML
    public void initialize() {
        // 1. Setup Sri Lanka Delivery Graph Data
        loadSriLankaGraphData();

        // 2. Setup Map Visualizer
        mapView = new MapView(mapCanvas);
        setNodeCoordinates();

        // 3. Populate ComboBoxes
        startComboBox.getItems().addAll(graph.getAllNodes());
        endComboBox.getItems().addAll(graph.getAllNodes());
        newDestinationComboBox.getItems().addAll(graph.getAllNodes());
        newStatusComboBox.getItems().addAll("Pending", "In-Transit", "Delivered", "Urgent Medical");
        newStatusComboBox.setValue("Pending");

        updateStatusComboBox.getItems().addAll("Pending", "In-Transit", "Delivered", "Urgent Medical");
        updateStatusComboBox.setValue("Delivered");

        trafficComboBox.getItems().addAll("Normal Traffic (1.0x)", "Moderate Traffic (1.5x)", "Heavy Congestion (2.2x)");
        trafficComboBox.setValue("Normal Traffic (1.0x)");

        // 4. Setup Package Table Columns
        colTracking.setCellValueFactory(new PropertyValueFactory<>("trackingCode"));
        colRecipient.setCellValueFactory(new PropertyValueFactory<>("recipientName"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destinationNode"));
        colUrgency.setCellValueFactory(new PropertyValueFactory<>("urgency"));
        colWeight.setCellValueFactory(new PropertyValueFactory<>("weightKg"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadSamplePackages();
        refreshPackageTable();

        // 5. Setup Big-O Table Columns
        colDsName.setCellValueFactory(new PropertyValueFactory<>("dsName"));
        colAccess.setCellValueFactory(new PropertyValueFactory<>("access"));
        colSearch.setCellValueFactory(new PropertyValueFactory<>("search"));
        colInsert.setCellValueFactory(new PropertyValueFactory<>("insert"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
        loadComplexityData();

        // 6. Draw Initial Map & Load Default Active Route for Tab 3
        mapView.draw(graph, null);
        loadInitialDefaultRoute();
        statusLabel.setText("System ready. Select Origin and Destination.");
    }

    private void loadInitialDefaultRoute() {
        Node start = graph.getNode("A");
        Node end = graph.getNode("G");
        if (start != null && end != null) {
            DijkstraSolver.Result result = dijkstraSolver.findShortestPath(graph, start, end);
            if (result.totalDistance > 0) {
                activeRouteDLL = new DoublyLinkedList<>();
                for (Node node : result.path) {
                    activeRouteDLL.addLast(node);
                }
                currentNavPointer = activeRouteDLL.getHead();
                updateDLLNavigationUI();
            }
        }
    }

    private void loadSriLankaGraphData() {
        Node colomboFort = new Node("A", "Colombo Fort Hub");
        Node colombo3 = new Node("B", "Kollupitiya (Col 03)");
        Node colombo7 = new Node("C", "Cinnamon Gardens (Col 07)");
        Node dehiwala = new Node("D", "Dehiwala Hub");
        Node negombo = new Node("E", "Negombo Air Cargo");
        Node gampaha = new Node("F", "Gampaha Logistics Depot");
        Node kandy = new Node("G", "Kandy Central");
        Node galle = new Node("H", "Galle Port Hub");

        graph.addEdge(colomboFort, colombo3, 3.5);
        graph.addEdge(colomboFort, colombo7, 4.0);
        graph.addEdge(colombo3, dehiwala, 8.0);
        graph.addEdge(colombo7, dehiwala, 9.2);
        graph.addEdge(colomboFort, negombo, 35.0);
        graph.addEdge(negombo, gampaha, 22.0);
        graph.addEdge(gampaha, kandy, 85.0);
        graph.addEdge(colombo7, gampaha, 28.0);
        graph.addEdge(dehiwala, galle, 115.0);
        graph.addEdge(kandy, galle, 180.0);
    }

    private void setNodeCoordinates() {
        mapView.setPosition("A", 180, 220); // Colombo Fort
        mapView.setPosition("B", 160, 310); // Col 03
        mapView.setPosition("C", 240, 290); // Col 07
        mapView.setPosition("D", 190, 420); // Dehiwala
        mapView.setPosition("E", 140, 110); // Negombo
        mapView.setPosition("F", 320, 140); // Gampaha
        mapView.setPosition("G", 520, 200); // Kandy
        mapView.setPosition("H", 340, 450); // Galle
    }

    private void loadSamplePackages() {
        packageBST.insert("TRK-1004", new PackageItem("TRK-1004", "Kamal Silva", "Kandy Central", 9, 1.2, "In-Transit"));
        packageBST.insert("TRK-1001", new PackageItem("TRK-1001", "Nimali Perera", "Col 03", 4, 3.5, "Pending"));
        packageBST.insert("TRK-1008", new PackageItem("TRK-1008", "Sunil Fernando", "Galle Port Hub", 10, 0.8, "Urgent Medical"));
        packageBST.insert("TRK-1002", new PackageItem("TRK-1002", "Anura Jayasinghe", "Negombo Air Cargo", 6, 5.0, "Pending"));
        packageBST.insert("TRK-1006", new PackageItem("TRK-1006", "Dilini De Silva", "Dehiwala Hub", 7, 2.1, "In-Transit"));
    }

    private void refreshPackageTable() {
        List<PackageItem> list = packageBST.getInOrderTraversal();
        packageTableView.setItems(FXCollections.observableArrayList(list));
        bstStatsLabel.setText("BST Package Count: " + packageBST.size() + " | Tree Height: " + packageBST.getHeight());
    }

    private void loadComplexityData() {
        ObservableList<BigOModel> data = FXCollections.observableArrayList(
                new BigOModel("Min-Heap (Priority Queue)", "O(1) peek", "O(n)", "O(log n)", "O(log n) poll"),
                new BigOModel("Binary Search Tree (BST)", "O(log n)", "O(log n)", "O(log n)", "O(log n)"),
                new BigOModel("Doubly Linked List", "O(n)", "O(n)", "O(1) head/tail", "O(1) head/tail"),
                new BigOModel("Circular Linked List", "O(n)", "O(n)", "O(1) append", "O(1) append"),
                new BigOModel("Adjacency List Graph", "O(1) vertex", "O(V + E)", "O(1) add edge", "O(E) remove edge")
        );
        complexityTableView.setItems(data);
    }

    // TAB 1 HANDLERS
    @FXML
    public void onFindRoute() {
        Node start = startComboBox.getValue();
        Node end = endComboBox.getValue();

        if (start == null || end == null) {
            statusLabel.setText("Error: Please select both Start and Destination nodes.");
            return;
        }

        // Apply traffic multiplier to graph edges
        String trafficSetting = trafficComboBox.getValue();
        double multiplier = 1.0;
        if (trafficSetting.contains("1.5x")) multiplier = 1.5;
        if (trafficSetting.contains("2.2x")) multiplier = 2.2;

        for (Node n : graph.getAllNodes()) {
            for (Edge e : graph.getEdges(n)) {
                e.setTrafficMultiplier(multiplier);
            }
        }

        DijkstraSolver.Result result = dijkstraSolver.findShortestPath(graph, start, end);

        if (result.totalDistance < 0) {
            distanceLabel.setText("N/A");
            timeLabel.setText("N/A");
            navigationStepsListView.getItems().setAll("No connected route exists between nodes.");
            mapView.draw(graph, null);
            statusLabel.setText("No route found!");
        } else {
            distanceLabel.setText(String.format("%.1f km", result.totalDistance));
            timeLabel.setText(String.format("%.0f mins", result.estimatedTimeMins));
            navigationStepsListView.getItems().setAll(result.stepInstructions);

            mapView.draw(graph, result.path);

            // Populate Doubly Linked List for Tab 3 Navigator
            activeRouteDLL = new DoublyLinkedList<>();
            for (Node node : result.path) {
                activeRouteDLL.addLast(node);
            }
            currentNavPointer = activeRouteDLL.getHead();
            updateDLLNavigationUI();

            statusLabel.setText("Route calculated using Custom MinHeap Dijkstra Engine!");
        }
    }

    // TAB 2 HANDLERS
    @FXML
    public void onSearchPackage() {
        String code = searchTrackingField.getText();
        if (code == null || code.trim().isEmpty()) {
            showAlert("Input Error", "Please enter a Tracking ID to search.");
            return;
        }

        PackageItem found = packageBST.search(code.trim().toUpperCase());
        if (found != null) {
            showAlert("Package Found in BST! O(log n)",
                    "Tracking ID: " + found.getTrackingCode() +
                    "\nRecipient: " + found.getRecipientName() +
                    "\nDestination: " + found.getDestinationNode() +
                    "\nUrgency Score: " + found.getUrgency() + "/10" +
                    "\nWeight: " + found.getWeightKg() + " kg" +
                    "\nStatus: " + found.getStatus());
        } else {
            showAlert("Package Not Found", "No package matching Tracking ID '" + code + "' was found in the BST.");
        }
    }

    @FXML
    public void onAddPackage() {
        String code = newTrackingField.getText();
        String recipient = newRecipientField.getText();
        Node dest = newDestinationComboBox.getValue();
        int urgency = (int) urgencySlider.getValue();
        String weightStr = newWeightField.getText();

        if (code == null || code.isEmpty() || recipient == null || recipient.isEmpty() || dest == null) {
            showAlert("Validation Error", "Please fill in Tracking ID, Recipient Name, and Destination.");
            return;
        }

        double weight = 1.0;
        try {
            if (weightStr != null && !weightStr.isEmpty()) {
                weight = Double.parseDouble(weightStr);
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Invalid weight value.");
            return;
        }

        String status = newStatusComboBox.getValue();
        if (status == null || status.isEmpty()) {
            status = (urgency == 10) ? "Urgent Medical" : "Pending";
        }

        PackageItem item = new PackageItem(code.trim().toUpperCase(), recipient.trim(), dest.getName(), urgency, weight, status);
        packageBST.insert(item.getTrackingCode(), item);
        refreshPackageTable();

        newTrackingField.clear();
        newRecipientField.clear();
        newWeightField.clear();
        showAlert("Success", "Package " + item.getTrackingCode() + " inserted into BST successfully!");
    }

    @FXML
    public void onSortPackagesByUrgency() {
        List<PackageItem> list = packageBST.getInOrderTraversal();
        // Custom QuickSort by urgency descending
        quickSortPackages(list, 0, list.size() - 1);
        packageTableView.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    public void onUpdatePackageStatus() {
        PackageItem selected = packageTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a package row in the table to update its status.");
            return;
        }

        String newStatus = updateStatusComboBox.getValue();
        if (newStatus != null && !newStatus.isEmpty()) {
            selected.setStatus(newStatus);
            packageTableView.refresh();
            showAlert("Status Updated", "Package [" + selected.getTrackingCode() + "] status updated to '" + newStatus + "'!");
        }
    }

    private void quickSortPackages(List<PackageItem> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSortPackages(list, low, pi - 1);
            quickSortPackages(list, pi + 1, high);
        }
    }

    private int partition(List<PackageItem> list, int low, int high) {
        int pivot = list.get(high).getUrgency();
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).getUrgency() >= pivot) { // Descending order
                i++;
                PackageItem temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        PackageItem temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }

    // TAB 3 HANDLERS
    private void updateDLLNavigationUI() {
        if (currentNavPointer != null) {
            currentStopLabel.setText(currentNavPointer.getData().getName());
        } else {
            currentStopLabel.setText("No active stop");
        }

        List<String> list = new ArrayList<>();
        DoublyLinkedList.Node<Node> curr = activeRouteDLL.getHead();
        int idx = 1;
        while (curr != null) {
            String marker = (curr == currentNavPointer) ? " ➔ [ACTIVE STOP]" : "";
            list.add("Stop " + idx + ": " + curr.getData().getName() + marker);
            curr = curr.getNext();
            idx++;
        }
        routeStopListView.getItems().setAll(list);
    }

    @FXML
    public void onStepForward() {
        if (currentNavPointer != null && currentNavPointer.getNext() != null) {
            currentNavPointer = currentNavPointer.getNext();
            updateDLLNavigationUI();
        }
    }

    @FXML
    public void onStepBackward() {
        if (currentNavPointer != null && currentNavPointer.getPrev() != null) {
            currentNavPointer = currentNavPointer.getPrev();
            updateDLLNavigationUI();
        }
    }

    @FXML
    public void onSwapStopUp() {
        if (activeRouteDLL.size() > 1) {
            activeRouteDLL.swap(0, 1);
            currentNavPointer = activeRouteDLL.getHead();
            updateDLLNavigationUI();
        }
    }

    @FXML
    public void onSwapStopDown() {
        if (activeRouteDLL.size() > 1) {
            activeRouteDLL.swap(activeRouteDLL.size() - 2, activeRouteDLL.size() - 1);
            currentNavPointer = activeRouteDLL.getHead();
            updateDLLNavigationUI();
        }
    }

    @FXML
    public void onGeneratePatrolLoop() {
        patrolCircularList = new CircularLinkedList<>();
        for (Node n : graph.getAllNodes()) {
            patrolCircularList.add(n);
        }

        List<Node> loop = patrolCircularList.getLoopSequence(18); // 3 full cycles
        StringBuilder sb = new StringBuilder("=== 24/7 Continuous Shift Patrol Loop (3 Cycles) ===\n\n");
        int count = 1;
        for (Node n : loop) {
            sb.append("Stop #").append(count).append(": ").append(n.getName()).append("\n");
            count++;
        }
        patrolLoopTextArea.setText(sb.toString());
    }

    // TAB 4 HANDLERS
    @FXML
    public void onRunBenchmarks() {
        int sampleSize = 10000;
        BenchmarkEngine.BenchmarkResult heapRes = BenchmarkEngine.runHeapBenchmark(sampleSize);
        BenchmarkEngine.BenchmarkResult bstRes = BenchmarkEngine.runBSTBenchmark(sampleSize);
        BenchmarkEngine.BenchmarkResult dllRes = BenchmarkEngine.runDoublyLinkedListBenchmark(sampleSize);

        heapCustomTimeLabel.setText(String.format("Custom MinHeap: %,d ns", heapRes.customTimeNs));
        heapJavaTimeLabel.setText(String.format("Java PriorityQueue: %,d ns", heapRes.javaStandardTimeNs));

        bstCustomTimeLabel.setText(String.format("Custom BST: %,d ns", bstRes.customTimeNs));
        bstJavaTimeLabel.setText(String.format("Java TreeSet: %,d ns", bstRes.javaStandardTimeNs));

        dllCustomTimeLabel.setText(String.format("Custom DLL: %,d ns", dllRes.customTimeNs));
        dllJavaTimeLabel.setText(String.format("Java LinkedList: %,d ns", dllRes.javaStandardTimeNs));

        showAlert("Benchmarks Completed", "Executed 10,000 iterations for MinHeap, BST, and DoublyLinkedList!");
    }

    // THEME TOGGLE
    @FXML
    public void onToggleTheme() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            rootBorderPane.getStyleClass().remove("light-theme");
            themeToggleButton.setText("🌙 Dark Mode");
        } else {
            rootBorderPane.getStyleClass().add("light-theme");
            themeToggleButton.setText("☀️ Light Mode");
        }
        mapView.setDarkMode(isDarkMode);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
