package com.nlu.cdweb.BookStore.repositories;

import com.nlu.cdweb.BookStore.entity.CartItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    public List<CartItemEntity> findByUser_Id(Long id);
    @Query("select c from CartItemEntity c where c.user.id = :userId and c.book.id = :bookId")
    public CartItemEntity findByUser_IdAAndBook_Id(@Param("userId") Long userId,@Param("bookId") Long bookId);
    public boolean existsByUser_IdAndBook_Id(Long userId, Long bookId);
}
