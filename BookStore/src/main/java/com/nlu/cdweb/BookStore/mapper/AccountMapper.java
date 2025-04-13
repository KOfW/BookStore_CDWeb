package com.nlu.cdweb.BookStore.mapper;

import com.nlu.cdweb.BookStore.dto.response.AccountDTO;
import com.nlu.cdweb.BookStore.entity.RoleEntity;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AccountMapper {
    public static AccountDTO toDTO(UserEntity user){
        return new AccountDTO(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getPasswordHash(),
                user.getRoles().stream()
                        .map(RoleEntity::getName)
                        .collect(Collectors.toList())
        );
    }

    public static UserEntity toEntity(AccountDTO dto){
        if(dto == null) return null;
        UserEntity user = new UserEntity();
        user.setId(dto.getId());
        user.setUserName(dto.getUsername());
        user.setPasswordHash(dto.getPassword());
        user.setEmail(dto.getEmail());
        return user;
    }

}
