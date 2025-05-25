package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.OrderRequest;
import com.nlu.cdweb.BookStore.dto.response.CartResponse;
import com.nlu.cdweb.BookStore.dto.response.OrderResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderService {
    public Page<OrderResponse> findAll(int page, int size);
    public OrderResponse findById(Long id);
    public List<OrderResponse> findByUserId(String token);
    public OrderResponse create(OrderRequest request, String token);
    public boolean delete(Long id);
    public OrderResponse update(Long id, OrderRequest request);
}
