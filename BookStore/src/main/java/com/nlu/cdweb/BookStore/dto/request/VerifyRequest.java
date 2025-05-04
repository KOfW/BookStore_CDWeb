package com.nlu.cdweb.BookStore.dto.request;

import lombok.Data;

@Data
public class VerifyRequest {
    private String token;
    private String otp;
}
