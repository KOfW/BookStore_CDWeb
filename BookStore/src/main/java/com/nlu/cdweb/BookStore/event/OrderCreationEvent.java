package com.nlu.cdweb.BookStore.event;

import com.nlu.cdweb.BookStore.entity.CartEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class OrderCreationEvent extends ApplicationEvent {
    CartEntity cart;

    public OrderCreationEvent(Object source, CartEntity x) {
        super(source);
        this.cart=x;
    }
}
