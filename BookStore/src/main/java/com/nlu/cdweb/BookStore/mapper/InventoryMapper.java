package com.nlu.cdweb.BookStore.mapper;

import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.dto.response.InventoryResponse;
import com.nlu.cdweb.BookStore.entity.CategoryEntity;
import com.nlu.cdweb.BookStore.entity.InventoryEntity;
import com.nlu.cdweb.BookStore.exception.EntityNotFoundException;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InventoryMapper {

    private BookRepository repo;

    public InventoryResponse toDTO(InventoryEntity entity) {
        InventoryResponse response = new InventoryResponse();
        response.setId(entity.getId());
        response.setProductId(entity.getBook().getId()); // hoặc entity.getBookId() nếu bạn có
        response.setQuantity(entity.getQuantity());
        response.setVersion(entity.getVersion());
        return response;
    }
}
