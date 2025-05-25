package com.nlu.cdweb.BookStore.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class OrderFailedEvent extends ApplicationEvent {
    String errorMessage;

    public OrderFailedEvent(Object source,String errorMessage) {
        super(source);
        this.errorMessage=errorMessage;
    }
}
