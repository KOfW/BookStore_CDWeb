package com.nlu.cdweb.BookStore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "inventory")
public class InventoryEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @OneToOne(cascade=CascadeType.ALL,orphanRemoval=true)
    @JoinColumn(name="book_id")
    private BookEntity book;
    @Column(name="quantity")
    private Integer quantity;
    @Version
    private Integer version;
}
