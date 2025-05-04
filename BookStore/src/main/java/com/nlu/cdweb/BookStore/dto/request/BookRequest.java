package com.nlu.cdweb.BookStore.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BookRequest {
    @NotBlank
    private String name;

    private String desc;

    @NotBlank
    private String sku;

    @NotNull
    @Positive(message="book price must be positive")
    private Double price;

    private Long categoryId;

    @NotNull
    private Integer quantity;

    private Long discountId;
}
