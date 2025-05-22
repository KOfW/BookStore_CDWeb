package com.nlu.cdweb.BookStore.repositories;

import com.nlu.cdweb.BookStore.entity.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {
//    Optional<DiscountEntity> findById(Long id);
}
