package com.nlu.cdweb.BookStore.repositories;

import com.nlu.cdweb.BookStore.entity.CartEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepositoriy extends JpaRepository<CartEntity, Long> {
    public CartEntity findByUser_Id(Long id);
    public Page<CartEntity> findAll(Pageable pageable);
}
