package com.parikshit.orderservice.service;

import com.parikshit.orderservice.dto.ProductResponse;
import com.parikshit.orderservice.model.Order;
import com.parikshit.orderservice.model.OrderItem;
import com.parikshit.orderservice.model.OrderStatus;
import com.parikshit.orderservice.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    // 🔥 CREATE ORDER (OMS FLOW)
    public Order createOrder(Order order) {

        // 🔥 1. ADDRESS VALIDATION
        if (order.getAddress() == null || order.getAddress().isEmpty()) {
            throw new RuntimeException("Invalid address");
        }

        // 🔥 2. INVENTORY VALIDATION + STOCK DEDUCTION
        for (OrderItem item : order.getItems()) {

            // 📦 CALL INVENTORY SERVICE TO FETCH PRODUCT
            String url = "http://localhost:8081/products/" + item.getProductId();

            ProductResponse product =
                    restTemplate.getForObject(url, ProductResponse.class);

            if (product == null) {
                throw new RuntimeException("Product not found");
            }

            // 📦 STOCK CHECK
            if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }

            // 📦 REDUCE STOCK THROUGH INVENTORY SERVICE
            String reduceUrl =
                    "http://localhost:8081/products/"
                            + item.getProductId()
                            + "/reduce?quantity="
                            + item.getQuantity();

            restTemplate.put(reduceUrl, null);
        }

        // 🔥 3. INITIAL ORDER STATUS
        order.setStatus(OrderStatus.CREATED);

        // 🔥 4. SAVE ORDER
        return orderRepository.save(order);
    }

    // 🔥 ALLOCATE ORDER (WMS FLOW)
    public Order allocateOrder(int orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 📦 ONLY CREATED ORDERS CAN BE ALLOCATED
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Only CREATED orders can be allocated");
        }

        order.setStatus(OrderStatus.ALLOCATED);

        return orderRepository.save(order);
    }

    // 🔥 PICK ORDER (WMS FLOW)
    public Order pickOrder(int orderId, String pickerId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 📦 ONLY ALLOCATED ORDERS CAN BE PICKED
        if (order.getStatus() != OrderStatus.ALLOCATED) {
            throw new RuntimeException("Order not allocated");
        }

        // 📦 ASSIGN PICKER
        order.setPickerId(pickerId);

        // 📦 UPDATE STATUS
        order.setStatus(OrderStatus.PICKED);

        return orderRepository.save(order);
    }

    // 🔥 PACK ORDER
    public Order packOrder(int orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PICKED) {
            throw new RuntimeException("Only PICKED orders can be packed");
        }

        order.setStatus(OrderStatus.PACKED);

        return orderRepository.save(order);
    }

    // 🔥 SHIP ORDER
    public Order shipOrder(int orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PACKED) {
            throw new RuntimeException("Only PACKED orders can be shipped");
        }

        order.setStatus(OrderStatus.SHIPPED);

        return orderRepository.save(order);
    }

    // 🔥 DELIVER ORDER
    public Order deliverOrder(int orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new RuntimeException("Only SHIPPED orders can be delivered");
        }

        order.setStatus(OrderStatus.DELIVERED);

        return orderRepository.save(order);
    }
}