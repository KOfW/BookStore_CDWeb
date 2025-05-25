package com.nlu.cdweb.BookStore.repositories;

import com.nlu.cdweb.BookStore.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
}
