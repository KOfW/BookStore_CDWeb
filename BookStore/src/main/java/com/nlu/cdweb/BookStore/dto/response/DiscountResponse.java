package com.nlu.cdweb.BookStore.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class DiscountResponse {
    private Long id;
    private String name;
    private String desc;
    private Double percent;
    private Boolean active;
    private List<Long> bookId;
    private Integer version;
}
