    package com.nlu.cdweb.BookStore.services.impl;

    import com.nlu.cdweb.BookStore.dto.request.LoginDTO;
    import com.nlu.cdweb.BookStore.dto.request.RegisterDTO;
    import com.nlu.cdweb.BookStore.entity.RoleEntity;
    import com.nlu.cdweb.BookStore.entity.UserEntity;
    import com.nlu.cdweb.BookStore.repositories.RoleRepository;
    import com.nlu.cdweb.BookStore.repositories.UserRepository;
    import com.nlu.cdweb.BookStore.services.IUserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.web.server.ResponseStatusException;

    import java.util.Collections;
    import java.util.List;

    @Service
    public class UserServiceImpl implements IUserService {

        private final AuthenticationManager manager;
        private final RoleRepository roleRepository;
        private final PasswordEncoder encoder;
        private final UserRepository userRepository;
        @Autowired
        public UserServiceImpl(AuthenticationManager manager, RoleRepository roleRepository, PasswordEncoder encoder, UserRepository userRepository) {
            this.manager = manager;
            this.roleRepository = roleRepository;
            this.encoder = encoder;
            this.userRepository = userRepository;
        }
        @Override
        public UserEntity addUser(RegisterDTO dto) {
            if(userRepository.findByUsername(dto.getUsername().trim()).isPresent()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username đã tồn tại");
            }
            if(userRepository.findByEmail(dto.getUsername().trim()).isPresent()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username đã tồn tại");
            }

            UserEntity user = new UserEntity();
            user.setUserName(dto.getUsername());
            user.setEmail(dto.getEmail());

            RoleEntity roles = roleRepository.findByName("USER").orElseThrow(() -> new UsernameNotFoundException("Not found User Role"));
            user.setRoles(Collections.singletonList(roles));

            user.setPasswordHash(encoder.encode(dto.getPassword()));

            userRepository.save(user);
            return user;
        }

        @Override
        public boolean userLogin(LoginDTO dto) {
            Authentication authentication = manager
                    .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        }

        @Override
        public List<UserEntity> findAllUser() {
            return userRepository.findAll();
        }

        @Override
        public boolean deleteUser(Long id) {
            UserEntity users = userRepository.findAllById(id);
            userRepository.delete(users);
            return true;
        }
    }
