package com.nlu.cdweb.BookStore.config;

import com.nlu.cdweb.BookStore.entity.RoleEntity;
import com.nlu.cdweb.BookStore.repositories.RoleRepository;
import com.nlu.cdweb.BookStore.utils.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoCreateRole {
    @Bean
    CommandLineRunner commandLineRunner(RoleRepository role){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                RoleEntity role1 = new RoleEntity(1L, Role.ADMIN);
                RoleEntity role2 = new RoleEntity(2L, Role.USER);

                role.save(role1);
                role.save(role2);
            }
        };
    }
}
