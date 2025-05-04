package com.nlu.cdweb.BookStore.services.impl;

import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.dto.response.InventoryResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.InventoryEntity;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import com.nlu.cdweb.BookStore.repositories.InventoryRepository;
import com.nlu.cdweb.BookStore.services.IInventoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InventoryService implements IInventoryService {
    @Autowired
    private final InventoryRepository inventoryRepository;
    @Autowired
    private final BookRepository bookRepository;
    @Override
    public InventoryResponse create(InventoryRequest inventoryRequest) {

        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setQuantity(inventoryRequest.getQuantity());

        BookEntity bookEntity = bookRepository.findById(inventoryRequest.getId_book())
                .orElseThrow(RuntimeException::new);
        inventoryEntity.setBook(bookEntity);

        InventoryEntity save = inventoryRepository.save(inventoryEntity);

        return new InventoryResponse(save.getId(), save.getBook().getId(), save.getQuantity(), save.getVersion());
    }
}
