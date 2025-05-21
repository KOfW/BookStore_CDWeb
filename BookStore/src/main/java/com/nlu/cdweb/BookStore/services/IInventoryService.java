package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.dto.response.InventoryResponse;
import org.springframework.data.domain.Page;

public interface IInventoryService {
    public Page<InventoryResponse> findAll(int page, int size);
    public InventoryResponse findById(Long id);
    public InventoryResponse findByProductId(Long id);
    public InventoryResponse create(InventoryRequest inventoryRequest);
    public boolean delete (Long id);
    public InventoryResponse update(Long id, InventoryRequest inventoryRequest);
}
