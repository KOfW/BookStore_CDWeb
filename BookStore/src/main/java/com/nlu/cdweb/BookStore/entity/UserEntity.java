package com.nlu.cdweb.BookStore.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nlu.cdweb.BookStore.utils.State;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "user") // Changed to lowercase to match standard naming
public class UserEntity extends BaseEntity implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "username",unique = true, nullable = false)
    private String userName;

    @Column(name = "email",unique = true, nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "passwordHash", nullable = false)
    private String passwordHash;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "birthDay")
    private Date dob;

    @Column(name = "photo")
    private String photo;

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL,orphanRemoval=true)
    @JsonManagedReference
    @ToString.Exclude
    private List<OrderEntity> order;

//    @OneToMany(mappedBy="user", cascade=CascadeType.ALL,orphanRemoval=true)
//    @JsonManagedReference
//    private List<ReviewEntity> review;

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL,orphanRemoval=true)
    @JsonManagedReference
    private List<WishListEntity> wishlist;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleEntity> role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.getEmail(); // hoặc username tuỳ bạn login bằng gì
    }

    @Override
    public String getPassword() {
        return this.getPasswordHash();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
