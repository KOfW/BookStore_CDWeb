package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.AddBookToDiscountRequest;
import com.nlu.cdweb.BookStore.dto.request.DiscountRequest;
import com.nlu.cdweb.BookStore.dto.response.DiscountResponse;
import org.springframework.data.domain.Page;

public interface IDiscountService {
    public Page<DiscountResponse> findAll(int page, int size);
    public DiscountResponse findById(Long id);
    public DiscountResponse create(DiscountRequest request);
    public boolean delete(Long id);
    public DiscountResponse update(Long id, DiscountRequest request);
    public DiscountResponse addProduct(AddBookToDiscountRequest request);
}
