package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.CartItemRequest;
import com.nlu.cdweb.BookStore.dto.response.CartItemResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICartItemService {
    public CartItemResponse findById(Long id);
    public Page<CartItemResponse> findAll(int page, int size);
    public CartItemResponse create(CartItemRequest request);
    public boolean delete(Long id);
    public CartItemResponse update(Long id, CartItemRequest request);
}
