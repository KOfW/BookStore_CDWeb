package com.nlu.cdweb.BookStore.dto.request;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long bookId;
    private Long cartId;
    private Integer quantity;
}
