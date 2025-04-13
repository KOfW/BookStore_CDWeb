package com.nlu.cdweb.BookStore.services;

import com.nlu.cdweb.BookStore.dto.request.LoginDTO;
import com.nlu.cdweb.BookStore.dto.request.RegisterDTO;
import com.nlu.cdweb.BookStore.entity.UserEntity;

import java.util.List;

public interface IUserService {
    public UserEntity addUser(RegisterDTO dto);
    public boolean userLogin(LoginDTO dto);
    public List<UserEntity> findAllUser();
    public boolean deleteUser(Long id);
}
