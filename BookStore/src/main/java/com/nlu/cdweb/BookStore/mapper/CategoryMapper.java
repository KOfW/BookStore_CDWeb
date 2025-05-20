package com.nlu.cdweb.BookStore.mapper;

import com.nlu.cdweb.BookStore.dto.request.CategoryRequest;
import com.nlu.cdweb.BookStore.dto.response.CategoryResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.CategoryEntity;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryMapper {

    @Autowired
    private BookRepository bookRepository;

    public CategoryEntity toEntity(CategoryRequest request){
        if(request == null) return null;

        CategoryEntity category = new CategoryEntity();
        category.setName(request.getName());

        return category;
    }
    public CategoryResponse toDTO(CategoryEntity entity){
        if(entity == null) return null;

        List<BookEntity> books = Optional.ofNullable(bookRepository.findByCategoryId(entity.getId()))
                .orElse(Collections.emptyList());

        CategoryResponse response = new CategoryResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setProductId(books.stream().map(BookEntity::getId).collect(Collectors.toList()));
        response.setVersion(entity.getVersion());

        return response;
    }
}
