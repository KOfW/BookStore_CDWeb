package com.nlu.cdweb.BookStore.controller;

import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.request.BookRequest;
import com.nlu.cdweb.BookStore.dto.response.BookResponse;
import com.nlu.cdweb.BookStore.entity.BookEntity;
import com.nlu.cdweb.BookStore.services.IBookService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/books")
public class BookController {

    private final IBookService bookService;
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> findAllBook(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        try{
            Page<BookResponse> responses = bookService.findAll(page, size);
            if(responses != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("200", "Successfull Retrieval of Product List",responses));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("204", "List of products is empty",""));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",""));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> findBookById(@PathVariable Long id){
        try{
            BookResponse response = bookService.findBookById(id);
            if(response != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("200", "Successfull Retrieval of Book With Id",response));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("204", "Book isn't found",""));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",""));
        }
    }
    @PostMapping("/create")
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id){
        try{
            if(id <= 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("400", "Client Entered a Negative id",""));
            }
            boolean checkDelete = bookService.deleteBookById(id);
            if(checkDelete){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("204", "Book was successfully Deleted",checkDelete));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("404", "Book isn't found",checkDelete));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",""));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody BookRequest request){
        try{
            if(id <= 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("400", "Client Entered a Negative id",""));
            }
            BookResponse response = bookService.updateBookById(id, request);
            if(response != null){
                return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(new ApiResponse("200", "Book was successfully Updated",response));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("404", "Book isn't found",response));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",""));
        }
    }
    @GetMapping("/categoryId/{id}")
    public ResponseEntity<ApiResponse> findBookByCategoryId(@PathVariable Long id){
        try{
            if(id <= 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("400", "Client Entered a Negative id",""));
            }
            List<BookResponse> responses = bookService.findBookByCategoryId(id);
            if(responses != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("200", "Successfull Retrieval of Book With Category Id",responses));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("204", "Book isn't found",""));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",""));
        }
    }
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search (
            @Parameter(description = "product name to search for", required = false)
            @RequestParam(required=false) String name,

            @Parameter(description = "product desc to search for", required = false)
            @RequestParam(required=false) String desc,

            @Parameter(description = "product discount status to search for", required = false)
            @RequestParam(required=false) Boolean discountStatus,

            @Parameter(description = "category name to search for", required = false)
            @RequestParam(required=false) String categoryName,

            @Parameter(description = "product min price to search for", required = false)
            @RequestParam(required=false) Double minPrice,

            @Parameter(description = "product max price to search for", required = false)
            @RequestParam(required=false) Double maxPrice,

            @Parameter(description = "The page number to retrieve", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "The number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size
    ){
        try{
            Page<BookResponse> searchPage = bookService.search(name, desc, discountStatus, categoryName, minPrice, maxPrice, page, size);
            if(searchPage != null){
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("201", "Search Book Succesfully",searchPage));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("400", "Client Entered a non Valid Entity Body",searchPage));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",""));
        }
    };
}
