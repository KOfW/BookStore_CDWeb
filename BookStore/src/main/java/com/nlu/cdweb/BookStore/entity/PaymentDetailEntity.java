package com.nlu.cdweb.BookStore.entity;

public class PaymentDetailEntity {
    private Long id;
    private OrderEntity order;
    private Double amount;
    private String paymentProvider;
    private String paymentStatus;
    private Integer version;
}
