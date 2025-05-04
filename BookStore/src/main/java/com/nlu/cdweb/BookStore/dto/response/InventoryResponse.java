package com.nlu.cdweb.BookStore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    Long id;
    Long productId;
    Integer quantity;
    Integer version;
}
