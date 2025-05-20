package com.nlu.cdweb.BookStore.controller;

import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.request.AddBookToCategoryRequest;
import com.nlu.cdweb.BookStore.dto.request.CategoryRequest;
import com.nlu.cdweb.BookStore.dto.response.CategoryResponse;
import com.nlu.cdweb.BookStore.services.ICategoryService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Page<CategoryResponse> findAll = categoryService.findAll(page, size);
            if (findAll != null && !findAll.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse("200", "Successful Retrieval of All Categories", findAll));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("404", "No Categories Found", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("500", "Internal Server Error", ""));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> findCategoryById(@PathVariable Long id) {
        try {
            if(id <= 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("400", "Not found with negative Id", ""));
            }
            CategoryResponse response = categoryService.findCategoryById(id);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse("200", "Successful Retrieval of Categories By Id", response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("404", "No Categories Found", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("500", "Internal Server Error", ""));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(@RequestBody CategoryRequest request) {
        try {
            CategoryResponse response = categoryService.create(request);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse("201", "Category Successful Created", response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("404", "Category Failed Created", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("500", "Internal Server Error", ""));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        try {
            if(id <= 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("400", "Not found with negative Id", ""));
            }
            boolean checkDelete = categoryService.delete(id);
            if (checkDelete) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse("204", "Category Successful Deleted", checkDelete));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("404", "Category Failed Deleted", checkDelete));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("500", "Internal Server Error", ""));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody CategoryRequest request) {
        try {
            if(id <= 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("400", "Not found with negative Id", ""));
            }
            CategoryResponse response = categoryService.update(id, request);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(new ApiResponse("202", "Category Successful Updated", response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("404", "Category Failed Updated", response));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("500", "Internal Server Error", ""));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> findCategoryById(
            @Parameter(description = "category name to search for", required = false)
            @RequestParam(required=false) String name,

            @Parameter(description = "product name to search for", required = false)
            @RequestParam(required=false) String productName,

            @Parameter(description = "The page number to retrieve", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "The number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<CategoryResponse> response = categoryService.search(name, productName, page, size);
            if (response != null && !response.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse("201", "Successful Retrieval of Categories", response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("404", "No Categories Found", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("500", "Internal Server Error", ""));
        }
    }
    @PatchMapping("/add_products")
    public ResponseEntity<CategoryResponse> addProducts(
            @Valid @RequestBody
            @Parameter(description = "add products to category request details", required = true)
            AddBookToCategoryRequest x){
        return ResponseEntity.ok(categoryService.addBookToCategory(x));
    }
}
