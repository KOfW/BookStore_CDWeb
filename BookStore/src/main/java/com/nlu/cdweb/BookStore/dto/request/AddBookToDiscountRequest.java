package com.nlu.cdweb.BookStore.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class AddBookToDiscountRequest {
    private Set<Long> bookId;
    private Long discountId;
}
