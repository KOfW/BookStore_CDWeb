package com.nlu.cdweb.BookStore.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BookResponse {
    @NotBlank
    private Long id;
    @NotBlank
    private String name;
    private String desc;
    @NotBlank
    private String sku; // mã quản lý hàng hóa
    @NotNull
    @Positive(message="book price must be positive")
    private Double price;
    private Long categoryId;
    @NotNull
    private Long discountId;
    private Integer version;
}
