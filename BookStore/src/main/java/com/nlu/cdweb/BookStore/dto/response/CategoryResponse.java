package com.nlu.cdweb.BookStore.dto.response;

import lombok.Data;

import java.util.List;
@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private List<Long> productId;
    private Integer version;
}
