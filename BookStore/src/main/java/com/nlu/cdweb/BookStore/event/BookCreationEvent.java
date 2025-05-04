package com.nlu.cdweb.BookStore.event;

import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookCreationEvent extends ApplicationEvent {
    InventoryRequest inventoryDTO;

    public BookCreationEvent(Object source, InventoryRequest inventoryDTO) {
        super(source);
        this.inventoryDTO = inventoryDTO;
    }
}
