package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.LoginRequest;
import com.nlu.cdweb.BookStore.dto.request.RegisterRequest;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import com.nlu.cdweb.BookStore.utils.State;

import java.util.List;

public interface IUserService {
    public UserEntity addUser(RegisterRequest dto);
    public String userLogin(LoginRequest dto);
    public List<UserEntity> findAllUser();
    public String sendOTP(String email);
    public boolean deleteUser(Long id);
    public boolean userActive(String email, State state);
}
