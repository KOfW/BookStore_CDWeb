package com.nlu.cdweb.BookStore.specification;

import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.CategoryEntity;
import com.nlu.cdweb.BookStore.entity.DiscountEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BookSpecification {
    /*
    * Root : đại diện cho entity (BookEntity) dùng để truy cập thuộc tính name
    * Query : đối tượng truy vấn
    * criteriaBuilder : cung cấp các phương thức để tạo điều kiện như like, conjuntion
    *  */
    public Specification<BookEntity> hasName(String name){
        return ((root, query, criteriaBuilder) -> {
            if(name==null || name.isBlank()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("name"), "%" + name + "%");
        });
    }
    public Specification<BookEntity> hasDesc(String desc){
        return ((root, query, criteriaBuilder) -> {
            if(desc==null || desc.isBlank()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("desc"), "%" + desc + "%");
        });
    }
    public Specification<BookEntity> hasCategoryName(String categoryName){
        return ((root, query, criteriaBuilder) -> {
            if(categoryName==null || categoryName.isBlank()){
                return criteriaBuilder.conjunction();
            }
            Join<BookEntity, CategoryEntity> categoryJoin = root.join("category");
            return criteriaBuilder.equal(categoryJoin.get("name"), categoryName);
        });
    }
    public Specification<BookEntity> hasPriceBetween(Double minPrice, Double maxPrice){
        return (root, query, criteriaBuilder) -> {
            if(minPrice==null && maxPrice==null){
                return criteriaBuilder.conjunction();
            }
            if(maxPrice==null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            if(minPrice==null){
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
        };
    }
    public Specification<BookEntity> hasDiscountStatus(Boolean status){
        return ((root, query, criteriaBuilder) -> {
            if(status==null){
                return criteriaBuilder.conjunction();
            }
            Join<BookEntity, DiscountEntity> joinDiscount = root.join("discount");
            return criteriaBuilder.equal(joinDiscount.get("active"),status);
        });
    }
}
