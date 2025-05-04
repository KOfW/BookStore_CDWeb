package com.nlu.cdweb.BookStore.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "discount")
public class DiscountEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount_name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String desc;

    @Column(name = "discount_percent")
    private Double percent;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BookEntity> books = new ArrayList<>();

    @Version
    private Integer version;
}
