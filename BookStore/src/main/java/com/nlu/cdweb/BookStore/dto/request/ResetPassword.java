package com.nlu.cdweb.BookStore.dto.request;

import lombok.Data;

@Data
public class ResetPassword {
    private String token;
    private String password;
    private String repassword;
}
