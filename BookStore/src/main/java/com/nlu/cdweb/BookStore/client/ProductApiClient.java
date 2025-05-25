package com.nlu.cdweb.BookStore.client;

import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.response.BookResponse;
import com.nlu.cdweb.BookStore.exception.CustomServerException;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;
@Service
public class ProductApiClient {
    @Value("http://localhost:8081/api/books")
    @NonFinal
    String BASE_URL;
    RestTemplate restTemplate = new RestTemplate();
    RestClient client=RestClient.create(BASE_URL);

    public List<BookResponse> findAllByIds(Set<Long> productIds){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Set<Long>> requestEntity = new HttpEntity<>(productIds, headers);
        var responseEntity = restTemplate.exchange(
                BASE_URL + "/product_ids",
                HttpMethod.POST,  // ✅ đổi từ GET sang POST
                requestEntity,
                ApiResponse.class
        );
        ApiResponse body = responseEntity.getBody();
        return (List<BookResponse>) body.getData();
    }

    public BookResponse findProductById(Long id){
        return client
                .get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new IllegalArgumentException("Either The Client Entered an Id that's below 1 or "
                            + "no product was found for id");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request,response) -> {
                    throw new CustomServerException("Server is down");
                })
                .toEntity(BookResponse.class)
                .getBody();
    }
}
