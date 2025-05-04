package com.nlu.cdweb.BookStore.services.impl;

import com.nlu.cdweb.BookStore.config.JwtGenerator;
import com.nlu.cdweb.BookStore.dto.request.LoginRequest;
import com.nlu.cdweb.BookStore.dto.request.RegisterRequest;
import com.nlu.cdweb.BookStore.entity.RoleEntity;
import com.nlu.cdweb.BookStore.entity.UserEntity;
import com.nlu.cdweb.BookStore.repositories.RoleRepository;
import com.nlu.cdweb.BookStore.repositories.UserRepository;
import com.nlu.cdweb.BookStore.services.IUserService;
import com.nlu.cdweb.BookStore.utils.Role;
import com.nlu.cdweb.BookStore.utils.State;
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
    private JwtGenerator jwtGenerator;
    @Autowired
    public UserServiceImpl(AuthenticationManager manager, RoleRepository roleRepository, PasswordEncoder encoder, UserRepository userRepository, JwtGenerator jwtGenerator) {
        this.manager = manager;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
    }
    @Override
    public UserEntity addUser(RegisterRequest dto) {
        if(userRepository.findByUserName(dto.getUserName().trim()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username đã tồn tại");
        }
        if(userRepository.findByEmail(dto.getEmail().trim()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại");
        }
        UserEntity user = new UserEntity();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setState(State.PENDING);

        RoleEntity roles = roleRepository.findByName(Role.USER).orElseThrow(() -> new UsernameNotFoundException("Not found User Role"));
        user.setRole(Collections.singletonList(roles));

        user.setPasswordHash(encoder.encode(dto.getPassword()));

        userRepository.save(user);
        return user;
    }

    @Override
    public String userLogin(LoginRequest dto) {
        /*
        * new UsernamePasswordAuthenticationToken(username, password) như là một gói xác thực
        *   Ban đầu là principal = username, credencials = password, authenticate = false ("Tôi có username và password, kiểm tra giúp tôi đúng không?")
        * manager.authenticate(...)
        *   Tìm một cái phù hợp để xác thực (AuthenticationProvider).
        *   Giao nhiệm vụ cho AuthenticationProvider xác thực cái token đó. (AuthenticationManager = "ông tổng quản lý", còn việc xác thực cụ thể do "nhân viên" (AuthenticationProvider) làm.)
        * AuthenticationProvider hoạt động như thế nào?
        *   Spring Security sẽ có sẵn 1 thằng gọi là DaoAuthenticationProvider (hoặc bạn tự custom cái riêng cũng được).
        *   DaoAuthenticationProvider sẽ:
        *       Lấy username ra.
        *       Gọi đến UserDetailsService (một service) để tải user từ database lên.
        *       UserDetails user = userDetailsService.loadUserByUsername(username);
        *   So sánh password:
        *       Password người dùng nhập (credentials) sẽ được mã hóa (ví dụ dùng BCrypt).
        *       So sánh với password đã lưu trong database.
        *
        * Kết quả sẽ trả về các giá tri liên quan đến authentication
        *  - authentication.getName() : "john"
        *  - authentication.getPrincipal() : UserDetails object (chứa username, password (mã hóa), các quyền)
        *  - authentication.getAuthorities() : [ROLE_ADMIN
        *  - authentication.isAuthenticated() : true
        * */
        Authentication authentication = manager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        /*
        * Tạo token từ các giá trị authentication trên
        * */
        String token = jwtGenerator.generateToken(authentication);
        /*
        Mỗi request HTTP (ví dụ như 1 lần gọi API) sẽ đi kèm một "túi" SecurityContext.
        Trong cái "túi" đó, Spring sẽ lưu:
            Ai đang đăng nhập (Authentication)
            Người đó có roles nào
            Các quyền gì
        Khi request tiếp theo tới, Spring Security có thể lấy từ SecurityContext ra để biết "À, user này là ai", "Có quyền gì", "Có được phép làm hành động này không".
        Nếu bạn không set authentication vào SecurityContext,
        → Spring sẽ không biết ai đang login, và user coi như là chưa đăng nhập.

        Flow :
            Người dùng gửi username/password.
            Bạn xác thực thành công (Authentication).
            Bạn gán authentication vào SecurityContextHolder:
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Từ đây về sau trong suốt vòng đời của request đó:
            Spring Security có thể SecurityContextHolder.getContext().getAuthentication() bất kỳ lúc nào để biết ai đang đăng nhập.
            Authorization (kiểm tra quyền) mới hoạt động chính xác
        */
         SecurityContextHolder.getContext().setAuthentication(authentication);
        return token;
    }

    @Override
    public List<UserEntity> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public String sendOTP(String email) {
        return null;
    }

    @Override
    public boolean deleteUser(Long id) {
        UserEntity users = userRepository.findAllById(id);
        userRepository.delete(users);
        return true;
    }

    @Override
    public boolean userActive(String email, State state) {
        try{
            userRepository.userActive(state, email);
            return true;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "can't query state");
        }

    }
}
