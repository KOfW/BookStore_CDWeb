package com.nlu.cdweb.BookStore.controller;

import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.request.BookRequest;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.services.IBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/books")
public class BookController {

    private final IBookService bookService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody BookRequest book){
        try{
            BookEntity createBook = bookService.addBook(book);
            if(createBook != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("201", "Add Book Succesfully",createBook));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("400", "Client Entered a non Valid Entity Body",""));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",""));
        }
    }


}
