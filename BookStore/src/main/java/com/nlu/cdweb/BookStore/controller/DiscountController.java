package com.nlu.cdweb.BookStore.controller;

import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.request.AddBookToCategoryRequest;
import com.nlu.cdweb.BookStore.dto.request.AddBookToDiscountRequest;
import com.nlu.cdweb.BookStore.dto.request.CategoryRequest;
import com.nlu.cdweb.BookStore.dto.request.DiscountRequest;
import com.nlu.cdweb.BookStore.dto.response.CategoryResponse;
import com.nlu.cdweb.BookStore.dto.response.DiscountResponse;
import com.nlu.cdweb.BookStore.services.IDiscountService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {
    @Autowired
    private IDiscountService discountService;

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Page<DiscountResponse> findAll = discountService.findAll(page, size);
            if (findAll != null && !findAll.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse("200", "Successful Retrieval of All Discount", findAll));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("404", "No Discount Found", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("500", "Internal Server Error", ""));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        try {
            if(id <= 0){
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse("400", "Cant found Discount with Negative Id", ""));
            }
            DiscountResponse response = discountService.findById(id);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse("200", "Successful Retrieval of Discount By Id", response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("404", "No Discount Found", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("500", "Internal Server Error", ""));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(@RequestBody DiscountRequest request) {
        try {
            DiscountResponse response = discountService.create(request);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse("200", "Successful Created Discount", response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("404", "Failed to Created Discount", ""));
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
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse("400", "Cant found Discount with Negative Id", ""));
            }
            boolean response = discountService.delete(id);
            if (response) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse("204", "Successful Deleted Discount", response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("404", "Failed to Deleted Discount", response));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("500", "Internal Server Error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody DiscountRequest request) {
        try {
            if(id <= 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("400", "Not found with negative Id", ""));
            }
            DiscountResponse response = discountService.update(id, request);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(new ApiResponse("202", "Discount Successfully Updated", response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("404", "Discount Failed Updated", response));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("500", "Internal Server Error", ""));
        }
    }

    @PatchMapping("/add_products")
    public ResponseEntity<DiscountResponse> addProducts(
            @Valid @RequestBody
            @Parameter(description = "add products to category request details", required = true)
            AddBookToDiscountRequest x){
        return ResponseEntity.ok(discountService.addProduct(x));
    }
}
