package com.nlu.cdweb.BookStore.config;

import com.nlu.cdweb.BookStore.entity.RoleEntity;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import com.nlu.cdweb.BookStore.repositories.UserRepository;
import com.nlu.cdweb.BookStore.utils.State;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* Hệ thống sẽ tự gọi phương thức này khi đăng nhập */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(usernameOrEmail)
                .or(() -> userRepository.findByUserName(usernameOrEmail))
                .orElseThrow(() -> {
                    logger.error("User not found with identifier: {}", usernameOrEmail);
                    return new UsernameNotFoundException("User not found: " + usernameOrEmail);
                });

        logger.info("User roles from DB: {}", user.getRole());

        if (user.getRole().isEmpty()) {
            logger.warn("User has no roles assigned: {}", usernameOrEmail);
        }

        if(user.getState().equals(State.ACTIVE)){
            return new User(user.getUsername(), user.getPasswordHash(),
                    mapRolesToAuthorities(user.getRole()));
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<RoleEntity> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }
}