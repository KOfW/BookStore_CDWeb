package com.nlu.cdweb.BookStore.services.event_listener;

import com.nlu.cdweb.BookStore.client.InventoryApiClient;
import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.entity.CartItemEntity;
import com.nlu.cdweb.BookStore.event.OrderCreationEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(makeFinal=true,level= AccessLevel.PRIVATE)
@Service
public class UpdateInventoryService {
    InventoryApiClient client;

    @EventListener
//    @Order(2)
    public void updateInventory(OrderCreationEvent event){
        var order = event.getCart();
        Map<Long, Integer> map = order.getCartItem().stream().collect(Collectors.toMap(CartItemEntity::getId, CartItemEntity::getQuantity));
        map.keySet()
                .forEach(id->client.updateInventory(new InventoryRequest(id,map.get(id))));
    }
}
