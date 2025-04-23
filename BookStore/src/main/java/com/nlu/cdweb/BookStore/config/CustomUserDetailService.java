    package com.nlu.cdweb.BookStore.config;

    import com.nlu.cdweb.BookStore.entity.RoleEntity;
    import com.nlu.cdweb.BookStore.entity.UserEntity;
    import com.nlu.cdweb.BookStore.repositories.UserRepository;
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
        private UserRepository userRepository;

        @Autowired
        public CustomUserDetailService (UserRepository userRepository){
            this.userRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
            return new User(user.getUserName(), user.getPasswordHash(), mapRolesToAuthority(user.getRoles()));
        }

        private Collection<GrantedAuthority> mapRolesToAuthority(List<RoleEntity> roles){
            return roles.stream().map(roleEntity -> new SimpleGrantedAuthority(roleEntity.getName())).collect(Collectors.toList());
        }
    }
