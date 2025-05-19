    package com.nlu.cdweb.BookStore.repositories;

    import com.nlu.cdweb.BookStore.entity.RoleEntity;
    import com.nlu.cdweb.BookStore.utils.Role;
    import org.springframework.data.jpa.repository.JpaRepository;

    import java.util.Optional;

    public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
        Optional<RoleEntity> findByName(Role name);
    }
