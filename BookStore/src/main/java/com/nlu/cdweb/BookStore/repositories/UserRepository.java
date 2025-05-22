package com.nlu.cdweb.BookStore.repositories;

import com.nlu.cdweb.BookStore.entity.UserEntity;
import com.nlu.cdweb.BookStore.utils.State;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public Optional<UserEntity> findById(Long id);
    public Optional<UserEntity> findByUserName(String userName);
    public Optional<UserEntity> findByEmail(String email);
    @Query("SELECT u FROM UserEntity u WHERE u.email = :input OR u.userName = :input")
    Optional<UserEntity> findByEmailOrUsername(@Param("input") String input);
    public void deleteById(Long id);
    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.state = :state WHERE u.email = :email")
    void userActive(@Param("state") State state, @Param("email") String email);
    public boolean existsByUserName(String userName);
    public boolean existsByEmail(String email);
}
