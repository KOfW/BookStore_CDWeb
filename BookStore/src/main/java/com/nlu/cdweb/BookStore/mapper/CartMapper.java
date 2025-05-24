package com.nlu.cdweb.BookStore.mapper;

import com.nlu.cdweb.BookStore.config.JwtGenerator;
import com.nlu.cdweb.BookStore.dto.request.CartRequest;
import com.nlu.cdweb.BookStore.dto.response.CartResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.CartEntity;
import com.nlu.cdweb.BookStore.entity.CartItemEntity;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import com.nlu.cdweb.BookStore.repositories.CartItemRepository;
import com.nlu.cdweb.BookStore.repositories.CartRepositoriy;
import com.nlu.cdweb.BookStore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartMapper {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CartRepositoriy cartRepo;
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private JwtGenerator jwt;

    public CartEntity toEntity(CartRequest request ,String token){
        if(token == null) return null;

        String inputUser = jwt.getUsernameFromJwt(token);
        CartEntity entity = new CartEntity();

        UserEntity user = userRepo.findByEmailOrUsername(inputUser).orElseThrow(RuntimeException::new);
        List<CartItemEntity> cartItems = new ArrayList<>();
        for(Long c : request.getCartItemId()){
            cartItems.add(cartItemRepo.findById(c).orElseThrow(RuntimeException::new));
        }

        entity.setUser(user);
        entity.setCartItem(cartItems);

        return entity;
    }

    public CartResponse toDTO(CartEntity entity){
        if(entity == null) return null;

        Map<String, Integer> cartItem = entity.getCartItem().stream().collect(Collectors.toMap(item -> item.getBook().getName(), CartItemEntity::getQuantity));
        CartResponse response = new CartResponse();
        response.setId(entity.getId());
        response.setUserId(entity.getUser().getId());
        response.setCartItem(cartItem);
        response.setTotal(entity.getTotal());

        return response;
    }
}
