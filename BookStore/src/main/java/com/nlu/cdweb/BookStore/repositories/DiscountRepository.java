package com.nlu.cdweb.BookStore.repositories;

import com.nlu.cdweb.BookStore.entity.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {
    Optional<DiscountEntity> findById(Long id);
}
