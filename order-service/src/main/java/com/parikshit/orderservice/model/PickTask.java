package com.parikshit.orderservice.model;

import jakarta.persistence.*;

@Entity
public class PickTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int orderId;

    private String pickerId;

    private String sourceBin;

    private String destinationContainer;

    private String status;

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getPickerId() {
        return pickerId;
    }

    public void setPickerId(String pickerId) {
        this.pickerId = pickerId;
    }

    public String getSourceBin() {
        return sourceBin;
    }

    public void setSourceBin(String sourceBin) {
        this.sourceBin = sourceBin;
    }

    public String getDestinationContainer() {
        return destinationContainer;
    }

    public void setDestinationContainer(String destinationContainer) {
        this.destinationContainer = destinationContainer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}