package com.nlu.cdweb.BookStore.repositories;

import com.nlu.cdweb.BookStore.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public UserEntity findAllById(Long id);
    public Optional<UserEntity> findByUsername(String username);
    public Optional<UserEntity> findByEmail(String email);
    public List<UserEntity> findAll();
    public void deleteById(Long id);
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
}
