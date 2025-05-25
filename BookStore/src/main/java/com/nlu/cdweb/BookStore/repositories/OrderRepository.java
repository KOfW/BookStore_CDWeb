package com.nlu.cdweb.BookStore.repositories;

import com.nlu.cdweb.BookStore.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    public List<OrderEntity> findByUser_Id(Long id);
}
