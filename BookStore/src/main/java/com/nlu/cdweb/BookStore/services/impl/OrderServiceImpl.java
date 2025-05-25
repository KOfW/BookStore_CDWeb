package com.nlu.cdweb.BookStore.services.impl;

import com.nlu.cdweb.BookStore.config.JwtGenerator;
import com.nlu.cdweb.BookStore.dto.request.OrderRequest;
import com.nlu.cdweb.BookStore.dto.response.OrderResponse;
import com.nlu.cdweb.BookStore.entity.*;
import com.nlu.cdweb.BookStore.event.OrderCreationEvent;
import com.nlu.cdweb.BookStore.event.OrderFailedEvent;
import com.nlu.cdweb.BookStore.exception.EntityNotFoundException;
import com.nlu.cdweb.BookStore.mapper.OrderMapper;
import com.nlu.cdweb.BookStore.repositories.*;
import com.nlu.cdweb.BookStore.services.IOrderService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private OrderDetailRepository orderItemRepo;
    @Autowired
    private CartRepositoriy cartRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private InventoryRepository inventoryRepo;
    @Autowired
    private OrderMapper mapper;
    @Autowired
    private JwtGenerator jwt;
    @Autowired
    private ProductAvaiability productAvaiability;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Override
    public Page<OrderResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderEntity> responses = orderRepo.findAll(pageable);
        return responses.map(mapper::toDTO);
    }

    @Override
    public OrderResponse findById(Long id) {
        return mapper.toDTO(orderRepo.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    public List<OrderResponse> findByUserId(String token) {
        String inputName = jwt.getUsernameFromJwt(token);
        UserEntity user = userRepo.findByEmailOrUsername(inputName).orElseThrow(RuntimeException::new);
        List<OrderEntity> order = orderRepo.findByUser_Id(user.getId());
        return order.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public OrderResponse create(OrderRequest request, String token) {
        String inputName = jwt.getUsernameFromJwt(token);
        UserEntity user = userRepo.findByEmailOrUsername(inputName).orElseThrow(RuntimeException::new);

        CartEntity cart = cartRepo.findByUser_Id(user.getId());

        Map<Long, Integer> map = cart.getCartItem().stream().collect(Collectors.toMap(item -> item.getBook().getId(), CartItemEntity::getQuantity));

        if(user != null){
            boolean areProductsAvailable = productAvaiability.checkAvaiability(map);
            if(areProductsAvailable){
                try{
                    eventPublisher.publishEvent(new OrderCreationEvent(this, cart));
                }catch(Exception e){
                    eventPublisher.publishEvent(new OrderFailedEvent(this,"Failed to place the order"));
                }
                OrderEntity order = mapper.toEntity(cart);
                return mapper.toDTO(orderRepo.save(order));
            }else{
                throw new RuntimeException("Products aren't available for this order");
            }
        }else{
            throw new EntityNotFoundException("user with id " + cart.getUser().getId() + " isn't found");
        }
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public OrderResponse update(Long id, OrderRequest request) {
        return null;
    }
}
