package com.parikshit.inventoryservice.controller;

import com.parikshit.inventoryservice.model.Product;
import com.parikshit.inventoryservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/")
    public String home() {
        return "Inventory Service Running!";
    }// 🔥 (POST)CREATE PRODUCT
    
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return service.saveProduct(product);
    }

    // 🔥 GET ALL PRODUCTS
    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    // 🔥 GET PRODUCT BY ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return service.getProductById(id);
    }

    // 🔥 REDUCE STOCK
    @PutMapping("/{id}/reduce")
    public Product reduceStock(@PathVariable int id,
                               @RequestParam int quantity) {
        return service.reduceStock(id, quantity);
    }
}