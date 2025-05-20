package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.dto.response.InventoryResponse;
import org.springframework.data.domain.Page;

public interface IInventoryService {
    InventoryResponse create(InventoryRequest inventoryRequest);
    public Page<InventoryResponse> findAll(int page, int size);
}
