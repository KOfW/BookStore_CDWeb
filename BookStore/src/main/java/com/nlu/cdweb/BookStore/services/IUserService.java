package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.Email;
import com.nlu.cdweb.BookStore.dto.request.LoginRequest;
import com.nlu.cdweb.BookStore.dto.request.RegisterRequest;
import com.nlu.cdweb.BookStore.dto.request.VerifyRequest;
import com.nlu.cdweb.BookStore.dto.response.AccountResponse;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import com.nlu.cdweb.BookStore.utils.State;
import org.springframework.data.domain.Page;

import java.util.Map;


public interface IUserService {
    public UserEntity addUser(RegisterRequest dto);
    public String userLogin(LoginRequest dto);
    public boolean deleteUser(Long id);
    public boolean userActive(String email, State state);
    public AccountResponse findUserById(Long id);
    public Map<String, String> requestResetPassword(Email email);
    public boolean responseResetPassword(VerifyRequest request);
    public String resetPassword(String email, String password, String repassowrd);
    Page<AccountResponse> getAllAccount(int page, int size);
}
