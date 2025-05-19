package com.nlu.cdweb.BookStore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class BookEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "book_name", nullable=false, unique = true, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String desc;

    @Column(name = "sku", nullable=false, unique = true, length = 16)
    private String sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(name = "price")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "discount_id")
    private DiscountEntity discount;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderDetailEntity> orderDetail = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<WishListEntity> wishList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference
    private AuthorEntity author;

    @Version
    private Integer version;

    @Override
    public String toString() {
        return "BookEntity(id=" + id + ", name=" + name + ")";
    }
}

