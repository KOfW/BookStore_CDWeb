package com.nlu.cdweb.BookStore.dto.request;

import com.nlu.cdweb.BookStore.validator.ValidUsernameOrEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Username hoặc email không được để trống")
    @ValidUsernameOrEmail
    private String username;

    @NotBlank(message = "Password không được để trống")
    private String password;
}
