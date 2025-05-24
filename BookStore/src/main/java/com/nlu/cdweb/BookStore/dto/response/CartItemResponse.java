package com.nlu.cdweb.BookStore.dto.response;

import lombok.Data;

@Data
public class CartItemResponse {
    private Long id;
    private Long bookId;
    private Long cartId;
    private Integer quantity;
}
