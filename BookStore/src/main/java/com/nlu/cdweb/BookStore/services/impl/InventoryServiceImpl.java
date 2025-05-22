package com.nlu.cdweb.BookStore.services.impl;

import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.dto.response.InventoryResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.InventoryEntity;
import com.nlu.cdweb.BookStore.exception.EntityNotFoundException;
import com.nlu.cdweb.BookStore.mapper.InventoryMapper;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import com.nlu.cdweb.BookStore.repositories.InventoryRepository;
import com.nlu.cdweb.BookStore.services.IInventoryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements IInventoryService {
    @Autowired
    private final InventoryRepository inventoryRepo;
    @Autowired
    private final BookRepository bookRepo;
    @Autowired
    private final InventoryMapper mapper;
    @Override
    @Transactional
    public InventoryResponse create(InventoryRequest inventoryRequest) {

        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setQuantity(inventoryRequest.getQuantity());

        BookEntity bookEntity = bookRepo.findById(inventoryRequest.getId_book())
                .orElseThrow(RuntimeException::new);
        inventoryEntity.setBook(bookEntity);

        InventoryEntity save = inventoryRepo.save(inventoryEntity);

        return new InventoryResponse(save.getId(), save.getBook().getId(), save.getQuantity(), save.getVersion());
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        InventoryEntity entity = inventoryRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found inventory with id: "+id));
        inventoryRepo.delete(entity);
        return true;
    }

    @Override
    @Transactional
    public InventoryResponse update(Long id, InventoryRequest inventoryRequest) {
        InventoryEntity entity = inventoryRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found inventory with id: "+id));

        entity.setQuantity(inventoryRequest.getQuantity());

        return mapper.toDTO(inventoryRepo.save(entity));
    }

    @Override
    public Page<InventoryResponse> findAll(int page, int size) {
        return inventoryRepo.findAll(PageRequest.of(page, size)).map(mapper::toDTO);
    }

    @Override
    public InventoryResponse findById(Long id) {
        return inventoryRepo.findById(id).map(mapper::toDTO).orElseThrow(RuntimeException::new);
    }

    @Override
    public InventoryResponse findByProductId(Long id) {
        return inventoryRepo.findByBook_Id(id).map(mapper::toDTO).orElseThrow(RuntimeException::new);
    }
}
