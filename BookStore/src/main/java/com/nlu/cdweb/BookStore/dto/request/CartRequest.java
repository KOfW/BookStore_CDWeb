package com.nlu.cdweb.BookStore.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class CartRequest {
    private Set<Long> cartItemId;
}
