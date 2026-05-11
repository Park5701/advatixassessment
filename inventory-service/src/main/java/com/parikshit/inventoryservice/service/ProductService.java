package com.parikshit.inventoryservice.service;

import com.parikshit.inventoryservice.model.Product;
import com.parikshit.inventoryservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    // 🔥 CREATE PRODUCT
    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    // 🔥 GET ALL PRODUCTS
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    // 🔥 GET PRODUCT BY ID
    public Product getProductById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // 🔥 REDUCE STOCK
    public Product reduceStock(int id, int quantity) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        product.setQuantity(product.getQuantity() - quantity);

        return repository.save(product);
    }
}