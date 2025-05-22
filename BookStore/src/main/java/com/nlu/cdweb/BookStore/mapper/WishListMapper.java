package com.nlu.cdweb.BookStore.mapper;

import com.nlu.cdweb.BookStore.dto.request.WishListRequest;
import com.nlu.cdweb.BookStore.dto.response.WishListResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import com.nlu.cdweb.BookStore.entity.WishListEntity;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import com.nlu.cdweb.BookStore.repositories.UserRepository;
import com.nlu.cdweb.BookStore.repositories.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WishListMapper {
    @Autowired
    private WishListRepository wishListRepo;
    @Autowired
    private BookRepository bookRepo;
    @Autowired
    private UserRepository userRepo;

    public WishListEntity toEntity(WishListRequest request){
        if(request == null) return null;

        Optional<BookEntity> book =  bookRepo.findById(request.getBookId());
        Optional<UserEntity> user =  userRepo.findById(request.getUserId());

        WishListEntity entity = new WishListEntity();
        entity.setBook(book.orElseThrow());
        entity.setUser(user.orElseThrow());

        return entity;
    }

    public WishListResponse toDTO(WishListEntity entity){
        if(entity == null) return null;

        WishListResponse response = new WishListResponse();
        response.setBookId(entity.getBook().getId());
        response.setUserId(entity.getUser().getId());

        return response;
    }
}
