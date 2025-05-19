package com.nlu.cdweb.BookStore.mapper;

import com.nlu.cdweb.BookStore.dto.request.BookRequest;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.CategoryEntity;
import com.nlu.cdweb.BookStore.exception.EntityNotFoundException;
import com.nlu.cdweb.BookStore.repositories.CategoryRepository;
import com.nlu.cdweb.BookStore.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    private CategoryRepository categoryRepository;
    private DiscountRepository discountRepository;
    @Autowired
    public BookMapper(CategoryRepository categoryRepository, DiscountRepository discountRepository) {
        this.categoryRepository = categoryRepository;
        this.discountRepository = discountRepository;
    }

    public BookEntity toEntity(BookRequest dto){
        if(dto == null) return null;

        BookEntity bookEntity = new BookEntity();
        bookEntity.setName(dto.getName());
        bookEntity.setDesc(dto.getDesc());
        bookEntity.setSku(dto.getSku());
        bookEntity.setPrice(dto.getPrice());

        bookEntity.setCategory(categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Not fount category with id: "+dto.getCategoryId())));
        bookEntity.setDiscount(discountRepository.findById(dto.getDiscountId()).orElseThrow(() -> new EntityNotFoundException("Not fount discount with id: "+dto.getDiscountId())));

        return bookEntity;
    }
}
