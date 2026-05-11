package com.parikshit.orderservice.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String customerName;

    private String address;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String pickerId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    // 🔥 GETTER FOR ID
    public int getId() {
        return id;
    }

    // 🔥 SETTER FOR ID
    public void setId(int id) {
        this.id = id;
    }

    // 🔥 GETTER FOR CUSTOMER NAME
    public String getCustomerName() {
        return customerName;
    }

    // 🔥 SETTER FOR CUSTOMER NAME
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // 🔥 GETTER FOR ADDRESS
    public String getAddress() {
        return address;
    }

    // 🔥 SETTER FOR ADDRESS
    public void setAddress(String address) {
        this.address = address;
    }

    // 🔥 GETTER FOR STATUS
    public OrderStatus getStatus() {
        return status;
    }

    // 🔥 SETTER FOR STATUS
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    // 🔥 GETTER FOR PICKER ID
    public String getPickerId() {
        return pickerId;
    }

    // 🔥 SETTER FOR PICKER ID
    public void setPickerId(String pickerId) {
        this.pickerId = pickerId;
    }

    // 🔥 GETTER FOR ORDER ITEMS
    public List<OrderItem> getItems() {
        return items;
    }

    // 🔥 SETTER FOR ORDER ITEMS
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}