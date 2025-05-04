package com.nlu.cdweb.BookStore.dto.request;

import com.nlu.cdweb.BookStore.entity.RoleEntity;
import com.nlu.cdweb.BookStore.validator.ValidUsernameOrEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username không được để trống")
    @ValidUsernameOrEmail
    private String userName;

    @NotBlank(message = "Password không được để trống")
    private String password;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Email khong được để trống")
    @ValidUsernameOrEmail
    private String email;
}
