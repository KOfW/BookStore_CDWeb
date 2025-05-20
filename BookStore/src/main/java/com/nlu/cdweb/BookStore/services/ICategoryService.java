package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.AddBookToCategoryRequest;
import com.nlu.cdweb.BookStore.dto.request.CategoryRequest;
import com.nlu.cdweb.BookStore.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;

public interface ICategoryService {
    public Page<CategoryResponse> findAll(int page, int size);
    public CategoryResponse findCategoryById(Long id);
    public CategoryResponse create(CategoryRequest request);
    public boolean delete(Long id);
    public CategoryResponse update(Long id, CategoryRequest request);
    public Page<CategoryResponse> search(String name, String productName, int page, int size);
    public CategoryResponse addBookToCategory(AddBookToCategoryRequest request);
}
