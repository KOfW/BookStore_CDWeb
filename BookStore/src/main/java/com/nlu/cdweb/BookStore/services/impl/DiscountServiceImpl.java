package com.nlu.cdweb.BookStore.services.impl;

import com.nlu.cdweb.BookStore.dto.request.AddBookToDiscountRequest;
import com.nlu.cdweb.BookStore.dto.request.DiscountRequest;
import com.nlu.cdweb.BookStore.dto.response.DiscountResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.entity.DiscountEntity;
import com.nlu.cdweb.BookStore.exception.EntityNotFoundException;
import com.nlu.cdweb.BookStore.mapper.DiscountMapper;
import com.nlu.cdweb.BookStore.repositories.BookRepository;
import com.nlu.cdweb.BookStore.repositories.DiscountRepository;
import com.nlu.cdweb.BookStore.services.IDiscountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements IDiscountService {
    @Autowired
    private DiscountRepository discountRepo;
    @Autowired
    private DiscountMapper mapper;
    @Autowired
    private BookRepository bookRepo;
    @Override
    public Page<DiscountResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DiscountEntity> entities = discountRepo.findAll(pageable);
        return entities.map(mapper::toDTO);
    }

    @Override
    public DiscountResponse findById(Long id) {
        DiscountEntity entity = discountRepo.findById(id).orElseThrow(()->new EntityNotFoundException("Can't found discount with id: "+id));
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional
    public DiscountResponse create(DiscountRequest request) {
        return mapper.toDTO(discountRepo.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Optional<DiscountEntity> d = discountRepo.findById(id);
        if (d.isPresent()) {
            DiscountEntity dd = d.get();
            List<BookEntity> books = new ArrayList<>(dd.getBooks()); // tạo bản sao tránh ConcurrentModification
            for (BookEntity book : books) {
                book.setDiscount(null); // bỏ liên kết discount
//                dd.removeBook(book);
                bookRepo.save(book);    // lưu lại từng book
            }
            discountRepo.delete(dd);    // sau khi các book đã bỏ liên kết
            return true;
        }
        return false;
    }



    @Override
    @Transactional
    public DiscountResponse update(Long id, DiscountRequest request) {
        DiscountEntity entity = discountRepo.findById(id).orElseThrow(()->new EntityNotFoundException("Can't found discount with id: "+id));

        entity.setName(request.getName());
        entity.setDesc(request.getDesc());
        entity.setPercent(request.getPercent());
        entity.setActive(request.getActive());

        return mapper.toDTO(discountRepo.save(entity));
    }

    @Override
    @Transactional
    public DiscountResponse addProduct(AddBookToDiscountRequest request) {
        List<BookEntity> books = bookRepo.findAllById(request.getBookId());
        DiscountEntity discount = discountRepo.findById(request.getDiscountId()).orElseThrow(()->new EntityNotFoundException("Can't found discount with id: "+request.getDiscountId()));
        books.forEach(discount::addBook);
        bookRepo.saveAll(books);
        return mapper.toDTO(discountRepo.save(discount));
    }
}
