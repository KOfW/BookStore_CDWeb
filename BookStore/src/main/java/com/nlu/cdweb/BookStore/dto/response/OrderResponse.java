package com.nlu.cdweb.BookStore.dto.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nlu.cdweb.BookStore.entity.OrderDetailEntity;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Data
public class OrderResponse {
    private Long id;
    private Long user;
    private Map<String, Integer> orderItem;
    private Double price;
    private String status;
    private Integer version;
}
