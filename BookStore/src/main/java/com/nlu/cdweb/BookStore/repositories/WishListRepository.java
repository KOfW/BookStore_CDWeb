package com.nlu.cdweb.BookStore.repositories;

import com.nlu.cdweb.BookStore.entity.WishListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishListEntity, Long> {
}
