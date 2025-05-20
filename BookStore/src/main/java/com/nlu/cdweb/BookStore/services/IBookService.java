package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.BookRequest;
import com.nlu.cdweb.BookStore.dto.response.BookResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IBookService {
    public Page<BookResponse> findAll(int page, int size);
    public BookResponse findBookById(Long id);
    public BookEntity addBook(BookRequest book);
    public boolean deleteBookById(Long id);
    public BookResponse updateBookById(Long id, BookRequest request);
    public List<BookResponse> findBookByCategoryId(Long id);
    public Page<BookResponse> search(String name, String desc, Boolean discountStatus, String categoryName, Double minPrice, Double maxPrice, int page, int size);
}
