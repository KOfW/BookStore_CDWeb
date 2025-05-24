package com.nlu.cdweb.BookStore.dto.response;

import lombok.Data;

import java.util.Map;

@Data
public class CartResponse {
    private Long id;
    private Long userId;
    private Map<String, Integer> cartItem;
    private Double total;
}
