package com.nlu.cdweb.BookStore.services.impl;

import com.nlu.cdweb.BookStore.config.JwtGenerator;
import com.nlu.cdweb.BookStore.dto.request.CartItemRequest;
import com.nlu.cdweb.BookStore.dto.response.CartItemResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.CartItemEntity;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import com.nlu.cdweb.BookStore.mapper.CartItemMapper;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import com.nlu.cdweb.BookStore.repositories.CartItemRepository;
import com.nlu.cdweb.BookStore.repositories.UserRepository;
import com.nlu.cdweb.BookStore.services.ICartService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CartServiceImpl implements ICartService {
    @Autowired
    private BookRepository bookRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private CartItemMapper mapper;
    @Autowired
    private JwtGenerator generator;

    @Override
    public CartItemResponse findById(Long id) {
        CartItemEntity entity = cartItemRepo.findById(id).orElseThrow();
        return mapper.toDTO(entity);
    }

    @Override
    public Page<CartItemResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CartItemEntity> entities = cartItemRepo.findAll(pageable);
        return entities.map(mapper::toDTO);
    }

    @Override
    public List<CartItemResponse> findByUserId(Long id, String token) {
        String username = generator.getUsernameFromJwt(token);
        UserEntity user = userRepo.findByEmail(username).orElseThrow();
        id = user.getId();
        List<CartItemEntity> cart = cartItemRepo.findByUser_Id(id);
        return cart.stream().map(mapper::toDTO).toList();
    }

    @Override
    public CartItemResponse create(CartItemRequest request, String token) {
        String input = generator.getUsernameFromJwt(token);
        System.out.println(input);
        UserEntity user = userRepo.findByEmailOrUsername(input)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!request.getUserId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized action for this user");
        }

        boolean isProductAlreadyAdded = cartItemRepo
                .existsByUser_IdAndBook_Id(user.getId(), request.getBookId());

        if (isProductAlreadyAdded) {
            throw new RuntimeException("Product already exists in the cart");
        }

        CartItemEntity entity = mapper.toEntity(request);
        CartItemEntity saved = cartItemRepo.save(entity);

        return mapper.toDTO(saved);
    }


    @Override
    public boolean delete(Long bookId, String token) {
        String email = generator.getUsernameFromJwt(token);
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        CartItemEntity cartItem = cartItemRepo.findByUser_IdAAndBook_Id(user.getId(), bookId);

        if (cartItem != null) {
            cartItemRepo.delete(cartItem);
            return true;
        }

        return false;
    }

    @Override
    public CartItemResponse update(CartItemRequest request, String token) {
        String username = generator.getUsernameFromJwt(token);
        UserEntity user = userRepo.findByEmail(username).orElseThrow();
        if (!request.getUserId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized action for this user");
        }
        CartItemEntity cartItem = cartItemRepo.findByUser_IdAAndBook_Id(user.getId(), request.getBookId());
        cartItem.setQuantity(request.getQuantity());
        return mapper.toDTO(cartItemRepo.save(cartItem));
    }
}
