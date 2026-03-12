package com.app.publicvendorprofile.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Component
public class CartClient {

    private final WebClient webClient;

    public CartClient(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Calls the Cart service to add an item to cart.
     * Returns the response body as a Map<String,Object>.
     */
    public Mono<Map<String, Object>> addItemToCart(Map<String, Object> payload) {
        Objects.requireNonNull(payload, "payload must not be null");
        String cartServiceUrl = System.getenv().getOrDefault("CART_SERVICE_URL", "http://localhost:8081");
        return webClient.post()
                .uri(cartServiceUrl + "/api/cart/items/add")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {});
    }
}
