package com.nlu.cdweb.BookStore.controller;

import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.request.CartItemRequest;
import com.nlu.cdweb.BookStore.dto.response.CartItemResponse;
import com.nlu.cdweb.BookStore.services.ICartItemService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartItem")
@AllArgsConstructor
@NoArgsConstructor
public class CartItemController {
    @Autowired
    private ICartItemService cartService;

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        try{
            Page<CartItemResponse> responsePage = cartService.findAll(page, size);
            if(responsePage != null || !responsePage.isEmpty()){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("201", "Successful Retrieval All CartItem",responsePage));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404", "Cartitem not found",responsePage));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable Long id){
        try{
            if(id <= 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("400", "Can not find Cartitem with negative id",""));
            }
            CartItemResponse response = cartService.findById(id);
            if(response != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("201", "Successful Retrieval All CartItem By Id",response));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404", "Cartitem not found",response));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(@RequestBody CartItemRequest request){
        try{
            CartItemResponse response = cartService.create(request);
            if(response != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("201", "Successful Created CartItem",response));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404", "Failed Create CartItem",response));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id){
        try{
            boolean response = cartService.delete(id);
            if(response){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("204", "Successful Delete CartItem",response));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404", "Failed Delete CartItem",response));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id ,@RequestBody CartItemRequest request){
        try{
            CartItemResponse response = cartService.update(id ,request);
            if(response != null){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("202", "Successful Update CartItem",response));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404", "Failed Update CartItem",response));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",e.getMessage()));
        }
    }
}
