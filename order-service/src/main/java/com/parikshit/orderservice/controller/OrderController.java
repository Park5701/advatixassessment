// 📁 controller/OrderController.java
package com.parikshit.orderservice.controller;

import com.parikshit.orderservice.model.Order;
import com.parikshit.orderservice.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    // 🔥 CREATE ORDER API
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return service.createOrder(order);
    }
}