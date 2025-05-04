package com.nlu.cdweb.BookStore.mapper;

import com.nlu.cdweb.BookStore.dto.response.AccountResponse;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AccountMapper {



    public static AccountResponse toDTO(UserEntity user){
        return new AccountResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getRole().stream()
                        .map(roleEntity -> roleEntity.getName().name())
                        .collect(Collectors.toList())
        );
    }

    public static UserEntity toEntity(AccountResponse dto){
        if(dto == null) return null;
        UserEntity user = new UserEntity();
        user.setId(dto.getId());
        user.setUserName( dto.getUsername());
        user.setPasswordHash(dto.getPassword());
        user.setEmail(dto.getEmail());
        return user;
    }

}
