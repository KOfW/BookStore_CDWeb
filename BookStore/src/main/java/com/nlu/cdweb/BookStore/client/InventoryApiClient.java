package com.nlu.cdweb.BookStore.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.dto.response.InventoryResponse;
import com.nlu.cdweb.BookStore.exception.CustomServerException;
import com.nlu.cdweb.BookStore.exception.InvalidBodyRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
@Service
public class InventoryApiClient {
    private final RestClient client;
    public InventoryApiClient(@Value("${inventory.api.url}") String baseUrl) {
        this.client = RestClient.create(baseUrl); // ✅ baseUrl luôn có giá trị
    }
    public InventoryResponse updateInventory(InventoryRequest x){
        return client
                .put()
                .body(x)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request,response) -> {
                    throw new InvalidBodyRequestException("The client entered an invalid request body");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request,response) -> {
                    throw new CustomServerException("Server is down");
                })
                .toEntity(InventoryResponse.class)
                .getBody();
    }

    public InventoryResponse findInventoryForProductid(Long productId) {
        ApiResponse raw = client
                .get()
                .uri("/products/{productId}", productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new IllegalArgumentException("Invalid or missing inventory for product ID");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    throw new CustomServerException("Server error");
                })
                .toEntity(ApiResponse.class)
                .getBody();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(raw.getData(), InventoryResponse.class);
    }

    public InventoryResponse createInventory(InventoryRequest inventoryRequest){
        return client
                .post()
                .accept(MediaType.APPLICATION_JSON)
                .body(inventoryRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new InvalidBodyRequestException("The client entered an invalid request body");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request,response) -> {
                    throw new CustomServerException("Server is down");
                })
                .toEntity(InventoryResponse.class)
                .getBody();
    }
}
