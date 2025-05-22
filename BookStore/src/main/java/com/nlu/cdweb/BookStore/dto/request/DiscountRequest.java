package com.nlu.cdweb.BookStore.dto.request;

import lombok.Data;

@Data
public class DiscountRequest {
    private String name;
    private String desc;
    private Double percent;
    private Boolean active;
}
