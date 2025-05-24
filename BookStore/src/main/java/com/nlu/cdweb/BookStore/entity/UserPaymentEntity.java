package com.nlu.cdweb.BookStore.entity;

import java.time.LocalDate;

public class UserPaymentEntity {
    private Long id;
    private UserEntity user;
    private String paymentType;
    private String paymentProvider;
    private Integer accountNumber;
    private LocalDate expireDate;
}
