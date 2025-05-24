package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.CartRequest;
import com.nlu.cdweb.BookStore.dto.response.CartResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICartService {
    public Page<CartResponse> findAll(int page, int size);
    public CartResponse findById(Long id);
    public CartResponse findByUserId(String token);
    public CartResponse create(CartRequest request, String token);
    public boolean delete(String token);
    public CartResponse update(CartRequest request, String token);
}
