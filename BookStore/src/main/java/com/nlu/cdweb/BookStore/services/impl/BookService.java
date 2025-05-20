package com.nlu.cdweb.BookStore.services.impl;

import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.dto.request.BookRequest;
import com.nlu.cdweb.BookStore.dto.response.BookResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.CategoryEntity;
import com.nlu.cdweb.BookStore.entity.DiscountEntity;
import com.nlu.cdweb.BookStore.event.BookCreationEvent;
import com.nlu.cdweb.BookStore.exception.AlreadyExistsException;
import com.nlu.cdweb.BookStore.exception.EntityNotFoundException;
import com.nlu.cdweb.BookStore.exception.OptimisticLockException;
import com.nlu.cdweb.BookStore.mapper.BookMapper;
import com.nlu.cdweb.BookStore.repositories.CategoryRepository;
import com.nlu.cdweb.BookStore.repositories.DiscountRepository;
import com.nlu.cdweb.BookStore.repositories.InventoryRepository;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import com.nlu.cdweb.BookStore.services.IBookService;
import com.nlu.cdweb.BookStore.specification.BookSpecification;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService implements IBookService {
    @Autowired
    private final BookRepository bookRepo;
    @Autowired
    private final CategoryRepository categoryRepo;
    @Autowired
    private final DiscountRepository discountRepository;
    @Autowired
    private final InventoryRepository inventoryRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private final BookMapper bookMapper;
    @Autowired
    private final BookSpecification specification;
    @Override
    @Transactional
    public BookEntity addBook(BookRequest bookDTO) {
        if(bookRepo.existsByNameIgnoreCase(bookDTO.getName()) || bookRepo.existsBySku(bookDTO.getSku())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"book with either input name or input sku already exists");
        }

        BookEntity bookEntity = bookMapper.toEntity(bookDTO);

        bookRepo.save(bookEntity);

        InventoryRequest inventoryDTO = new InventoryRequest(bookEntity.getId(), bookDTO.getQuantity());
        eventPublisher.publishEvent(new BookCreationEvent(this,inventoryDTO));

        return bookEntity;
    }

    @Override
    public boolean deleteBookById(Long id) {
        if (!bookRepo.existsById(id)) {
            throw new EntityNotFoundException("Not found book with id: " + id);
        }
        bookRepo.deleteById(id);
        return true;
    }

    @Override
    public BookResponse updateBookById(Long id, BookRequest request) {
        BookEntity entity = bookRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Not found book with id: "+id));
        entity.setName(request.getName());
        entity.setDesc(request.getDesc());
        entity.setSku(request.getSku());
        entity.setPrice(request.getPrice());

        CategoryEntity cate = categoryRepo.findById(request.getCategoryId()).orElseThrow();
        entity.setCategory(cate);

        DiscountEntity discount = discountRepository.findById(request.getDiscountId()).orElseThrow();
        entity.setDiscount(discount);

        try{
            BookEntity saved = bookRepo.save(entity);
            return bookMapper.toDTO(saved);
        }catch(DataIntegrityViolationException e){
            throw new AlreadyExistsException("A product with this name already exists.");
        }catch(ObjectOptimisticLockingFailureException e){
            throw new OptimisticLockException("This product has been updated by another user. Please review the changes.");
        }
    }

    @Override
    public List<BookResponse> findBookByCategoryId(Long id) {
        return bookRepo.findByCategoryId(id).stream().map(bookMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<BookResponse> search(String name, String desc, Boolean discountStatus, String categoryName, Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<BookEntity> spec = Specification.where(specification.hasName(name)
                .and(specification.hasDesc(desc))
                .and(specification.hasCategoryName(categoryName))
                .and(specification.hasDiscountStatus(discountStatus))
                .and(specification.hasPriceBetween(minPrice, maxPrice))
        );

        Page<BookEntity> books = bookRepo.findAll(spec, pageable);
        List<BookResponse> response = books.stream().map(bookMapper::toDTO).collect(Collectors.toList());
        return new PageImpl<>(response, pageable, books.getTotalElements());
    }

    @Override
    public Page<BookResponse> findAll(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<BookEntity> books = bookRepo.findAll(pageable);
        return books.map(bookMapper::toDTO);
    }

    @Override
    public BookResponse findBookById(Long id) {
        BookEntity entity = bookRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found book with id: "+id));
        return bookMapper.toDTO(entity);
    }
}
