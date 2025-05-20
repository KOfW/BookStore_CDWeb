package com.nlu.cdweb.BookStore.specification;

import com.nlu.cdweb.BookStore.entity.CategoryEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CategorySpecification {
    public Specification<CategoryEntity> hasName(String name){
        return (root, query, criteriaBuilder) -> {
            if(name==null || name.isBlank()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("name"), "%" + name+ "%");
        };
    }

    public Specification<CategoryEntity> hasDesc(String desc){
        return (root, query, criteriaBuilder) -> {
            if(desc==null || desc.isBlank()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("desc"), "%" + desc+ "%");
        };
    }

    public Specification<CategoryEntity> hasProductsWithName(String productName){
        return (root, query, criteriaBuilder) -> {
            if(productName==null || productName.isBlank()){
                return criteriaBuilder.conjunction();
            }
            var productJoin = root.join("products");
            return criteriaBuilder.equal(productJoin.get("name"), productName);
        };
    }


}
