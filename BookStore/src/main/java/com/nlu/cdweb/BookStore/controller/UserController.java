package com.nlu.cdweb.BookStore.controller;


import com.nlu.cdweb.BookStore.config.JwtGenerator;
import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.request.*;
import com.nlu.cdweb.BookStore.dto.response.AuthResponse;

import com.nlu.cdweb.BookStore.services.IOTPService;
import com.nlu.cdweb.BookStore.services.IUserService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class UserController {
    private final IUserService userService;
    private final IOTPService otpService;
    private final JwtGenerator generator;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    public UserController(IUserService userService, IOTPService otpService, JwtGenerator generator) {
        this.userService = userService;
        this.otpService = otpService;
        this.generator = generator;
    }
    /*Chức năng đăng nhập*/
    @PostMapping("/login")
    ResponseEntity<ApiResponse> login(@RequestBody LoginRequest dto){
        try{
            String checkLogin = userService.userLogin(dto);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("ok", "login successfully",new AuthResponse(checkLogin)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("failed", "login failed", "No object"));
        }
    }
    /*Chức năng đăng ký*/
    @PostMapping("/register")
    ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest dto) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", "Register Successfully", userService.addUser(dto)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse("failed", "register failed", ""));
        }
    }
    /*Lấy tất user theo page và size*/
    @GetMapping("/getAllUser")
    ResponseEntity<ApiResponse> getAllUser(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", "This is a list of user", userService.getAllAccount(page, size)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse("failed", "Cant found any user", ""));
        }
    }
    /*Lấy user theo id*/
    @GetMapping("/user/{id}")
    ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", "This is user with id: "+id, userService.findUserById(id)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse("failed", "Cant found any user", ""));
        }
    }
    /*Gửi yêu cầu quên mật khẩu sau khi nhập form email*/
    @PostMapping("/requestResetPassword")
    ResponseEntity<ApiResponse> requestResetPassword(@RequestBody Email email) {
        try {
            Map<String, String> response = userService.requestResetPassword(email);
            if (response != null) {
                return ResponseEntity.ok(new ApiResponse("success", "OTP sent successfully", response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("failed", "Failed to send OTP", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Internal error while sending OTP", false));
        }
    }
    /*Xác thực mã OTP khi người dùng điên vào form OTP*/
    @PostMapping("/responseResetPassword")
    ResponseEntity<ApiResponse> responseResetPassword(@RequestBody VerifyRequest request) {
        try {
            boolean valid = userService.responseResetPassword(request);
            if (valid) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse("ok", "success", request.getToken()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("failed", "Failed", false));            }
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("failed", "Failed", false));
        }
    }
    /*Đổi mật khảu sau khi người dùng nhập vào form nhập lại mật khẩu mới*/
    @PostMapping("/resetPassword")
    ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPassword resetPassword) {
        try {
            String email = jwtGenerator.getEmailFromJwtOtp(resetPassword.getToken());
            String checkPassword = userService.resetPassword(email, resetPassword.getPassword(), resetPassword.getRepassword());
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("ok", "reset password successfully",checkPassword));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("failed", "reset password failed",false));
        }
    }
    /*Xóa user theo id của user*/
    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id){
        try{
            boolean deleteUser = userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("ok", "delete user "+ id +" successfully", deleteUser));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("failed", "register failed", false));
        }
    }
}
