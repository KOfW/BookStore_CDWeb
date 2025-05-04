package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.dto.response.InventoryResponse;

public interface IInventoryService {
    InventoryResponse create(InventoryRequest inventoryRequest);
}
