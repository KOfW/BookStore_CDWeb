package com.nlu.cdweb.BookStore.controller;


import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.request.LoginRequest;
import com.nlu.cdweb.BookStore.dto.request.RegisterRequest;
import com.nlu.cdweb.BookStore.dto.response.AccountResponse;
import com.nlu.cdweb.BookStore.dto.response.AuthResponse;
import com.nlu.cdweb.BookStore.mapper.AccountMapper;
import com.nlu.cdweb.BookStore.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
public class UserController {
    private final IUserService userService;
    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

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

    @PostMapping("/register")
    ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest dto) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", "Register Successfully", userService.addUser(dto)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse("failed", "register failed", ""));
        }
    }

    @GetMapping("/getAll")
    ResponseEntity<ApiResponse> getAllUser() {
        try{
            List<AccountResponse> dtos = userService.findAllUser()
                    .stream()
                    .map(AccountMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("success", "This is List User", dtos));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("failed", "Cant get List User", ""));
        }
    }

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
