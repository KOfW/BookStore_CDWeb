package com.nlu.cdweb.BookStore.repositories;

import com.nlu.cdweb.BookStore.entity.InventoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    Optional<InventoryEntity> findById(Long id);

    @Override
    Page<InventoryEntity> findAll(Pageable pageable);
}
