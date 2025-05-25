package com.nlu.cdweb.BookStore.mapper;

import com.nlu.cdweb.BookStore.config.JwtGenerator;
import com.nlu.cdweb.BookStore.dto.request.OrderRequest;
import com.nlu.cdweb.BookStore.dto.response.CartResponse;
import com.nlu.cdweb.BookStore.dto.response.OrderResponse;
import com.nlu.cdweb.BookStore.entity.*;
import com.nlu.cdweb.BookStore.exception.EntityNotFoundException;
import com.nlu.cdweb.BookStore.repositories.CartRepositoriy;
import com.nlu.cdweb.BookStore.repositories.OrderDetailRepository;
import com.nlu.cdweb.BookStore.repositories.OrderRepository;
import com.nlu.cdweb.BookStore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class OrderMapper {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private OrderDetailRepository orderDetailRepo;
    @Autowired
    private CartRepositoriy cartRepo;
    @Autowired
    private JwtGenerator jwt;
    @Autowired
    private UserRepository userRepo;

//    public OrderEntity toEntity(OrderRequest request ,String token){
//        String inputName = jwt.getUsernameFromJwt(token);
//        UserEntity user = userRepo.findByEmailOrUsername(inputName)
//                .orElseThrow(() -> new EntityNotFoundException("Error get User at OrderMapper"));
//
//        CartEntity cart = cartRepo.findByUser_Id(user.getId());
//        if (cart == null) {
//            throw new EntityNotFoundException("Cart not found for user: " + user.getId());
//        }
//
//        OrderEntity entity = new OrderEntity();
//        entity.setUser(user);
//        entity.setStatus("PENDING");
//        entity.setPrice(cart.getTotal());
//
//        orderRepo.save(entity);
//
//        List<OrderDetailEntity> orderDetails = new ArrayList<>();
//
//        for(CartItemEntity cartItem : cart.getCartItem()){
//            OrderDetailEntity orderDetail = new OrderDetailEntity();
//            orderDetail.setBook(cartItem.getBook());
//            orderDetail.setQuantity(cartItem.getQuantity());
//            orderDetail.setOrder(entity);
//            orderDetailRepo.save(orderDetail);
//            orderDetails.add(orderDetail);
//        }
//
//        entity.setOrderDetail(orderDetails);
//        return entity;
//    }

    public OrderEntity toEntity(CartEntity cart){
        if(cart == null) return null;

        OrderEntity order = new OrderEntity();
        order.setUser(cart.getUser());
        order.setStatus("PENDING");
        order.setPrice(cart.getTotal());

        List<OrderDetailEntity> orderDetails = cart.getCartItem().stream().map(item -> {
            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setBook(item.getBook());
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setOrder(order);
            return orderDetail;
        }).toList();

        order.setOrderDetail(orderDetails);

        return order;
    }

    public OrderResponse toDTO(OrderEntity entity){
        if(entity == null) return null;

        Map<String, Integer> orderItems = entity.getOrderDetail().stream().collect(Collectors.toMap(item -> item.getBook().getName(), OrderDetailEntity::getQuantity));

        OrderResponse response = new OrderResponse();
        response.setId(entity.getId());
        response.setStatus(entity.getStatus());
        response.setPrice(entity.getPrice());
        response.setUser(entity.getUser().getId());
        response.setVersion(entity.getVersion());
        response.setOrderItem(orderItems);

        return response;
    }
}
