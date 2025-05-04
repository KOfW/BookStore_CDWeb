package com.nlu.cdweb.BookStore.services.impl;

import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.dto.request.BookRequest;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.CategoryEntity;
import com.nlu.cdweb.BookStore.entity.DiscountEntity;
import com.nlu.cdweb.BookStore.event.BookCreationEvent;
import com.nlu.cdweb.BookStore.repositories.CategoryRepository;
import com.nlu.cdweb.BookStore.repositories.DiscountRepository;
import com.nlu.cdweb.BookStore.repositories.InventoryRepository;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import com.nlu.cdweb.BookStore.services.IBookService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    private ApplicationEventPublisher eventPublisher;
    @Override
    @Transactional
    public BookEntity addBook(BookRequest bookDTO) {
        if(bookRepo.existsByNameIgnoreCase(bookDTO.getName()) || bookRepo.existsBySku(bookDTO.getSku())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"book with either input name or input sku already exists");
        }
        BookEntity bookEntity = new BookEntity();
        bookEntity.setName(bookDTO.getName());
        bookEntity.setDesc(bookDTO.getDesc());
        bookEntity.setSku(bookDTO.getSku());
        bookEntity.setPrice(bookDTO.getPrice());

        CategoryEntity cate = categoryRepo.findById(bookDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        DiscountEntity discount = discountRepository.findById(bookDTO.getDiscountId())
                .orElseThrow(() -> new RuntimeException("Discount not found"));

        bookEntity.setCategory(cate);
        bookEntity.setDiscount(discount);

        BookEntity save = bookRepo.save(bookEntity);

        InventoryRequest inventoryDTO = new InventoryRequest(save.getId(), bookDTO.getQuantity());
        eventPublisher.publishEvent(new BookCreationEvent(this,inventoryDTO));

        return bookEntity;
    }
}
