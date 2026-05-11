// 📁 model/OrderStatus.java
package com.parikshit.orderservice.model;

public enum OrderStatus {
    CREATED,
    ALLOCATED,
    PICK_IN_PROGRESS,
    PICKED,
    PACKED,
    SHIPPED,
    DELIVERED
}
