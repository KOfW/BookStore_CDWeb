package com.nlu.cdweb.BookStore.repositories;

import com.nlu.cdweb.BookStore.dto.response.InventoryResponse;
import com.nlu.cdweb.BookStore.entity.InventoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    Optional<InventoryEntity> findById(Long id);
    public Optional<InventoryEntity> findByBook_Id(Long id);
    @Override
    Page<InventoryEntity> findAll(Pageable pageable);
}
