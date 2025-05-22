package com.nlu.cdweb.BookStore.mapper;

import com.nlu.cdweb.BookStore.dto.request.DiscountRequest;
import com.nlu.cdweb.BookStore.dto.response.DiscountResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.DiscountEntity;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DiscountMapper {
    @Autowired
    private BookRepository bookRepo;

    public DiscountResponse toDTO(DiscountEntity entity){
        if(entity == null) return null;

        List<BookEntity> books = bookRepo.findByDiscountId(entity.getId());

        DiscountResponse response = new DiscountResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDesc(entity.getDesc());
        response.setPercent(entity.getPercent());
        response.setActive(entity.getActive());
        response.setBookId(books.stream().map(BookEntity::getId).collect(Collectors.toList()));
        response.setVersion(entity.getVersion());

        return response;
    }

    public DiscountEntity toEntity(DiscountRequest request){
        if(request == null) return null;

        DiscountEntity entity = new DiscountEntity();
        entity.setName(request.getName());
        entity.setDesc(request.getDesc());
        entity.setPercent(request.getPercent());
        entity.setActive(request.getActive());

        return entity;
    }
}
