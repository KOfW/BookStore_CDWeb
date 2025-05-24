package com.nlu.cdweb.BookStore.services.impl;

import com.nlu.cdweb.BookStore.config.JwtGenerator;
import com.nlu.cdweb.BookStore.dto.request.CartRequest;
import com.nlu.cdweb.BookStore.dto.response.CartResponse;
import com.nlu.cdweb.BookStore.entity.CartEntity;
import com.nlu.cdweb.BookStore.entity.CartItemEntity;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import com.nlu.cdweb.BookStore.exception.EntityNotFoundException;
import com.nlu.cdweb.BookStore.mapper.CartMapper;
import com.nlu.cdweb.BookStore.repositories.CartItemRepository;
import com.nlu.cdweb.BookStore.repositories.CartRepositoriy;
import com.nlu.cdweb.BookStore.repositories.UserRepository;
import com.nlu.cdweb.BookStore.services.ICartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartRepositoriy cartRepo;
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CartMapper mapper;
    @Autowired
    private JwtGenerator jwt;

    @Override
    public Page<CartResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CartEntity> pageCart = cartRepo.findAll(pageable);
        return pageCart.map(mapper::toDTO);
    }

    @Override
    public CartResponse findById(Long id) {
        CartEntity entity = cartRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found cart with id: "+id));
        return mapper.toDTO(entity);
    }

    @Override
    public CartResponse findByUserId(String token) {
        String inputName = jwt.getUsernameFromJwt(token);
        UserEntity user = userRepo.findByEmailOrUsername(inputName).orElseThrow(() -> new EntityNotFoundException("Not found user with name: "+inputName));
        CartEntity cart = cartRepo.findByUser_Id(user.getId());

        return mapper.toDTO(cart);
    }

    @Override
    @Transactional
    public CartResponse create(CartRequest request, String token) {
        String inputName = jwt.getUsernameFromJwt(token);
        UserEntity user = userRepo.findByEmailOrUsername(inputName)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        CartEntity cart = new CartEntity();
        cart.setUser(user);

        List<CartItemEntity> items = new ArrayList<>();
        for(Long id : request.getCartItemId()) {
            CartItemEntity item = cartItemRepo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
            items.add(item);
            item.setCart(cart); // Bidirectional relationship
            cartItemRepo.save(item);
        }

        cart.setCartItem(items);
        cart.setTotal(calculateTotal(items));

        return mapper.toDTO(cartRepo.save(cart));
    }

    private Double calculateTotal(List<CartItemEntity> items) {
        return items.stream()
                .mapToDouble(i -> i.getQuantity() * i.getBook().getPrice())
                .sum();
    }

    @Override
    public boolean delete(String token) {
        String inputName = jwt.getUsernameFromJwt(token);
        UserEntity user = userRepo.findByEmailOrUsername(inputName).orElseThrow(RuntimeException::new);

        CartEntity cart = cartRepo.findByUser_Id(user.getId());
        cartRepo.delete(cart);

        return true;
    }

    @Override
    public CartResponse update(CartRequest request, String token) {
        String inputName = jwt.getUsernameFromJwt(token);
        UserEntity user = userRepo.findByEmailOrUsername(inputName).orElseThrow(RuntimeException::new);

        CartEntity cart = cartRepo.findByUser_Id(user.getId());

        List<CartItemEntity> cartItems = cart.getCartItem();

        for(Long ids : request.getCartItemId()){
            cartItems.add(cartItemRepo.findById(ids).orElseThrow(RuntimeException::new));
        }

        Double total = cartItems.stream().mapToDouble(cartItemTotal -> cartItemTotal.getQuantity() * cartItemTotal.getBook().getPrice()).sum();


        cart.setCartItem(cartItems);
        cart.setTotal(total);

        cartRepo.save(cart);

        return mapper.toDTO(cart);
    }

}
