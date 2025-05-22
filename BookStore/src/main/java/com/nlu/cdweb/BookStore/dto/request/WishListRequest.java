package com.nlu.cdweb.BookStore.dto.request;

import lombok.Data;

@Data
public class WishListRequest {
    private Long userId;
    private Long bookId;
}
