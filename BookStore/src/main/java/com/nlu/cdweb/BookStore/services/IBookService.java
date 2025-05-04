package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.BookRequest;
import com.nlu.cdweb.BookStore.entity.BookEntity;

public interface IBookService {
    public BookEntity addBook(BookRequest book);
}
