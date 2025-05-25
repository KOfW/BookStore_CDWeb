package com.nlu.cdweb.BookStore.dto.request;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nlu.cdweb.BookStore.entity.OrderDetailEntity;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Data
public class OrderRequest {
//    private Long id;
//    private UserEntity user;
//    private List<OrderDetailEntity> orderDetail = new ArrayList<>();
//    private Double price;
    private String status;
//    private Integer version;
}
