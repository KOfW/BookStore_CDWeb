package com.nlu.cdweb.BookStore.repositories;

import com.nlu.cdweb.BookStore.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long>, JpaSpecificationExecutor<BookEntity> {
    Optional<BookEntity> findById(Long id);
    List<BookEntity> findByCategoryId(Long id);
    List<BookEntity> findByCategoryName(String name);
    List<BookEntity> findByDiscountActive(Boolean active);
    boolean existsByNameIgnoreCase(String bookName);
    boolean existsBySku(String sku);
}
