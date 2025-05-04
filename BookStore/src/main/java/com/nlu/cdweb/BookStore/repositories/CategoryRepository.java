package com.nlu.cdweb.BookStore.repositories;

import com.nlu.cdweb.BookStore.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findById(Long id);

}
