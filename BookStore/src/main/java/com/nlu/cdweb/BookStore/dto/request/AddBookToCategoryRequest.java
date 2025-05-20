package com.nlu.cdweb.BookStore.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class AddBookToCategoryRequest {
    private Long catetegoryId;
    private Set<Long> bookId;
}
