package com.nlu.cdweb.BookStore.mapper;

import com.nlu.cdweb.BookStore.config.JwtGenerator;
import com.nlu.cdweb.BookStore.dto.request.CartItemRequest;
import com.nlu.cdweb.BookStore.dto.response.CartItemResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.CartItemEntity;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import com.nlu.cdweb.BookStore.repositories.CartItemRepository;
import com.nlu.cdweb.BookStore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemMapper {

    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BookRepository bookRepo;
    @Autowired
    private JwtGenerator generator;

    public CartItemResponse toDTO(CartItemEntity entity){
        if(entity == null) return null;

        CartItemResponse response = new CartItemResponse();
        response.setId(entity.getId());
        response.setUserId(entity.getUser().getId());
        response.setBookId(entity.getBook().getId());
        response.setQuantity(entity.getQuantity());

        return response;
    }

    public CartItemEntity toEntity(CartItemRequest request) {
        if (request == null) {
            return null;
        }

        UserEntity user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        BookEntity book = bookRepo.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + request.getBookId()));

        CartItemEntity entity = new CartItemEntity();
        entity.setUser(user);
        entity.setBook(book);
        entity.setQuantity(request.getQuantity());

        return entity;
    }
}
