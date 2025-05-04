package com.nlu.cdweb.BookStore.services.event_listener;

import com.nlu.cdweb.BookStore.client.InventoryApiClient;
import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.event.BookCreationEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(makeFinal=true,level= AccessLevel.PRIVATE)
@Service
public class InventoryCreationService {
    InventoryApiClient inventoryApiClient;

    @EventListener
    @Async
    public void createInventory(BookCreationEvent event){
        InventoryRequest inventoryDTO = event.getInventoryDTO();
        inventoryApiClient.createInventory(inventoryDTO);
    }
}
