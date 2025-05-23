package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.CartItemRequest;
import com.nlu.cdweb.BookStore.dto.response.CartItemResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICartService {
    public CartItemResponse findById(Long id);
    public Page<CartItemResponse> findAll(int page, int size);
    public List<CartItemResponse> findByUserId(Long id, String jwt);
    public CartItemResponse create(CartItemRequest request, String jwt);
    public boolean delete(Long bookId, String jwt);
    public CartItemResponse update(CartItemRequest request, String jwt);
}
