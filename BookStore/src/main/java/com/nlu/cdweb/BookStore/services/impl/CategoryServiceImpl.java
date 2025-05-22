package com.nlu.cdweb.BookStore.services.impl;

import com.nlu.cdweb.BookStore.dto.request.AddBookToCategoryRequest;
import com.nlu.cdweb.BookStore.dto.request.CategoryRequest;
import com.nlu.cdweb.BookStore.dto.response.CategoryResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.CategoryEntity;
import com.nlu.cdweb.BookStore.exception.EntityNotFoundException;
import com.nlu.cdweb.BookStore.mapper.CategoryMapper;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import com.nlu.cdweb.BookStore.repositories.CategoryRepository;
import com.nlu.cdweb.BookStore.services.ICategoryService;
import com.nlu.cdweb.BookStore.specification.CategorySpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private final BookRepository bookRepo;
    @Autowired
    private final CategoryRepository categoryRepo;
    @Autowired
    private final CategoryMapper mapper;
    @Autowired
    private final CategorySpecification specification;
    @Override
    public Page<CategoryResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryEntity> responses = categoryRepo.findAll(pageable);
        return responses.map(mapper::toDTO);
    }

    @Override
    public CategoryResponse findCategoryById(Long id) {
        CategoryEntity category = categoryRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Cant found category id: "+id));
        return mapper.toDTO(category);
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        CategoryEntity entity = mapper.toEntity(request);
        categoryRepo.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        CategoryEntity category = categoryRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Cant found category id: "+id));
        categoryRepo.delete(category);
        return true;
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        CategoryEntity category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find category with id: " + id));

        category.setName(request.getName());

        CategoryEntity updated = categoryRepo.save(category);
        return mapper.toDTO(updated);
    }


    @Override
    public Page<CategoryResponse> search(String name, String productName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<CategoryEntity> spec = Specification.where(specification.hasName(name))
                .and(specification.hasProductsWithName(productName));
        Page<CategoryEntity> category = categoryRepo.findAll(spec, pageable);
        List<CategoryResponse> responses = category.stream().map(mapper::toDTO).collect(Collectors.toList());
        return new PageImpl<>(responses, pageable, category.getTotalElements());
    }

    @Override
    @Transactional
    public CategoryResponse addBookToCategory(AddBookToCategoryRequest request) {
        CategoryEntity category = categoryRepo.findById(request.getCatetegoryId()).orElseThrow(() -> new EntityNotFoundException("Cant found category id: "+request.getCatetegoryId()));

        List<BookEntity> books = bookRepo.findAllById(request.getBookId());

        books.forEach(category::addProduct);
        bookRepo.saveAll(books);

        return mapper.toDTO(categoryRepo.save(category));
    }
}
