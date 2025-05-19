package com.nlu.cdweb.BookStore.mapper;

import com.nlu.cdweb.BookStore.dto.request.RegisterRequest;
import com.nlu.cdweb.BookStore.dto.response.AccountResponse;
import com.nlu.cdweb.BookStore.entity.RoleEntity;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import com.nlu.cdweb.BookStore.repositories.RoleRepository;

import com.nlu.cdweb.BookStore.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AccountMapper {
    private final PasswordEncoder encoder;
    private final RoleRepository repo;
    @Autowired
    public AccountMapper(@Lazy PasswordEncoder encoder, RoleRepository repo) {
        this.encoder = encoder;
        this.repo = repo;
    }

    public AccountResponse toDTO(UserEntity user){
        return new AccountResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().stream()
                        .map(roleEntity -> roleEntity.getName().name())
                        .collect(Collectors.toList())
        );
    }

    public UserEntity toEntity(RegisterRequest dto){
        if(dto == null) return null;
        Optional<RoleEntity> role = repo.findByName(Role.USER);
        UserEntity user = new UserEntity();
        user.setUserName( dto.getUserName());
        user.setPasswordHash(encoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        role.ifPresent(r -> user.setRole(List.of(r)));
        return user;
    }

}
