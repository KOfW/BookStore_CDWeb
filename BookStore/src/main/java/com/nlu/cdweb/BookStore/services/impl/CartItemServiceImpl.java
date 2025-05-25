package com.nlu.cdweb.BookStore.services.impl;

import com.nlu.cdweb.BookStore.config.JwtGenerator;
import com.nlu.cdweb.BookStore.dto.request.CartItemRequest;
import com.nlu.cdweb.BookStore.dto.response.CartItemResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.CartEntity;
import com.nlu.cdweb.BookStore.entity.CartItemEntity;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import com.nlu.cdweb.BookStore.exception.EntityNotFoundException;
import com.nlu.cdweb.BookStore.mapper.CartItemMapper;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import com.nlu.cdweb.BookStore.repositories.CartItemRepository;
import com.nlu.cdweb.BookStore.repositories.CartRepositoriy;
import com.nlu.cdweb.BookStore.repositories.UserRepository;
import com.nlu.cdweb.BookStore.services.ICartItemService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CartItemServiceImpl implements ICartItemService {
    @Autowired
    private BookRepository bookRepo;
    @Autowired
    private CartRepositoriy cartRepo;
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private CartItemMapper mapper;
    @Autowired
    private JwtGenerator generator;

    @Override
    public CartItemResponse findById(Long id) {
        CartItemEntity entity = cartItemRepo.findById(id).orElseThrow();
        return mapper.toDTO(entity);
    }

    @Override
    public Page<CartItemResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CartItemEntity> entities = cartItemRepo.findAll(pageable);
        return entities.map(mapper::toDTO);
    }

    @Override
    public CartItemResponse create(CartItemRequest request) {
        return mapper.toDTO(cartItemRepo.save(mapper.toEntity(request)));
    }

    @Override
    public boolean delete(Long id) {
        CartItemEntity cartItem = cartItemRepo.findById(id).orElseThrow(RuntimeException::new);
        cartItemRepo.delete(cartItem);
        return true;
    }

    @Override
    public CartItemResponse update(Long id, CartItemRequest request) {
        CartItemEntity cartItem = cartItemRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Can find cart item with id: "+id));
        cartItem.setBook(bookRepo.findById(request.getBookId()).orElseThrow(() -> new EntityNotFoundException("Can find book with id: "+request.getBookId())));
        cartItem.setCart(cartRepo.findById(request.getCartId()).orElseThrow(() -> new EntityNotFoundException("Can find cart with id: "+request.getCartId())));
        cartItem.setQuantity(request.getQuantity());
        return mapper.toDTO(cartItemRepo.save(cartItem));
    }
}
