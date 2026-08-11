package com.nibm.routewise.model;

// Model representing a delivery package for Member 2 BST search & logistics.

public class PackageItem implements Comparable<PackageItem> {

    private final String trackingCode;
    private final String recipientName;
    private final String destinationNode;
    private final int urgency; // 1 (Low) - 10 (Urgent/Medical)
    private final double weightKg;
    private String status;

    public PackageItem(String trackingCode, String recipientName, String destinationNode, int urgency, double weightKg, String status) {
        this.trackingCode = trackingCode;
        this.recipientName = recipientName;
        this.destinationNode = destinationNode;
        this.urgency = urgency;
        this.weightKg = weightKg;
        this.status = status;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getDestinationNode() {
        return destinationNode;
    }

    public int getUrgency() {
        return urgency;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int compareTo(PackageItem o) {
        return this.trackingCode.compareTo(o.trackingCode);
    }

    @Override
    public String toString() {
        return "[" + trackingCode + "] " + recipientName + " -> " + destinationNode + " (Urgency: " + urgency + "/10)";
    }
}
