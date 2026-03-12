package com.app.publicvendorprofile.controller;

import com.app.publicvendorprofile.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/cart/items")
public class CartController {

    private final VendorService vendorService;

    public CartController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @Operation(summary = "Add item(s) to cart (forwards to Cart service)")
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Map<String, Object> payload) {
        Map<String, Object> result = vendorService.addToCart(payload);
        return ResponseEntity.ok(result);
    }

}
